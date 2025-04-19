package com.futuro.proyecto.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.proyecto.dto.CompanyDto;
import com.futuro.proyecto.services.CompanyService;

@RestController
@RequestMapping("/companies")
@PreAuthorize("denyAll()")
public class CompanyController {
	
	@Autowired
    private CompanyService companyService;
	
	@PostMapping("/{adminId}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto, @PathVariable Long adminId) {
		System.out.println("Admin ID recibido: " + adminId);
        CompanyDto companyInsertada = companyService.create(companyDto, adminId);
        if (companyInsertada.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(companyInsertada);
        }
        return ResponseEntity.status(HttpStatus.OK).body(companyInsertada);
    }
	
	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<CompanyDto> update(@RequestBody CompanyDto companyDto, @RequestHeader("company_api_key") String companyApiKey){
		CompanyDto companyActualizada = companyService.update(companyDto,companyApiKey);
		return ResponseEntity.status(HttpStatus.OK).body(companyActualizada);
	}
	
	@GetMapping("/findAll")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<List<CompanyDto>> findAll(){
		List<CompanyDto> companies = companyService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(companies);
	}
	
	@GetMapping("/findByApiKey/{companyApiKey}")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<CompanyDto> findByApiKey(@PathVariable String companyApiKey){
		CompanyDto companyDto = companyService.findByApiKey(companyApiKey);
		return ResponseEntity.status(HttpStatus.OK).body(companyDto);
	}
}
