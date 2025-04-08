package com.futuro.proyecto.dto;

import java.sql.Date;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorDataValueDto {

	private Integer id;
	private SensorDataDto data;
	private Integer variableId;
	private String value;
	private Date fechaRecepcion;
}
