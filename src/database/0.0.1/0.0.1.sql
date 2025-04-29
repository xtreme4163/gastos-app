BEGIN;

--Tabla gasto
CREATE TABLE IF NOT EXISTS gasto (
  id        BIGSERIAL PRIMARY KEY,
  tipo_gasto      VARCHAR(255),
  importe   NUMERIC(19,2));

--Tabla usuarios
CREATE TABLE IF NOT EXISTS usuario (
  id        BIGSERIAL PRIMARY KEY,
  tipo_usuario      VARCHAR(255),
  pass VARCHAR(255) not null,
  username   VARCHAR(255) not null);

COMMIT;