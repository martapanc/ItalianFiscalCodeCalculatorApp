package com.example.fiscalcode_java.fiscalCode.utils;

import com.example.fiscalcode_java.fiscalCode.models.Town;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static String[] readTownNameList(InputStream path) throws IOException {
        List<Town> towns = read(path);
        String[] townNames = new String[towns.size()];
        for (int i = 0; i < towns.size(); i++) {
            townNames[i] = towns.get(i).getTownName() + " (" + towns.get(i).getProvince() + ")";
        }
        return townNames;
    }
}
