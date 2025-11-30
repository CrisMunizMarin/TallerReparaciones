package dao.interfaces;
import java.util.ArrayList;

import entities.Reparacion;
public interface ReparacionDAO {

	void insert(Reparacion r);
	void update(Reparacion r);
	void delete(int id_reparacion);
	ArrayList<Reparacion> findAll();
	ArrayList<Reparacion> findByVehiculoId(int id_vehiculo);
	Reparacion findById(int id_reparacion);
	ArrayList<Reparacion> findByMatricula(String matricula);
}
