CREATE DATABASE IF NOT EXISTS tpv_relacional;

USE tpv_relacional;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL
);

CREATE TABLE mesas (
    numero INT PRIMARY KEY,
    estado VARCHAR(30) NOT NULL
);

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    precio DECIMAL(6,2) NOT NULL
);

CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_ticket INT NOT NULL,
    numero_mesa INT NOT NULL,
    total DECIMAL(8,2) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (numero_mesa)
        REFERENCES mesas(numero)
);

CREATE TABLE ticket_productos (
    id_ticket INT,
    id_producto INT,
    cantidad INT DEFAULT 1,

    PRIMARY KEY (id_ticket, id_producto),

    FOREIGN KEY (id_ticket)
        REFERENCES tickets(id),

    FOREIGN KEY (id_producto)
        REFERENCES productos(id)
);