package com.example.fiscalcode_java.fiscalcode.constants;

import com.example.fiscalcode_java.R;

import java.util.HashMap;
import java.util.Map;

public class ErrorMap {

    public static Map<String, Integer> getErrorMap() {
        Map<String, Integer> errorMap = new HashMap<>();
        errorMap.put("err_gender_day", R.string.err_gender_day);
        errorMap.put("err_last_name", R.string.err_last_name);
        errorMap.put("err_first_name", R.string.err_first_name);
        return errorMap;
    };
}
