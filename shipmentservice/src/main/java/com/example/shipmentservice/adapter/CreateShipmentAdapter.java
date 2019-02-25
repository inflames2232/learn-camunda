package com.example.shipmentservice.adapter;

import com.example.shipmentservice.ProcessConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.shipment.ShipmentOrderRequest;
import dto.shipment.ShipmentOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreateShipmentAdapter implements JavaDelegate {

    private final ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String shipmentOrderRequestJson = (String) delegateExecution.getVariable(ProcessConstants.VAR_NAME_validatedOrder);
        ShipmentOrderRequest shipmentOrderRequest = objectMapper.readValue(shipmentOrderRequestJson, ShipmentOrderRequest.class);

        ShipmentOrderResponse shipmentOrderResponse = new ShipmentOrderResponse();
        shipmentOrderResponse.setAmount(shipmentOrderRequest.getAmount());
        shipmentOrderResponse.setOrderId(shipmentOrderRequest.getOrderId());
        shipmentOrderResponse.setTransactionUUID(shipmentOrderRequest.getTransactionUUID());
        shipmentOrderResponse.setShipmentUUID(UUID.randomUUID());

        delegateExecution.setVariable(ProcessConstants.VAR_NAME_shipmentOrder, objectMapper.writeValueAsString(shipmentOrderResponse));
        log.info("Shipment Order Created");
    }
}
