package com.futuro.proyecto.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.proyecto.dto.LocationDto;
import com.futuro.proyecto.services.LocationService;

@RestController
@RequestMapping("/locations")
//@PreAuthorize("denyAll()")
public class LocationController {

	@Autowired
	private LocationService locationService;
	
	@GetMapping("/findAll")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
//	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<LocationDto>> findAll(@RequestHeader("company_api_key") String companyApiKey) {
        UUID companyUuid = UUID.fromString(companyApiKey);
        List<LocationDto> locationsDto = locationService.findAll(companyUuid);
        return ResponseEntity.ok(locationsDto);
    }
	
	@PostMapping("/createLocation")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
//	@PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<LocationDto> create(@RequestBody LocationDto locationDto, @RequestHeader("company_api_key") String companyApiKey) {
        UUID companyUuid = UUID.fromString(companyApiKey);
        LocationDto locationInsertada = locationService.create(locationDto, companyUuid);
        return ResponseEntity.status(HttpStatus.CREATED).body(locationInsertada);
    }
	
	@GetMapping("/find/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
//	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<LocationDto> findById(@PathVariable Long id, @RequestHeader("company_api_key") String companyApiKey){
		UUID companyUuid = UUID.fromString(companyApiKey);
		LocationDto locationDto = locationService.findById(id, companyUuid);
		return ResponseEntity.status(HttpStatus.OK.value()).body(locationDto);
	}

	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
//	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<LocationDto> update(@RequestBody LocationDto locationDto, @RequestHeader("company_api_key") String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		LocationDto locationActualizado = locationService.update(locationDto, companyUuid);
		return ResponseEntity.status(HttpStatus.OK).body(locationActualizado);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
//	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<LocationDto> deleteById(@PathVariable Long id, @RequestHeader("company_api_key") String companyApiKey){
		UUID companyUuid = UUID.fromString(companyApiKey);
        locationService.deleteById(id, companyUuid);
        return ResponseEntity.status(HttpStatus.OK).body(new LocationDto());
	}
	
}
