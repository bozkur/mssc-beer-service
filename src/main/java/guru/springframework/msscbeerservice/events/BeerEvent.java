package guru.springframework.msscbeerservice.events;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author cevher
 */
@Data
@RequiredArgsConstructor
@Builder
public class BeerEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 3738178240496566665L;
    private final BeerDto beerDto;
}
