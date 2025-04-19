package com.futuro.proyecto.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.proyecto.dto.ResponseDataDto;
import com.futuro.proyecto.dto.SensorDataDto;
import com.futuro.proyecto.services.SensorDataService;

@RestController
@RequestMapping("/api")
public class SensorDataController {
	
	@Autowired
	private SensorDataService sensorDataService;
	
	@PostMapping("/v1/sensor_data")
//	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<String> receiveSensorData(@RequestBody SensorDataDto sensorDataDto) {
        sensorDataService.processSensorData(sensorDataDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Datos ingresados correctamente");
    }
	
	@GetMapping("/v1/sensor_data")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> getSensorData(
            @RequestParam("from") Long fromEpoch,
            @RequestParam("to") Long toEpoch,
            @RequestParam("sensor_id") List<Integer> sensorIds,
            @RequestParam("company_api_key") String companyApiKey){

        List<ResponseDataDto> data = sensorDataService.getSensorData(fromEpoch, toEpoch, sensorIds, companyApiKey);
        if (data == null || data.isEmpty()) {
             return ResponseEntity.ok(Collections.emptyList());
        } else {
            return ResponseEntity.ok(data);
        }
	}
	
}
