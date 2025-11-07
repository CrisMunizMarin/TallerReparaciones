package dao.mysql;

public class UsuarioDAOMySQL {
	boolean login(String dni, String password);
	int insert(Usuario u);
	ArrayList<Usuario u> findall();
	Usuario findByNombre(String nombre);
}
