package dao.interfaces;

import java.util.ArrayList;
import entities.*;


public interface ClienteDAO {

	void insert(Cliente c);
	void update(Cliente c);
	void delete(String dni);
	ArrayList<Cliente> findall();
	Cliente findByDni(String dni);
	

}
