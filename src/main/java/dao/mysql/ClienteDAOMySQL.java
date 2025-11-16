package dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import dao.DBConnection;
import dao.interfaces.ClienteDAO;
import entities.Cliente;


public class ClienteDAOMySQL implements ClienteDAO {
	private Connection conexion;
	
	public ClienteDAOMySQL() {
		conexion = DBConnection.getInstance().getConnection();
	}
	
	/* Estoy metiendo los PreparedStatement dentro de la llamada del try() para que al finalizar esa 
	 * llamada ese PreparedStatement se cierre automaticamente porque he leido que no es bueno dejar esa conexión abierta
	 *  */
	
	@Override
	public void insert(Cliente c) {
		String sql = "INSERT INTO cliente(nombre, dni_cliente, telefono, email) VALUES(?,?,?,?);";
		try(PreparedStatement pst = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			//Statement.RETURN_GENERATED_KEYS esto lo usamos porque tenemose los id autoincrementales dentro de la BD(no los introducimos nosotros a mano)
			
			//Seteamos los parametros de la clase Cliente
			pst.setString(1, c.getNombre());
			pst.setString(2, c.getDni_cliente());
			pst.setString(3,c.getTelefono());
			pst.setString(4,c.getEmail());
			
			int result = pst.executeUpdate();
			
			//Obtenemos la tabla virtual de datos que genera JAVA despues de ejecutar el SELECT. 
			//El ResultSet es un objeto de java que nos proporciona los datos de esa consulta a la BD. Lee datos
			//Lo metemos todo en un Try para poder cerrar el ResultSet
			try(ResultSet rs = pst.getGeneratedKeys()){
				if(rs.next()) {
					int IdGenerado = rs.getInt(1);
					
					//Seteo el id_cliente por el que me ha dado esa insercion y asi conocer el nuevo id de la inserccion
					c.setId_cliente(IdGenerado);
					System.out.println("Inserrcion ok, El ID asignado para esta inserccion es: " + IdGenerado);
				}
			
			
			}
			System.out.println("resultado de insercción: " + result + " filas afectadas");
		}catch(SQLException e) {
			System.out.println("Error en la insercción: " + e.getMessage());
		}
	}

	@Override
	public void update(Cliente c) {
		String sql = "UPDATE cliente SET nombre=?, dni_cliente=?, telefono=?, email=? WHERE id_cliente=?";
		try(PreparedStatement pst =conexion.prepareStatement(sql)) {
			
			
			pst.setString(1, c.getNombre());	
			pst.setString(2, c.getDni_cliente());
			pst.setString(3, c.getTelefono());
			pst.setString(4, c.getEmail());
			pst.setInt(5, c.getId_cliente());  //Condicion del WHERE
				
			
			int result = pst.executeUpdate();
			System.out.println("resultado de insercción: " + result + " filas afectadas");
			
			}catch(SQLException e) {
			System.out.println("Error en la actualización: " + e.getMessage());
		}
		
		
		
	}

	@Override
	public void delete(String dni_cliente) {
		String sql = "DELETE FROM cliente WHERE dni_cliente=?";
		try(PreparedStatement pst =conexion.prepareStatement(sql)) {
			
			//Setea en la posición 1 el dni_cliente
			pst.setString(1, dni_cliente);
			int result = pst.executeUpdate();
			
			System.out.println("Filas eliminadas con Dni: " + dni_cliente + " : " + result );
			
		}catch(SQLException e) {
			System.out.println("Error al eliminar el cliente: " + e.getMessage());
		}
		
	}

	/*Este metodo se usa para listar todos los clientes por si se necesita en algún momento. 
	 * Este metodo usa el segundo constructor que se ocupa de reconstruir el objeto para poder guardarlo en el array  */
	@Override
	public ArrayList<Cliente> findAll() {
		ArrayList<Cliente> listaClientes = new ArrayList<>();
		
		//Realizo la consulta sql para que em traiga todos los clientes guardados
		String sql = "SELECT id_cliente, nombre, dni_cliente, telefono, email FROM cliente";
		
		//Realizamos el try pero no usamos PreparedStatement porque no tenemos parametros(?), solo usamos Statement para que nos lo liste.
		//Tanto el Statement como el ResultSet lo metemos como parametro dentro del try para que al finalizar se cierren
		//Se usa un esecuteQuery porque es una consulta SELECT
		try(Statement stmt= conexion.createStatement(); ResultSet rs= stmt.executeQuery(sql)){
			
			
			//Iteramos sobre los datos que nos devuelve la BD en el ResultSet
			while(rs.next()) {
				//Componemos un objeto cliente por cada fila que nos devuelva
				Cliente c = new Cliente(
						rs.getInt("id_cliente"),
						rs.getString("nombre"),
						rs.getString("dni_cliente"),
						rs.getString("telefono"),
						rs.getString("email")
						);
				listaClientes.add(c);
			}
		}catch(SQLException e) {
			System.out.println("Error al listar todos los clientes: " + e.getMessage());
		}
		
		return listaClientes;
	}
	
	//este metodo se usa para encontrar a un cliente por su dni (usamos el segundo cosntructor)
	@Override
	public Cliente findByDni(String dni_cliente) {
		String sql ="SELECT id_cliente, nombre, dni_cliente, telefono, email FROM cliente WHERE dni_cliente= ?";
		//Creamos el cliente con valor null por si no lo encuentra en la BD
		Cliente c = null;
		
		try(PreparedStatement pst = conexion.prepareStatement(sql)){
			pst.setString(1, dni_cliente);
			try(ResultSet rs = pst.executeQuery()){
				if (rs.next()) {
	                // Mapeo de datos del ResultSet al objeto Cliente
	                 	c = new Cliente(
	                    rs.getInt("id_cliente"),
	                    rs.getString("nombre"),
	                    rs.getString("dni_cliente"),
	                    rs.getString("telefono"),
	                    rs.getString("email")
	                );
	            }
			}
		}catch(SQLException e){
			System.out.println("Error al encontar el cliente por su dni: " + e.getMessage());
		}
		return c;
	}
	
	

}
