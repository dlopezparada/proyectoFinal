package com.futuro.proyecto.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.futuro.proyecto.dto.LocationDto;
import com.futuro.proyecto.dto.SensorDataDto;
import com.futuro.proyecto.dto.SensorDataInputDto;
import com.futuro.proyecto.dto.SensorDataValueInputDto;
import com.futuro.proyecto.dto.SensorDto;
import com.futuro.proyecto.models.Location;
import com.futuro.proyecto.models.Sensor;
import com.futuro.proyecto.models.SensorData;
import com.futuro.proyecto.models.SensorDataValue;
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
	
	
    @Override
    public void processSensorData(SensorDataDto sensorDataDto) { 
		UUID sensorUuid = UUID.fromString(sensorDataDto.getApiKey());
        Sensor sensor = sensorRepository.findBySensorApiKey(sensorUuid);
        if (sensor == null) {
            throw new NoSuchElementException("Sensor no encontrado para el API Key: " + sensorDataDto.getApiKey());
        }

        for (SensorDataDto.MedicionDTO medicionDTO : sensorDataDto.getJsonData()) {
            Timestamp fechaMedicionTimestamp = Timestamp.valueOf(LocalDateTime.ofEpochSecond(medicionDTO.getDatetime(), 0, ZoneOffset.UTC));

            //Se crea SensorData
            SensorData sensorData = new SensorData();
            sensorData.setSensor(sensor);
            sensorData.setFechaIngreso(Timestamp.valueOf(LocalDateTime.now()));
            sensorData = sensorDataRepository.save(sensorData);

            for (Map.Entry<String, Object> entry : medicionDTO.getMediciones().entrySet()) {
                String variable = entry.getKey();
                Object valor = entry.getValue();

                //Se crea SensorDataValue
                SensorDataValue sensorDataValue = new SensorDataValue();
                sensorDataValue.setData(sensorData);
                sensorDataValue.setVariable(variable);
                sensorDataValue.setValue(String.valueOf(valor));
                sensorDataValue.setFechaMedicion(fechaMedicionTimestamp);
                sensorDataValueRepository.save(sensorDataValue);
            }
        }
    }

}

