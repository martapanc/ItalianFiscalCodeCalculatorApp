package com.example.fiscalcode_java.fiscalcode.constants;

import java.util.HashMap;
import java.util.Map;

public class DateFormat {
    public static final String DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DD_MM_YY = "dd/MM/yy";

    public static Map<Integer, String> getMonthCodeMap() {
        Map<Integer, String> monthMap = new HashMap<>();
        monthMap.put(1, "A");
        monthMap.put(2, "B");
        monthMap.put(3, "C");
        monthMap.put(4, "D");
        monthMap.put(5, "E");
        monthMap.put(6, "H");
        monthMap.put(7, "L");
        monthMap.put(8, "M");
        monthMap.put(9, "P");
        monthMap.put(10, "R");
        monthMap.put(11, "S");
        monthMap.put(12, "T");
        return monthMap;
    }
}
