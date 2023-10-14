package guru.springframework.brewery.model.events;

import guru.springframework.brewery.model.BeerDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author cevher
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 3738178240496566665L;
    private BeerDto beerDto;
}
