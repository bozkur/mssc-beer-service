package guru.springframework.msscbeerservice.events;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import lombok.AllArgsConstructor;

import java.io.Serial;

/**
 * @author cevher
 */
@AllArgsConstructor
public class BrewBeerEvent extends BeerEvent{
    @Serial
    private static final long serialVersionUID = 938264382931294419L;

    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
