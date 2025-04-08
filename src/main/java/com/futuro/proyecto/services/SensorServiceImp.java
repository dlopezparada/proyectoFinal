package com.futuro.proyecto.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import com.futuro.proyecto.dto.CompanyDto;
import com.futuro.proyecto.dto.LocationDto;
import com.futuro.proyecto.models.Location;
import com.futuro.proyecto.models.Sensor;
import com.futuro.proyecto.repository.CompanyRepository;
import com.futuro.proyecto.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.futuro.proyecto.dto.SensorDto;
import com.futuro.proyecto.repository.SensorRepository;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SensorServiceImp implements SensorService{
	
	@Autowired
    private  SensorRepository sensorRepository;
	@Autowired
	private  LocationRepository locationRepository;
	@Autowired
	private CompanyRepository companyRepository;


    public SensorServiceImp(SensorRepository sensorRepository,  LocationRepository locationRepository) {
        this.sensorRepository = sensorRepository;
        this.locationRepository = locationRepository;
    }
    
    private void validateCompanyApiKey(UUID companyApiKey) {
        if (companyApiKey == null) {
            throw new IllegalArgumentException("Company API key is required");
        }
        if (!companyRepository.existsByCompanyApiKey(companyApiKey)) {
            throw new IllegalArgumentException("Invalid company API key");
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
	public SensorDto findById(Long id) {
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El ID no puede ser nulo o menor o igual a cero");
		}

		Sensor sensor = sensorRepository.findById(id.intValue())
				.orElseThrow(() -> new RuntimeException("Sensor no encontrado con ID: " + id));

		Location location = sensor.getLocation();
		LocationDto locationDto = null;

		if (location != null) {
			locationDto = LocationDto.builder()
					.id(location.getId())
//					.companyId(location.getCompany().getId())
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
	public SensorDto create(SensorDto sensorDto) {

		Sensor sensor = new Sensor();
		sensor.setId(sensorDto.getId());
		sensor.setSensorName(sensorDto.getSensorName());
		sensor.setSensorCategory(sensorDto.getSensorCategory());
		sensor.setSensorMeta(sensorDto.getSensorMeta());
		sensor.setSensorApiKey(UUID.randomUUID());

		if (sensorDto.getLocation() != null && sensorDto.getLocation().getId() != null) {
			Location location = locationRepository.findById(sensorDto.getLocation().getId())
			.orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "La ubicación con ID " + sensorDto.getLocation().getId() + " no existe."));

			sensor.setLocation(location);
		}

		Sensor savedSensor = sensorRepository.save(sensor);

		Location location = savedSensor.getLocation();
		LocationDto locationDto = null;
		if (location != null) {
			locationDto = LocationDto.builder()
					.id(location.getId())
//					.companyId(location.getCompany().getId())
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
	public SensorDto updateByApiKey(String sensorApiKey, SensorDto sensorDto) {

		if (sensorApiKey == null || sensorApiKey.trim().isEmpty()) {
			throw new IllegalArgumentException("API key no puede ser nulo o vacío");
		}

		UUID uuid = UUID.fromString(sensorApiKey);

		Sensor sensor = sensorRepository.findBySensorApiKey(uuid);
		
		if(sensor == null) {
			throw new NoSuchElementException("No se encontro el sensor de api key: " + uuid);
		}

		// Actualizamos los campos editables
		sensor.setSensorName(sensorDto.getSensorName());
		sensor.setSensorCategory(sensorDto.getSensorCategory());
		sensor.setSensorMeta(sensorDto.getSensorMeta());

		if (sensorDto.getLocation() != null) {
			Location location = locationRepository.findById(sensorDto.getLocation().getId())
					.orElseThrow(() -> new RuntimeException("Location no encontrada con id: " + sensorDto.getLocation().getId()));
			sensor.setLocation(location);
		}
		Location location = sensor.getLocation();
		LocationDto locationDto = null;
		if (location != null) {
			locationDto = LocationDto.builder()
					.id(location.getId())
//					.companyId(location.getCompany().getId())
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
				.location(locationDto) // Puedes mapear esto mejor si es necesario
				.build();
	}

	@Override
	@Transactional
	public SensorDto deleteById(Long id) {

		Sensor sensor = sensorRepository.findById(id.intValue())
				.orElseThrow(() -> new RuntimeException("Sensor no encontrado con ID: " + id));

		Location location = sensor.getLocation();
		LocationDto locationDto = null;

		if (location != null) {
			locationDto = LocationDto.builder()
					.id(location.getId())
//					.companyId(location.getCompany().getId())
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
	public SensorDto findByApiKey(String apiKey) {

		if (!existsByApiKey(apiKey)) {
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
//					.companyId(location.getCompany().getId())
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
	public Boolean existsByApiKey(String apiKey) {

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
