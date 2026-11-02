CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO usuario (tipo_usuario, pass, username)
VALUES ('OWNER',  crypt('tiraquelibras', gen_salt('bf', 10)), 'admindesarrollo');