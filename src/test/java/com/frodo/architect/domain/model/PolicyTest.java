package com.frodo.architect.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

/**
 * A pure domain TEST
 */
public class PolicyTest {

    @Test
    void newPolicyIsAlwaysDraft() {

        /**
         *         Given a new policy
         *         When it is created
         *         Then it should be in DRAFT status
         */

        Policy testPolicy = createTestPolicy();

        assertThat(testPolicy.getPolicyStatus()).isEqualTo(PolicyStatus.DRAFT);

    }

    @Test
    void activePolicyTransitionsToActive() {
        // given - a DRAFT policy
        // when - activate() is called
        // then - status is ACTIVE

        Policy draftPolicy = createTestPolicy();

        draftPolicy.activate();

        assertThat(draftPolicy.getPolicyStatus()).isEqualTo(PolicyStatus.ACTIVE);

    }

    @Test
    void activatingNonDraftPolicyThrowsException() {
        // given - a DRAFT policy that has been activated
        // when - activate() is called again
        // then - IllegalStateException is thrown

        Policy draftPolicy = createTestPolicy();

        draftPolicy.activate();
        assertThrows(IllegalStateException.class, () -> draftPolicy.activate()); // second activate - must throw
    }

    private Policy createTestPolicy() {
        return Policy.create(
                UUID.randomUUID(),
                "POL-TEST-001",
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Money(new BigDecimal("100000"), Currency.getInstance("NOK")),
                LocalDate.of(1900, 1, 1),
                LocalDate.of(2099, 1, 1)
        );
    }

}
