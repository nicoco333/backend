CREATE TABLE clientes (
    id_cliente SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    apellido VARCHAR(255),
    mail VARCHAR(255) UNIQUE NOT NULL,
    telefono VARCHAR(50)
);

INSERT INTO clientes (nombre, apellido, mail, telefono) VALUES 
('Juan', 'Perez', 'juan@test.com', '123456');