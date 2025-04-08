package com.futuro.proyecto.services;

import java.util.List;
import java.util.UUID;

import com.futuro.proyecto.dto.SensorDto;

public interface SensorService {
	
	List<SensorDto> findAll(UUID companyApiKey);

    SensorDto findById(Long id);

    SensorDto create(SensorDto sensorDto);

    //SensorDto update(SensorDto sensorDto);

    SensorDto updateByApiKey(String sensorApiKey, SensorDto sensorDto);

    SensorDto deleteById(Long id);

    SensorDto findByApiKey(String apiKey);

    Boolean existsByApiKey(String apiKey);

}