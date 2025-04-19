package com.futuro.proyecto.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.proyecto.models.SensorDataValue;

@Repository
public interface SensorDataValuesRepository extends JpaRepository<SensorDataValue, Integer>{

	List<SensorDataValue> findByDataSensorIdInAndFechaMedicionBetween(List<Integer> sensorIds, Timestamp fechaInicio, Timestamp fechaFin);

}
