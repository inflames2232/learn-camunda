package com.example.shipmentservice.adapter;

import com.example.shipmentservice.ProcessConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.shipment.ShipmentOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendShipmentAdapter implements JavaDelegate {

    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String shipmentOrderResponseJson = (String) delegateExecution.getVariable(ProcessConstants.VAR_NAME_shipmentOrder);
        ShipmentOrderResponse shipmentOrderResponse = objectMapper.readValue(shipmentOrderResponseJson, ShipmentOrderResponse.class);
        Map<String, Object> headers  = new HashMap<>();
        headers.put(KafkaHeaders.TOPIC, "shipmentOrderReceive");
        MessageHeaders messageHeaders = new MessageHeaders(headers);
        kafkaTemplate.send(new GenericMessage<>(shipmentOrderResponse, messageHeaders));
        log.info("Shipment Order Send");
    }
}
