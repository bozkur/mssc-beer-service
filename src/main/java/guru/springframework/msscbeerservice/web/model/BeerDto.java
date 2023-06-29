package guru.springframework.msscbeerservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
    private UUID id;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityInHand;
    private BigDecimal price;
}
