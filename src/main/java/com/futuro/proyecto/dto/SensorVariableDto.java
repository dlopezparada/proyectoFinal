package com.futuro.proyecto.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorVariableDto {

	private Integer id;
	private SensorDto sensor;
	private String name;
	private String unit;
}
