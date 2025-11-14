package dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import dao.mysql.ClienteDAOMySQL;
import entities.Cliente;

class ClienteDAOTest {

	@Test
	void testInsert() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
		ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
		
		Cliente cliente1 = new Cliente("Ana", "87654321F", "987654654", "ana@gmail.com");
		clienteDAO.insert(cliente1);
		int IdGenerado = cliente1.getId_cliente();
		
		 System.out.println("el cliente " + cliente1.getNombre() + " ha sido insertado con el id: " + IdGenerado);
			
		
	}
	
	@Test
	void testUpdate() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
		ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
		
		Cliente cliente2 = new Cliente("Eva", "45236789E", "987654321", "eva@gmail.com") ;
		clienteDAO.insert(cliente2);
		int IdGenerado = cliente2.getId_cliente();
	
		
		//Modificacion
		cliente2.setEmail("evaModificado@gmail.com");
		clienteDAO.update(cliente2);
		
		
		System.out.println("El cliente: " + cliente2.getNombre() + " con id: " + IdGenerado + " se le han modificado datos" );
	}

}
