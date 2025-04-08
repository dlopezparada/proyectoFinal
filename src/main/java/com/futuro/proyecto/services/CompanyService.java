package com.futuro.proyecto.services;

import java.util.List;

import com.futuro.proyecto.dto.CompanyDto;

public interface CompanyService {

	public List<CompanyDto> findAll();

	public CompanyDto create(CompanyDto companyDto, Long idAdmin);
	
	public CompanyDto getById(Long id);
	
//	public CompanyDto update(Long id, CompanyDto companyDto, Long idAdmin);

}
