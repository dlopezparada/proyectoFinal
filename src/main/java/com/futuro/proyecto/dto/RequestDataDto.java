package com.futuro.proyecto.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestDataDto {
	
	private Long from;
	private Long to;
	private List<Long> id; 

}
