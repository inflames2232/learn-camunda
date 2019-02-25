package com.camunda.demo.springboot.adapter;

import com.camunda.demo.springboot.ProcessConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.shipment.ShipmentOrderResponse;
import dto.validate.ValidatedOrderResponse;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@KafkaListener(topics = {"validatedOrderReceive", "shipmentOrderReceive"})
public class KafkaGroupListener {

    @Autowired
    private ProcessEngine camunda;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaHandler
    public void getValidatedOrderResponse(
            @Payload ValidatedOrderResponse validatedOrderResponse
            /*@Headers KafkaHeaders kafkaHeaders*/
            ) throws JsonProcessingException {
        log.info("receive response {}", validatedOrderResponse);
        String jsonValidatedOrder = objectMapper.writeValueAsString(validatedOrderResponse);
        camunda.getRuntimeService().createMessageCorrelation(ProcessConstants.MSG_NAME_ValidateOrder)
                .processInstanceVariableEquals(ProcessConstants.VAR_NAME_orderId, validatedOrderResponse.getOrderId())
                .setVariable(ProcessConstants.VAR_NAME_transactionUUID, validatedOrderResponse.getTransactionUUID())
                .setVariable(ProcessConstants.VAR_NAME_validatedOrder, jsonValidatedOrder)
                .correlateWithResult();
    }


    @KafkaHandler
    public void getShipmentOrderResponse(
            @Payload ShipmentOrderResponse shipmentOrderResponse
            /*@Headers KafkaHeaders kafkaHeaders*/
    ) throws JsonProcessingException {
        log.info("receive response {}", shipmentOrderResponse);
        String jsonShipmentOrder = objectMapper.writeValueAsString(shipmentOrderResponse);
        camunda.getRuntimeService().createMessageCorrelation(ProcessConstants.MSG_NAME_ShipmentOrder)
                .processInstanceVariableEquals(ProcessConstants.VAR_NAME_orderId, shipmentOrderResponse.getOrderId())
                .setVariable(ProcessConstants.VAR_NAME_shipmentUUID, shipmentOrderResponse.getShipmentUUID())
                .setVariable(ProcessConstants.VAR_NAME_shipmentOrder, jsonShipmentOrder)
                .correlateWithResult();
    }
}
