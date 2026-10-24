CREATE TABLE IF NOT EXISTS customers (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS cars (
  id SERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL UNIQUE,
  daily_price NUMERIC(10,2) NOT NULL CHECK (daily_price > 0),
  is_available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS rentals (
  id SERIAL PRIMARY KEY,
  customer_id INT NOT NULL REFERENCES customers(id),
  car_id INT NOT NULL REFERENCES cars(id),
  start_date DATE NOT NULL,
  days INT NOT NULL CHECK (days > 0),
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE', 'FINISHED'))
);

-- Keep the application's active-rental rules true under concurrent requests.
CREATE UNIQUE INDEX IF NOT EXISTS one_active_rental_per_customer
  ON rentals(customer_id) WHERE status = 'ACTIVE';
CREATE UNIQUE INDEX IF NOT EXISTS one_active_rental_per_car
  ON rentals(car_id) WHERE status = 'ACTIVE';

-- Sample-only records; do not use real customer contact details here.
INSERT INTO customers(name, phone) VALUES
  ('Example Customer 1', '+10000000001'),
  ('Example Customer 2', '+10000000002'),
  ('Example Customer 3', '+10000000003')
ON CONFLICT DO NOTHING;

INSERT INTO cars(name, daily_price, is_available) VALUES
  ('Mercedes G-Class', 55000, TRUE),
  ('Audi A6', 38000, TRUE),
  ('Tesla Model 3', 30000, TRUE),
  ('Changan CS55', 22000, TRUE)
ON CONFLICT DO NOTHING;
