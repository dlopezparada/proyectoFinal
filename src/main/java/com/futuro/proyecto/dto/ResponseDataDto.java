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
public class ResponseDataDto {

    private Integer sensorId;
    private Long datetime;
    private Map<String, String> variables;
}