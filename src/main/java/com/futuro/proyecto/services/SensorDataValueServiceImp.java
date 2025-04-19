package com.futuro.proyecto.services;

import com.futuro.proyecto.dto.SensorDataValueDto;
import com.futuro.proyecto.models.SensorData;
import com.futuro.proyecto.models.SensorDataValue;
import com.futuro.proyecto.repository.SensorDataRepository;
import com.futuro.proyecto.repository.SensorDataValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SensorDataValueServiceImp implements SensorDataValueService {

    @Autowired
    private SensorDataValuesRepository sensorDataValueRepository;

    @Autowired
    private SensorDataRepository sensorDataRepository;

    @Override
    public List<SensorDataValueDto> findAll() {
        return sensorDataValueRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SensorDataValueDto findById(Long id) {
        return sensorDataValueRepository.findById(id.intValue())
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public SensorDataValueDto create(SensorDataValueDto dto) {
        Optional<SensorData> sensorDataOpt = sensorDataRepository.findById(dto.getSensorDataId());
        if (sensorDataOpt.isEmpty()) {
            return null;
        }

        SensorData sensorData = sensorDataOpt.get();

        SensorDataValue entity = SensorDataValue.builder()
                .data(sensorDataOpt.get())
                .value(dto.getValue())
                .variable(dto.getVariable())
                .fechaMedicion(sensorData.getFechaIngreso())
                .build();

        SensorDataValue saved = sensorDataValueRepository.save(entity);
        return toDto(saved);
    }

    @Override
    public void deleteById(Long id) {
        sensorDataValueRepository.deleteById(id.intValue());
    }

    private SensorDataValueDto toDto(SensorDataValue value) {
        return SensorDataValueDto.builder()
                .id(value.getId())
                .sensorDataId(value.getData() != null ? value.getData().getId() : null)
                .value(value.getValue())
                .variable(value.getVariable())
                .fecha(value.getFechaMedicion())
                .build();
    }
}

