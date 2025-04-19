package com.futuro.proyecto.services;

import com.futuro.proyecto.dto.SensorDataValueDto;

import java.util.List;

public interface SensorDataValueService {
    List<SensorDataValueDto> findAll();
    SensorDataValueDto findById(Long id);
    SensorDataValueDto create(SensorDataValueDto dto);
    void deleteById(Long id);
}

