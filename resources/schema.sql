
DROP TABLE IF EXISTS rentals;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS customers;
--клиенты создавать
CREATE TABLE customers (
  id SERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  phone VARCHAR(30) NOT NULL UNIQUE
);

--карс создавать
CREATE TABLE cars (
  id SERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL UNIQUE,
  daily_price NUMERIC(10,2) NOT NULL CHECK (daily_price > 0),
  is_available BOOLEAN NOT NULL DEFAULT TRUE
);

--Таблица аренд
CREATE TABLE rentals (
  id SERIAL PRIMARY KEY,
  customer_id INT NOT NULL REFERENCES customers(id),
  car_id INT NOT NULL REFERENCES cars(id),
  start_date DATE NOT NULL,
  days INT NOT NULL CHECK (days > 0),
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

--Клиенты
INSERT INTO customers(name, phone) VALUES
('Ardak', '+7-777-112-56-31'),
('Nursultan', '+7-701-202-33-49'),
('Arai', '+7-775-933-40-15');

--Машины
INSERT INTO cars(name, daily_price, is_available) VALUES
('Mercedes G-Class', 55000, TRUE),
('Audi A6', 38000, TRUE),
('Tesla Model 3', 30000, TRUE),
('Changan CS55', 22000, TRUE);

--Проверка
SELECT * FROM customers;
SELECT * FROM cars;
SELECT * FROM rentals;



