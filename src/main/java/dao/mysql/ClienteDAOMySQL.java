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
	
	@Override
	public void insert(Cliente c) {
		String sql = "INSERT INTO cliente(nombre, dni_cliente, telefono, email) VALUES(?,?,?,?);";
		try(PreparedStatement pst = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			
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
			System.out.println("resultado de insercción: " + result);
		}catch(SQLException e) {
			System.out.println("No ok" + e.getMessage());
		}
	}

	@Override
	public void update(Cliente c) {
		try {
			String sql = "UPDATE cliente SET nombre=?, dni_cliente=?, telefono=?, email=? WHERE id_cliente=?";
			PreparedStatement pst =conexion.prepareStatement(sql);
			
			pst.setString(1, c.getNombre());	
			pst.setString(2, c.getDni_cliente());
			pst.setString(3, c.getTelefono());
			pst.setString(4, c.getEmail());
			pst.setInt(5, c.getId_cliente());
				
			
			int result = pst.executeUpdate();
			System.out.println("resultado de insercción: " + result);
			
			}catch(SQLException e) {
			System.out.println("No ok" + e.getMessage());
		}
		
		
		
	}

	@Override
	public void delete(String dni) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Cliente> findall() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cliente findByDni(String dni) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
