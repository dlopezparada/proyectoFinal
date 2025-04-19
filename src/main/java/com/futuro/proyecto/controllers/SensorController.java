package com.futuro.proyecto.controllers;

import com.futuro.proyecto.dto.ApiResponse;
import com.futuro.proyecto.dto.SensorDto;
import com.futuro.proyecto.services.SensorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sensors")
@PreAuthorize("denyAll()")
public class SensorController {

	private final SensorService sensorService;

	public SensorController(SensorService sensorService) {
		this.sensorService = sensorService;
	}

	// traemos todo los sensores
	@GetMapping("/listar")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<List<SensorDto>> findAll(@RequestHeader("company_api_key") String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		return ResponseEntity.ok(sensorService.findAll(companyUuid));
	}

	// aca se crea los sensores
	@PostMapping("/crear")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> create(@RequestBody SensorDto sensorDto, @RequestHeader("company_api_key") String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		SensorDto saved = sensorService.create(sensorDto, companyUuid);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	// controlador que nos buscar sensor x tocken
	@GetMapping("/buscar-sensor/{apiKey}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<SensorDto> buscarSensorKey(@PathVariable String apiKey, @RequestHeader("company_api_key") String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		SensorDto dto = sensorService.findByApiKey(apiKey, companyUuid);
		return ResponseEntity.ok(dto);
	}

	// controlador que nos permite validar tocken de sensor
	@GetMapping("/exists/{apiKey}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> existsByApiKey(@PathVariable String apiKey, @RequestHeader("company_api_key") String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		boolean exists = sensorService.existsByApiKey(apiKey, companyUuid);
		if (exists) {
			return ResponseEntity.ok("Valid Token!!");
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<ApiResponse<SensorDto>> delete(@PathVariable Long id, @RequestHeader("company_api_key") String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		SensorDto deletedSensor = sensorService.deleteById(id, companyUuid);
		ApiResponse<SensorDto> respuesta = ApiResponse.<SensorDto>builder().message("Sensor deleted successfully")
				.data(deletedSensor).build();

		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<SensorDto> findById(@PathVariable Long id, @RequestHeader("company_api_key") String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		SensorDto sensorDto = sensorService.findById(id, companyUuid);
		return ResponseEntity.ok(sensorDto);
	}

	@PutMapping("/update/apikey/{sensorApiKey}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<SensorDto> updateByApiKey(@PathVariable String sensorApiKey,@RequestBody SensorDto sensorDto, @RequestHeader("company_api_key") String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		SensorDto updated = sensorService.updateByApiKey(sensorApiKey, sensorDto, companyUuid);
		return ResponseEntity.ok(updated);
	}

}
