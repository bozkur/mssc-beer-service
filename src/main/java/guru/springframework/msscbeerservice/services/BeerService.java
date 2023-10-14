package guru.springframework.msscbeerservice.services;

import guru.springframework.brewery.model.BeerDto;
import guru.springframework.brewery.model.BeerPagedList;
import guru.springframework.brewery.model.BeerStyle;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {
    BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beer);

    BeerDto updateBeer(UUID beerId, BeerDto beer);

    void deleteById(UUID beerId);

    BeerPagedList listBeers(String beerName, BeerStyle beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

    BeerDto getBeerByUpc(String upc);
}
