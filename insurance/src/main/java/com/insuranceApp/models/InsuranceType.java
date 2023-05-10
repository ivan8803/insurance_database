package com.insuranceApp.models;

import lombok.Getter;

@Getter
public enum InsuranceType {

    CESTOVNI_POJISTENI ("Cestovní pojištění"),
    ZIVOTNI_POJISTENI ("Životní pojištění"),
    POJISTENI_MAJETKU ("Pojištění majetku"),
    POJISTENI_ODPOVEDNOSTI ("Pojištění odpovědnosti"),
    HAVARIJNI_POJISTENI ("Havarijní pojištění"),
    URAZOVE_POJISTENI ("úrazové pojištění");

    private String text;

    InsuranceType(String text) {
        this.text = text;
    }
}
