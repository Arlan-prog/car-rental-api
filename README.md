# Car Rental (Java + JDBC + PostgreSQL)

## Project Overview
Console-based Java application for a simple car rental system.
Uses 'PostgreSQL' as a database and JDBC (PreparedStatement) for CRUD operations.

## Features
- Customers CRUD
- Cars CRUD
- Create Rental (Customer rents a Car)
- Updates car availability after rental
- Business rule + error handling with custom exceptions

## OOP (Advanced)
- Abstract class: BaseEntity (common fields + abstract methods)
- Inheritance: Customer and Car extend 'BaseEntity'
- Interfaces: Validatable, PricedItem
- Composition: Rental has Customer + Car
- Polymorphism: describe() implemented differently in subclasses

## Database
Tables:
- customers(id, name, phone UNIQUE)
- cars(id, name UNIQUE, daily_price CHECK > 0, is_available)
- rentals(id, customer_id FK, car_id FK, start_date, days CHECK > 0, status)

SQL file: src/main/resources/schema.sql
## How to Run
1. Create database: car_rental_db in pgAdmin
2. Run schema.sql (creates tables + inserts seed data)
3. Open project in IntelliJ IDEA
4. Run Main.java

## Screenshots
Screenshots are in docs/screenshots/:
1. customers in pgAdmin
2. cars before rental
3. console successful run
4. console error/business rule
5. rentals table after running program
6. cars after rental (`is_available=false`)

## Reflection
I learned how to connect Java to PostgreSQL using JDBC, implement CRUD with PreparedStatement,
and apply OOP concepts (abstract class, interfaces, inheritance, composition) with layered architecture
(controller → service → repository).
