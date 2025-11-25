package controlador;

import dao.DAOFactory;
import dao.MySQLDAOFactory;
import entities.*;
import vista.VistaTaller;
import dao.mysql.ClienteDAOMySQL;
import dao.mysql.ReparacionDAOMySQL;
import dao.mysql.UsuarioDAOMySQL;
import dao.mysql.VehiculoDAOMySQL;
import java.sql.Date;

public class ControladorTaller {
	//Debemos crear un SINGLETON
	private static ControladorTaller instance;
	
	//Añadimos la llamada a la vista
	private VistaTaller vista;
	
	
	//Instancias de los DAOs 
	private ReparacionDAOMySQL reparacionDAO;
	private ClienteDAOMySQL clienteDAO;
	private UsuarioDAOMySQL usuarioDAO;
	private VehiculoDAOMySQL vehiculoDAO;
	private Usuario usuarioLogueado;
	
	
	//Contructor del Singleton
	private ControladorTaller() {
		DAOFactory factory = new MySQLDAOFactory();
		this.reparacionDAO = (ReparacionDAOMySQL) factory.getReparacionDAO();
        this.vehiculoDAO = (VehiculoDAOMySQL) factory.getVehiculoDAO();
        this.usuarioDAO = (UsuarioDAOMySQL) factory.getUsuarioDAO();
        // Inicializar Vista (Fachada)
        this.vista = new VistaTaller();
        //Inicializar el usuario como invitado
        this.usuarioLogueado = null;
	}
	
	//Acceso (llamamos a la instancia creada del controlador taller)
	public static ControladorTaller getInstance() {
		if(instance == null) {
			instance = new ControladorTaller();
		}
		return instance;
		
	}
	
	//IMPLEMANTAMOS LA LOGICA DE NEGOCIO (RELATIVAS A LOS CASOS DE USO)
	
	//Arrancar la aplicaion
	public void arancarAplicacion() {
		while(!salir) {
			int opcion = vista.mostrarMenuPrincipal(rolActivo());
			
			switch(rolActivo()) {
			case INVITADO:
                manejarMenuInvitado(opcion);
                break;
            case MECANICO:
                manejarMenuMecanico(opcion);
                break;
            case ADMINISTRADOR: 
                manejarMenuAdmin(opcion);
                break;
			}
		}
	}
	
	//CU2: LOGIN
	public boolean login() {
		//Pedimos los datos de la vista
		vista.mostrarMensaje("*********INICIO DE SESION********");
		String dni = vista.pedirDniUsuario();
		String pass = vista.pedirPasswordUsuario();
		
		//Llamamos al DAO de Ususario
		Usuario usuario = usuarioDAO.login(dni, pass);
		
		//Verificamos el inicio se sesion
		if(usuario != null) {
			this.usuarioLogueado = usuario;
			vista.mostrarMensaje("\nBienvenido, "+ usuario.getNombre_usuario() + " con rol: " + usuario.getRol());
			return true;
		}else {
			vista.mostrarMensaje("Error: credenciales incorrectas");
			return false;
		}
		
		
	}
	
	//Salida de sesion y vuelta al principio
	public void logOut() {
		this.usuarioLogueado = null;
		vista.mostrarMensaje("Sesion cerrada. Has vuelto al perfil de invitado");
	}
	
	//Retornar el rol para identificar como se ha logueado el usuario
	public Rol rolActivo() {
		if(usuarioLogueado != null) {
			return usuarioLogueado.getRol();
		}else {
			return Rol.INVITADO;
		}
		
	}
	
	//CU3: REGISTRAR REPARACION
	public boolean registrarReparacion(String matricula, String descripcion, double costeEstimado) {
		
		//Buscamos el vehiculo par asignar la reparacion
		Vehiculo vehiculo = vehiculoDAO.findByMatricula(matricula);
		
		//si no lo encuentra
		if(vehiculo == null) {
			System.out.println("Error: la matricula no se encuentra");
			return false;
		}
		
		
		
		//Si lo encuentra, creamos una nueva reparacion
		try {
			//Obtenemos la fecha de entrada de hoy
			java.sql.Date fechaEntrada = java.sql.Date.valueOf(java.time.LocalDate.now());
			
			Reparacion nuevaReparacion = new Reparacion(
					descripcion,
					fechaEntrada,
					costeEstimado,
					Estado.PENDIENTE,
					vehiculo.getId_vehiculo(),
					null //No le añadimos el mecanico, eso lo hacemos en cuanto el estado pase a REPARACION que es ahi cuando se le asigna
					);
			reparacionDAO.insert(nuevaReparacion);
			return true;
		}catch(Exception e) {
			System.err.println("Error: no se ha podido insertar el vehiculo"+ e.getMessage());
			return false;
		}
		
	}
	
	
}
