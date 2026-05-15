package com.frodo.architect.domain.model;

import java.util.List;
import java.util.Objects;

/**
 * A DDD value object. It is immutable. It is a record
 * @param streetAddress
 * @param city
 * @param postalCode
 */
public record Property(String streetAddress, String city, String postalCode) {

    private static final List<int[]> SUPPORTED_REGIONS = List.of(
            new int[]{20001, 29999} // Skåne
    );

    public Property {
        // Basic null/blank validation
        Objects.requireNonNull(streetAddress, "streetAddress cannot be null");
        Objects.requireNonNull(city, "city cannot be null");
        Objects.requireNonNull(postalCode, "postalCode cannot be null");

        if (streetAddress.isBlank() || city.isBlank()) {
            throw new IllegalArgumentException("Fields cannot be blank");
        }

        // Validate format: "123 45" or "12345"
        if (!postalCode.matches("^\\d{3}\\s?\\d{2}$")) {
            throw new IllegalArgumentException("Invalid postal code format");
        }

        // Normalize
        String normalized = postalCode.replace(" ", "");

        int prefix = Integer.parseInt(normalized.substring(0, 3));

        // Skåne County only: 20001–29999
        if (prefix < 200 || prefix > 299) {
            throw new IllegalArgumentException(
                    "Postal code is not in Skåne County range (200 01 – 299 99)"
            );
        }
    }

}
