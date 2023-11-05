package homework05.spoonacular;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;

import homework03.spoonacular.IngredientSubstitutesDto;
import homework03.spoonacular.recipers.Ingredient;
import homework05.spoon.ConvertAmountsDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestSpoonacular {
    private static final WireMockServer wireMockServer = new WireMockServer();
    private static final int port = 8080;
    private static String baseUrl;

    private static final Logger logger
            = LoggerFactory.getLogger(TestSpoonacular.class);

    @BeforeAll
    static void startServer() {
        baseUrl = "http://localhost:" + port;
        wireMockServer.start();
        configureFor("localhost", port);
        logger.info("Start WireMockServer on port {}", port);
    }

    @Test
    void testSearchRecipesByIngredientsMock() throws IOException, URISyntaxException {
        logger.info("Тест проверяющий результат по ингредиентам");
        //given
        ObjectMapper mapper = new ObjectMapper();
        IngredientSubstitutesDto substitutesDto = new IngredientSubstitutesDto();
        substitutesDto.setIngredient("tomato");


        logger.debug("Формирование мока для GET /recipes/findByIngredients");

        stubFor(get(urlPathEqualTo("/recipes/findByIngredients"))
                .withQueryParam("ingredients", equalTo("tomato"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(substitutesDto))));


        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/recipes/findByIngredients");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("ingredients", "tomato")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/recipes/findByIngredients")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        IngredientSubstitutesDto responseBody = mapper.readValue(response.getEntity().getContent(), IngredientSubstitutesDto.class);

        assertEquals("tomato", responseBody.getIngredient());

    }

    @Test
    void testSearchIngredientsMock() throws IOException, URISyntaxException {
        logger.info("Тест проверяющий информацию по ингредиенту");
        //given
        ObjectMapper mapper = new ObjectMapper();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("cheese");
        ingredient.setId(10);


        logger.debug("Формирование мока для GET /food/ingredients/search");

        stubFor(get(urlPathEqualTo("/food/ingredients/search"))
                .withQueryParam("query", equalTo("cheese"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(ingredient))));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/food/ingredients/search");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("query", "cheese")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/food/ingredients/search")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        Ingredient responseBody = mapper.readValue(response.getEntity().getContent(), Ingredient.class);

        assertEquals("cheese", responseBody.getName());
        assertEquals(10, responseBody.getId());
    }

    @Test
    void testReturtConvertAmountsMock() throws IOException, URISyntaxException {
        logger.info("Тест проверяюший конвертацию");
        //given
        ObjectMapper mapper = new ObjectMapper();
        ConvertAmountsDto convertAmountsDto = new ConvertAmountsDto();
        convertAmountsDto.setAnswer("2 cups salt are 584 grams");
        convertAmountsDto.setSourceUnit("cups");
        convertAmountsDto.setSourceAmount(2.0);
        convertAmountsDto.setTargetAmount(584.0);
        convertAmountsDto.setType("CONVERSION");
        convertAmountsDto.setTargetUnit("grams");

        logger.debug("Формирование мока для GET /recipes/convert");
        stubFor(get(urlPathEqualTo("/recipes/convert"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(convertAmountsDto))));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/recipes/convert");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("ingredientName", "salt")
                .addParameter("sourceAmount", String.valueOf(2.0))
                .addParameter("sourceUnit", "cups")
                .addParameter("targetUnit", "grams")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/recipes/convert")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        ConvertAmountsDto responseBody = mapper.readValue(response.getEntity().getContent(), ConvertAmountsDto.class);

        assertEquals("2 cups salt are 584 grams", responseBody.getAnswer());
        assertEquals(584,responseBody.getTargetAmount());
    }

    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
        logger.info("Stop WireMockServer");
    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}

