package com.learn.validateservice.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.validateservice.ProcessConstants;
import dto.validate.ValidatedOrderResponse;
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
@Slf4j
@RequiredArgsConstructor
public class SendValidatedOrderAdapter implements JavaDelegate {

    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String validatedOrderResponseJson = (String) delegateExecution.getVariable(ProcessConstants.VAR_NAME_validatedOrder);
        ValidatedOrderResponse validatedOrderResponse = objectMapper.readValue(validatedOrderResponseJson, ValidatedOrderResponse.class);
        Map<String, Object> headers  = new HashMap<>();
        headers.put(KafkaHeaders.TOPIC, "validatedOrderReceive");
        MessageHeaders messageHeaders = new MessageHeaders(headers);
        kafkaTemplate.send(new GenericMessage<>(validatedOrderResponse, messageHeaders));
        log.info("Validated Order Send");
    }
}
