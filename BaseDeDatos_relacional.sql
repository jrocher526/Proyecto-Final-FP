-- 1. Crear la base de datos y usarla
CREATE DATABASE IF NOT EXISTS tpv_restaurante;
USE tpv_restaurante;

-- 2. Crear tabla de Usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    rol VARCHAR(20) DEFAULT 'CAMARERO'
);

-- 3. Crear tabla de Productos
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL
);

-- 4. Crear tabla de Tickets
CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total DECIMAL(10, 2) NOT NULL,
    observaciones TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos

-- Insertar usuarios iniciales
INSERT INTO usuarios (nombre, password, rol) VALUES ('admin', '1234', 'ADMINISTRADOR');
INSERT INTO usuarios (nombre, password, rol) VALUES ('DAVID', '2004', 'CAMARERO');

-- Insertar productos en la carta
INSERT INTO productos (nombre, categoria, precio) VALUES ('CERVEZA', 'BEBIDA', 2.50);
INSERT INTO productos (nombre, categoria, precio) VALUES ('REFRESCO', 'BEBIDA', 2.00);
INSERT INTO productos (nombre, categoria, precio) VALUES ('AGUA', 'BEBIDA', 1.50);
INSERT INTO productos (nombre, categoria, precio) VALUES ('PATATAS BRAVAS', 'COMIDA', 4.50);
INSERT INTO productos (nombre, categoria, precio) VALUES ('BOCADILLO CALAMARES', 'COMIDA', 5.00);
INSERT INTO productos (nombre, categoria, precio) VALUES ('TARTA DE QUESO', 'POSTRE', 3.50);
