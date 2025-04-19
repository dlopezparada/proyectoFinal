package com.futuro.proyecto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.proyecto.models.SensorData;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer>{

	Optional<SensorData> findById(Long sensorId);

	List<SensorData> findBySensor_Id(Integer id); 
		
	
}
