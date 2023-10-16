package homework03.accuWeather;

import homework03.accuweather.location.Location;

import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AccuWeatherTest extends AccUWeatherAbstractTest {
    @Test
    void testInformationAboutAdministrativeAreasIsValue() {

        List<Location> response = given()
                .queryParam("apikey", getApiKey())
                .queryParam("offset", 25)
                .when()
                .get(getBaseUrl() + "/locations/v1/adminareas")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(4000l))
                .extract()
                .body().jsonPath().getJsonObject(".");

        Assertions.assertEquals(25, response.size());

    }

    @Test
    void testInformationAboutAdministrativeAreasNotValue() {

        List<Location> response = given()
                .queryParam("apikey", getApiKey())
                .when()
                .get(getBaseUrl() + "/locations/v1/adminareas")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(4000l))
                .extract()
                .body().jsonPath().getJsonObject(".");

        Assertions.assertNotEquals(100, response.size());


    }

    @Test
    void testInformationAboutAllRegions() {
        JsonPath response = given()
                .queryParam("apikey", getApiKey())
                .when()
                .get(getBaseUrl() + "/locations/v1/regions")
                .prettyPeek()
                .body()
                .jsonPath();


        assertThat(response.get("[0].ID"), equalTo("AFR"));
        assertThat(response.get("[1].ID"), equalTo("ANT"));
        assertThat(response.get("[2].ID"), equalTo("ARC"));
        assertThat(response.get("[3].ID"), equalTo("ASI"));
        assertThat(response.get("[4].ID"), equalTo("CAC"));
        assertThat(response.get("[5].ID"), equalTo("EUR"));
        assertThat(response.get("[6].ID"), equalTo("MEA"));
        assertThat(response.get("[7].ID"), equalTo("NAM"));
        assertThat(response.get("[8].ID"), equalTo("OCN"));
        assertThat(response.get("[9].ID"), equalTo("SAM"));


    }

    @Test
    void testInformationAboutAllCountriesWithinSpecifiedRegion() {
        List<Location> response = given()
                .queryParam("apikey", getApiKey())
                .pathParams("regionCode", "ANT")
                .when()
                .get(getBaseUrl() + "/locations/v1/countries/{regionCode}")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(4000l))
                .extract()
                .body().jsonPath().getJsonObject(".");

        Assertions.assertEquals(3, response.size());


    }

    @Test
    void testReturnAutocompleteOfTheSearchText() {

        List<Location> response = given()
                .queryParam("apikey", getApiKey())
                .queryParam("q", "weather")
                .when()
                .get(getBaseUrl() + "/locations/v1/cities/search/")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(3000l))
                .extract()
                .body().jsonPath().getList(".", Location.class);


        Assertions.assertEquals("Weather", response.get(0).getEnglishName());
    }

    @Test
    void testInformationReturnsForCitiesReturnMoskow() {
        given()
                .queryParam("apikey", getApiKey())
                .queryParam("q", "Moskow")
                .when()
                .get(getBaseUrl() + "/locations/v1/cities/autocomplete/")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(3000l));


    }

    @Test
    void testReturnsInformationForTheTop50Cities() {
        given()
                .queryParam("apikey", getApiKey())
                .pathParams("number", "50")
                .when()
                .get(getBaseUrl() + "/locations/v1/topcities/{number}")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(3000l));


    }

    @Test
    void testGetWeatherOneDayReturn() {

        given()
                .queryParam("apikey", getApiKey())
                .pathParams("code", "28143")
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/1day/{code}")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(4000l))
                .assertThat()
                .body("DailyForecasts.size()", is(1));
    }
    @Test
    void testGetWeatherFiveDayReturn() {

        given()
                .queryParam("apikey", getApiKey())
                .pathParams("code", "28143")
                .when()
                .get(getBaseUrl() + "/forecasts/v1/daily/5day/{code}")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(Matchers.lessThan(4000l))
                .assertThat()
                .body("DailyForecasts.size()", is(5));
    }


}


