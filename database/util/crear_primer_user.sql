CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO usuario (tipo_usuario, pass, username)
VALUES (
           'OWNER',
           crypt ('XXXX', gen_salt ('bf', 10)),
           'admindesarrollo'
       );