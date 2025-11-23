CREATE TABLE transportistas (
    id_transportista SERIAL PRIMARY KEY,
    nombre VARCHAR(255),
    apellido VARCHAR(255),
    licencia VARCHAR(50)
);

CREATE TABLE camiones (
    patente VARCHAR(20) PRIMARY KEY,
    disponibilidad BOOLEAN DEFAULT TRUE,
    capacidad_kg DOUBLE PRECISION,
    capacidad_m3 DOUBLE PRECISION,
    consumo_gl DOUBLE PRECISION,
    costo_km DOUBLE PRECISION,
    id_transportista INTEGER REFERENCES transportistas(id_transportista)
);