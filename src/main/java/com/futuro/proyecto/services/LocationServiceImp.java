package com.futuro.proyecto.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.futuro.proyecto.dto.CompanyDto;
import com.futuro.proyecto.dto.LocationDto;
import com.futuro.proyecto.models.Company;
import com.futuro.proyecto.models.Location;
import com.futuro.proyecto.models.Sensor;
import com.futuro.proyecto.repository.CompanyRepository;
import com.futuro.proyecto.repository.LocationRepository;
import com.futuro.proyecto.repository.SensorRepository;

@Service
public class LocationServiceImp implements LocationService{
	
	@Autowired
    private LocationRepository locationRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private SensorRepository sensorRepository;

	@Override
    public List<LocationDto> findAll(UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);
		
		List<Location> locationList = locationRepository.findAll();
        if (locationList == null || locationList.isEmpty()) {
            throw new NoSuchElementException("No se encontraron ubicaciones.");
        }
        
        return locationList.stream()
                .map(location -> LocationDto.builder()
                        .id(location.getId())
                        .locationName(location.getLocationName())
                        .locationCountry(location.getLocationCountry())
                        .locationCity(location.getLocationCity())
                        .locationMeta(location.getLocationMeta())
                        .company(CompanyDto.builder()
                                .id(location.getCompany().getId())
                                .companyName(location.getCompany().getCompanyName())
                                .build())
                        .build())
                .collect(Collectors.toList());
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
	public LocationDto findById(Long id, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);
	    Location location = locationRepository.findById(id).orElse(new Location());
	    if (location == null || location.getId() == null) {
	        throw new NoSuchElementException("No se encontro la location.");
	    }
	    return LocationDto.builder()
	            .id(location.getId())
	            .locationName(location.getLocationName())
	            .locationCountry(location.getLocationCountry())
	            .locationCity(location.getLocationCity())
	            .locationMeta(location.getLocationMeta())
	            .company(CompanyDto.builder()
	                    .id(location.getCompany().getId())
	                    .companyName(location.getCompany().getCompanyName())
//	                    .companyApiKey(location.getCompany().getCompanyApiKey())
	                    .build())
	            .build();
	}

	@Override
	public LocationDto create(LocationDto locationDto, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);
		
	    System.out.println("Company API Key recibido en el servicio: " +companyApiKey);

	    Company company = companyRepository.findByCompanyApiKey(companyApiKey).orElseThrow(() -> new NoSuchElementException("company api kei invalido: " + companyApiKey));
	    
	    if (locationDto.getLocationName() == null || locationDto.getLocationName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ubicación no puede estar vacío o nulo.");
	    }
	    if (locationDto.getLocationCountry() == null || locationDto.getLocationCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("El país de la ubicación no puede estar vacío o nulo.");
	    }
	    if (locationDto.getLocationCity() == null || locationDto.getLocationCity().trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad de la ubicación no puede estar vacía o nulo.");
	    }

	    Location location = Location.builder()
	            .locationName(locationDto.getLocationName())
	            .locationCountry(locationDto.getLocationCountry())
	            .locationCity(locationDto.getLocationCity())
	            .locationMeta(locationDto.getLocationMeta())
	            .company(company)
	            .build();

	    Location locationInsertada = locationRepository.save(location);

	    if (locationInsertada == null) {
            throw new RuntimeException("Error al guardar la ubicación.");
	    }

	    return mapLocationToDto(locationInsertada);
	}
	
	private LocationDto mapLocationToDto(Location location) {
	    if (location == null) {
	        return null;
	    }
	    return LocationDto.builder()
	            .id(location.getId())
	            .locationName(location.getLocationName())
	            .locationCountry(location.getLocationCountry())
	            .locationCity(location.getLocationCity())
	            .locationMeta(location.getLocationMeta())
	            .company(mapCompanyToDto(location.getCompany()))
	            .build();
	}
	
	private CompanyDto mapCompanyToDto(Company company) {
	    if (company == null) {
	        return null;
	    }
	    return CompanyDto.builder()
	            .id(company.getId())
	            .companyName(company.getCompanyName())
	            .build();
	}

	@Override
	public LocationDto update(LocationDto locationDto, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);
	    System.out.println("Company API Key recibido en el servicio: " +companyApiKey);
	    
	    if (locationDto == null || locationDto.getId() == null) {
            throw new NoSuchElementException("ID de ubicación no proporcionado.");
        }

        if (locationDto.getLocationName() == null || locationDto.getLocationName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la ubicación no puede estar vacío o nulo.");
        }
        if (locationDto.getLocationCountry() == null || locationDto.getLocationCountry().trim().isEmpty()) {
            throw new IllegalArgumentException("El país de la ubicación no puede estar vacío o nulo.");
        }
        if (locationDto.getLocationCity() == null || locationDto.getLocationCity().trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad de la ubicación no puede estar vacía o nulo.");
        }
	    
	    Location existingLocation = locationRepository.findById(locationDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Ubicación con ID " + locationDto.getId() + " no encontrada."));

	    existingLocation.setLocationName(locationDto.getLocationName());
	    existingLocation.setLocationCountry(locationDto.getLocationCountry());
	    existingLocation.setLocationCity(locationDto.getLocationCity());
	    existingLocation.setLocationMeta(locationDto.getLocationMeta());

	    Location locationActualizada = locationRepository.save(existingLocation);

	    return LocationDto.builder()
	            .id(locationActualizada.getId())
	            .locationName(locationActualizada.getLocationName())
	            .locationCountry(locationActualizada.getLocationCountry())
	            .locationCity(locationActualizada.getLocationCity())
	            .locationMeta(locationActualizada.getLocationMeta())
	            .company(CompanyDto.builder()
	                    .id(locationActualizada.getCompany().getId())
	                    .companyName(locationActualizada.getCompany().getCompanyName())
//	                    .companyApiKey(locationActualizada.getCompany().getCompanyApiKey())
	                    .build())
	            .build();
	}

	@Override
	public LocationDto deleteById(Long id, UUID companyApiKey) {
		validateCompanyApiKey(companyApiKey);
	    System.out.println("Company API Key recibido en el servicio: " +companyApiKey);

	    Location location = locationRepository.findById(id)
	    		.orElseThrow(() -> new NoSuchElementException("Ubicación no encontrada con el id: " + id));
	    
	    List<Sensor> sensors = sensorRepository.findByLocation_Id(location.getId());
	    
	    if ( !sensors.isEmpty() ) {
	    	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede eliminar un location con sensores asociados");
	    }
	    
        locationRepository.deleteById(id);
        
        return LocationDto.builder()
                .id(location.getId())
                .locationName(location.getLocationName())
                .locationCountry(location.getLocationCountry())
                .locationCity(location.getLocationCity())
                .locationMeta(location.getLocationMeta())
                .company(CompanyDto.builder()
                        .id(location.getCompany().getId())
                        .companyName(location.getCompany().getCompanyName())
                        .build())
                .build();
    }

	

}
