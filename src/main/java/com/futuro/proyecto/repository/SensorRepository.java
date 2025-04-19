package com.futuro.proyecto.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.futuro.proyecto.models.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer>{

	Sensor findBySensorApiKey(UUID sensorApiKey);
    Boolean existsBySensorApiKey(UUID sensorApiKey);
    
    @Query("SELECT s.id FROM Sensor s WHERE s.location.company.companyApiKey = :companyKey")
    List<Integer> findIdsByCompanyApiKey(@Param("companyKey") UUID companyKey);
	List<Sensor> findByLocation_Id(Long id);
}
