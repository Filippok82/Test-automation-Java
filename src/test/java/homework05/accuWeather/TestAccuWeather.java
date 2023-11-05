package homework05.accuWeather;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;

import homework03.accuweather.location.AdministrativeArea;
import homework03.accuweather.location.Country;
import homework03.accuweather.location.Location;
import homework03.accuweather.location.Region;
import homework03.accuweather.weather.DailyForecast;
import homework03.accuweather.weather.Headline;

import homework03.accuweather.weather.Weather;
import homework05.accu.weather.City;
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
import java.util.ArrayList;
import java.util.List;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAccuWeather {
    private static final WireMockServer wireMockServer = new WireMockServer();
    private static final int port = 8080;
    private static String baseUrl;

    private static final Logger logger
            = LoggerFactory.getLogger(TestAccuWeather.class);

    @BeforeAll
    static void startServer() {
        baseUrl = "http://localhost:" + port;
        wireMockServer.start();
        configureFor("localhost", port);
        logger.info("Start WireMockServer on port {}", port);
    }


    @Test
    void testInformationAboutAdministrativeAreasMock() throws IOException {
        logger.info("Тест проверяющий информцию об административных районах код ответ 200 запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setEnglishName("Butha-Buthe");
        administrativeArea.setId("100");


        logger.debug("Формирование мока для GET /locations/v1/adminareas");
        stubFor(get(urlPathEqualTo("/locations/v1/adminareas"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(administrativeArea))));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/adminareas");
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/adminareas")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        AdministrativeArea responseBody = mapper.readValue(response.getEntity().getContent(), AdministrativeArea.class);

        assertEquals("Butha-Buthe", responseBody.getEnglishName());
        assertEquals("100", responseBody.getId());

    }

    @Test
    void testInformationAboutAdministrativeAreasIsValueMock() throws IOException, URISyntaxException {
        logger.info("Тест проверяющий информацию об административных районах");
        //given
        ObjectMapper mapper = new ObjectMapper();
        AdministrativeArea administrativeArea = new AdministrativeArea();
        administrativeArea.setEnglishName("Butha-Buthe");
        administrativeArea.setId("100");


        logger.debug("Формирование мока для GET /locations/v1/adminareas");


        stubFor(get(urlPathEqualTo("/locations/v1/adminareas"))
                .withQueryParam("offset", equalTo("25"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(administrativeArea))));


        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/adminareas");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("offset", String.valueOf(25))
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/adminareas")));
        assertEquals(200, response.getStatusLine().getStatusCode());


    }

    @Test
    void testInformationAboutAllRegionsMock() throws IOException, URISyntaxException {
        logger.info("Тест проверяющий информацию о регионах");
        //given
        ObjectMapper mapper = new ObjectMapper();
        Region region = new Region();
        region.setId("AFR");
        region.setLocalizedName("Africa");
        region.setEnglishName("Africa");

        logger.debug("Формирование мока для GET  /locations/v1/regions");

        stubFor(get(urlPathEqualTo("/locations/v1/regions"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(region))));


        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/regions");

        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/regions")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        Region responseBody = mapper.readValue(response.getEntity().getContent(), Region.class);

        assertEquals("AFR", responseBody.getId());
        assertEquals("Africa", responseBody.getLocalizedName());
        assertEquals("Africa", responseBody.getEnglishName());
    }

    @Test
    void testInformationAboutAllCountriesWithinSpecifiedRegionMock() throws IOException, URISyntaxException {
        logger.info("Тест проверяющий информацию обо всех странах в пределах указанного региона");
        //given
        ObjectMapper mapper = new ObjectMapper();
        Country country = new Country();
        country.setId("AQ");
        country.setLocalizedName("Antarctica");
        country.setEnglishName("Antarctica");


        logger.debug("Формирование мока для GET /locations/v1/countries/ANT");


        stubFor(get(urlPathEqualTo("/locations/v1/countries/ANT"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(country))));


        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/countries/ANT");

        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/countries/ANT")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        Country responseBody = mapper.readValue(response.getEntity().getContent(), Country.class);

        assertEquals("AQ", responseBody.getId());

    }

    @Test
    void testReturnAutocompleteOfTheSearchTextMock() throws IOException, URISyntaxException {
        logger.info("Тест проверяющий автозаполнение запроса");
        //given
        ObjectMapper mapper = new ObjectMapper();
        Location location = new Location();
        location.setLocalizedName("weather");
        Region region = new Region();
        region.setId("AFR");
        region.setLocalizedName("Africa");
        region.setEnglishName("Africa");
        location.setRegion(region);


        logger.debug("Формирование мока для GET /locations/v1/cities/search/");


        stubFor(get(urlPathEqualTo("/locations/v1/cities/search/"))
                .withQueryParam("q", equalTo("weather"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(location))));


        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/cities/search/");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("q", "weather")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/cities/search/")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        Location responseBody = mapper.readValue(response.getEntity().getContent(), Location.class);

        assertEquals("weather", responseBody.getLocalizedName());
        assertEquals("AFR", responseBody.getRegion().getId());


    }

    @Test
    void testInformationReturnsForCitiesReturnMoscowMock() throws IOException, URISyntaxException {
        logger.info("Тест проверяющий информацию для города");
        //given
        ObjectMapper mapper = new ObjectMapper();
        Location location = new Location();
        location.setLocalizedName("Moscow");
        Country country = new Country();
        country.setLocalizedName("Russia");
        location.setCountry(country);

        logger.debug("Формирование мока для GET /locations/v1/cities/autocomplete");

        stubFor(get(urlPathEqualTo("/locations/v1/cities/autocomplete"))
                .withQueryParam("q", equalTo("Moscow"))
                .willReturn(aResponse().withStatus(200).withBody(mapper.writeValueAsString(location))));


        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/cities/autocomplete");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("q", "Moscow")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/cities/autocomplete")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        Location responseBody = mapper.readValue(response.getEntity().getContent(), Location.class);


        assertEquals("Russia", responseBody.getCountry().getLocalizedName());

    }

    @Test
    void testReturnsInformationForTheTop50Cities() throws IOException {
        logger.info("Тест проверяющий информацию 50 лучшим городам");
        //given
        ObjectMapper mapper = new ObjectMapper();
        City city = new City();
        city.setLocalizedName("Santiago");

        logger.debug("Формирование мока для GET /locations/v1/topcities/50");

        stubFor(get(urlPathEqualTo("/locations/v1/topcities/50"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(city))));


        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/topcities/50");

        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/topcities/50")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        City responseBody = mapper.readValue(response.getEntity().getContent(), City.class);

        assertEquals("Santiago", responseBody.getLocalizedName());

    }

    @Test
    void testGetWeatherOneDayReturnMock() throws IOException {
        logger.info("Тест проверяющий информацию о погоде на один день");
        //give
        ObjectMapper mapper = new ObjectMapper();
        Weather weather = new Weather();
        Headline headline = new Headline();
        headline.setCategory("rain");
        headline.setText("Rain, some heavy, and thunderstorms to affect the area Wednesday evening");
        weather.setHeadline(headline);
        DailyForecast dailyForecast = new DailyForecast();
        List<DailyForecast> dailyForecasts = new ArrayList<>();
        dailyForecasts.add(dailyForecast);
        weather.setDailyForecasts(dailyForecasts);

        logger.debug("Формирование мока для GET /forecasts/v1/daily/1day/113487");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/1day/113487"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(weather))));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/forecasts/v1/daily/1day/113487");
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/forecasts/v1/daily/1day/113487")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        Weather responseBody = mapper.readValue(response.getEntity().getContent(), Weather.class);
        assertEquals("rain", responseBody.getHeadline().getCategory());
        assertEquals(1, responseBody.getDailyForecasts().size());
    }

    @Test
    void testGetWeatherFiveDayReturnMock() throws IOException {
        logger.info("Тест проверяющий информацию о погоде на пять дней");
        //give
        ObjectMapper mapper = new ObjectMapper();
        Weather weather = new Weather();
        Headline headline = new Headline();
        headline.setCategory("rain");
        headline.setText("Rain, some heavy, and thunderstorms to affect the area Wednesday evening");
        weather.setHeadline(headline);
        DailyForecast dailyForecast = new DailyForecast();
        List<DailyForecast> dailyForecasts = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dailyForecasts.add(dailyForecast);
        }

        weather.setDailyForecasts(dailyForecasts);

        logger.debug("Формирование мока для GET /forecasts/v1/daily/5day/113487");
        stubFor(get(urlPathEqualTo("/forecasts/v1/daily/5day/113487"))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(weather))));

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet request = new HttpGet(getBaseUrl() + "/forecasts/v1/daily/5day/113487");
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/forecasts/v1/daily/5day/113487")));
        assertEquals(200, response.getStatusLine().getStatusCode());

        Weather responseBody = mapper.readValue(response.getEntity().getContent(), Weather.class);
        assertEquals("Rain, some heavy, and thunderstorms to affect the area Wednesday evening", responseBody.getHeadline().getText());
        assertEquals(5, responseBody.getDailyForecasts().size());
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

