package com.example.fiscalcode_java.fiscalcode.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Town {

    private String name;
    private String cadastral_code;
    private String province;
}
