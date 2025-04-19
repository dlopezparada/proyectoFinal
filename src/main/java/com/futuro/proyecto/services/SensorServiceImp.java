package com.futuro.proyecto.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import com.futuro.proyecto.dto.CompanyDto;
import com.futuro.proyecto.dto.LocationDto;
import com.futuro.proyecto.models.Location;
import com.futuro.proyecto.models.Sensor;
import com.futuro.proyecto.models.SensorData;
import com.futuro.proyecto.repository.CompanyRepository;
import com.futuro.proyecto.repository.LocationRepository;
import com.futuro.proyecto.repository.SensorDataRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.futuro.proyecto.dto.SensorDto;
import com.futuro.proyecto.repository.SensorRepository;

@Service
public class SensorServiceImp implements SensorService{
	
	@Autowired
    private  SensorRepository sensorRepository;
	@Autowired
	private  LocationRepository locationRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private SensorDataRepository sensorDataRepository;


    public SensorServiceImp(SensorRepository sensorRepository,  LocationRepository locationRepository) {
        this.sensorRepository = sensorRepository;
        this.locationRepository = locationRepository;
    }
    
    private void validateCompanyApiKey(UUID companyApiKey) {
        if (companyApiKey == null) {
            throw new IllegalArgumentException("Company API key es requerido");
        }
        if (!companyRepository.existsByCompanyApiKey(companyApiKey)) {
            throw new IllegalArgumentException("company API key invalido");
        }
    }

    @Override
	public List<SensorDto> findAll(UUID companyApiKey) {
    	validateCompanyApiKey(companyApiKey);
    	
    	 List<Sensor> sensorList = sensorRepository.findAll();
         if (sensorList == null || sensorList.isEmpty()) {
             throw new NoSuchElementException("No se encontraron sensores.");
         }
         return sensorList.stream()
                 .map(sensor -> {
                     LocationDto locationDto = null;
                     if (sensor.getLocation() != null) {
                         Location location = locationRepository.findById(sensor.getLocation().getId())
                                 .orElseThrow(() -> new NoSuchElementException("Ubicación no encontrada para el sensor con ID: " + sensor.getId()));

                         locationDto = LocationDto.builder()
                                 .id(location.getId())
                                 .company(CompanyDto.builder()
                                         .id(location.getCompany().getId())
                                         .companyName(location.getCompany().getCompanyName())
                                         .build())
                                 .locationName(location.getLocationName())
                                 .locationCountry(location.getLocationCountry())
                                 .locationCity(location.getLocationCity())
                                 .locationMeta(location.getLocationMeta())
                                 .build();
                     }

                     return SensorDto.builder()
                             .id(sensor.getId())
                             .sensorName(sensor.getSensorName())
                             .sensorCategory(sensor.getSensorCategory())
                             .sensorMeta(sensor.getSensorMeta())
                             .sensorApiKey(sensor.getSensorApiKey())
                             .location(locationDto)
                             .build();
                 })
                 .collect(Collectors.toList());
	}

	@Override
	public SensorDto findById(Long id, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El ID no puede ser nulo o menor o igual a cero");
		}

		Sensor sensor = sensorRepository.findById(id.intValue())
				.orElseThrow(() -> new NoSuchElementException("Sensor no encontrado con ID: " + id));

		Location location = sensor.getLocation();
		LocationDto locationDto = null;

		if (location != null) {
			locationDto = LocationDto.builder()
					.id(location.getId())
					.company(CompanyDto.builder()
							.id(location.getCompany().getId())
							.companyName(location.getCompany().getCompanyName())
							.companyApiKey(location.getCompany().getCompanyApiKey())
							.build())
					.locationName(location.getLocationName())
					.locationCountry(location.getLocationCountry())
					.locationCity(location.getLocationCity())
					.locationMeta(location.getLocationMeta())
					.build();
		}

