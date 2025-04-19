package com.futuro.proyecto.dto;


import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SensorDto {

	private Integer id;

	@NotNull(message = "La ubicación es obligatoria")
	private LocationDto location;

	@NotBlank(message = "El nombre del sensor es obligatorio")
	private String sensorName;

	@NotBlank(message = "La categoría del sensor es obligatoria")
	private String sensorCategory;

	@NotBlank(message = "El campo meta del sensor es obligatorio")
	private String sensorMeta;
	private Long locationId;

	private UUID sensorApiKey;
	
}