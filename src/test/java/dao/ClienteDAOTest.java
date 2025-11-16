package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dao.mysql.ClienteDAOMySQL;
import entities.Cliente;

class ClienteDAOTest {

	@Test
	void testInsert() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
		ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
		
		//Insertamos un nuevo cliente
		Cliente cliente1 = new Cliente("Ana", "87654321F", "987654654", "ana@test.com");
		clienteDAO.insert(cliente1);
		int IdGenerado = cliente1.getId_cliente();
		
		//Verificación:Recuperar el cliente insertado por su id generado automaticamente
		Cliente verificacionCliente = clienteDAO.findByDni(cliente1.getDni_cliente());
		
		//Asercciones
		assertNotNull(verificacionCliente,"El cliente no debería ser Null despues del insert");
		assertEquals("Ana", verificacionCliente.getNombre(), "El nombre no coincide");
		assertEquals("ana@test.com", verificacionCliente.getEmail(), "El email no coincide");
		assertTrue(IdGenerado > 0, "El id generado debe ser mayor que 0");
		
		 System.out.println("Test insert ok. El cliente " + verificacionCliente.getNombre() + " ha sido insertado con el id: " + IdGenerado);
		
		 //Limpieza del test(recomendado??)
		 //clienteDAO.delete(cliente1.getDni_cliente());
		
	}
	
	@Test
	void testUpdate() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
		ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
		
		//Insertamos un nuevo cliente
		Cliente cliente2 = new Cliente("Eva", "45236789E", "987654321", "eva@test.com") ;
		clienteDAO.insert(cliente2);
		int IdGenerado = cliente2.getId_cliente();
	
		
		//Modificacion
		cliente2.setEmail("evaModificado@test.com");
		clienteDAO.update(cliente2);
		
		//Verificacion:Modificar datos en uno de los clientes
		Cliente verificacionCliente = clienteDAO.findByDni(cliente2.getDni_cliente());
		
		//Asercciones
		assertNotNull(verificacionCliente, "El cliente no deberia serNull despues del update");
		assertEquals("evaModificado@test.com",verificacionCliente.getEmail(), "El email den la BD ha sido modificado correctamente.");
		
		System.out.println("Test update ok. El email del cliente: " + cliente2.getNombre() + " con id: " + IdGenerado + " se ha actualizado a: " + verificacionCliente.getEmail() );
		
		//Limpieza del test(recomendado??)
		//clienteDAO.delete(cliente2.getDni_cliente());
	}
	
	@Test
	void testfindAll() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
		ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
		
		//Para realizar este test la BD tiene que tener algún cliente
		//Hacemos un conteo inicial de como esta el array en este momento
		int conteoInicial = clienteDAO.findAll().size();
		System.out.println("el nuemro de clientes en la lista de clientes es de: " + conteoInicial);
		
		//Insertamos un par de nuevos clientes
		Cliente cliente3 = new Cliente("Raul", "11111111R", "666111222", "raul@test.com");
		Cliente cliente4 = new Cliente("Berta", "22222222B", "666333444", "berta@test.com");
		clienteDAO.insert(cliente3);
		clienteDAO.insert(cliente4);
		
		//LLamamos al metodo
		ArrayList<Cliente> listaClientes = clienteDAO.findAll();
		
		//Asercciones
		int conteoFinal = conteoInicial + 2;
		
		assertNotNull(listaClientes, "La lista de clientes no debe ser Null");
		assertEquals(conteoFinal, listaClientes.size(), "El tamaño de la lista no es el esparado");
		
		System.out.println("Test findAll ok. El total de clientes de la lista es de: " + listaClientes.size());
		
		//Limpieza del test(recomendado??)
		//clienteDAO.delete(cliente3.getDni_cliente());
		//clienteDAO.delete(cliente4.getDni_cliente());
		
	}
	
	@Test
	void testfindByDni() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
		ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
		
		//Insertamos un cliente
		Cliente cliente5= new Cliente("Carlos", "12345678X", "987123456", "carlos@test.com");
		clienteDAO.insert(cliente5);
		
		//Buscamos al cliente por el dni
		Cliente clienteBuscado = clienteDAO.findByDni("12345678X");
		
		
		//Asercciones en caso de encontrarlo
		assertNotNull(clienteBuscado, "El cliente con dni: "+ clienteBuscado.getDni_cliente()+ " no fue encontrado");
		assertEquals("12345678X", clienteBuscado.getDni_cliente(),"El dni buscado no coincide con ningún cliente");
		
		//Aserccion si no lo encuentra
		Cliente clienteNoExistente = clienteDAO.findByDni("00000000Z");
		assertNull(clienteNoExistente, "No se debe encontrar un dni de un cliente inexistente");
		
		System.out.println("Test findByDni ok. El cleinte encontrado es: " + clienteBuscado.getNombre()+ " con dni: " + clienteBuscado.getDni_cliente());
		
		
		//Limpieza del test (borrar el dni buscado)
		//clienteDAO.delete("12345678X");
	}

}
