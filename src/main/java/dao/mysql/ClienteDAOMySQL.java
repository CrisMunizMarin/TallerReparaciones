package dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
		try {
		//PreparedStatement
		String sql = "INSERT INTO cliente(nombre, dni_cliente, telefono, email) VALUES(?,?,?,?);";
		PreparedStatement pst =conexion.prepareStatement(sql);
		
		pst.setString(2, c.getNombre());
		pst.setString(3, c.getDni_cliente());
		pst.setString(4,c.getTelefono());
		pst.setString(5,c.getEmail());
		
		int result = pst.executeUpdate();
		System.out.println("resultado de insercción: " + result);
		}catch(SQLException e) {
			System.out.println("No ok" + e.getMessage());
		}
	}

	@Override
	public void update(Cliente c) {
		
		
		/*UPDATE empleados
		SET puesto = 'Supervisor',
		    salario = 3500.00,
		    estado = 'activo'
		WHERE id = 3;*/
		
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
