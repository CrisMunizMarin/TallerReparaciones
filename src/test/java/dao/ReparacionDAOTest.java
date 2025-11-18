package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import dao.mysql.*;
import entities.Cliente;
import entities.Reparacion;
import entities.Rol;
import entities.Usuario;
import entities.Vehiculo;

class ReparacionDAOTest {

	@Test
	void testinsert() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
		ReparacionDAOMySQL reparacionDAO = (ReparacionDAOMySQL) factory.getReparacionDAO();
		ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
		UsuarioDAOMySQL usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
		VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
		
		//Insertar un cliente
		Cliente cliente1 = new Cliente("Ana", "87654321F", "987654654", "ana@test.com");
		clienteDAO.insert(cliente1);
		int clienteId = cliente1.getId_cliente();
		
		 //Insertar un vehiculo
		Vehiculo vehiculo1 = new Vehiculo("INS8888", "Renault", "Clio", clienteId);
	    vehiculoDAO.insert(vehiculo1);
	    int vehiculoId = vehiculo1.getId_vehiculo();
	    
	    //Insertar usuario mecanico
	    Usuario usuario1 = new Usuario("usuario1Test", "77777777U", "pass123", Rol.MECANICO);
	    usuarioDAO.insert(usuario1);
	    int usuarioId = usuario1.getId_usuario();
		
		//Insertar una reparación
		//Reparacion reparacion1 = new Reparacion("fuga de aceite", Date.);
	}

}
