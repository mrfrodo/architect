package com.frodo.architect.infrastructure.adapter.in.web;

import com.frodo.architect.domain.model.Money;
import com.frodo.architect.domain.model.Policy;
import com.frodo.architect.application.port.in.CreatePolicyCommand;
import com.frodo.architect.application.port.in.CreatePolicyUseCase;
import com.frodo.architect.application.port.in.GetPolicyUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Currency;

@RestController
public class PolicyController {

    private final CreatePolicyUseCase createPolicyUseCase;
    private final GetPolicyUseCase getPolicyUseCase;

    public PolicyController(CreatePolicyUseCase createPolicyUseCase, GetPolicyUseCase getPolicyUseCase) {
        this.createPolicyUseCase = createPolicyUseCase;
        this.getPolicyUseCase = getPolicyUseCase;
    }

    @PostMapping("/policies")
    public ResponseEntity<Void> create(@RequestBody CreatePolicyRequest createPolicyRequest) {

        var command = new CreatePolicyCommand(
                createPolicyRequest.policyNumber(),
                createPolicyRequest.propertyId(),
                createPolicyRequest.policyHolderId(),
                new Money(new BigDecimal(createPolicyRequest.premiumAmount()),
                        Currency.getInstance(createPolicyRequest.premiumCurrency())),
                createPolicyRequest.fromDate(),
                createPolicyRequest.toDate()
        );

        Policy newPolicy = createPolicyUseCase.createPolicy(command);
        System.out.println("created: " + newPolicy.getPolicyNumber());

        // 4. Return 201 Created with the Location header
        URI location = null;
        return ResponseEntity.created(location).build();
    }

}