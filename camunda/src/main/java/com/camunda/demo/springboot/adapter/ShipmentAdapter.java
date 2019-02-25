package com.camunda.demo.springboot.adapter;

import com.camunda.demo.springboot.ProcessConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.shipment.ShipmentOrderRequest;
import dto.validate.ValidatedOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShipmentAdapter implements JavaDelegate {
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ValidatedOrderResponse validatedOrderResponse =
                objectMapper.readValue((String)delegateExecution.getVariable(ProcessConstants.VAR_NAME_validatedOrder),
                        ValidatedOrderResponse.class);
        ShipmentOrderRequest shipmentOrderRequest = modelMapper.map(validatedOrderResponse, ShipmentOrderRequest.class);
        Message<ShipmentOrderRequest> message = MessageBuilder
                .withPayload(shipmentOrderRequest)
                .setHeader(KafkaHeaders.TOPIC, "shipmentOrderSend")
                .setHeader(KafkaHeaders.MESSAGE_KEY, shipmentOrderRequest.getOrderId())
                .setHeader(KafkaHeaders.PARTITION_ID, 0)
                .build();
        log.info("send request to shipment {}", message.getPayload());
        kafkaTemplate.send(message);
    }
}
