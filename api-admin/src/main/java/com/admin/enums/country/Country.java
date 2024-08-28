package com.admin.enums.country;

import lombok.Getter;

import java.util.Optional;

@Getter
public enum Country {
    US("United States"),
    KR("South Korea"),
    JP("Japan"),
    CN("China"),
    UK("United Kingdom"),
    DE("Germany"),
    FR("France"),
    IT("Italy"),
    ES("Spain"),
    CA("Canada"),
    AU("Australia"),
    NZ("New Zealand"),
    SG("Singapore"),
    TH("Thailand"),
    MY("Malaysia"),
    ID("Indonesia"),
    PH("Philippines"),
    VN("Vietnam"),
    IN("India"),
    BR("Brazil");

    private final String fullName;

    Country(String fullName) {
        this.fullName = fullName;
    }

    public static Optional<Country> fromCode(String code) {
        for (Country country : Country.values()) {
            if (country.name().equalsIgnoreCase(code)) { // 소문자로 들어올 경우 대비
                return Optional.of(country);
            }
        }
        return Optional.empty();
    }

    public static boolean isValidCountry(String code, String name) {
        return fromCode(code)
                .map(country -> country.fullName.equalsIgnoreCase(name))
                .orElse(false);
    }
}
