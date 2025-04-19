package com.futuro.proyecto.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorDataValueDto {

	private Integer id;
	private Integer sensorDataId;
	private String variable;
	private String value;
	private Timestamp fecha;
}
