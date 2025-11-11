package dao;

import dao.interfaces.ClienteDAO;
import dao.mysql.ClienteDAOMySQL;

public interface DAOFactory {
	public ClienteDAO getClienteDAO();
	/*public static ClienteDAOMySQL getClienteDAO() {
		return new ClienteDAOMySQL();
		
	}*/
}
