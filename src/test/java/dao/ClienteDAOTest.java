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
		 System.out.println("el cliente " + cliente1.getNombre() + " ha sido insertado");
			
		
	}
	
	@Test
	void testUpdate() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
		ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
		
		Cliente cliente1 = new Cliente("Ana", "87654321F", "987654654", "anaModificado@gmail.com") ;
		clienteDAO.update(cliente1);
		 System.out.println("el cliente " + cliente1.getId_cliente() + " ha sido modificado");
	}

}
