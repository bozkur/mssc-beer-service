package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping({"/api/v1/beer"})
public class BeerController {

    @GetMapping({"/{beerId}"})
    public ResponseEntity<BeerDto> getBeerById(@PathVariable UUID beerId) {
        //TODO impl.
        BeerDto beerDto = BeerDto.builder().id(beerId).build();
        return new ResponseEntity<>(beerDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createNewBeer(@RequestBody BeerDto beer) {
        //TODO impl.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Location", "/api/v1/beer/" + UUID.randomUUID());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping({"/{beerId}"})
    public void updateBeerById(@PathVariable UUID beerId, @RequestBody BeerDto beer) {
        //TODO Impl
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping({"/{beerId}"})
    public void deleteBeer(@PathVariable UUID beerId) {
        //TODO Impl
    }
}
