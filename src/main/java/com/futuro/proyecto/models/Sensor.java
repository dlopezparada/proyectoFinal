package com.futuro.proyecto.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sensors")
public class Sensor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_location")
	private Location location;

	@Column(name = "sensorname")
	private String sensorName;
	@Column(name = "sensorcategory")
	private String sensorCategory;
	@Column(name = "sensormeta")
	private String sensorMeta;
	@Column(name = "sensorapikey")
	private UUID sensorApiKey;
	
}
