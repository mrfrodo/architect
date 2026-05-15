package com.frodo.architect.infrastructure.adapter.in.web;

import com.frodo.architect.domain.model.Property;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePolicyRequest(
        UUID policyHolderId,
        Property property,     // Nested DDD Value Object
        LocalDate fromDate,
        LocalDate toDate
) {}