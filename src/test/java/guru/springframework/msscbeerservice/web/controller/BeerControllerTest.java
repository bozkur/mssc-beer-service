package guru.springframework.msscbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbeerservice.bootstrap.BootLoader;
import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyle;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    private static final String BEER_API_URL = "/api/v1/beer";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BeerService beerService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .uris().withHost("dev.springframework.guru").withPort(80).withScheme("https")).build();
    }

    @Test
    @DisplayName("Get beer by its id.")
    void shouldGetBeerById() throws Exception {
        UUID beerId = UUID.randomUUID();
        BeerDto expectedBeerDto = createBeer();
        expectedBeerDto.setId(beerId);
        when(beerService.getBeerById(beerId)).thenReturn(expectedBeerDto);
        mockMvc.perform(get(BEER_API_URL + "/{beerId}", beerId).param("iscold", "yes"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(beerId.toString())))
                .andDo(document("v1/beer-get",
                        pathParameters(parameterWithName("beerId").description("UUID of the desired beer to get.")),
                        queryParameters(parameterWithName("iscold").description("Is beer Cold Query Param")),
                        responseFields(
                                fieldWithPath("id").description("Id of the beer"),
                                fieldWithPath("createdDate").description("Creation date for the beer record"),
                                fieldWithPath("lastModifiedDate").description("Last modification date for the beer record"),
                                fieldWithPath("beerName").description("Name of the beer"),
                                fieldWithPath("beerStyle").description("Style of the beer"),
                                fieldWithPath("upc").description("UPC number of the beer"),
                                fieldWithPath("quantityInHand").description("Quantity of the beer in the store"),
                                fieldWithPath("price").description("Price of the beer")
                        )
                ));
    }

    @Test
    @DisplayName("Create a new beer")
    void shouldCreateNewBeer() throws Exception {
        BeerDto beer = createBeer();
        BeerDto savedBeer = createBeer();
        savedBeer.setId(UUID.randomUUID());
        when(beerService.saveNewBeer(ArgumentMatchers.any())).thenReturn(savedBeer);
        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);
        String expectedLocationHeader = "/api/v1/beer/" + savedBeer.getId().toString();
        mockMvc.perform(post(BEER_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().stringValues("Location",expectedLocationHeader))
                .andDo(document("v1/beer-new",
                        requestFields(
                                fields.withPath("id").ignored(),
                                fields.withPath("createdDate").ignored(),
                                fields.withPath("lastModifiedDate").ignored(),
                                fields.withPath("beerName").description("Name of the beer"),
                                fields.withPath("beerStyle").description("Style of the beer"),
                                fields.withPath("upc").description("UPC number of the beer"),
                                fields.withPath("quantityInHand").ignored(),
                                fields.withPath("price").description("Price of the beer")
                        )));
    }

    private BeerDto createBeer() {
        return BeerDto.builder().beerName("Edelmeister")
                .beerStyle(BeerStyle.LAGER)
                .price(new BigDecimal("12.0"))
                .upc(BootLoader.BEER3_UPC)
                .build();
    }

    @Test
    @DisplayName("Update a beer with its id")
    void shouldUpdateBeerById() throws Exception {
        BeerDto beer = createBeer();
        UUID id = UUID.randomUUID();
        mockMvc.perform(put(BEER_API_URL + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Delete a beer")
    void shouldDeleteBeer() throws Exception {
        mockMvc.perform(delete(BEER_API_URL + "/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private static class ConstrainedFields {

        private final ConstraintDescriptions constraintDescriptions;

        ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints").value(StringUtils
                    .collectionToDelimitedString(this.constraintDescriptions
                            .descriptionsForProperty(path), ". ")));
        }
    }
}