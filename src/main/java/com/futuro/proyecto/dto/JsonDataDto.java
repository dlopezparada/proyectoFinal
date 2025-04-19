package com.futuro.proyecto.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonDataDto {
    private long datetime;
    private Map<String, Object> variables = new HashMap<>();

    @JsonAnySetter
    public void addVariable(String key, Object value) {
        if (!"datetime".equals(key)) {
            variables.put(key, value);
        }
    }
}