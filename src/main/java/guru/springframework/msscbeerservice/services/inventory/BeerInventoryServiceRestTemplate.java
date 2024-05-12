package guru.springframework.msscbeerservice.services.inventory;

import guru.springframework.msscbeerservice.services.inventory.model.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author cevher
 */
@Profile("!local-discovery")
@Slf4j
@Component
public class BeerInventoryServiceRestTemplate implements BeerInventoryService {
    public static final String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";
    private final RestTemplate restTemplate;

    @Value("${sfg.brewery.beer-inventory-service-host}")
    private String beerInventoryHost;

    public BeerInventoryServiceRestTemplate(RestTemplateBuilder restTemplateBuilder, @Value("${sfg.brewery.inventory-user}") String username,
                                            @Value("${sfg.brewery.inventory-password}") String password) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication(username, password).build();
    }

    @Override
    public Integer getOnHandInventory(UUID beerId) {
        ResponseEntity<List<BeerInventoryDto>> responseEntity = restTemplate.exchange(beerInventoryHost + INVENTORY_PATH, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                }, beerId);

        return Objects.requireNonNull(responseEntity.getBody())
                .stream()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();
    }
}
