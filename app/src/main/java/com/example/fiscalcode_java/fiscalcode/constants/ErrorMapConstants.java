package com.example.fiscalcode_java.fiscalcode.constants;

import com.example.fiscalcode_java.R;

import java.util.HashMap;
import java.util.Map;

public class ErrorMapConstants {

    public static Map<String, Integer> getErrorMap() {
        Map<String, Integer> errorMap = new HashMap<>();
        errorMap.put("err_first_name", R.string.err_first_name);
        errorMap.put("err_last_name", R.string.err_last_name);
        errorMap.put("err_gender_day", R.string.err_gender_day);
        errorMap.put("err_gender_parse", R.string.err_gender_parse);
        errorMap.put("err_dob_format", R.string.err_dob_format);
        errorMap.put("err_date_parse", R.string.err_date_parse);
        errorMap.put("err_date_invalid", R.string.err_date_invalid);
        errorMap.put("err_date_range", R.string.err_date_range);
        errorMap.put("err_pob_not_found", R.string.err_pob_not_found);
        errorMap.put("err_fc_char", R.string.err_fc_char);
        return errorMap;
    }
}
