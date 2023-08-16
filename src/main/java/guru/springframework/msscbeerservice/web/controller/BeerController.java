package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping({"/api/v1/"})
public class BeerController {

    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    private final BeerService beerService;

    @GetMapping(produces = {"application/json"}, path = "beer")
    public ResponseEntity<BeerPagedList> listBeers(Integer pageNumber, Integer pageSize,
                                                   String beerName, BeerStyle beerStyle,
                                                   @RequestParam(required = false) Boolean showInventoryOnHand) {

        if(pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        BeerPagedList beerList = beerService.listBeers(beerName, beerStyle, PageRequest.of(pageNumber, pageSize), showInventoryOnHand);
        return new ResponseEntity<>(beerList, HttpStatus.OK);
    }
    @GetMapping({"beer/{beerId}"})
    public ResponseEntity<BeerDto> getBeerById(@PathVariable UUID beerId, @RequestParam(required = false) Boolean showInventoryOnHand) {
        if(showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }
        BeerDto beerDto = beerService.getBeerById(beerId, showInventoryOnHand);
        return new ResponseEntity<>(beerDto, HttpStatus.OK);
    }


    @GetMapping("beerUpc/{upc}")
    public ResponseEntity<BeerDto> gerBeerByUpc(@PathVariable String upc) {
        BeerDto beerDto = beerService.getBeerByUpc(upc);
        return new ResponseEntity<>(beerDto, HttpStatus.OK);
    }

    @PostMapping("beer")
    public ResponseEntity<BeerDto> createNewBeer(@Validated @RequestBody BeerDto beer) {
        BeerDto saved = beerService.saveNewBeer(beer);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Location", "/api/v1/beer/" + saved.getId());
        return new ResponseEntity<>(saved, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping({"beer/{beerId}"})
    public ResponseEntity<BeerDto> updateBeerById(@PathVariable UUID beerId, @Validated @RequestBody BeerDto beer) {
        BeerDto updated = beerService.updateBeer(beerId, beer);
        return new ResponseEntity<>(updated, HttpStatus.NO_CONTENT);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping({"beer/{beerId}"})
    public void deleteBeer(@PathVariable UUID beerId) {
        beerService.deleteById(beerId);
    }
}
