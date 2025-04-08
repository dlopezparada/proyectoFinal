package com.futuro.proyecto.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.proyecto.models.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer>{

	Sensor findBySensorApiKey(UUID sensorApiKey);
    Boolean existsBySensorApiKey(UUID sensorApiKey);
}
