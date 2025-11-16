package dao.interfaces;

import java.util.ArrayList;


import entities.Usuario;

public interface UsuarioDAO {

	void insert(Usuario u);

	void update(Usuario u);

	void delete(String dni_usuario);

	ArrayList<Usuario> findAll();

	Usuario findByNombre(String nombre_usuario);

}
