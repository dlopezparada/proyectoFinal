package com.futuro.proyecto.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.proyecto.models.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long>{

	
}
