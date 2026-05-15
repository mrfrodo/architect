package com.frodo.architect.application.service;

import com.frodo.architect.domain.model.Money;
import com.frodo.architect.domain.model.Policy;
import com.frodo.architect.domain.model.PolicyStatus;
import com.frodo.architect.application.port.in.CreatePolicyCommand;
import com.frodo.architect.application.port.in.CreatePolicyUseCase;
import com.frodo.architect.application.port.out.PolicyRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PolicyServiceTest {

    @Test
    void createPolicy() {
        // given
        PolicyRepository repository = Mockito.mock(PolicyRepository.class);
        CreatePolicyUseCase policyService = new PolicyService(repository);
        CreatePolicyCommand command = new CreatePolicyCommand(
                "POL-TEST-001",
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Money(new BigDecimal("100000"), Currency.getInstance("NOK")),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2027, 1, 1)
        );

        // when
        Policy createdPolicy = policyService.createPolicy(command);

        // then
        assertThat(createdPolicy.getPolicyStatus()).isEqualTo(PolicyStatus.DRAFT);
        Mockito.verify(repository).save(createdPolicy);
    }
}
