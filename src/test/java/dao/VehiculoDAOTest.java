package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dao.mysql.ClienteDAOMySQL;
import dao.mysql.VehiculoDAOMySQL;
import entities.Cliente;
import entities.Vehiculo;

class VehiculoDAOTest {

	@Test
	void tesInsert() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	    
	    //  Insertar Cliente
	    Cliente clienteVehiculo = new Cliente("clienteVehiculo", "11111111T", "600111222", "clienteVehiculo@test.com");
	    clienteDAO.insert(clienteVehiculo);
	    int clienteId = clienteVehiculo.getId_cliente();

	    // Insertar Vehículo
	    Vehiculo vehiculo1 = new Vehiculo("INS8888", "Renault", "Clio", clienteId);
	    vehiculoDAO.insert(vehiculo1);
	    int IdGenerado = vehiculo1.getId_vehiculo();

	    //  Verificacion
	    Vehiculo verificacionVehiculo = vehiculoDAO.findByMatricula("INS8888");
	    
	    assertNotNull(verificacionVehiculo, "El vehículo no se encontró después de la inserción.");
	    assertTrue(IdGenerado > 0, "El ID del vehículo debe ser mayor que 0.");
	    assertEquals(clienteId, verificacionVehiculo.getCliente_id(), 
	                 "ERROR: La clave foránea cliente_id no se guardó correctamente.");
	    
	    System.out.println("Test insert Vehiculo OK. ID generado: " + IdGenerado);
	    
