package com.futuro.proyecto.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SensorDataDto {

	@JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("json_data")
    private MedicionDTO[] jsonData;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public MedicionDTO[] getJsonData() {
        return jsonData;
    }

    public void setJsonData(MedicionDTO[] jsonData) {
        this.jsonData = jsonData;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MedicionDTO {
        @JsonProperty("datetime")
        private Long datetime;

        private Map<String, Object> mediciones;

        public Long getDatetime() {
            return datetime;
        }


        public void setDatetime(Long datetime) {
            this.datetime = datetime;
        }

        public Map<String, Object> getMediciones() {
            return mediciones;
        }

        public void setMediciones(Map<String, Object> mediciones) {
            this.mediciones = mediciones;
        }

        public MedicionDTO() {
        }

        public MedicionDTO(Long datetime, Map<String, Object> mediciones) {
            this.datetime = datetime;
            this.mediciones = mediciones;
        }

        @JsonProperty("temp")
        public void setTemp(Double temp) {
            addMedicion("temp", temp);
        }

        @JsonProperty("humidity")
        public void setHumidity(Double humidity) {
            addMedicion("humidity", humidity);
        }

        @JsonProperty("velocidad")
        public void setVelocidad(Double velocidad) {
            addMedicion("velocidad", velocidad);
        }

        @JsonProperty("posicion")
        public void setPosicion(Double posicion) {
            addMedicion("posicion", posicion);
        }

        @JsonProperty("voltaje")
        public void setVoltaje(Double voltaje) {
            addMedicion("voltaje", voltaje);
        }

        @JsonProperty("corriente")
        public void setCorriente(Double corriente) {
            addMedicion("corriente", corriente);
        }

        private void addMedicion(String key, Object value) {
            if (this.mediciones == null) {
                this.mediciones = new java.util.HashMap<>();
            }
            this.mediciones.put(key, value);
        }
    }
}
