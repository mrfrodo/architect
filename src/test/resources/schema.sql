CREATE TABLE policies (
    -- Technical identity (Internal PK)
    policy_id UUID PRIMARY KEY,

    -- Business identity (Human-readable, indexed for fast lookup)
    policy_number VARCHAR(50) NOT NULL UNIQUE,

    -- Relationships
    property_id UUID NOT NULL,
    policy_holder_id UUID NOT NULL,

    -- Flattened Money Value Object
    premium_amount DECIMAL(15, 2) NOT NULL,
    premium_currency VARCHAR(3) NOT NULL,

    -- Validity Dates
    from_date DATE NOT NULL,
    to_date DATE NOT NULL,

    -- Policy State (Stored as String to easily match the Java Enum)
    policy_status VARCHAR(20) NOT NULL,

    -- Standard guardrails for date integrity
    CONSTRAINT chk_policy_dates CHECK (to_date >= from_date),
    CONSTRAINT chk_premium_positive CHECK (premium_amount >= 0)
);

-- Index for human searches via policy number (e.g., POL-2026-001)
CREATE INDEX idx_policies_business_number ON policies(policy_number);