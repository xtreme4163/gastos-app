BEGIN;

--Tabla gasto
CREATE TABLE IF NOT EXISTS gasto (
  id BIGSERIAL PRIMARY KEY,
  id_usuario BIGINT NOT null,
  concepto      VARCHAR(255) NOT NULL,
  importe   NUMERIC(19,2) NOT NULL);

ALTER TABLE gasto
ADD CONSTRAINT fk_gasto_usuario
      FOREIGN KEY (id_usuario) REFERENCES usuario(id);

CREATE INDEX idx_gasto_id_usuario
    ON gasto (id_usuario);

ALTER TABLE gasto ADD COLUMN fecha date NOT NULL DEFAULT CURRENT_DATE;

--Tabla usuarios
CREATE TABLE IF NOT EXISTS usuario (
  id        BIGSERIAL PRIMARY KEY,
  tipo_usuario      VARCHAR(255),
  pass VARCHAR(255) not null,
  username   VARCHAR(255) not null);

COMMIT;