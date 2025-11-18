package dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;
import dao.DBConnection;
import dao.interfaces.VehiculoDAO;
import entities.Vehiculo;

public class VehiculoDAOMySQL implements VehiculoDAO {
	private Connection conexion;
    
    public VehiculoDAOMySQL() {
        conexion = DBConnection.getInstance().getConnection();
    }

	@Override
	public void insert(Vehiculo v) {
        String sql = "INSERT INTO vehiculo(matricula, marca, modelo, cliente_id) VALUES(?,?,?,?);";
        
        try(PreparedStatement pst = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            //Seteamos los parámetros del vehículo
            pst.setString(1, v.getMatricula());
            pst.setString(2, v.getMarca());
            pst.setString(3, v.getModelo());
            //Seteamos la clave foránea (cliente_id)
            pst.setInt(4, v.getCliente_id()); 
            
            int result = pst.executeUpdate();
            // Obtenemos el ID generado automáticamente
            try(ResultSet rs = pst.getGeneratedKeys()){
                if(rs.next()) {
                    int IdGenerado = rs.getInt(1);
                    
                    //Seteo el id_vehiculo por el que me ha dado esa insercion y asi conocer el nuevo id de la inserccion
                    v.setId_vehiculo(IdGenerado); 
                    System.out.println(" Inserción de Vehículo OK. ID asignado: " + IdGenerado);
                }
            }
            System.out.println("resultado de insercción: " + result + " filas afectadas");
        } catch(SQLException e) {
            System.out.println("Error al insertar vehículo: " + e.getMessage());
        }
        
    		
	}

	@Override
	public void update(Vehiculo v) {
	    String sql = "UPDATE vehiculo SET matricula=?, marca=?, modelo=?, cliente_id=? WHERE id_vehiculo=?";
	    try (PreparedStatement pst = conexion.prepareStatement(sql)) {
	        
	        // Seteamos los nuevos valores
	        pst.setString(1, v.getMatricula());
	        pst.setString(2, v.getMarca());
	        pst.setString(3, v.getModelo());
	        // Seteamos la clave foránea, por si cambia de dueño
	        pst.setInt(4, v.getCliente_id()); 
	        pst.setInt(5, v.getId_vehiculo()); //WHERE
	            
	        int result = pst.executeUpdate();
	        
	        System.out.println("Actualización de Vehículo OK. " + result + " filas afectadas.");
	        
	    } catch(SQLException e) {
	        System.out.println("Error en la actualización de vehículo: " + e.getMessage());
	    }
		
	}

	@Override
	public void delete(String matricula) {
	    String sql = "DELETE FROM vehiculo WHERE matricula = ?";
	    try (PreparedStatement pst = conexion.prepareStatement(sql)) {
	        
	        // Setea en la posicion 1 la matricula
	        pst.setString(1, matricula); 
	        int result = pst.executeUpdate();
	        
	        System.out.println("Filas de vehículo eliminadas con matrícula " + matricula + ": " + result);
	        
	    } catch(SQLException e) {
	        System.out.println("Error al eliminar vehículo por matrícula: " + e.getMessage());
	    }
		
	}

	@Override
	public Vehiculo findByMatricula(String matricula) {
	    String sql = "SELECT id_vehiculo, matricula, marca, modelo, cliente_id FROM vehiculo WHERE matricula = ?";
	    Vehiculo vehiculo = null;
	    
	    try (PreparedStatement pst = conexion.prepareStatement(sql)) {
	    	// Setea en la posicion 1 la matricula
	        pst.setString(1, matricula);
	        try (ResultSet rs = pst.executeQuery()) {
	        	// Si encontramos un resultado, reconstruimos el objeto Vehiculo incluido el id_vehiculo
	            if (rs.next()) {
	                vehiculo = new Vehiculo(
	                    rs.getInt("id_vehiculo"),
	                    rs.getString("matricula"),
	                    rs.getString("marca"),
	                    rs.getString("modelo"),
	                    rs.getInt("cliente_id") // Clave foránea
	                );
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println("Error al buscar vehículo por matrícula: " + e.getMessage());
	    }
	    return vehiculo; 
	}

	@Override
	public ArrayList<Vehiculo> findAll() {
		ArrayList<Vehiculo> listaVehiculos = new ArrayList<>();
	    String sql = "SELECT id_vehiculo, matricula, marca, modelo, cliente_id FROM vehiculo";
	    
	    try (Statement stmt = conexion.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) { 
	    	//Iteramos sobre los datos que nos devuelve la BD en el ResultSet
	        while (rs.next()) {
	        	//Componemos un objeto vehiculo por cada fila que nos devuelva
	            Vehiculo v = new Vehiculo(
	                rs.getInt("id_vehiculo"),
	                rs.getString("matricula"),
	                rs.getString("marca"),
	                rs.getString("modelo"),
	                rs.getInt("cliente_id") // Clave foránea
	            );
	            listaVehiculos.add(v);
	        }
	    } catch (SQLException e) {
	        System.out.println(" Error al listar los vehículos: " + e.getMessage());
	    }
	    return listaVehiculos;
	}

	@Override
	public ArrayList<Vehiculo> findByClienteId(int cliente_id) {
		ArrayList<Vehiculo> listaVehiculosPorCliente = new ArrayList<>();
	    
	    // Se hace una consulta SELECT que busca por el id_cliente
	    String sql = "SELECT id_vehiculo, matricula, marca, modelo, cliente_id FROM vehiculo WHERE cliente_id = ?";
	    try (PreparedStatement pst = conexion.prepareStatement(sql)) {
	        
	        //// Setea en la posicion 1 al cliente_id
	        pst.setInt(1, cliente_id);
	        try (ResultSet rs = pst.executeQuery()) {
	            
	            // Usamos 'while' porque puede haber mas de 1 vehiculo que pertenece al mismo cliente
	            while (rs.next()) {
	            	//Componemos un objeto vehiculo por cada fila que nos devuelva
	                Vehiculo v = new Vehiculo(
	                    rs.getInt("id_vehiculo"),
	                    rs.getString("matricula"),
	                    rs.getString("marca"),
	                    rs.getString("modelo"),
	                    rs.getInt("cliente_id") 
	                );
	                listaVehiculosPorCliente.add(v);
	            }
	        }
	    } catch (SQLException e) {
	        System.out.println(" Error al buscar vehículos por ID de cliente: " + e.getMessage());
	    }
	    return listaVehiculosPorCliente;
	}

}
