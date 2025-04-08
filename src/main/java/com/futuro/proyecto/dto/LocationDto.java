package com.futuro.proyecto.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

	private Long id;
	private CompanyDto company;
	private String locationName;
	private String locationCountry;
	private String locationCity;
	private String locationMeta;
}
