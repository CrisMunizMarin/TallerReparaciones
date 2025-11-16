package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import dao.mysql.UsuarioDAOMySQL;
import entities.Rol;
import entities.Usuario;

class UsuarioDAOTest {

	@Test
	void testInsert() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
	    UsuarioDAOMySQL usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
	    
	    //Insertamos un nuevo usuario
	    Usuario usuario1 = new Usuario("usuario1Test", "77777777U", "pass123", Rol.MECANICO);
	    usuarioDAO.insert(usuario1);
	    int IdGenerado = usuario1.getId_usuario();
	    
	    //Verificacion mediante el nombre de usuario
	    Usuario verificacionUsuario = usuarioDAO.findByNombre("usuario1Test");
	    
	    //Asercciones
	    assertNotNull(verificacionUsuario, "El usuario no puede ser Null después del insert.");
	    assertEquals(Rol.MECANICO, verificacionUsuario.getRol(), "El Rol no coincide.");
	    assertTrue(IdGenerado > 0, "El ID generado debe ser mayor que 0.");
	    
	    //Falta assert para verificar la password
	    
	    System.out.println("Test insert Usuario ok. El Usuario " + verificacionUsuario.getNombre_usuario() + " insertado con ID: " + IdGenerado);
	    
	    // 5. Limpieza
	    //usuarioDAO.delete(usuario1.getDni_usuario());
	}

	@Test
	void testUpdate() {
		MySQLDAOFactory factory = new MySQLDAOFactory();
	    UsuarioDAOMySQL usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
	    
	    // Insertar un usuario
	    Usuario usuario2 = new Usuario("usuario2Test", "34543218Y", "pass222", Rol.ADMINISTRADOR);
	    usuarioDAO.insert(usuario2);
	    int IdGenerado = usuario2.getId_usuario();
	    //  Modificar la contraseña 
	    usuario2.setPassword("pass345"); 
	    usuarioDAO.update(usuario2);
	    
	    // Verificamos mediante el nombre de usuario
	    Usuario verificacionUsuario = usuarioDAO.findByNombre("usuario2Test");
	    
	    //Asercciones
	    assertNotNull(verificacionUsuario, "El usuario no puede ser null después del update.");
	    System.out.println("Test update ok. La contraseña del : " + usuario2.getNombre_usuario() + " con id: " + IdGenerado + " se ha actualizado a: " + verificacionUsuario.getPassword() );
	    
	    //Limpieza
	    //usuarioDAO.delete(usuario2.getDni_usuario());
	}
	
	@Test
	void testFindAll() {
	    MySQLDAOFactory factory = new MySQLDAOFactory();
	    UsuarioDAOMySQL usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
	    
	    // Creamos dos usuarios
	    Usuario usuario3 = new Usuario("usuario3", "90000001A", "passA", Rol.MECANICO);
	    Usuario usuario4 = new Usuario("usuario4", "90000002B", "passB", Rol.ADMINISTRADOR);
	    
	    // Contabilizamos el numero de usuario antes de rellenar el array
	    int conteoInicial = usuarioDAO.findAll().size(); 

	    // Insertamos usuarios
	    usuarioDAO.insert(usuario3); 
	    usuarioDAO.insert(usuario4);
	    
	    //Llamamos al metodo
	    ArrayList<Usuario> listaUsuarios = usuarioDAO.findAll();
	    
	    //Asserciones
	    int conteoFinal = conteoInicial + 2;
	    
	    assertNotNull(listaUsuarios, "La lista de usuarios no debe ser null.");
	    assertEquals(conteoFinal, listaUsuarios.size(), 
	                 "El tamaño de la lista de usuarios no es la esperada.");
	    
	    System.out.println("Test findAll Usuario OK. Total de usuarios listados: " + listaUsuarios.size());
	    
	    // Limpieza
	    //usuarioDAO.delete("90000001A");
	   // usuarioDAO.delete("90000002B");
	}
	
	@Test
	void testfindByNombre() {
		 MySQLDAOFactory factory = new MySQLDAOFactory();
		    UsuarioDAOMySQL usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
		
		//Insertamos un usuario
		Usuario usuario5= new Usuario("usuario5", "78743216T", "pass678", Rol.INVITADO);
		usuarioDAO.insert(usuario5);
		
		//Buscamos al usuario por el nombre
		Usuario usuarioBuscado = usuarioDAO.findByNombre("usuario5");

		//Asercciones en caso de encontrarlo
		assertNotNull(usuarioBuscado, "El usuario con nombre: "+ usuarioBuscado.getNombre_usuario()+ " no fue encontrado");
		assertEquals("78743216T", usuarioBuscado.getDni_usuario(),"El dni buscado no coincide con ningún usuario");
		assertEquals(Rol.INVITADO, usuarioBuscado.getRol(), 
                "El rol recuperado no coincide.");
		//Aserccion si no lo encuentra
		Usuario usuarioNoExistente = usuarioDAO.findByNombre("usuario6");
		assertNull(usuarioNoExistente, "No se debe encontrar un usuario inexistente");
		
		System.out.println("Test findByNombre ok. El usuario encontrado es: " + usuarioBuscado.getNombre_usuario());
		
		
		//Limpieza del test (borrar el dni buscado)
		//usuarioDAO.delete("78743216T");
	}
}