	    //  LIMPIEZA 
	    //vehiculoDAO.delete("INS8888"); // Borra el vehículo
	    //clienteDAO.delete(clienteVehiculo.getDni_cliente()); // Borra el cliente
	}
	@Test
	void testUpdateVehiculo() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	    
	    // Insertar Cliente Original y Cliente Nuevo (Dueño) ---
	    Cliente cViejo = new Cliente("Cliente Viejo", "22222222O", "600222333", "cOriginal@mail.com"); 
	    clienteDAO.insert(cViejo);
	    int idViejo = cViejo.getId_cliente();
	    
	    //Insertar un vehiculo(viejo) para el cliente viejo
	    Vehiculo vehiculo1 = new Vehiculo("AAA3452", "Renault", "Clio", idViejo);
	    vehiculoDAO.insert(vehiculo1);
	    int idVehiculo1PK = vehiculo1.getId_vehiculo();  //Guradamos la PK del vehiculo para ver que se halla actualizado este mismo

	    // Actulizar datos del vehiculo del cliente viejo
	    vehiculo1.setMarca("AUDI"); // Modificar Marca
	    vehiculo1.setMatricula("UPD0001"); //Modificar Matricula
	    vehiculo1.setModelo("Q7");//Modificar modelo
	   //Actualizar el vehiculo1
	    vehiculoDAO.update(vehiculo1);

	    // Verificación(busqueda por matricula)
	    Vehiculo vehiculoVerificado1 = vehiculoDAO.findByMatricula("UPD0001");
	    
	    assertNotNull(vehiculoVerificado1, "El vehículo no se encontró después de la actualización.");
	    assertEquals("AUDI", vehiculoVerificado1.getMarca(), "ERROR: La marca debe ser (AUDI)");
	    assertEquals("Q7", vehiculoVerificado1.getModelo(), "ERROR: El modelo debe ser (Q7).");
	    assertEquals(idViejo, vehiculoVerificado1.getCliente_id(), "ERROR: El id_cliente (dueño) ha cambiado y deberia ser el mismo");
	    assertEquals(idVehiculo1PK, vehiculoVerificado1.getId_vehiculo(), "ERROR: El ID del vehiculo no deberia cambiar");
	    System.out.println("Test update Vehiculo OK. El cliente ha cambiado de vehiculo ");
	    
	    //Limpieza
	    //vehiculoDAO.delete("UPD0001"); 
	    //clienteDAO.delete(cViejo.getDni_cliente());
	    
	}
	@Test
	void testUpadateVehiculoCliente(){
		  MySQLDAOFactory factory = new MySQLDAOFactory();
		  ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
		  VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
		    
		  // Clientes
		  // Cliente 1: El dueño original
		  Cliente cliente1 = new Cliente("Vendedor", "22222223O", "600222333", "viejo@mail.com");
		  // Cliente 2: El nuevo dueño
		  Cliente cliente2 = new Cliente("Comprador", "33333333N", "600444555", "nuevo@mail.com");
		  clienteDAO.insert(cliente1);
		  clienteDAO.insert(cliente2);
		  int idViejo = cliente1.getId_cliente();
		  int idNuevo = cliente2.getId_cliente(); 

		   // Insertar el vehículo para el Cliente Viej
		   Vehiculo vehiculoViejo = new Vehiculo("CVD4567","BMW","X5", idViejo);
		   vehiculoDAO.insert(vehiculoViejo);
		    
		    // Modificar solo el dueño
		    // 1. Modificar ÚNICAMENTE la clave foránea 
		    vehiculoViejo.setCliente_id(idNuevo); 
		    
		    // 2. Actualizar datos 
		    vehiculoDAO.update(vehiculoViejo);

		    //Verificacion (Leemos los datos del vehiculo viejo) ---
		    Vehiculo vehiculoViejoVerificado = vehiculoDAO.findByMatricula("CVD4567");
		    
		    assertNotNull(vehiculoViejoVerificado , "El vehículo debe seguir existiendo con su matrícula original.");
		    
		    // ASERCIÓN 1 (Datos del Vehículo): Los datos de la unidad NO deben cambiar.
		    assertEquals("BMW", vehiculoViejoVerificado .getMarca(), "ERROR: La marca no se mantuvo (debe ser BMW).");
		    assertEquals("X5", vehiculoViejoVerificado .getModelo(), "ERROR: El modelo no se mantuvo (debe ser X5).");
		    
		    // ASERCIÓN 2 (Relación con Cliente): El dueño debe ser el NUEVO.
		    assertEquals(idNuevo, vehiculoViejoVerificado .getCliente_id(), "ERROR: El cliente_id no se actualizó. El dueño debe ser el nuevo comprador.");             
		    System.out.println("Test Cambio de Dueño (solo FK) OK.");
		    
		    //Limpieza
		    //vehiculoDAO.delete("CVD4567"); 
		    //clienteDAO.delete(cliente1.getDni_cliente());
		    //clienteDAO.delete(cliente2.getDni_cliente());
		}
	@Test
	void testDelete() {
	    MySQLDAOFactory factory = new MySQLDAOFactory();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	 // Datos iniaciales
        String dni = "99999999D";
        String matricula = "DEL333";
        
        Cliente c = new Cliente("Test Del", dni, "600111222", "test.del@mail.com");
        clienteDAO.insert(c);
        
        Vehiculo v = new Vehiculo(matricula, "Fiat", "Punto", c.getId_cliente());
        vehiculoDAO.insert(v);

        // Hacemos una verificacion inicial
        assertNotNull(vehiculoDAO.findByMatricula(matricula));

        // Ejecutamos el metodo
        vehiculoDAO.delete(matricula);

        // Verificamos de nuevo
        assertNull(vehiculoDAO.findByMatricula(matricula));
        
        // Limpiamos
        //clienteDAO.delete(dni);
	}
	@Test
	void testFindByMatricula() {
	    MySQLDAOFactory factory = new MySQLDAOFactory();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	 // PDatos iniciales
        String dni = "12341234P";
        String matricula = "FBM444";
        String marca = "Honda";
        //Insertamos datos cliente
        Cliente c = new Cliente("Test FBM", dni, "600111222", "fbm@mail.com");
        clienteDAO.insert(c);
        int clienteId = c.getId_cliente();
        //Insertamos datos vehiculo
        Vehiculo v = new Vehiculo(matricula, marca, "Civic", clienteId);
        vehiculoDAO.insert(v);

        // Llamamos al metodo
        Vehiculo vEncontrado = vehiculoDAO.findByMatricula(matricula);

        // Asserts
        assertNotNull(vEncontrado);
        assertEquals(marca, vEncontrado.getMarca());
        assertEquals(clienteId, vEncontrado.getCliente_id());
        
        // Caso Negativo
        assertNull(vehiculoDAO.findByMatricula("NONONO"));
        
        // LIMPIEZA
        //vehiculoDAO.delete(matricula);
        //clienteDAO.delete(dni);
	}
	@Test
	void testFindAll() {
	    MySQLDAOFactory factory = new MySQLDAOFactory();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	 // Datos iniciales
        String dni = "44444444L";
        String mat1 = "FA555";
        String mat2 = "FA666";
        //Insertamos un cliente
        Cliente c = new Cliente("Test FA", dni, "600555666", "fa@mail.com");
        clienteDAO.insert(c);
        int clienteId = c.getId_cliente();
        //Verificamos la lsta antes de inluir vehiculos
        int countInicial = vehiculoDAO.findAll().size(); 
        //Insertamos Vehiculos
        vehiculoDAO.insert(new Vehiculo(mat1, "Ford", "Focus", clienteId));
        vehiculoDAO.insert(new Vehiculo(mat2, "Opel", "Astra", clienteId));
        
        // Llamamos al metodo
        ArrayList<Vehiculo> lista = vehiculoDAO.findAll();
        
        // Assercciones
        assertNotNull(lista);
        // Debe ser el conteo inicial más los dos nuevos
        assertEquals(countInicial + 2, lista.size()); 
        
        // Limpieza
        //vehiculoDAO.delete(mat1);
        //vehiculoDAO.delete(mat2);
        //clienteDAO.delete(dni);
	}
	@Test
	void testFindByClienteId() {
	    MySQLDAOFactory factory = new MySQLDAOFactory();
	    ClienteDAOMySQL clienteDAO = (ClienteDAOMySQL) factory.getClienteDAO();
	    VehiculoDAOMySQL vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
	 // Datos iniciales
        Cliente c1 = new Cliente("Test C1", "10000000T", "600000100", "testc1@mail.com");
        Cliente c2 = new Cliente("Test C2", "20000000A", "600000200", "testc2@mail.com");
        clienteDAO.insert(c1);
        clienteDAO.insert(c2);
        int idC1 = c1.getId_cliente();
        int idC2 = c2.getId_cliente();
        //Datos para las matriculas
        String mat1 = "FCID777";
        String mat2 = "FCID888";
        String mat3 = "FCID999"; // Para C2 (aislado)
        
        // Insertamos 2 vehículos para C1 y 1 para C2
        vehiculoDAO.insert(new Vehiculo(mat1, "Seat", "Panda", idC1));
        vehiculoDAO.insert(new Vehiculo(mat2, "Nissan", "Micra", idC1));
        vehiculoDAO.insert(new Vehiculo(mat3, "Ford", "Fiesta", idC2));
        
        // Llamamos al metodo
        ArrayList<Vehiculo> lista = vehiculoDAO.findByClienteId(idC1);
        
        // Asserciones
        assertNotNull(lista);
        // Solo debe encontrar los 2 vehículos de C1
        assertEquals(2, lista.size()); 
        assertEquals(idC1, lista.get(0).getCliente_id()); 
                 
        // Limpieza
        //vehiculoDAO.delete(mat1);
        //vehiculoDAO.delete(mat2);
        //vehiculoDAO.delete(mat3);
        //clienteDAO.delete(c1.getDni_cliente());
        //clienteDAO.delete(c2.getDni_cliente());
	}
}
