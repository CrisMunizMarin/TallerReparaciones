package dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import dao.DBConnection;
import dao.interfaces.UsuarioDAO;
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
			
			//Seteamos los parametros de la clase Usuario
			pst.setString(1, u.getNombre_usuario());
			pst.setString(2, u.getDni_usuario());
			pst.setString(3, u.getPassword());
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
	
	public void update(Usuario c) {
		try {
			String sql ="UPDATE usuario SET nombre_usuario=?,dni_usuario=?, password=?, rol=? WHERE id_usuario=?";
			PreparedStatement pst =conexion.prepareStatement(sql);
			
			
					
		}catch(SQLException e) {
			System.out.println("No ok" + e.getMessage());
	}
	//boolean login(String dni, String password);
	//int insert(Usuario u);
	//ArrayList<Usuario u> findall();
	//Usuario findByNombre(String nombre);

}
}