package com.frodo.architect.application.service;

import com.frodo.architect.domain.model.Policy;
import com.frodo.architect.application.port.in.CreatePolicyCommand;
import com.frodo.architect.application.port.in.CreatePolicyUseCase;
import com.frodo.architect.application.port.in.GetPolicyUseCase;
import com.frodo.architect.application.port.out.PolicyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PolicyService implements CreatePolicyUseCase, GetPolicyUseCase {

    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public Policy createPolicy(CreatePolicyCommand command) {
        Policy newPolicy = Policy.create(
                UUID.randomUUID(),
                command.policyNumber(),
                command.propertyId(),
                command.policyHolderId(),
                command.premium(),
                command.fromDate(),
                command.toDate()
        );
        policyRepository.save(newPolicy);
        return newPolicy;
    }

    @Override
    public Optional<Policy> findById(UUID policyId) {
        return Optional.empty();
    }
}
