package com.frodo.architect.application.port.out;

import com.frodo.architect.domain.model.Policy;

import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port — defines what the domain requires for Policy persistence.
 * Implementations live in the adapter layer.
 */
public interface PolicyRepository {
    void save (Policy policy);
    Optional<Policy> findById(UUID policyId);
}
