package dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dao.DBConnection;
import dao.Utils.PasswordUtils;
import dao.interfaces.UsuarioDAO;
import entities.Rol;
import entities.Usuario;

public class UsuarioDAOMySQL implements UsuarioDAO{
private Connection conexion;
	
	public UsuarioDAOMySQL() {
		conexion = DBConnection.getInstance().getConnection();
	}
	
	@Override
	public void insert(Usuario u) {
		String sql = "INSERT INTO usuario(nombre_usuario, dni_usuario, password, rol) VALUES(?,?,?,?);";
		try(PreparedStatement pst = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
			//Debemos obtener la password y hashearla
			String passwordHasheada = PasswordUtils.hashPassword(u.getPassword());
			//Seteamos los parametros de la clase Usuario
			pst.setString(1, u.getNombre_usuario());
			pst.setString(2, u.getDni_usuario());
			//usamos nuestra password ya hasheada
			pst.setString(3, passwordHasheada);
			//Convertimos la ENUM rol a String antes de guardarlo en BD
			pst.setString(4,u.getRol().toString().toUpperCase());
			
			int result = pst.executeUpdate();
			
			//Obtenemos la tabla virtual de datos que genera JAVA despues de ejecutar el SELECT. 
			//El ResultSet es un objeto de java que nos proporciona los datos de esa consulta a la BD. Lee datos
			//Lo metemos todo en un Try para poder cerrar el ResultSet
			try(ResultSet rs = pst.getGeneratedKeys()){
				if(rs.next()) {
					int IdGenerado = rs.getInt(1);
					
					//Seteo el id_cliente por el que me ha dado esa insercion y asi conocer el nuevo id de la inserccion
					u.setId_usuario(IdGenerado);
					System.out.println("Inserrcion ok, El ID asignado para esta inserccion es: " + IdGenerado);
				}
			
			
			}
			System.out.println("resultado de insercción: " + result);
		}catch(SQLException e) {
			System.out.println("No ok" + e.getMessage());
		}
	}
	@Override
	public void update(Usuario u) {
		String sql = "UPDATE usuario SET nombre_usuario=?, dni_usuario=?, password=?, rol=? WHERE id_usuario=?";
		try(PreparedStatement pst =conexion.prepareStatement(sql)) {
			//Debemos obtener la password y hashearla
			String passwordHasheada = PasswordUtils.hashPassword(u.getPassword());
			
			//Seteamos los parametros de la clase Usuario
            pst.setString(1, u.getNombre_usuario());
            pst.setString(2, u.getDni_usuario());
            pst.setString(3, passwordHasheada);
            pst.setString(4,u.getRol().toString().toUpperCase());
            pst.setInt(5, u.getId_usuario());  //WHERE
            
            int result = pst.executeUpdate();
            System.out.println("resultado de la actualización: " + result + " filas afectadas");	
		}catch(SQLException e) {
			System.out.println("Error en la actualización: " + e.getMessage());
	}
	}	
	@Override
	public void delete(String dni_usuario) {
	    String sql = "DELETE FROM usuario WHERE dni_usuario = ?";
	    try (PreparedStatement pst = conexion.prepareStatement(sql)) {
	    	//Setea en la posición 1 el dni_cliente
	        pst.setString(1, dni_usuario); 
	        int result = pst.executeUpdate();
	        
	        System.out.println("Filas de usuario eliminadas con dni " + dni_usuario + ": " + result);
	        
	    } catch(SQLException e) {
	        System.out.println("Error al eliminar el usuario: " + e.getMessage());
	    }
	}
	@Override
	public ArrayList<Usuario> findAll() {
		ArrayList<Usuario> listaUsuarios = new ArrayList<>();
		
		//Realizo la consulta sql para que em traiga todos los usuarios guardados
		String sql = "SELECT id_usuario, nombre_usuario, dni_usuario, password, rol FROM usuario";
		
		//Realizamos el try pero no usamos PreparedStatement porque no tenemos parametros(?), solo usamos Statement para que nos lo liste.
		//Tanto el Statement como el ResultSet lo metemos como parametro dentro del try para que al finalizar se cierren
		//Se usa un esecuteQuery() porque es una consulta SELECT
		try(Statement stmt= conexion.createStatement(); ResultSet rs= stmt.executeQuery(sql)){
			
			
			//Iteramos sobre los datos que nos devuelve la BD en el ResultSet
			while(rs.next()) {
				//Tenenmos que mapear el campo ENUM rol de la BD(String) a la ENUM de JAVA
				Rol rolObtenido = Rol.valueOf(rs.getString("rol").toUpperCase());
				//Componemos un objeto cliente por cada fila que nos devuelva
				Usuario u = new Usuario(
						rs.getInt("id_usuario"),
						rs.getString("nombre_usuario"),
						rs.getString("dni_usuario"),
						rs.getString("password"),
						rolObtenido
						);
				listaUsuarios.add(u);
			}
		}catch(SQLException e) {
			System.out.println("Error al listar todos los usuarios: " + e.getMessage());
		}
		
		return listaUsuarios;
	}
	
	@Override
	public Usuario findByNombre(String nombre_usuario) {
		String sql ="SELECT id_usuario, nombre_usuario, dni_usuario, password, rol FROM usuario WHERE nombre_usuario= ?";
		//Creamos el Usuario con valor null por si no lo encuentra en la BD
		Usuario u = null;
		
		try(PreparedStatement pst = conexion.prepareStatement(sql)){
			pst.setString(1, nombre_usuario);
			try(ResultSet rs = pst.executeQuery()){
				if (rs.next()) {
					//Mapeamos el campo rol de la ENUM
					Rol rolObtenido = Rol.valueOf(rs.getString("rol").toUpperCase());
	                // Mapeo de datos del ResultSet al objeto Cliente
	                 	u = new Usuario(
	                 			rs.getInt("id_usuario"),
	                            rs.getString("nombre_usuario"),
	                            rs.getString("dni_usuario"),
	                            rs.getString("password"),
	                            rolObtenido
	                );
	            }
			}
		}catch(SQLException e){
			System.out.println("Error al encontar el usuario por su nombre: " + e.getMessage());
		}
		return u;
	}
	
	//Preguntar por este metodo
	//boolean login(String dni, String password);

	


}
