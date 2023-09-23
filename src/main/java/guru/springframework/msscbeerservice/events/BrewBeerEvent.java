package guru.springframework.msscbeerservice.events;

import guru.springframework.msscbeerservice.web.model.BeerDto;

import java.io.Serial;

/**
 * @author cevher
 */
public class BrewBeerEvent extends BeerEvent{
    @Serial
    private static final long serialVersionUID = 938264382931294419L;

    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