		return SensorDto.builder()
				.id(sensor.getId())
				.sensorName(sensor.getSensorName())
				.sensorCategory(sensor.getSensorCategory())
				.sensorMeta(sensor.getSensorMeta())
				.sensorApiKey(sensor.getSensorApiKey())
				.location(locationDto)
				.build();
	}

	@Override
	public SensorDto create(SensorDto sensorDto, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);
		System.out.println("Company API Key recibido en el servicio: " +companyApiKey);
		
		if(sensorDto.getLocationId() == null) {
			throw new IllegalArgumentException("id de location nulo");
		}
		
		Location location2 = locationRepository.findById(sensorDto.getLocationId()).orElseThrow(() -> new NoSuchElementException("id de location invalido: " + sensorDto.getLocationId()));
		
		if(sensorDto.getSensorName() == null || sensorDto.getSensorName().trim().equals("")) {
			throw new IllegalArgumentException("sensor name viene nulo o vacio");
		}
		if(sensorDto.getSensorCategory() == null || sensorDto.getSensorCategory().trim().equals("")) {
			throw new IllegalArgumentException("sensor category viene nulo o vacio");
		}
		
		
		Sensor sensor = new Sensor();
		sensor.setSensorName(sensorDto.getSensorName());
		sensor.setSensorCategory(sensorDto.getSensorCategory());
		sensor.setSensorMeta(sensorDto.getSensorMeta());
		sensor.setSensorApiKey(UUID.randomUUID());


		sensor.setLocation(location2);

		Sensor savedSensor = sensorRepository.save(sensor);

		Location location = savedSensor.getLocation();
		LocationDto locationDto = null;
		if (location != null) {
			locationDto = LocationDto.builder()
					.id(location.getId())
					.company(CompanyDto.builder()
							.id(location.getCompany().getId())
							.companyName(location.getCompany().getCompanyName())
							.companyApiKey(location.getCompany().getCompanyApiKey())
							.build())
					.locationName(location.getLocationName())
					.locationCity(location.getLocationCity())
					.locationCountry(location.getLocationCountry())
					.locationMeta(location.getLocationMeta())
					.build();
		}

		return SensorDto.builder()
				.id(savedSensor.getId())
				.sensorName(savedSensor.getSensorName())
				.sensorCategory(savedSensor.getSensorCategory())
				.sensorMeta(savedSensor.getSensorMeta())
				.sensorApiKey(savedSensor.getSensorApiKey())
				.location(locationDto)
				.build();

	}

	@Override
	public SensorDto updateByApiKey(String sensorApiKey, SensorDto sensorDto, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);

		if (sensorApiKey == null || sensorApiKey.trim().isEmpty()) {
			throw new IllegalArgumentException("API key no puede ser nulo o vacío");
		}

		UUID uuid = UUID.fromString(sensorApiKey);

		Sensor sensor = sensorRepository.findBySensorApiKey(uuid);
		
		if(sensor == null) {
			throw new NoSuchElementException("No se encontro el sensor de api key: " + uuid);
		}
		
		if(sensorDto.getSensorName() == null || sensorDto.getSensorName().trim().equals("")) {
			throw new IllegalArgumentException("Sensor name se encuentra nulo o vacio");
		}
		if(sensorDto.getSensorCategory() == null || sensorDto.getSensorCategory().trim().equals("")) {
			throw new IllegalArgumentException("Sensor category se encuentra nulo o vacio");
		}

		// Actualizamos los campos editables
		sensor.setSensorName(sensorDto.getSensorName());
		sensor.setSensorCategory(sensorDto.getSensorCategory());
		sensor.setSensorMeta(sensorDto.getSensorMeta());
		
		if(sensorDto.getLocationId() == null ) {
			throw new IllegalArgumentException("Location id nulo o vacio");
		}

		Location location = sensor.getLocation();
		LocationDto locationDto = null;
		if (location != null) {
			locationDto = LocationDto.builder()
					.id(location.getId())
					.company(CompanyDto.builder()
							.id(location.getCompany().getId())
							.companyName(location.getCompany().getCompanyName())
							.companyApiKey(location.getCompany().getCompanyApiKey())
							.build())
					.locationName(location.getLocationName())
					.locationCountry(location.getLocationCountry())
					.locationCity(location.getLocationCity())
					.locationMeta(location.getLocationMeta())
					.build();
		}

		Sensor saved = sensorRepository.save(sensor);

		return SensorDto.builder()
				.id(saved.getId())
				.sensorName(saved.getSensorName())
				.sensorCategory(saved.getSensorCategory())
				.sensorMeta(saved.getSensorMeta())
				.sensorApiKey(saved.getSensorApiKey())
				.location(locationDto)
				.build();
	}

	@Override
	@Transactional
	public SensorDto deleteById(Long id, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);

		Sensor sensor = sensorRepository.findById(id.intValue())
				.orElseThrow(() -> new NoSuchElementException("Sensor no encontrado con ID: " + id));

		List<SensorData> sensordatas = sensorDataRepository.findBySensor_Id(sensor.getId());
		
		if ( !sensordatas.isEmpty() ) {
	    	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar un sensor con data asociada");
	    }
		
		Location location = sensor.getLocation();
		LocationDto locationDto = null;

		if (location != null) {
			locationDto = LocationDto.builder()
					.id(location.getId())
					.company(CompanyDto.builder()
							.id(location.getCompany().getId())
							.companyName(location.getCompany().getCompanyName())
							.companyApiKey(location.getCompany().getCompanyApiKey())
							.build())
					.locationName(location.getLocationName())
					.locationCountry(location.getLocationCountry())
					.locationCity(location.getLocationCity())
					.locationMeta(location.getLocationMeta())
					.build();
		}

		SensorDto dto = SensorDto.builder()
				.id(sensor.getId())
				.sensorName(sensor.getSensorName())
				.sensorCategory(sensor.getSensorCategory())
				.sensorMeta(sensor.getSensorMeta())
				.sensorApiKey(sensor.getSensorApiKey())
				.location(locationDto)
				.build();

		sensorRepository.delete(sensor);

		return dto;
	}

	@Override
	public SensorDto findByApiKey(String apiKey, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);
		if (!existsByApiKey(apiKey, companyApiKey)) {
			throw new RuntimeException("ApiKey not found: " + apiKey);
		}

		UUID uuidKey;
		try {
			uuidKey = UUID.fromString(apiKey);

		}catch (IllegalArgumentException e) {
			throw new RuntimeException("ApiKey not found" + apiKey);
		}

		Sensor sensor = sensorRepository.findBySensorApiKey(uuidKey);
		
		if(sensor == null) {
			throw new NoSuchElementException("No se encontro el sensor de api key: " + uuidKey);
		}


		Location location = sensor.getLocation();
		LocationDto locationDto = null;
		if (location != null) {
			locationDto = LocationDto.builder()
					.id(location.getId())
					.company(CompanyDto.builder()
							.id(location.getCompany().getId())
							.companyName(location.getCompany().getCompanyName())
							.companyApiKey(location.getCompany().getCompanyApiKey())
							.build())
					.locationName(location.getLocationName())
					.locationCountry(location.getLocationCountry())
					.locationCity(location.getLocationCity())
					.locationMeta(location.getLocationMeta())
					.build();
		}


		return SensorDto.builder()
				.id(sensor.getId())
				.sensorName(sensor.getSensorName())
				.sensorCategory(sensor.getSensorCategory())
				.sensorMeta(sensor.getSensorMeta())
				.sensorApiKey(sensor.getSensorApiKey())
				.location(locationDto)
				.build();
	}

	@Override
	public Boolean existsByApiKey(String apiKey, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);
		if (apiKey==null || apiKey.trim().isEmpty()) {
			return false;
		}

		try {
			UUID uuidKey = UUID.fromString(apiKey);
			return sensorRepository.existsBySensorApiKey(uuidKey);

		}catch (IllegalArgumentException e){
			return false;
		}

	}
	
}
