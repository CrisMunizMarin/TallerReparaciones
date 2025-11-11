package dao;

import dao.interfaces.ClienteDAO;
import dao.mysql.ClienteDAOMySQL;

public class MySQLDAOFactory implements DAOFactory {
	

	@Override
	public ClienteDAO getClienteDAO() {
		
		return new ClienteDAOMySQL();
	}

	
}
