package guru.springframework.msscbeerservice.web.mappers;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.brewery.model.BeerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author cevher
 */
@Slf4j
public abstract class BeerMapperDecorator implements BeerMapper {
    private BeerInventoryService beerInventoryService;
    private BeerMapper mapper;

    @Autowired
    public void setBeerInventoryService(BeerInventoryService beerInventoryService) {
        this.beerInventoryService = beerInventoryService;
    }

    @Autowired
    public void setMapper(BeerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BeerDto beer2BeerDto(Beer beer) {
        return mapper.beer2BeerDto(beer);
    }

    @Override
    public BeerDto beer2BeerDtoWithInventory(Beer beer) {
        BeerDto beerDto = mapper.beer2BeerDto(beer);
        beerDto.setQuantityInHand(beerInventoryService.getOnHandInventory(beer.getId()));
        return beerDto;
    }
}
