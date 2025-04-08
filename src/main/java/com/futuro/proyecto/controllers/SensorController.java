package com.futuro.proyecto.controllers;

import com.futuro.proyecto.dto.ApiResponse;
import com.futuro.proyecto.dto.SensorDto;
import com.futuro.proyecto.services.SensorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
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
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<SensorDto>> findAll(@RequestHeader("company_api_key") String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		return ResponseEntity.ok(sensorService.findAll(companyUuid));
	}

	// aca se crea los sensores
	@PostMapping("/crear")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<?> create(@Valid @RequestBody SensorDto sensorDto, BindingResult result) {
		if (result.hasErrors()) {
			// se entrega los errores de campos vacios
			List<String> errores = result.getFieldErrors().stream()
					.map(err -> err.getField() + ": " + err.getDefaultMessage()).toList();
			return ResponseEntity.badRequest().body(errores);
		}

		SensorDto saved = sensorService.create(sensorDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	// controlador que nos buscar sensor x tocken
	@GetMapping("/buscar-sensor/{apiKey}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<SensorDto> buscarSensorKey(@PathVariable String apiKey) {
		try {
			SensorDto dto = sensorService.findByApiKey(apiKey);
			return ResponseEntity.ok(dto);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// controlador que nos permite validar tocken de sensor
	@GetMapping("/exists/{apiKey}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<?> existsByApiKey(@PathVariable String apiKey) {
		boolean exists = sensorService.existsByApiKey(apiKey);
		if (exists) {
			return ResponseEntity.ok("Valid Token!!");
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			// return ResponseEntity.ok("Tocken Valids!!" + exists);
		}
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<ApiResponse<SensorDto>> delete(@PathVariable Long id) {
		SensorDto deletedSensor = sensorService.deleteById(id);
		ApiResponse<SensorDto> respuesta = ApiResponse.<SensorDto>builder().message("Sensor deleted successfully")
				.data(deletedSensor).build();

		return ResponseEntity.ok(respuesta);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<SensorDto> findById(@PathVariable Long id) {
		SensorDto sensorDto = sensorService.findById(id);
		return ResponseEntity.ok(sensorDto);
	}

	@PutMapping("/update/apikey/{sensorApiKey}")
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<SensorDto> updateByApiKey(@PathVariable String sensorApiKey,@RequestBody @Valid SensorDto sensorDto) {

		SensorDto updated = sensorService.updateByApiKey(sensorApiKey, sensorDto);
		return ResponseEntity.ok(updated);
	}

}
