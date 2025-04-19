package com.futuro.proyecto.services;

import java.util.List;
import java.util.UUID;

import com.futuro.proyecto.dto.SensorDto;

public interface SensorService {
	
	List<SensorDto> findAll(UUID companyApiKey);

    SensorDto findById(Long id, UUID companyApiKey);

    SensorDto create(SensorDto sensorDto, UUID companyApiKey);

    SensorDto updateByApiKey(String sensorApiKey, SensorDto sensorDto, UUID companyApiKey);

    SensorDto deleteById(Long id, UUID companyApiKey);

    SensorDto findByApiKey(String apiKey, UUID companyApiKey);

    Boolean existsByApiKey(String apiKey, UUID companyApiKey);

}