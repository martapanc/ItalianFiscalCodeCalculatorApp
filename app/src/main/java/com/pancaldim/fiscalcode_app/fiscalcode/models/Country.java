package com.pancaldim.fiscalcode.fiscalcode.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    private String name;
    private String country_code;
    private String cadastral_code;
    private String continent;
    private String area;
}
