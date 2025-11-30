package dao.interfaces;

import java.util.ArrayList;


import entities.Usuario;

public interface UsuarioDAO {

	void insert(Usuario u);

	void update(Usuario u);

	void delete(String dni_usuario);

	ArrayList<Usuario> findAll();

	Usuario findByNombre(String nombre_usuario);

	Usuario findByDni(String dni_usuario);

	boolean login(String dni_usuario, String password);

}
