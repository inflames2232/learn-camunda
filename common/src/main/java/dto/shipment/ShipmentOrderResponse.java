package dto.shipment;

import lombok.Data;

import java.util.UUID;

@Data
public class ShipmentOrderResponse {
    private UUID transactionUUID;
    private String orderId;
    private Integer amount;
    private UUID shipmentUUID;
}
