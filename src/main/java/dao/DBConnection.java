package dao;

import java.io.FileInputStream;
import java.io.IOException;
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
	
			//1.2 Hacer con un FileInputStream
			Properties props = new Properties();
			FileInputStream file = new FileInputStream("src\\main\\resources\\conexion.properties");
			props.load(file);
			dataSource.setUrl(props.getProperty("url"));
			dataSource.setUser(props.getProperty("user"));
			dataSource.setPassword(props.getProperty("password"));
			
			file.close();
			
			
			//1.4 Main
			conexionMySQL = dataSource.getConnection();
			System.out.println("Conexion establecida correctamente");
		}catch(SQLException  |  IOException e) {
			System.err.println("Error al conectar con MySql: " + e.getMessage());
		
		}
		
	}
	
	
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
