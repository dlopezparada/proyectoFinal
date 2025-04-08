package com.futuro.proyecto.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.proyecto.models.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
	
	Optional<Company> findByCompanyApiKey(UUID companyApiKey);

    Boolean existsByCompanyApiKey(UUID companyApiKey);

}
