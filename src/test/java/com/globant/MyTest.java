package com.globant;

import io.restassured.response.Response;
import org.apache.commons.validator.GenericValidator;
import org.json.JSONObject;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.List;


import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class MyTest {

@Test
    public void test1() {
        Response response;
        response = given().get("https://swapi.dev/api/people/2/");

        Assert.assertEquals(response.getStatusCode(), 200);
        assertEquals(6,response.jsonPath().getList("films").size());
        assertEquals("gold", response.jsonPath().getJsonObject("skin_color"));
    }

@Test
    public void test1_Version2() {
        Response response;
        response = given().get("https://swapi.dev/api/people/2");

        System.out.println(response.body());
        assertEquals(200, response.getStatusCode());

        assertEquals(6,response.jsonPath().getList("films").size());
        String body = response.getBody().asString();
        JSONObject json = new JSONObject(body);

        String skinColor = "gold";
        System.out.println(json.getString("skin_color"));
        assertEquals(skinColor,json.getString("skin_color"));
        assertEquals(skinColor,response.jsonPath().getJsonObject("skin_color"));
    }

@Test
    public void test2(){
        Response response;
        response = given().get("https://swapi.dev/api/people/2/");

        assertEquals(200, response.getStatusCode());

        List<String> allFilms;
        allFilms = response.jsonPath().getJsonObject("films");
        Response secondFilm;
        secondFilm = given().get(allFilms.get(1));
        String date = secondFilm.jsonPath().getJsonObject("release_date").toString();
        assertTrue(GenericValidator.isDate(date, "yyyy-MM-dd", true));
        System.out.println(date);


        List<String> characters = secondFilm.jsonPath().getList("characters");
        assertTrue( "Characters list is empty or has only one element",characters.size() > 1);
        List<String> planets = secondFilm.jsonPath().getList("planets");
        assertTrue( "Planets list is empty or has only one element",planets.size() > 1);
        List<String> starships = secondFilm.jsonPath().getList("starships");
        assertTrue( "Starships list is empty or has only one element",starships.size() > 1);
        List<String> vehicles = secondFilm.jsonPath().getList("vehicles");
        assertTrue( "Vehicles list is empty or has only one element",vehicles.size() > 1);
        List<String> species = secondFilm.jsonPath().getList("species");
        assertTrue( "Species list is empty or has only one element",species.size() > 1);
    }
    @Test
    public void test3(){
        Response response;
        response = given().get("https://swapi.dev/api/people/2/");
        assertEquals(200, response.getStatusCode());

        List<String> allFilms = response.jsonPath().getJsonObject("films");
        Response secondFilm;
        secondFilm = given().get(allFilms.get(1));

        List<String> allPlanets = secondFilm.jsonPath().getJsonObject("planets");
        Response firstPlanet = given().get(allPlanets.get(0));
        String gravityResult = "1.1 standard";
        assertEquals(gravityResult, firstPlanet.jsonPath().getJsonObject("gravity"));
        assertEquals("tundra, ice caves, mountain ranges",firstPlanet.jsonPath().getJsonObject("terrain"));

        Response planetUrl ;
        planetUrl = given().get("https://swapi.dev/api/planets/4/");

        assertEquals(firstPlanet.asString(), planetUrl.asString());
    }
    @Test
    public void test4(){
        Response response;
        response = given().get("https://swapi.dev/api/films/7/");
        assertEquals(404, response.getStatusCode());
    }
}
