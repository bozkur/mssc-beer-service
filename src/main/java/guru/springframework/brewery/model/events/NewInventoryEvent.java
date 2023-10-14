package guru.springframework.brewery.model.events;

import guru.springframework.brewery.model.BeerDto;

import java.io.Serial;

/**
 * @author cevher
 */
public class NewInventoryEvent extends BeerEvent{
    @Serial
    private static final long serialVersionUID = -1767703357372754708L;

    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
