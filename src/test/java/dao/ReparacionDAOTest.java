package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dao.mysql.*;
import entities.Cliente;
import entities.Estado;
import entities.Reparacion;
import entities.Rol;
import entities.Usuario;
import entities.Vehiculo;

class ReparacionDAOTest {

	@Test
	void testInsert() {
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
	    Integer usuarioId = usuario1.getId_usuario();
		
		//Insertar una reparación
		Reparacion reparacion1 = new Reparacion("fuga de aceite", Date.valueOf("2025-11-15"), 120.10,Estado.REPARACION, vehiculoId,usuarioId);
		
		reparacionDAO.insert(reparacion1);
		int idReparacion = reparacion1.getId_reparacion();
		//Verificaion
		
		assertTrue(reparacion1.getId_reparacion() > 0, "El id generado tiene que ser mayor a 0");
		assertEquals(vehiculoId, reparacion1.getVehiculo_id(), "Error: el id del vehiculo no coincide");
		assertEquals(usuarioId, reparacion1.getUsuario_id(), "Error: el id de usuario no coincide");
		//Se añade un Delta: 0.0001 para hacer coincidir el resultado con el redondeo
		assertEquals(120.10, reparacion1.getCoste_estimado(),0.0001, "Error: el coste estimado no coincide");
		assertEquals(Estado.REPARACION, reparacion1.getEstado(), "Error: el estado de la reparacion no coincide");
		
		System.out.println("Test insert reparacion OK. ID generado: " + idReparacion);
		
		// Limpieza
	    //reparacionDAO.delete(reparacion1.getId_reparacion());
	    //vehiculoDAO.delete(vehiculo1.getMatricula());
	    //usuarioDAO.delete(usuario1.getDni_usuario());
	    //clienteDAO.delete(cliente1.getDni_cliente());
		
	}
	
	@Test
	void testUpdate() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
	    ReparacionDAOMySQL reparacionDAO = (ReparacionDAOMySQL) factory.getReparacionDAO();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    UsuarioDAOMySQL usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	    
	    //Insertar datos
	    Cliente cliente1 = new Cliente("Beatriz", "11111111B", "666666666", "bea@test.com");
	    clienteDAO.insert(cliente1);
	    
	    Vehiculo vehiculo1 = new Vehiculo("XYZ9999", "Ford", "Focus", cliente1.getId_cliente());
	    vehiculoDAO.insert(vehiculo1);
	    int vehiculoId = vehiculo1.getId_vehiculo();
	    
	    Usuario usuario1 = new Usuario("mecanico2", "22222222U", "pass456", Rol.MECANICO);
	    usuarioDAO.insert(usuario1);
	    Integer usuarioId = usuario1.getId_usuario();
	    
	    Reparacion reparacion2 = new Reparacion(
	            "Fallo de motor",         
	            Date.valueOf("2025-10-01"),             
	            500.00,                   // Coste original: 500.00
	            Estado.REPARACION,        // Estado original
	            vehiculoId,               
	            usuarioId                 
	        );
	    //Insertamos la reparacion
	    reparacionDAO.insert(reparacion2);
	    int idReparacion = reparacion2.getId_reparacion();
	    
	    //Cambios
	    reparacion2.setCoste_estimado(850.75); // Nuevo coste
	    reparacion2.setEstado(Estado.FINALIZADO); // Nuevo estado
	    reparacion2.setDescripcion("Fallo de motor y sustitución de turbo"); // Nueva descripción
	    
	    reparacionDAO.update(reparacion2);
	    
	    //Aserciones
	    assertNotNull(reparacion2, "La reparación actualizada debe ser recuperable de la base de datos.");
	    
	    
	    // Verificar el Coste (usando delta)
	    assertEquals(850.75, reparacion2.getCoste_estimado(), 
	                 0.001,"El coste estimado debe haberse actualizado a 850.75."); 
	                 
	    // Verificar el Estado
	    assertEquals(Estado.FINALIZADO, reparacion2.getEstado(), 
	                 "El estado debe haberse actualizado a FINALIZADO.");
	                 
