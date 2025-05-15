--liquibase formatted sql
--changeset ifanov:1 labels:v0.0.1

CREATE TYPE "gas_types_type" AS ENUM ('ai_92', 'ai_95', 'ai_98', 'ai_100', 'diesel');
CREATE TYPE "statuses_type" AS ENUM ('created', 'in_process', 'success', 'error');

CREATE TABLE "orders" (
	"id" text primary key constraint orders_id_length_ctr check (length("id") < 64),
    "status" statuses_type not null,
    "gas_type" gas_types_type not null,
    "price" text not null,
    "quantity" text not null,
    "summary_price" text not null,
	"lock" text not null constraint orders_lock_length_ctr check (length(id) < 64)
);

CREATE INDEX orders_gas_type_idx on "orders" using hash ("gas_type");
CREATE INDEX orders_status_idx on "orders" using hash ("status");
