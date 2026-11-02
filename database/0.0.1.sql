BEGIN;

--Tabla usuarios
CREATE TABLE IF NOT EXISTS usuario (
    id        BIGSERIAL PRIMARY KEY,
    tipo_usuario      VARCHAR(255),
    pass VARCHAR(255) not null,
    username   VARCHAR(255) not null);

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

--Añadir tipo gasto

--Tabla tipo_gasto (cada usuario configura sus propios tipos)
CREATE TABLE IF NOT EXISTS tipo_gasto (
        id BIGSERIAL PRIMARY KEY,
        id_usuario BIGINT NOT NULL,
        nombre VARCHAR(255) NOT NULL,
    CONSTRAINT fk_tipo_gasto_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id),
    CONSTRAINT uk_tipo_gasto_usuario_nombre UNIQUE (id_usuario, nombre)
    );
CREATE INDEX IF NOT EXISTS idx_tipo_gasto_id_usuario
    ON tipo_gasto (id_usuario);

--Relación opcional gasto -> tipo_gasto (los gastos existentes quedan sin tipo)
ALTER TABLE gasto ADD COLUMN IF NOT EXISTS id_tipo_gasto BIGINT NULL;

ALTER TABLE gasto
    ADD CONSTRAINT fk_gasto_tipo_gasto
        FOREIGN KEY (id_tipo_gasto) REFERENCES tipo_gasto(id)
            ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_gasto_id_tipo_gasto
    ON gasto (id_tipo_gasto);


COMMIT;