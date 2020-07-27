package com.example.fiscalcode_java.fiscalcode.utils;

import com.example.fiscalcode_java.fiscalcode.models.Country;
import com.example.fiscalcode_java.fiscalcode.models.Town;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadTownListHelper {

    public static List<Town> readTowns(InputStream path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode obj = objectMapper.readValue(path, JsonNode.class);
        List<Town> towns = new ArrayList<>();
        for (JsonNode town : obj.get("towns")) {
            towns.add(new Town(town.get("id").asText(), town.get("cc").asText(), town.get("p").asText()));
        }

        return towns;
    }

    public static List<Country> readCountries(InputStream path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode obj = objectMapper.readValue(path, JsonNode.class);
        List<Country> countries = new ArrayList<>();
        for (JsonNode country : obj.get("foreign_countries")) {
            countries.add(new Country(
                    country.get("name").asText(),
                    country.get("country_code").asText(),
                    country.get("code").asText(),
                    country.get("continent").asText(),
                    country.get("area").asText()));
        }
        return countries;
    }

    public static String[] readTownNameList(InputStream townPath, InputStream countryPath) throws IOException {
        List<Town> towns = readTowns(townPath);
        List<Country> countries = readCountries(countryPath);
        String[] placeNames = new String[towns.size() + countries.size()];
        for (int i = 0; i < towns.size(); i++) {
            placeNames[i] = towns.get(i).getName() + " (" + towns.get(i).getProvince() + ")";
        }
        for (int i = 0; i < countries.size(); i++) {
            placeNames[i + towns.size()] = countries.get(i).getName() + " (" + countries.get(i).getCountry_code() + ")";
        }
        return placeNames;
    }
}
