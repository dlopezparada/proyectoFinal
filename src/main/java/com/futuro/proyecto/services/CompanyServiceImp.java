package com.futuro.proyecto.services;

import java.util.ArrayList;
import java.util.List;
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
    
//    @Override
//    public CompanyDto create(CompanyDto companyDto, Long adminId) {
//    	System.out.println("Admin ID recibido en el servicio: " + adminId);
//        if (companyRepository.existsById(companyDto.getId())) {
//            return new CompanyDto();
//        }
//
//        Admin admin = adminRepository.findById(adminId).orElse(null);
//        if (admin == null) {
//            return new CompanyDto();
//        }
//
//        Company company = Company.builder()
//                .id(companyDto.getId())
//                .companyName(companyDto.getCompanyName())
//                .companyApiKey(UUID.randomUUID())
//                .admin(admin)
//                .build();
//
//        Company companyInsertada = companyRepository.save(company);
//
//        if (companyInsertada == null) {
//            return new CompanyDto();
//        }
//
//        return companyDto;
//    }
    
    @Override
    public CompanyDto getById(Long id) {
        Company company = companyRepository.findById(id).orElse(new Company());
        if (company == null || company.getId() == null) {
            return new CompanyDto();
        }
        return CompanyDto.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .companyApiKey(company.getCompanyApiKey())
                .build();
    }

    

}
