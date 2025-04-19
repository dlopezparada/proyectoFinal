package com.futuro.proyecto.services;

import java.util.List;

import com.futuro.proyecto.dto.JsonDataDto;
import com.futuro.proyecto.dto.ResponseDataDto;
import com.futuro.proyecto.dto.SensorDataDto;
import com.futuro.proyecto.dto.SensorDataInputDto;

public interface SensorDataService {
	
	void processSensorData(SensorDataDto sensorDataDto);
	void saveSensorDataFromKafka(String apiKey, List<JsonDataDto> readings);
	List<ResponseDataDto> getSensorData(Long from, Long to, List<Integer> sensorIds,String companyApiKey);
    
}
