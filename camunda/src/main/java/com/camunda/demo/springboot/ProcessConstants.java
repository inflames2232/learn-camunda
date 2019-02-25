package com.camunda.demo.springboot;

import dto.validate.ValidatedOrderResponse;

public class ProcessConstants {

  public static final String PROCESS_KEY_order = "order";
  
  public static final String VAR_NAME_orderId = "orderId";
  public static final String VAR_NAME_amount = "amount";
  public static final String VAR_NAME_shipmentUUID = "shipmentUUID";
  public static final String VAR_NAME_transactionUUID = "transactionUUID";

  public static final String VAR_NAME_validatedOrder = "validatedOrder";
  public static final String VAR_NAME_shipmentOrder = "shipmentOrder";

  public static final String MSG_NAME_ValidateOrder = "Message_ValidateOrder";
  public static final String MSG_NAME_ShipmentOrder = "Message_ShipmentOrder";
  
}
