package com.frodo.architect.adapter.out.persistence;

import com.frodo.architect.domain.model.Money;
import com.frodo.architect.domain.model.Policy;
import com.frodo.architect.domain.model.PolicyStatus;
import com.frodo.architect.application.port.out.PolicyRepository;
import com.frodo.architect.infrastructure.adapter.out.persistence.PolicyJdbcAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

@JdbcTest
@Import(PolicyJdbcAdapter.class)
public class PolicyJdbcAdapterTest {

    @Autowired
    private PolicyRepository adapter;

    @Test
    void saveThenFindPolicy() {

        // given
        Policy policy =  Policy.create(
                UUID.randomUUID(),
                "POL-TEST-001",
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Money(new BigDecimal("100000"), Currency.getInstance("NOK")),
                LocalDate.of(1900, 1, 1),
                LocalDate.of(2099, 1, 1)
        );

        // when
        adapter.save(policy);
        Optional<Policy> found = adapter.findById(policy.getPolicyId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getPolicyStatus()).isEqualTo(PolicyStatus.DRAFT);

    }

    @Test
    void savedActivePolicyComesBackAsActive() {
        // given - create and activate a policy
        // given
        Policy policy =  Policy.create(
                UUID.randomUUID(),
                "POL-TEST-001",
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Money(new BigDecimal("100000"), Currency.getInstance("NOK")),
                LocalDate.of(1900, 1, 1),
                LocalDate.of(2099, 1, 1)
        );

        policy.activate();

        // when
        adapter.save(policy);
        Optional<Policy> found = adapter.findById(policy.getPolicyId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getPolicyStatus()).isEqualTo(PolicyStatus.ACTIVE);
    }

}
