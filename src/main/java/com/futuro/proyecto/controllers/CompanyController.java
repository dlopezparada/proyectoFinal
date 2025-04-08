package com.futuro.proyecto.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto, @PathVariable Long adminId) {
		System.out.println("Admin ID recibido: " + adminId);
        CompanyDto companyInsertada = companyService.create(companyDto, adminId);
        if (companyInsertada.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(companyInsertada);
        }
        return ResponseEntity.status(HttpStatus.OK).body(companyInsertada);
    }

}
