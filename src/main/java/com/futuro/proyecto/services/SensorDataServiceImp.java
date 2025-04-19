package com.futuro.proyecto.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.futuro.proyecto.dto.JsonDataDto;
import com.futuro.proyecto.dto.LocationDto;
import com.futuro.proyecto.dto.ResponseDataDto;
import com.futuro.proyecto.dto.SensorDataDto;
import com.futuro.proyecto.dto.SensorDataInputDto;
import com.futuro.proyecto.dto.SensorDataValueInputDto;
import com.futuro.proyecto.dto.SensorDto;
import com.futuro.proyecto.models.Location;
import com.futuro.proyecto.models.Sensor;
import com.futuro.proyecto.models.SensorData;
import com.futuro.proyecto.models.SensorDataValue;
import com.futuro.proyecto.repository.CompanyRepository;
import com.futuro.proyecto.repository.SensorDataRepository;
import com.futuro.proyecto.repository.SensorDataValuesRepository;
import com.futuro.proyecto.repository.SensorRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class SensorDataServiceImp implements SensorDataService {

	@Autowired
	private SensorDataRepository sensorDataRepository;
	@Autowired
	private SensorRepository sensorRepository;
	@Autowired
	private SensorDataValuesRepository sensorDataValueRepository;
	@Autowired
	private CompanyRepository companyRepository;
	
	
    @Override
    public void processSensorData(SensorDataDto sensorDataDto) { 
    	if(sensorDataDto == null) {
    		throw new IllegalArgumentException("Hacen faltas parametros");
    	}
    	if(sensorDataDto.getApiKey() == null || sensorDataDto.getApiKey().trim().equals("")) {
    		throw new IllegalArgumentException("No se encontro el api key del sensor");
    	}
		UUID sensorUuid = UUID.fromString(sensorDataDto.getApiKey());
        Sensor sensor = sensorRepository.findBySensorApiKey(sensorUuid);
        if (sensor == null) {
            throw new NoSuchElementException("Sensor no encontrado para el API Key: " + sensorDataDto.getApiKey());
        }
        
        List<JsonDataDto> jsonDataList = sensorDataDto.getJsonData();
        if(jsonDataList == null || jsonDataList.isEmpty()) {
        	throw new IllegalArgumentException("Se recibio un cuerpo del sensor "+sensor.getId()+" vacio");
        }
        
        for(JsonDataDto jsonDataDto : jsonDataList) {
        	Timestamp fechaMedicionTimestamp = Timestamp.valueOf(LocalDateTime.ofEpochSecond(jsonDataDto.getDatetime(), 0, ZoneOffset.UTC));
        	
        	SensorData sensorData = SensorData.builder()
                    .sensor(sensor)
                    .fechaIngreso(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            sensorData = sensorDataRepository.save(sensorData);
            
            if (jsonDataDto.getVariables() == null) {
            	throw new IllegalArgumentException("No se recibieron mediciones del sensor "+sensor.getId());
            }
            for (Map.Entry<String, Object> entry : jsonDataDto.getVariables().entrySet()) {
            	String variable = entry.getKey();
            	Object value = entry.getValue();
            	
            	SensorDataValue sensorDataValue = SensorDataValue.builder()
            			.data(sensorData)
            			.variable(variable)
            			.value(String.valueOf(value))
            			.fechaMedicion(fechaMedicionTimestamp)
            			.build();
            	sensorDataValueRepository.save(sensorDataValue);
            }
        }
        System.out.println("Datos ingresados correctamente");
    }
    
    @Override
	public void saveSensorDataFromKafka(String apiKey, List<JsonDataDto> readings) {
    	try {
	    	UUID sensorUuid = UUID.fromString(apiKey);
			Sensor sensor = sensorRepository.findBySensorApiKey(sensorUuid);
	
			if (sensor == null) {
				System.out.println("No se encontró sensor para la API Key: " + apiKey);
	            return;
			}
	
			SensorData sensorData = sensorDataRepository.save(
				SensorData.builder()
				.sensor(sensor)
				.fechaIngreso(Timestamp.from(Instant.now()))
				.build()
			);
	
			for (JsonDataDto jsonDataDto : readings) {
	        	Timestamp fechaMedicionTimestamp = Timestamp.valueOf(LocalDateTime.ofEpochSecond(jsonDataDto.getDatetime(), 0, ZoneOffset.UTC));
	
				List<SensorDataValue> values = new ArrayList<>();
	
				for (Map.Entry<String, Object> entry :
						Optional.ofNullable(jsonDataDto.getVariables()).orElse(Collections.emptyMap()).entrySet()) {
					
					values.add(
							SensorDataValue.builder()
									.data(sensorData)
									.variable(entry.getKey())
									.value(String.valueOf(entry.getValue()))
									.fechaMedicion(fechaMedicionTimestamp)
									.build()
					);
				}
	
				sensorDataValueRepository.saveAll(values);
			}
			System.out.println("se guardaron correctamente los datos enviados para el sensor de apiKey: " + apiKey + ", Los datos se guardaron para el SensorData de id: "+sensorData.getId());
    	} catch (IllegalArgumentException e) {
            System.out.println("La API Key recibida no tiene un formato UUID válido: " + apiKey);
        }
	}

	@Override
	public List<ResponseDataDto> getSensorData(Long from, Long to, List<Integer> sensorIds, String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		validateCompanyApiKey(companyUuid);
		
		if (sensorIds == null || sensorIds.isEmpty()) {
            throw new IllegalArgumentException("Lista de id sensores vacia o nula");
        }
		if (from == null || to == null) {
             throw new IllegalArgumentException("Los parametros 'from' y 'to' son obligatorios.");
        }
		if (from >= to) {
            throw new IllegalArgumentException("El parametro 'from' debe ser anterior a 'to'.");
        }
		List<Integer> allowedSensorIds = sensorRepository.findIdsByCompanyApiKey(companyUuid);
		List<Integer> idsAlowed = sensorIds.stream()
                .filter(allowedSensorIds::contains) 
                .distinct()
                .collect(Collectors.toList());
		if (idsAlowed.isEmpty()) {
			throw new NoSuchElementException("Advertencia: Ninguno de los sensor_id proporcionados pertenece a la compañía con API key: " + companyApiKey);
        }
		
		Timestamp fromTimestamp = Timestamp.from(Instant.ofEpochSecond(from));
        Timestamp toTimestamp = Timestamp.from(Instant.ofEpochSecond(to));

//        Instant ahora = Instant.now();
//        
//        Timestamp timestampActual = Timestamp.from(ahora);
//        String fechaString = "2025-02-24 00:11:35";
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        LocalDateTime ldt = LocalDateTime.parse(fechaString, formatter);
//        Timestamp timestampAsUTC = Timestamp.from(ldt.toInstant(ZoneOffset.UTC));

		
		List<SensorDataValue> sensorDataValues = sensorDataValueRepository.findByDataSensorIdInAndFechaMedicionBetween(
				idsAlowed,
				fromTimestamp,
				toTimestamp
        );
		
		if (sensorDataValues.isEmpty()) {
			throw new NoSuchElementException("No se encontraron datos para los criterios especificados.");
        }
		
        Map<Integer, List<SensorDataValue>> bySensorIdMap = sensorDataValues.stream()
            .collect(Collectors.groupingBy(
                value -> value.getData().getSensor().getId()
            ));

        Map<Integer, Map<Long, Map<String, String>>> groupedData = new HashMap<>();

        bySensorIdMap.forEach((sensorId, valuesForThisSensor) -> {
            Map<Long, Map<String, String>> byTimestampMap = valuesForThisSensor.stream()
                .collect(Collectors.groupingBy(
                    value -> value.getFechaMedicion().toInstant().getEpochSecond(),
                    HashMap::new,
                    Collectors.toMap(
                        SensorDataValue::getVariable,
                        SensorDataValue::getValue,
                        (existingValue, newValue) -> newValue
                    )
                ));
            groupedData.put(sensorId, byTimestampMap);
        });

        List<ResponseDataDto> responseList = new ArrayList<>();
        groupedData.forEach((sensorId, datetimeMap) -> {
        	datetimeMap.forEach((datetime, variables) -> {
                ResponseDataDto dto = ResponseDataDto.builder()
                        .sensorId(sensorId)
                        .datetime(datetime)
                        .variables(variables)
                        .build();
                responseList.add(dto);
            });
        });
        return responseList;
		
//		return null;
	}
	
	private void validateCompanyApiKey(UUID companyApiKey) {
        if (companyApiKey == null) {
            throw new IllegalArgumentException("Company API key is required");
        }
        if (!companyRepository.existsByCompanyApiKey(companyApiKey)) {
            throw new IllegalArgumentException("Invalid company API key");
        }
    }
}

