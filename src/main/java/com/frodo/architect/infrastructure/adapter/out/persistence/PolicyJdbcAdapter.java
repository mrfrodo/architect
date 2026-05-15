package com.frodo.architect.infrastructure.adapter.out.persistence;

import com.frodo.architect.domain.model.Money;
import com.frodo.architect.domain.model.Policy;
import com.frodo.architect.domain.model.PolicyStatus;
import com.frodo.architect.application.port.out.PolicyRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PolicyJdbcAdapter implements PolicyRepository {

    private final JdbcTemplate jdbcTemplate;

    public PolicyJdbcAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Policy policy) {
        jdbcTemplate.update(
                "INSERT INTO policies (policy_id, policy_number, property_id, policy_holder_id, premium_amount, premium_currency, from_date, to_date, policy_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                policy.getPolicyId(),
                policy.getPolicyNumber(),
                policy.getPropertyId(),
                policy.getPolicyHolderId(),
                policy.getPremium().amount(),
                policy.getPremium().currency().getCurrencyCode(),
                policy.getFromDate(),
                policy.getToDate(),
                policy.getPolicyStatus().name()
        );
    }

    @Override
    public Optional<Policy> findById(UUID policyId) {

        String sql = "SELECT policy_id, policy_number, property_id, policy_holder_id, " +
                "premium_amount, premium_currency, from_date, to_date, policy_status " +
                "FROM policies WHERE policy_id = ?";

        try {
            Policy policy = jdbcTemplate.queryForObject(sql, policyRowMapper(), policyId);
            return Optional.ofNullable(policy);
        } catch (EmptyResultDataAccessException e) {
            // Spring throws this if 0 rows are returned. Hexagonal outbound ports expect an empty Optional.
            return Optional.empty();
        }
    }

    private RowMapper<Policy> policyRowMapper() {
        return (rs, rowNum) -> {
            BigDecimal amount = rs.getBigDecimal("premium_amount");
            String currencyCode = rs.getString("premium_currency");
            Money premium = new Money(amount, Currency.getInstance(currencyCode));
            PolicyStatus status = PolicyStatus.valueOf(rs.getString("policy_status"));

            return Policy.reconstitute(
                    rs.getObject("policy_id", UUID.class),
                    rs.getString("policy_number"),
                    rs.getObject("property_id", UUID.class),
                    rs.getObject("policy_holder_id", UUID.class),
                    premium,
                    rs.getObject("from_date", LocalDate.class),
                    rs.getObject("to_date", LocalDate.class),
                    status
            );
        };
    }
}
