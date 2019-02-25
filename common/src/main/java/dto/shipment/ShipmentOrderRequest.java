package dto.shipment;

import lombok.Data;

import java.util.UUID;

@Data
public class ShipmentOrderRequest {
    private UUID transactionUUID;
    private String orderId;
    private Integer amount;
}
