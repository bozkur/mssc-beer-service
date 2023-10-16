package guru.springframework.msscbeerservice.services.order;

import guru.springframework.brewery.model.BeerOrderDto;
import guru.springframework.brewery.model.BeerOrderLineDto;
import guru.springframework.brewery.model.events.ValidateBeerOrderRequest;
import guru.springframework.brewery.model.events.ValidateBeerOrderResult;
import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cevher
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BeerValidationListener {

    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateBeerOrderRequest validateOrderRequest) {
        BeerOrderDto beerOrderDto = validateOrderRequest.getBeerOrderDto();
        List<BeerOrderLineDto> beerOrderLines = beerOrderDto.getBeerOrderLines();
        boolean result = true;
        for (BeerOrderLineDto bol : beerOrderLines) {
            Beer byUpc = beerRepository.findByUpc(bol.getUpc());
            if (byUpc == null) {
                result = false;
                break;
            }
        }
        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESULT_QUEUE, new ValidateBeerOrderResult(beerOrderDto.getId(), result));
    }
}
