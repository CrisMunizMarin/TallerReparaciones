package dao.mysql;

public class ClienteDAOMySQL {
	int insert(Cliente c);
	int update(Cliente c);
	in delete(String dni);
	ArrayList<Cliente c> findall();
	Cliente findByDni(String dni);

}
