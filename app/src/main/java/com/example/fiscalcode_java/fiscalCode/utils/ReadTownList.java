package com.example.fiscalcode_java.fiscalCode.utils;

import com.example.fiscalcode_java.fiscalCode.models.Country;
import com.example.fiscalcode_java.fiscalCode.models.Town;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadTownList {

    public static List<Town> read(InputStream path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode obj = objectMapper.readValue(path, JsonNode.class);
        List<Town> towns = new ArrayList<>();
        for (JsonNode town : obj.get("comuni")) {
            towns.add(new Town(town.get("nome").asText(), town.get("codice").asText(), town.get("provincia").asText()));
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
        List<Town> towns = read(townPath);
        List<Country> countries = readCountries(countryPath);
        String[] placeNames = new String[towns.size() + countries.size()];
        for (int i = 0; i < towns.size(); i++) {
            placeNames[i] = towns.get(i).getTownName() + " (" + towns.get(i).getProvince() + ")";
        }
        for (int i = 0; i < countries.size(); i++) {
            placeNames[i + towns.size()] = countries.get(i).getName() + " (" + countries.get(i).getCountry_code() + ")";
        }
        return placeNames;
    }
}
