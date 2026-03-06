CREATE TABLE reward_transaction (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_id BIGINT,
    description VARCHAR(255) NOT NULL,
    points_delta INTEGER NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_reward_transaction_customer ON reward_transaction(customer_id);
CREATE INDEX idx_reward_transaction_created_at ON reward_transaction(created_at DESC);
