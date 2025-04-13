package br.com.clash_utilities.model;

public record Location(
        int id, String name, boolean isCountry, String countryCode
) {
}