	    // Verificar la Descripción
	    assertEquals("Fallo de motor y sustitución de turbo", reparacion2.getDescripcion(),
	                 "La descripción debe haberse actualizado.");
	                 
	   //Limpieza
	    //reparacionDAO.delete(idReparacion);
	    //vehiculoDAO.delete(vehiculo1.getMatricula());
	    //usuarioDAO.delete(usuario1.getDni_usuario()); 
	    //clienteDAO.delete(cliente1.getDni_cliente());
	    
	    System.out.println("Test update reparacion OK. ID actualizado: " + idReparacion);
	    
	}
	
	@Test 
	void testDelete(){
		MySQLDAOFactory factory = new MySQLDAOFactory();
	    ReparacionDAOMySQL reparacionDAO = (ReparacionDAOMySQL) factory.getReparacionDAO();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    UsuarioDAOMySQL usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	    
	    //Insertamos datos
	    Cliente cliente1 = new Cliente("Carlos", "33333333C", "555555555", "carlos@test.com");
	    clienteDAO.insert(cliente1);
	    
	    Vehiculo vehiculo1 = new Vehiculo("ASD1234", "Opel", "Corsa", cliente1.getId_cliente());
	    vehiculoDAO.insert(vehiculo1);
	    int vehiculoId = vehiculo1.getId_vehiculo();
	    
	    Usuario usuario1 = new Usuario("mecanico3", "44444444U", "pass789", Rol.MECANICO);
	    usuarioDAO.insert(usuario1);
	    Integer usuarioId = usuario1.getId_usuario();
	    
	    Reparacion reparacion3 = new Reparacion(
	            "Cambio de neumáticos",         
	            Date.valueOf("2025-10-10"),             
	            150.00,                   
	            Estado.PENDIENTE,        
	            vehiculoId,               
	            usuarioId                 
	        );
	    
	    reparacionDAO.insert(reparacion3);
	    int idReparacion = reparacion3.getId_reparacion();
	    
	    //Verificamos que tenemos la reparaciona antes de borrarla
	    assertNotNull(reparacionDAO.findAll());
	    
	    //Borramos la reparacion
	    reparacionDAO.delete(idReparacion);
	    
	    //Asercciones para verificar que se ha borrado
	    assertNull(reparacionDAO.findAll());
	    
	    //Limpieza
	    //vehiculoDAO.delete(vehiculo1.getMatricula());
	    //usuarioDAO.delete(usuario1.getDni_usuario()); 
	    //clienteDAO.delete(cliente1.getDni_cliente());
	    
	    System.out.println("Test delete reparacion OK. ID actualizado: " + idReparacion);
	    
	}
	
	@Test
	void testFindAll() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
	    ReparacionDAOMySQL reparacionDAO = (ReparacionDAOMySQL) factory.getReparacionDAO();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    UsuarioDAOMySQL usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	    
	    //Inserccion de datos
	    Cliente cliente1 = new Cliente("FindAllTester", "X9999999X", "999999999", "all@test.com");
	    clienteDAO.insert(cliente1);
	    int clienteId = cliente1.getId_cliente();
	    Vehiculo vehiculo1 = new Vehiculo("XYZ1234", "Audi", "A4", cliente1.getId_cliente());
	    vehiculoDAO.insert(vehiculo1);
	    int vehiculoId = vehiculo1.getId_vehiculo();
	    
	    // Usuario
	    Usuario usuario1 = new Usuario("user_all", "66666666U", "passall", Rol.MECANICO);
	    usuarioDAO.insert(usuario1);
	    Integer usuarioId = usuario1.getId_usuario();
	    
	    //Verificacion antes de meter datos
	    int conteoInicial = reparacionDAO.findAll().size();
	    
	    //Reparaciones
	    Reparacion reparacion4 = new Reparacion("Revisión anual", Date.valueOf("2025-05-01"), 100.00, Estado.PENDIENTE, vehiculoId, usuarioId);
	    reparacionDAO.insert(reparacion4);
	    int reparacion4Id = reparacion4.getId_reparacion();
	    
	    Reparacion reparacion5 = new Reparacion("Cambio de batería", Date.valueOf("2025-05-05"), 150.50, Estado.REPARACION, vehiculoId, usuarioId);
	    reparacionDAO.insert(reparacion5);
	    int reparacion5Id = reparacion5.getId_reparacion();
	    
	    //Llamamos al metodo
	    ArrayList<Reparacion> listaReparaciones = reparacionDAO.findAll();
	    int conteoFinal = conteoInicial +2;
	    
	    assertEquals(conteoFinal, listaReparaciones.size(), "Error: el conteo final debe ser 2");
	    
	    System.out.println("Test findAll reparaciones OK. Registros encontrados: " + listaReparaciones.size());
	}
	
	@Test
	void testFindByVehiculoId() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
	    ReparacionDAOMySQL reparacionDAO = (ReparacionDAOMySQL) factory.getReparacionDAO();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	    UsuarioDAOMySQL usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
	    
	    //Insertamos datos
	    Cliente cliente1 = new Cliente("Cliente1", "A1111111A", "111111111", "a@test.com");
	    clienteDAO.insert(cliente1);
	    Vehiculo vehiculo1 = new Vehiculo("Vehiculo1", "MarcaA", "ModeloA", cliente1.getId_cliente());
	    vehiculoDAO.insert(vehiculo1);
	    int idVehiculoBuscado = vehiculo1.getId_vehiculo(); // ID que usaremos para la búsqueda

	    Cliente cliente2 = new Cliente("Cliente2", "B2222222B", "222222222", "b@test.com");
	    clienteDAO.insert(cliente2);
	    Vehiculo vehiculo2 = new Vehiculo("Vehiculo2", "MarcaB", "ModeloB", cliente2.getId_cliente());
	    vehiculoDAO.insert(vehiculo2);
	    int idVehiculoExtra = vehiculo2.getId_vehiculo();

	    // Usuario (Necesario para la FK)
	    Usuario usuario1 = new Usuario("mecanico4", "55555555U", "pass101", Rol.MECANICO);
	    usuarioDAO.insert(usuario1);
	    Integer usuarioId = usuario1.getId_usuario();
	    
	    //Reparaciones
	 // Reparación 1: Asociada al Vehículo1 (DEBE aparecer)
	    Reparacion rep1 = new Reparacion("Alineación", Date.valueOf("2025-01-01"), 50.00, Estado.PENDIENTE, idVehiculoBuscado, usuarioId);
	    reparacionDAO.insert(rep1);

	    // Reparación 2: Asociada al Vehículo2 (NO debe aparecer)
	    Reparacion rep2 = new Reparacion("Freno de mano", Date.valueOf("2025-02-01"), 100.00, Estado.FINALIZADO, idVehiculoExtra, usuarioId);
	    reparacionDAO.insert(rep2);

	    // Reparación 3: Asociada al Vehículo1 (DEBE aparecer)
	    Reparacion rep3 = new Reparacion("Cambio aceite", Date.valueOf("2025-03-01"), 80.00, Estado.REPARACION, idVehiculoBuscado, usuarioId);
	    reparacionDAO.insert(rep3);
	    
	    //LLamamos al metodo
	    ArrayList<Reparacion> listaReparacionesVehiculo = reparacionDAO.findByVehiculoId(idVehiculoBuscado);
	    
	    //Asserciones
	    assertEquals(2, listaReparacionesVehiculo.size(), "Deberían encontrarse 2 reparaciones para el vehiculo." + vehiculo1.getId_vehiculo());
	    
	    System.out.println("Test findByVehiculoId OK. Reparaciones encontradas: " + listaReparacionesVehiculo.size());
	    
	    //Limpieza
	 
	    //reparacionDAO.delete(rep1.getId_reparacion());
	    //reparacionDAO.delete(rep2.getId_reparacion()); 
	    //reparacionDAO.delete(rep3.getId_reparacion());

	    
	    //vehiculoDAO.delete(vehiculo1.getMatricula());
	    //vehiculoDAO.delete(vehiculo2.getMatricula());
	    //clienteDAO.delete(cliente1.getDni_cliente());
	    //clienteDAO.delete(cliente2.getDni_cliente());
	    //usuarioDAO.delete(usuario1.getDni_usuario());
	    
	}

}
