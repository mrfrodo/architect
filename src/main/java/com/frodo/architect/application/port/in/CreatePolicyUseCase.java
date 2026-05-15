package com.frodo.architect.application.port.in;

import com.frodo.architect.domain.model.Policy;

public interface CreatePolicyUseCase {
    Policy createPolicy(CreatePolicyCommand command);
}
