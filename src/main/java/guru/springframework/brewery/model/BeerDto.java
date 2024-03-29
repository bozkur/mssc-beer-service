package guru.springframework.brewery.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -2170987336821659311L;
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime createdDate;
    @Null
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime lastModifiedDate;
    @Null
    private UUID id;
    @NotBlank
    @Size(min = 3, max = 100)
    private String beerName;
    @NotNull
    private BeerStyle beerStyle;
    @NotNull
    @Positive
    private String upc;

    private Integer quantityInHand;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull
    @Positive
    private BigDecimal price;
}
