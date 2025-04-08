package com.futuro.proyecto.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataInputDto {

	private String api_key;
    private List<SensorDataValueInputDto> json_data;
}
