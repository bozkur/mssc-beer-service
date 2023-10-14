package guru.springframework.msscbeerservice.bootstrap;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.brewery.model.BeerStyle;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.stream.Stream;

@Component
public class BootLoader implements CommandLineRunner {

    public static final String BEER1_UPC = "0631234200036";
    public static final String BEER2_UPC = "0631234300019";
    public static final String BEER3_UPC = "0083783375213";
    private final BeerRepository beerRepository;

    public BootLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) {
        loadBeers();
    }

    private void loadBeers() {
        Beer mangoBobs = createBeer("Mango Bobs", BeerStyle.IPA, new BigDecimal("12.95"), 12, 200, BEER1_UPC);
        Beer galaxyCat = createBeer("Galaxy Cat", BeerStyle.PALE_ALE, new BigDecimal("12.95"), 12, 200, BEER2_UPC);
        Beer pinballPorter = createBeer("Pinball Porter", BeerStyle.PORTER, new BigDecimal("12.95"), 12, 200, BEER3_UPC);

        if (beerRepository.count() == 0) {
            Stream.of(mangoBobs, galaxyCat, pinballPorter).forEach(beerRepository::save);
        }
    }

    private Beer createBeer(String name, BeerStyle style, BigDecimal price, int minOnHand,
                            int quantityToBrew, String upc) {
        return Beer.builder().beerName(name)
                .beerStyle(style.toString())
                .price(price)
                .upc(upc)
                .minOnHand(minOnHand)
                .quantityToBrew(quantityToBrew)
                .createdDate(Timestamp.from(Instant.now()))
                .lastModifiedDate(Timestamp.from(Instant.now()))
                .version(1L)
                .build();

    }
}
