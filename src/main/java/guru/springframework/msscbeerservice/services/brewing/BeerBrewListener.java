package guru.springframework.msscbeerservice.services.brewing;

import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.brewery.model.events.BrewBeerEvent;
import guru.springframework.brewery.model.events.NewInventoryEvent;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.brewery.model.BeerDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * @author cevher
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BeerBrewListener {

    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent event) {
        BeerDto beerDto = event.getBeerDto();
        Beer beer = beerRepository.getReferenceById(beerDto.getId());
        beerDto.setQuantityInHand(beer.getQuantityToBrew());

        log.debug("Brewed beer {} Quantity on hand: {}", beer.getMinOnHand(), beerDto.getQuantityInHand());
        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, new NewInventoryEvent(beerDto));
    }
}
