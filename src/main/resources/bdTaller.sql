CREATE DATABASE IF NOT EXISTS taller;

USE taller;

CREATE TABLE cliente(
id_cliente INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
dni_cliente VARCHAR(15) NOT NULL UNIQUE,
telefono VARCHAR(45),
email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE vehiculo(
id_vehiculo INT AUTO_INCREMENT PRIMARY KEY,
matricula VARCHAR(45) UNIQUE NOT NULL,
marca VARCHAR(45),
modelo VARCHAR(45),
cliente_id INT,
FOREIGN KEY (cliente_id) REFERENCES cliente(id_cliente) ON DELETE CASCADE
-- Si se elimina un cliente se eliminan todos sus vehiculos (ON DELETE CASCADE)
);

CREATE TABLE usuario(
id_usuario INT AUTO_INCREMENT PRIMARY KEY,
nombre_usuario VARCHAR(45) UNIQUE NOT NULL,
dni_usuario VARCHAR(15) NOT NULL UNIQUE,
password VARCHAR(255) NOT NULL,
rol ENUM("INVITADO", "MECANICO", "ADMINISTRADOR")
);

CREATE TABLE reparacion(
id_reparacion INT AUTO_INCREMENT PRIMARY KEY,
descripcion VARCHAR(255) NOT NULL,
fecha_entrada DATE NOT NULL,
coste_estimado DOUBLE NOT NULL,
estado ENUM("PENDIENTE", "REPARACION", "FINALIZADO") default "PENDIENTE",
fecha_salida DATE NULL,
vehiculo_id INT,
usuario_id INT,
FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id_vehiculo) ON DELETE CASCADE, -- Si se elimina un vehiculo, todas sus reparaciones se eliminan automaticamnete
FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario) ON DELETE SET NULL -- Si se elimina un usuario, la reparación se mantiene pero no su usuario (se setea a Null)
);