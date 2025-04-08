package com.futuro.proyecto.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataValueInputDto {

	private long datetime;
    private Map<String, Double> variables;
    
}
