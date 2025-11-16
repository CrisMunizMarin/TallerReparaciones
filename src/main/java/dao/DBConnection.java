package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;



public class DBConnection {
	private static DBConnection instance;
	Connection conexionMySQL = null;
	
	private DBConnection() {
		try {
			
			MysqlDataSource dataSource = new MysqlDataSource();
//			
	
			//1.1 Hacer la conexión buscando con InputStream
			Properties props = new Properties();
			
			//Esto evita tener que usar rutas absolutas o relativas. Obtiene un cargador de clases y le pide que busque el archivo "conexion.propierties".
			//Lo busca dentro del paquete compilado dela aplicación y no dentro del disco duro
			InputStream input = getClass().getClassLoader().getResourceAsStream("conexion.properties");
			
			props.load(input);
			
			dataSource.setUrl(props.getProperty("url"));
			dataSource.setUser(props.getProperty("user"));
			dataSource.setPassword(props.getProperty("password"));
			
			
			input.close();
			
			//1.2 Main
			conexionMySQL = dataSource.getConnection();
			System.out.println("Conexion establecida correctamente");
		}catch(SQLException  |  IOException e) {
			System.err.println("Error al conectar con MySql: " + e.getMessage());
		
		}
		
	}
	
	//Creamos la instancia del Singleton de la conexion a la BD
	public static DBConnection getInstance() {
		if(instance == null) {
			instance = new DBConnection();
		}
		return instance;
		
	}
	
	public Connection getConnection() {
		return instance.conexionMySQL;
	}
	
}
