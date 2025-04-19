package com.futuro.proyecto.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.proyecto.dto.CompanyDto;
import com.futuro.proyecto.models.Admin;
import com.futuro.proyecto.models.Company;
import com.futuro.proyecto.repository.AdminRepository;
import com.futuro.proyecto.repository.CompanyRepository;

@Service
public class CompanyServiceImp implements CompanyService{

	@Autowired
    private CompanyRepository companyRepository;
	
	@Autowired
    private AdminRepository adminRepository;

    @Override
    public List<CompanyDto> findAll() {
        List<Company> companies = companyRepository.findAll();
        if (companies == null) {
            return new ArrayList<CompanyDto>();
        }
        return companies.stream().map(companyTemporal ->
                CompanyDto.builder()
                        .id(companyTemporal.getId())
                        .companyName(companyTemporal.getCompanyName())
                        .companyApiKey(companyTemporal.getCompanyApiKey())
                        .build()
        ).toList();
    }
    
    @Override
    public CompanyDto create(CompanyDto companyDto, Long adminId) {
        System.out.println("Admin ID recibido en el servicio: " + adminId);
        
        Admin admin = adminRepository.findById(adminId).orElse(new Admin());
        if (admin == null || admin.getId() == null) {
        	System.out.println("Id de admin invalido " + adminId);
            return new CompanyDto();
        }
        if (companyDto.getCompanyName() == null || companyDto.getCompanyName().trim().equals("")) {
        	throw new IllegalArgumentException("Nombre de compañia nulo o vacio");
        }

        Company company = Company.builder()
                .companyName(companyDto.getCompanyName())
                .companyApiKey(UUID.randomUUID())
                .admin(admin)
                .build();

        Company companyInsertada = companyRepository.save(company);

        if (companyInsertada == null) {
            return new CompanyDto();
        }

        return mapCompanyToDto(companyInsertada);
    }
    
    private CompanyDto mapCompanyToDto(Company company) {
        return CompanyDto.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .companyApiKey(company.getCompanyApiKey())
                .build();
    }
    
    
    @Override
    public CompanyDto findByApiKey(String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
        Company company = companyRepository.findByCompanyApiKey(companyUuid).orElse(new Company());
        if (company == null || company.getId() == null) {
            return new CompanyDto();
        }
        return CompanyDto.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .companyApiKey(company.getCompanyApiKey())
                .build();
    }

	@Override
	public CompanyDto update(CompanyDto companyDto, String companyApiKey) {
		UUID companyUuid = UUID.fromString(companyApiKey);
		validateCompanyApiKey(companyUuid);
		System.out.println("Tratando de actualizar company de companyApiKey "+companyApiKey);
		if(companyDto == null) {
			throw new IllegalArgumentException("companyDto nulo o vacio");
		}
		if(companyDto.getCompanyName() == null || companyDto.getCompanyName().trim().equals("")) {
			throw new IllegalArgumentException("companyName nulo o vacio");
		}
		Company companyExistente = companyRepository.findByCompanyApiKey(companyUuid).orElseThrow(() -> new NoSuchElementException("Company no encontrado"));
		companyExistente.setCompanyName(companyDto.getCompanyName());
		
		Company companyActualizada = companyRepository.save(companyExistente);
		
		return CompanyDto.builder()
				.id(companyActualizada.getId())
				.companyName(companyActualizada.getCompanyName())
				.companyApiKey(companyActualizada.getCompanyApiKey())
				.build();
		
	}
	
	private void validateCompanyApiKey(UUID companyApiKey) {
        if (companyApiKey == null) {
            throw new IllegalArgumentException("Company API key es requerido");
        }
        if (!companyRepository.existsByCompanyApiKey(companyApiKey)) {
            throw new IllegalArgumentException("company API key invalido");
        }
    }

    

}
