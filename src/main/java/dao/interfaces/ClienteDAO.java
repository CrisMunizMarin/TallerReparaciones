package dao.interfaces;

import java.util.ArrayList;
import entities.*;


public interface ClienteDAO {

	void insert(Cliente c);
	void update(Cliente c);
	void delete(String dni);
	ArrayList<Cliente> findAll();
	Cliente findByDni(String dni_cliente);
	Cliente findById(int id_cliente);
	

}
