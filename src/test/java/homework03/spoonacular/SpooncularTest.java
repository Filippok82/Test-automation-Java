package homework03.spoonacular;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.Argument;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpooncularTest extends SpoonacularAbstractTest {
    @Test
    void TestSearchRecipesMaxCalories() throws ParseException, IOException {


        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "pasta")
                .queryParam("maxCalories", 600)
                .queryParam("number", 10)
                .when().get(getBaseUrl() + "/recipes/complexSearch")
                .prettyPeek()
                .then().statusCode(200)
                .time(lessThan(5000L))
                .body("number", is(10))
                .body("results.nutrition.nutrients.flatten().findAll { it.amount < 600 }.name", hasItems("Calories"));

    }

    @Test
    void testSearchRecipesByMaxReadyTimeMaxProtein() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("maxReadyTime", 15)
                .queryParam("maxProtein", 100)
                .when().get(getBaseUrl() + "/recipes/complexSearch")
                .prettyPeek()
                .then().statusCode(200)
                .time(lessThan(4000L))
                .body("results.nutrition.nutrients.flatten().findAll { it.amount < 100 }.name", hasItems("Protein"));


    }

    @Test
    void testSearchRecipesMaxVitaminC() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", 5)
                .queryParam("maxVitaminC", 100)
                .when().get(getBaseUrl() + "/recipes/complexSearch")
                .prettyPeek()
                .then().statusCode(200)
                .time(lessThan(4000L))
                .body("results.nutrition.nutrients.flatten().findAll { it.amount < 100 }.name", hasItems("Vitamin C"));


    }

    @Test
    void testSearchRecipesByIngredients() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", 1)
                .queryParam("ingredients", "egg, milk")
                .when().get(getBaseUrl() + "/recipes/findByIngredients")
                .prettyPeek()
                .then().statusCode(200)
                .time(lessThan(5000L))
                .body("usedIngredients.aisle.flatten()", hasItems("Milk, Eggs, Other Dairy"))
                .body("usedIngredientCount ", hasItems(2));


    }

    @Test
    void testSearchIngredients() {

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "tomat")
                .when().get(getBaseUrl() + "/food/ingredients/search")
                .prettyPeek()
                .then().statusCode(200)
                .time(lessThan(5000L))
                .body("results.name.flatten()", hasItems("tomato"));


    }

    @Test
    void testSearchInfirmationProduct() {
        given()
                .queryParam("apiKey", getApiKey())
                .pathParams("code", "22340")
                .when().get(getBaseUrl() + "/food/products/{code}")
                .prettyPeek()
                .then().statusCode(200)
                .time(lessThan(5000L));

    }

    @Test
    void testAutocompleteMenuItemSearch() {

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "pizza")
                .queryParam("number", 10)
                .when().get(getBaseUrl() + "/food/products/suggest")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(lessThan(5000L))
                .body("results.size() ", is(10));
    }

    @Test
    void testReturnWineDiscription() {

        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("wine", "zinfandel")
                .when().get(getBaseUrl() + "/food/wine/description")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(lessThan(5000L))
                .body("wineDescription", notNullValue());
    }

    @Test
    void testReturtWinePairingByPrice() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("food", "pizza")
                .queryParam("maxPrice", 9)
                .when().get(getBaseUrl() + "/food/wine/pairing")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(lessThan(5000L))
                .body("productMatches.price.flatten()", hasItems("$8.989999771118164"))
                .body("pairingText", notNullValue());



    }

    @Test
    void testReturtConvertAmounts() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("ingredientName", "salt")
                .queryParam("sourceAmount", 2)
                .queryParam("sourceUnit", "cups")
                .queryParam("targetUnit", "grams")
                .when().get(getBaseUrl() + "/recipes/convert")
                .prettyPeek()
                .then()
                .statusCode(200)
                .time(lessThan(6000L))
                .body("targetAmount", is(584.f))
                .body("sourceAmount", is(2.f));


    }



}

