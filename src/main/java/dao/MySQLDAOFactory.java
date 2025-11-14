package dao;

import dao.interfaces.*;
import dao.mysql.*;

public class MySQLDAOFactory implements DAOFactory {
	

	@Override
	public ClienteDAO getClienteDAO() {
		
		return new ClienteDAOMySQL();
	}

	@Override
	public UsuarioDAO getUsuarioDAO() {
		
		return new UsuarioDAOMySQL();
	}
	
	@Override
	public VehiculoDAO getVehiculoDAO() {
		
		return new VehiculoDAOMySQL();
	}

	@Override
	public ReparacionDAO getReparacionDAO() {
		
		return new ReparacionDAOMySQL();
	}
}
