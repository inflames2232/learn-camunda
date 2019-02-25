package com.learn.validateservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.validateservice.ProcessConstants;
import dto.validate.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaOrderListener {

    private final ProcessEngine processEngine;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "validateOrderSend")
    //@SendTo("validatedOrderReceive")
    public void listenWithHeaders(
            @Payload OrderRequest orderRequest
            /*@Headers KafkaHeaders kafkaHeaders*/
    ) throws JsonProcessingException {

        log.info("receive request {}", orderRequest);
        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcessConstants.VAR_NAME_order, objectMapper.writeValueAsString(orderRequest));
        processEngine.getRuntimeService()
                .startProcessInstanceByKey(ProcessConstants.PROCESS_KEY_validate, variables);

    }

}
