package com.futuro.proyecto.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.proyecto.dto.SensorDataDto;
import com.futuro.proyecto.dto.SensorDataInputDto;
import com.futuro.proyecto.services.SensorDataService;

@RestController
@RequestMapping("/sensorData")
public class SensorDataController {
	
	@Autowired
	private SensorDataService sensorDataService;
	
	@PostMapping("/insertData")
    @PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<String> receiveSensorData(@RequestBody SensorDataDto sensorDataDto) {
        sensorDataService.processSensorData(sensorDataDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Data received successfully");
    }
	
}
