package com.futuro.proyecto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.proyecto.models.SensorDataValue;

@Repository
public interface SensorDataValuesRepository extends JpaRepository<SensorDataValue, Integer>{

}
