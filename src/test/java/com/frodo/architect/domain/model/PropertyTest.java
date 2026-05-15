package com.frodo.architect.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertyTest {

    @Test
    @DisplayName("Should successfully create Property when inputs are valid (Happy Path)")
    void shouldCreatePropertyWhenInputsAreValid() {
        // Given  & When
        Property propertyWithSpace = new Property("Stortorget 1", "Malmö", "211 22");
        Property propertyWithoutSpace = new Property("Lundavägen 4", "Lund", "22240");

        // Then
        assertAll("Valid property creation",
                () -> assertEquals("Stortorget 1", propertyWithSpace.streetAddress()),
                () -> assertEquals("Malmö", propertyWithSpace.city()),
                () -> assertEquals("211 22", propertyWithSpace.postalCode()),

                () -> assertEquals("Lundavägen 4", propertyWithoutSpace.streetAddress()),
                () -> assertEquals("Lund", propertyWithoutSpace.city()),
                () -> assertEquals("22240", propertyWithoutSpace.postalCode())
        );
    }

}
