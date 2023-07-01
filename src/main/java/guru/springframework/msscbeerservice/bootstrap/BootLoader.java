package guru.springframework.msscbeerservice.bootstrap;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BootLoader implements CommandLineRunner {
    private final BeerRepository beerRepository;

    public BootLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) {
        loadBeers();
    }

    private void loadBeers() {
        Beer efes = Beer.builder().beerName("Efes Pilsen")
                .beerStyle("PALE")
                .price(new BigDecimal("38.00"))
                .upc(33000001L)
                .minOnHand(100)
                .quantityToBrew(200)
                .minOnHand(12)
                .build();
        Beer em = Beer.builder().beerName("Edelmeister 0.0")
                .beerStyle("LAGER")
                .price(new BigDecimal("19.90"))
                .upc(33000002L)
                .minOnHand(100)
                .quantityToBrew(12)
                .build();

        if (beerRepository.count() == 0) {
            beerRepository.save(efes);
            beerRepository.save(em);
        }
    }
}
