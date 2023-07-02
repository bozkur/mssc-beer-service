package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyle;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.UUID;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    private static final String BEER_API_URL = "/api/v1/beer";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get beer by its id.")
    void shouldGetBeerById() throws Exception {
        UUID beerId = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.get(BEER_API_URL + "/" + beerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(beerId.toString())));
    }

    @Test
    @DisplayName("Create a new beer")
    void shouldCreateNewBeer() throws Exception {
        BeerDto beer = createBeer();
        mockMvc.perform(MockMvcRequestBuilders.post(BEER_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"));
    }

    private BeerDto createBeer() {
        return BeerDto.builder().beerName("Edelmeister")
                .beerStyle(BeerStyle.LAGER)
                .price(new BigDecimal("12.0"))
                .upc(1L)
                .build();
    }

    @Test
    @DisplayName("Update a beer with its id")
    void shouldUpdateBeerById() throws Exception {
        BeerDto beer = createBeer();
        UUID id = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.put(BEER_API_URL + "/" +id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Delete a beer")
    void shouldDeleteBeer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BEER_API_URL + "/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}