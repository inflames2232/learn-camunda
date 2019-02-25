package com.example.shipmentservice.messaging;

import com.example.shipmentservice.ProcessConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.shipment.ShipmentOrderRequest;
import dto.shipment.ShipmentOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaShipmentListener {

    private final ProcessEngine processEngine;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "shipmentOrderSend")
    //@SendTo("shipmentOrderReceive")
    public void listenWithHeaders(
            @Payload ShipmentOrderRequest shipmentOrderRequest
            /*@Headers KafkaHeaders kafkaHeaders*/
    ) throws JsonProcessingException {
        log.info("receive request {}", shipmentOrderRequest);

        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcessConstants.VAR_NAME_validatedOrder, objectMapper.writeValueAsString(shipmentOrderRequest));
        processEngine.getRuntimeService()
                .startProcessInstanceByKey(ProcessConstants.PROCESS_KEY_shipment, variables);
    }
}
