CREATE TABLE estados (
    id_estado SERIAL PRIMARY KEY,
    nombre VARCHAR(50),
    ambito VARCHAR(50)
);

CREATE TABLE solicitudes_traslado (
    id_solicitud SERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL, -- Solo guardamos el ID
    fecha DATE,
    origen VARCHAR(255),
    destino VARCHAR(255),
    id_estado INTEGER REFERENCES estados(id_estado),
    costo_estimado DOUBLE PRECISION
);

-- Insertar estados básicos
INSERT INTO estados (nombre, ambito) VALUES ('BORRADOR', 'SOLICITUD'), ('PENDIENTE', 'SOLICITUD'), ('EN_VIAJE', 'SOLICITUD');