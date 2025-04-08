package com.futuro.proyecto.services;

import java.util.List;
import java.util.UUID;

import com.futuro.proyecto.dto.LocationDto;

public interface LocationService {
	

    LocationDto findById(Long id, UUID companyApiKey);

    LocationDto create(LocationDto locationDto, UUID companyApiKey);

    LocationDto update(LocationDto locationDto, UUID companyApiKey);

    LocationDto deleteById(Long id, UUID companyApiKey);

	List<LocationDto> findAll(UUID companyApiKey);

}
