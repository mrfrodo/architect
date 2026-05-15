package com.frodo.architect.adapter.in.web;

import com.frodo.architect.application.port.in.CreatePolicyCommand;
import com.frodo.architect.application.port.in.CreatePolicyUseCase;
import com.frodo.architect.application.port.in.GetPolicyUseCase;
import com.frodo.architect.domain.model.Money;
import com.frodo.architect.domain.model.Policy;
import com.frodo.architect.infrastructure.adapter.in.web.PolicyController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreatePolicyUseCase createPolicyUseCase;

    @MockitoBean
    private GetPolicyUseCase getPolicyUseCase;

    @Test
    @DisplayName("POST /policies should map HTTP request to CreatePolicyCommand and return 201 Created")
    void shouldCreatePolicyAndReturn201() throws Exception {
        // Given
        String expectedPolicyNumber = "POL-2026-001";

        // Create a REAL instance using your public factory method instead of 'new'
        Policy realPolicy = Policy.create(
                UUID.randomUUID(),
                expectedPolicyNumber,
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Money(BigDecimal.valueOf(100000), Currency.getInstance("SEK")),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2027, 1, 1)
        );

        // Stub the mock use case to return your valid entity
        when(createPolicyUseCase.createPolicy(any(CreatePolicyCommand.class)))
                .thenReturn(realPolicy);

        mockMvc.perform(post("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "policyHolderId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
                    "property": {
                        "streetAddress": "Stortorget 1",
                        "city": "Malmö",
                        "postalCode": "211 22"
                    },
                    "fromDate": "2026-01-01",
                    "toDate": "2027-01-01"
                }
                """))
                .andExpect(status().isCreated());

        // Assert
        ArgumentCaptor<CreatePolicyCommand> commandCaptor = ArgumentCaptor.forClass(CreatePolicyCommand.class);
        verify(createPolicyUseCase).createPolicy(commandCaptor.capture());

        CreatePolicyCommand capturedCommand = commandCaptor.getValue();
        assertEquals(expectedPolicyNumber, capturedCommand.policyNumber());
    }

    @Test
    @DisplayName("POST /policies should underwrite a valid property request and return 201 Created")
    void shouldCreatePolicyAndReturn201asd() throws Exception {

        mockMvc.perform(post("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "policyNumber": "POL-2026-001",
                    "propertyId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
                    "policyHolderId": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
                    "premiumAmount": 100000,
                    "premiumCurrency": "SEK",
                    "fromDate": "2026-01-01",
                    "toDate": "2027-01-01"
                }
                """))
                .andExpect(status().isCreated());

        Mockito.verify(createPolicyUseCase).createPolicy(any(CreatePolicyCommand.class));
    }

}