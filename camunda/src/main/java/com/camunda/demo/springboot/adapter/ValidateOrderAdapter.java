package com.camunda.demo.springboot.adapter;

import com.camunda.demo.springboot.ProcessConstants;
import dto.validate.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class ValidateOrderAdapter implements JavaDelegate {

    private final KafkaTemplate<String, Message> kafkaTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setAmount((Integer) delegateExecution.getVariable(ProcessConstants.VAR_NAME_amount));
        orderRequest.setOrderId((String) delegateExecution.getVariable(ProcessConstants.VAR_NAME_orderId));
        Message<OrderRequest> message = MessageBuilder
                .withPayload(orderRequest)
                .setHeader(KafkaHeaders.TOPIC, "validateOrderSend")
                .setHeader(KafkaHeaders.MESSAGE_KEY, orderRequest.getOrderId())
                .setHeader(KafkaHeaders.PARTITION_ID, 0)
                .build();
        log.info("send request {}", message.getPayload());
        kafkaTemplate.send(message);
    }
}
