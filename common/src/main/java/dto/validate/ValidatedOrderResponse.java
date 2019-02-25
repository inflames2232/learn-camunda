package dto.validate;

import lombok.Data;
import java.util.UUID;

@Data
public class ValidatedOrderResponse {
    private UUID transactionUUID;
    private String orderId;
    private Integer amount;
}
