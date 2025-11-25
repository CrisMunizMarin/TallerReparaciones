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
			
			//Necesitamos la fecha de salida porque lo tenemos en el cosntructor
			java.sql.Date fechaSalida = null;
			
			Reparacion nuevaReparacion = new Reparacion(
					descripcion,
					fechaEntrada,
					costeEstimado,
					Estado.PENDIENTE,
					fechaSalida,
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
	
	//CU4: CAMBIAR ESTADO DE LA REPARACION (ESTO LO HACE EL MECANICO)
	public void cambiarEstadoReparacion() {
		//Pedimos el rol porque el que puede hacer estos cmabios es el mecanico
		if(rolActivo() != Rol.MECANICO) {
			vista.mostrarMensaje("Error: necesitas ser MEcanico para poder actualizar una reparación");
			return;
		}
		vista.mostrarMensaje("*********CAMBIO DE ESTADO DE REPARACION********");
		
		//Pedimos en la vista el Id de la reparacion
		int idReparacion = vista.pedirIdReparacion();
		
		//Buscamos la reparacion en reparacion por su id
		Reparacion reparacionBuscada = reparacionDAO.findById(idReparacion);
		
		//Verificamos la reparacion si no se encuntra
		if(reparacionBuscada == null) {
			vista.mostrarMensaje("Error: la reparacion con id: "+ idReparacion + " no se ha encontrado");
			return;
		}
		
		//Si la reparacion se encuentra, la validamos 
		String estadoNuevo = vista.pedirNuevoEstado();
		Estado nuevoEstado;
		try {
			nuevoEstado = Estado.valueOf(estadoNuevo.toUpperCase());
		}catch(IllegalArgumentException e) {
	        vista.mostrarMensaje("ERROR: El estado no es válido.");
	        return;
		}
		
		//Una vez la validamos le tenemos que cambiar el estado (modificamos el objeto)
		reparacionBuscada.setEstado(nuevoEstado);
		
		//Si el estado es FINALIZADO hay que actuaizar datos porque esto me sirve para las estadisticas
		if(nuevoEstado == Estado.FINALIZADO) {
			vista.mostrarMensaje("ATENCIÓN: Actualizando datos de finalización.");
			
			//Actaulizamos los datos de la fecha de salida a fecha de hoy
			java.sql.Date fechaSalida = java.sql.Date.valueOf(java.time.LocalDate.now());
	        reparacionBuscada.setFecha_salida(fechaSalida);
	        
	        //Actualizamos el coste, aunque no haria falta porque manejamos datos fijos en cuanto al pvp de la reparacion
	        //pero seria interesante de cara a si empezamos a meter prv por dia o algo asi
	        double costeFinal = vista.pedirCoste("Coste FINAL de la Reparación (€)");
	        reparacionBuscada.setCoste_estimado(costeFinal);
	        
	        reparacionDAO.update(reparacionBuscada);
	        vista.mostrarMensaje("Reparación " + idReparacion + " actualizada a estado: " + nuevoEstado.toString() + ".");
		}
		
	}
	
	//CU5: GESTION DE CLIENTES Y VEHICULOS (LO HACE EL ADMINISTRADOR)
	public void gestionarClientesYVehiculos() {
		//Validamos el rol ya que tiene que ser el Administrador
		if(rolActivo() != Rol.ADMINISTRADOR) {
			vista.mostrarMensaje("Error: necesitas ser ADMINISTRADOR para poder gestionar los clientes y los vehiculos");
			return;
		}
		boolean volver = false;
	    while (!volver) {
	        int opcion = vista.mostrarMenuGestion();

	        switch (opcion) {
	            case 1:
	                registrarCliente();
	                break;
	            case 2:
	                modificarCliente();
	                break;
	            case 3:
	                eliminarCliente();
	                break;
	            case 4:
	                registrarVehiculo();
	                break;
	            case 5:
	                eliminarVehiculo();
	                break;
	            case 0:
	                volver = true;
	                break;
	            default:
	                vista.mostrarMensaje("Opción no válida. Inténtelo de nuevo.");
	        }
	    }
	}
	
	
}
