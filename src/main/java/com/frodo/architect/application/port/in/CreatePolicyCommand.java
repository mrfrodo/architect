package com.frodo.architect.application.port.in;

import com.frodo.architect.domain.model.Money;
import java.time.LocalDate;
import java.util.UUID;

public record CreatePolicyCommand(String policyNumber,
                                  UUID propertyId,
                                  UUID policyHolderId,
                                  Money premium,
                                  LocalDate fromDate,
                                  LocalDate toDate) {
}
