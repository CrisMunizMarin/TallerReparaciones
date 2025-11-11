package controlador;

import dao.DAOFactory;
import entities.*;
import dao.mysql.ClienteDAOMySQL;

public class ControladorTaller {
	//Este es mi main
	public static void main(String[] args) {
		ClienteDAOMySQL clienteDAO = DAOFactory.getClienteDAO();
		
		clienteDAO.insert(new Cliente("Andrés García", "87674323T", "555-1001", "andres.garcia@mail.com"));
	}

	

}
