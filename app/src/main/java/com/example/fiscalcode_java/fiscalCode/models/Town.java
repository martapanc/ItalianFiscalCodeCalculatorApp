package com.example.fiscalcode_java.fiscalCode.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Town {

    private String townName;
    private String townCode;
    private String province;
}
