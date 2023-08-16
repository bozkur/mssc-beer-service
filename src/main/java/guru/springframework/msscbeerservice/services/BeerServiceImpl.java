package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.controller.NotFoundException;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;


    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand) {
        log.info("Getting beer with id {}", beerId);
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
        return (showInventoryOnHand == null || !showInventoryOnHand) ? beerMapper.beer2BeerDto(beer) : beerMapper.beer2BeerDtoWithInventory(beer);
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
       return beerMapper.beer2BeerDto(beerRepository.save(beerMapper.beerDto2Beer(beerDto)));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer foundBeer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        foundBeer.setBeerName(beerDto.getBeerName());
        foundBeer.setBeerStyle(beerDto.getBeerStyle().name());
        foundBeer.setPrice(beerDto.getPrice());
        foundBeer.setUpc(beerDto.getUpc());

        return beerMapper.beer2BeerDto(beerRepository.save(foundBeer));
    }

    @Override
    public void deleteById(UUID beerId) {
        beerRepository.deleteById(beerId);
    }

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, BeerStyle beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {
        log.info("Listing beers...");
        String beerStyleStr = (beerStyle == null) ? null : beerStyle.toString();
        Page<Beer> beerPage;
        if(StringUtils.hasText(beerName) && StringUtils.hasText(beerStyleStr)) {
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyleStr, pageRequest);
        } else if (StringUtils.hasText(beerName) && !StringUtils.hasText(beerStyleStr)) {
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && StringUtils.hasText(beerStyleStr)) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyleStr, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }
        Function<Beer, BeerDto> mapper = (showInventoryOnHand == null || !showInventoryOnHand) ? beerMapper::beer2BeerDto : beerMapper::beer2BeerDtoWithInventory;
        return new BeerPagedList(beerPage.getContent()
                .stream()
              .map(mapper)
                .collect(Collectors.toList()),
                PageRequest.of(beerPage.getPageable().getPageNumber(),
                        beerPage.getPageable().getPageSize()),
                beerPage.getTotalElements());
    }
}
