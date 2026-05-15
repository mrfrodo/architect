package com.frodo.architect.application.port.in;

import com.frodo.architect.domain.model.Policy;

import java.util.Optional;
import java.util.UUID;

public interface GetPolicyUseCase {
    Optional<Policy> findById(UUID policyId);
}
