package com.learn.validateservice.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.validateservice.ProcessConstants;
import dto.validate.OrderRequest;
import dto.validate.ValidatedOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateOrderAdapter implements JavaDelegate {

    private final ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String orderRequestJson = (String) delegateExecution.getVariable(ProcessConstants.VAR_NAME_order);
        OrderRequest orderRequest = objectMapper.readValue(orderRequestJson, OrderRequest.class);
        ValidatedOrderResponse validatedOrderResponse = new ValidatedOrderResponse();
        validatedOrderResponse.setAmount(orderRequest.getAmount());
        validatedOrderResponse.setOrderId(orderRequest.getOrderId());
        validatedOrderResponse.setTransactionUUID(UUID.randomUUID());

        ObjectValue serializedValue =
                Variables
                        .objectValue(validatedOrderResponse)
                        .serializationDataFormat(Variables.SerializationDataFormats.JAVA)
                        .create();

        delegateExecution.setVariable(ProcessConstants.VAR_NAME_validatedOrder, objectMapper.writeValueAsString(validatedOrderResponse));
        log.info("Order Validated");
    }

}
