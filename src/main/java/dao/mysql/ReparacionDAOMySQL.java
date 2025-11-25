package dao.mysql;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;



import dao.DBConnection;
import dao.interfaces.ReparacionDAO;
import entities.Estado;
import entities.Reparacion;


public class ReparacionDAOMySQL implements ReparacionDAO{
private Connection conexion;
	
	public ReparacionDAOMySQL() {
		conexion = DBConnection.getInstance().getConnection();
	}
	
	@Override
	public void insert(Reparacion r) {
		//No incluimos la ENUM estado porque tiene un valor por defecto
		String sql = "INSERT INTO reparacion (descripcion, fecha_entrada, coste_estimado, estado, fecha_salida, vehiculo_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
		
        try (PreparedStatement pst = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Asignar los valores a los parámetros
            pst.setString(1, r.getDescripcion());
            pst.setDate(2, r.getFecha_entrada()); 
            pst.setDouble(3, r.getCoste_estimado());
            pst.setString(4, r.getEstado().toString().toUpperCase());
            
          //Como la fecha de salida es null hay que manejarla
            if(r.getFecha_salida() != null) {
                pst.setDate(5, r.getFecha_salida());
            } else {
                // Si la fecha es null el ponemos null explicito
                pst.setObject(5, null); 
            } 
            
            pst.setInt(6, r.getVehiculo_id());
       
       
            //Como el suaurio puede ser null, debemos manejarlo
            if(r.getUsuario_id() != null) {
            	pst.setInt(7, r.getUsuario_id());
            }else {
            	pst.setObject(7, null);
            }
            
            //Ejecutar la inserción
            pst.executeUpdate();

            // 3. Recuperar la clave generada (id_reparacion)
            
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    // Asignamos el ID autogenerado al objeto Reparacion
                    r.setId_reparacion(rs.getInt(1)); 
                }
            } 

        } catch (SQLException e) {
            System.out.println("Error al insertar reparación." + e.getMessage());
            e.printStackTrace();
        }
		
	}

	@Override
	public void update(Reparacion r) {
		String sql= "UPDATE reparacion SET descripcion=?, fecha_entrada=?, coste_estimado=?, estado=?, fecha_salida=?, vehiculo_id=?, usuario_id=? WHERE id_reparacion=?";
		
		try (PreparedStatement pst = conexion.prepareStatement(sql)) {

	        //Setear campos
	        pst.setString(1, r.getDescripcion());
	        pst.setDate(2, r.getFecha_entrada()); 
	        pst.setDouble(3, r.getCoste_estimado());
	        pst.setString(4,r.getEstado().toString().toUpperCase());
	        
	        //Como la fecha de salida es null hay que manejarla
            if(r.getFecha_salida() != null) {
                pst.setDate(5, r.getFecha_salida());
            } else {
                // Si la fecha es null el ponemos null explicito
                pst.setObject(5, null); 
            } 
            
	        pst.setInt(6, r.getVehiculo_id());
	        
	        // 2. Manejo de la FK usuario_id (puede ser NULL)
	        if (r.getUsuario_id() != null) {
	            pst.setInt(7, r.getUsuario_id()); 
	        } else {
	            // Si el mecánico se desasigna, ponemos NULL
	            pst.setObject(7, null); 
	        }

	        
	        pst.setInt(8, r.getId_reparacion()); //WHERE
	        
	        //Obtenemos los resultados  y los comprobamos
	        int result = pst.executeUpdate();
	        
	        if (result == 0) {
	            System.out.println("ERROR: No se encontró la reparación con ID " + r.getId_reparacion() + " para actualizar.");
	        }

	    } catch (SQLException e) {
	        System.out.println("Error al actualizar reparación. Verifique que el ID de vehículo/usuario exista." + e.getMessage());
	        
	    }
		
	}

	@Override
	public void delete(int id_reparacion) {
		String sql = "DELETE FROM reparacion WHERE id_reparacion = ?";
		
		try (PreparedStatement ps = conexion.prepareStatement(sql)) {

	        // Seteamos el valor del id de la reparación a 1 porque es lo que le estoy pidiendo
	        ps.setInt(1, id_reparacion); 
	        
	        // 
	        int result = ps.executeUpdate();
	        
	        if (result == 0) {
	            System.out.println("ERROR: No se encontró la reparación con ID " + id_reparacion + " para eliminar.");
	        } else {
	            System.out.println("Reparación con ID " + id_reparacion + " eliminada correctamente.");
	        }

	    } catch (SQLException e) {
	        System.out.println("Error al eliminar reparación con ID " + id_reparacion + "." + e.getMessage());
	        
	    }
	}

	@Override
	public ArrayList<Reparacion> findAll() {
		ArrayList<Reparacion> listaReparaciones = new ArrayList<>();
		
		String sql = "SELECT id_reparacion, descripcion, fecha_entrada, coste_estimado, estado,fecha_salida, vehiculo_id, usuario_id FROM reparacion";
		try (PreparedStatement pst = conexion.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

		        // Recorremos cada fila del resultado
		        while (rs.next()) {
		            
		            // 1. Manejo del FK usuario_id (puede ser NULL)
		            // Usamos rs.getObject() para recuperar el Integer o null
		            Integer idUsuario = (Integer) rs.getObject("usuario_id");
		          //Tenenmos que mapear el campo ENUM estado de la BD(String) a la ENUM de JAVA
					Estado estadoObtenido = Estado.valueOf(rs.getString("estado").toUpperCase());
					//  Mapeo del campo fecha_salida
		            java.sql.Date fechaSalida = rs.getDate("fecha_salida");

		            // 2. Mapeo el constructor
		            Reparacion r = new Reparacion(
		                rs.getInt("id_reparacion"),
		                rs.getString("descripcion"),
		                rs.getDate("fecha_entrada"),
		                rs.getDouble("coste_estimado"),
		                estadoObtenido,
		                fechaSalida,
		                rs.getInt("vehiculo_id"),
		                idUsuario 
		            );
		            
		            listaReparaciones.add(r);
		        }

		    } catch (SQLException e) {
		        System.out.println("Error al listar todas las reparaciones." + e.getMessage());
		        
		    }
		    return listaReparaciones;
		
	}

	@Override
	public ArrayList<Reparacion> findByVehiculoId(int id_vehiculo) {
		ArrayList<Reparacion> listaReparacionesVehiculo = new ArrayList<>();
		String sql = "SELECT id_reparacion, descripcion, fecha_entrada, coste_estimado, estado,fecha_salida, vehiculo_id, usuario_id FROM reparacion WHERE vehiculo_id = ?";
		
		try (PreparedStatement pst = conexion.prepareStatement(sql)) {
	        
	        // Setear el parámetro de búsqueda
	        pst.setInt(1, id_vehiculo);
	        
	        
	        try (ResultSet rs = pst.executeQuery()) {
	            
	            // Recorremos cada reparación encontrada
	            while (rs.next()) {
	                
	                // Mapeo de la columna opcional (puede ser NULL)
	                Integer idUsuario = (Integer) rs.getObject("usuario_id");
	              //Tenenmos que mapear el campo ENUM estado de la BD(String) a la ENUM de JAVA
					Estado estadoObtenido = Estado.valueOf(rs.getString("estado").toUpperCase());
				//  Mapeo del campo fecha_salida
		            java.sql.Date fechaSalida = rs.getDate("fecha_salida");

	                // Mapeo directo al constructor de Reparacion
	                Reparacion r = new Reparacion(
	                    rs.getInt("id_reparacion"),
	                    rs.getString("descripcion"),
	                    rs.getDate("fecha_entrada"),
	                    rs.getDouble("coste_estimado"),
	                    estadoObtenido,
	                    fechaSalida,
	                    rs.getInt("vehiculo_id"),
	                    idUsuario 
	                    
	                );
	                
	                listaReparacionesVehiculo.add(r);
	            }
	        } 

	    } catch (SQLException e) {
	        System.out.println("Error al buscar reparaciones por ID de vehículo: " + id_vehiculo + e.getMessage());
	       
	    }
	    return listaReparacionesVehiculo;
	}
	
	
	//Este metodos se crea porque necesitamos encontrar la reparacion para poder actualizar su estado
	@Override
	public Reparacion findById(int id_reparacion) {
		String sql = "SELECT id_reparacion, descripcion, fecha_entrada, coste_estimado, estado,fecha_salida, vehiculo_id, usuario_id FROM reparacion WHERE id_reparacion = ?";
	    Reparacion r = null; 

	    try (PreparedStatement pst = conexion.prepareStatement(sql)) {
		        
	        pst.setInt(1, id_reparacion);
		        
	        try (ResultSet rs = pst.executeQuery()) {
	            
	            if (rs.next()) { 
	                // Mapeamos el usuario, el estado y la fecha
	                Integer idUsuario = (Integer) rs.getObject("usuario_id");
	                Estado estadoObtenido = Estado.valueOf(rs.getString("estado").toUpperCase());
	                java.sql.Date fechaSalida = rs.getDate("fecha_salida");
	                
	                //Preparamos el objeto
	                r = new Reparacion(
	                    rs.getInt("id_reparacion"),
	                    rs.getString("descripcion"),
	                    rs.getDate("fecha_entrada"),
	                    rs.getDouble("coste_estimado"),
	                    estadoObtenido,
	                    fechaSalida,
	                    rs.getInt("vehiculo_id"),
	                    idUsuario 
	                );
	            }
	        } 

	    } catch (SQLException e) {
	        System.out.println("Error al buscar reparación por ID: " + id_reparacion + e.getMessage());
	    }
	    return r; // Devuelve el objeto r (reparacion)
	}

}
