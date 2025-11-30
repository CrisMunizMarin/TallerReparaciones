package dao;

import dao.interfaces.ClienteDAO;
import dao.interfaces.ReparacionDAO;
import dao.interfaces.UsuarioDAO;
import dao.interfaces.VehiculoDAO;


public interface DAOFactory {
	public ClienteDAO getClienteDAO();
	public UsuarioDAO getUsuarioDAO();
	public VehiculoDAO getVehiculoDAO();
	public ReparacionDAO getReparacionDAO()
;	
}
