package guru.springframework.msscbeerservice.web.mappers;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.brewery.model.BeerDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author cevher
 */
@SpringBootTest
class BeerMapperDecoratorTest {

    @MockBean
    private BeerInventoryService inventoryService;

    @Autowired
    private BeerMapperDecorator decorator;

    @Test
    void shouldMapToDtoWithDto() {
        Beer beer = Beer.builder().beerName("test").id(UUID.randomUUID()).build();
        int numInHand = 15;
        when(inventoryService.getOnHandInventory(beer.getId())).thenReturn(numInHand);

        BeerDto beerDto = decorator.beer2BeerDtoWithInventory(beer);

        assertThat(beerDto.getQuantityInHand(), Matchers.equalTo(numInHand));
    }
}