package guru.springframework.msscbeerservice.services.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author cevher
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerInventoryDto {
    private UUID id;
    private UUID beerId;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
    private Integer quantityInHand;
}
