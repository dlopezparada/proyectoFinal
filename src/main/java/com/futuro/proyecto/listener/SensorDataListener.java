package com.futuro.proyecto.listener;

import com.futuro.proyecto.dto.SensorDataDto;
import com.futuro.proyecto.services.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SensorDataListener {

    @Autowired
    private SensorDataService sensorDataService;

    @KafkaListener(topics = "tf-minera-01", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "sensorKafkaListenerFactory")
    public void listenSensorData(SensorDataDto sensorDataRequest) {

        if (sensorDataRequest.getJsonData() != null) {
            sensorDataService.saveSensorDataFromKafka(sensorDataRequest.getApiKey(), sensorDataRequest.getJsonData());
        } else {
            System.out.println("json_data vino como null");
        }
    }
}