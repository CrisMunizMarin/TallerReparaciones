package dao.interfaces;

import java.util.ArrayList;

import entities.Vehiculo;

public interface VehiculoDAO {
	void insert(Vehiculo v);
    void update(Vehiculo v);
    void delete(String matricula);
    
    Vehiculo findByMatricula(String matricula);
    ArrayList<Vehiculo> findAll();
    
    // CRUCIAL: Necesitas este método por la relación de la BD
    ArrayList<Vehiculo> findByClienteId(int clienteId);
}
