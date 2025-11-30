package vista;
import java.util.Scanner;
import entities.*;
import controlador.ControladorTaller;
import dao.mysql.ClienteDAOMySQL;
import dao.mysql.ReparacionDAOMySQL;
import dao.mysql.UsuarioDAOMySQL;
import dao.mysql.VehiculoDAOMySQL;

public class VistaTaller {
	//Aqui se meten los menus interactivos
	
	private ControladorTaller controlador = ControladorTaller.getInstance();
	private Usuario u = null;
	
	//Entrada de datos por scanner
	private Scanner entrada = new Scanner(System.in);
	
	
	//Inicializamos el controlador
	public VistaTaller() {
		controlador = ControladorTaller.getInstance();
    }
	
	//Menu principal, por defecto sale el menu de invitado, aqui tambien se pueden ver las reparaciones finalizadas
	public void menuPrincipal() {
		int opcion = -1;
		
		while(opcion != 0) {
	        System.out.println("\n******* MENÚ PRINCIPAL *******");
	        System.out.println("\n***SESION DE INVITADO***");
	        System.out.println("1. Iniciar sesion");
	        System.out.println("2. Visualizar reparaciones finalizadas");
	        System.out.println("0. Salir");
	        System.out.println("Seleccione una opción: ");
	        
	        String eleccion = entrada.nextLine();
	        
	        try {
	        	opcion = Integer.parseInt(eleccion);
	        }catch(NumberFormatException e) {
	        	System.out.println("Opción inválida. Por favor introduzca un número.");
	        	opcion = -1;
	        	continue;
	        }
	        
	        switch (opcion) {
	        
	        case 1:
	        	login();
	        	break;
	        		
	        	
	        case 2:
	        	controlador.visualizarReparacionesFinalizadas();
	        	break;
	        	
	        case 0:
	        	System.out.println("Saliendo de la aplicación....Hasta pronto");
	        	break;
	        
	        default:
	        	System.out.println("Opción inválida.");
	        	
	        }
		}
    }


    
    // --- case 1: Login (CU2) ---
	private void login() {
		System.out.println("****LOGIN****");
		System.out.print("Usuario (DNI): ");
        String usuario = entrada.nextLine();
        System.out.print("Contraseña: ");
        String contrasenia = entrada.nextLine();

        u = controlador.login(usuario, contrasenia);

        if (u != null) {
            System.out.println("\nHola, " + u.getNombre_usuario());
            rolActivo(u);
        } else {
            System.out.println("Credenciales incorrectas");
        }
	}
	
	//Metodo auxiliar. Dependiendo del rol le daremos un menu u otro
	private void rolActivo(Usuario u) {
		if(u.getRol() == Rol.ADMINISTRADOR){
			menuAdministrador();
		}else if(u.getRol() == Rol.MECANICO) {
			menuMecanico();
		}else {
			System.out.println("Rol no reconocido");
		}
	}
	
	//Submenús dependiendo del rol
	//Menu para el mecanico
	private void menuMecanico() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n**** MENÚ MECÁNICO ****");
            System.out.println("1. Registrar nueva reparación"); //CU3
            System.out.println("2. Cambiar estado de reparación"); //CU4
            System.out.println("0. Salir de la sesión");

            opcion =  entrada.nextInt();
            entrada.nextLine();

            switch (opcion) {

            case 1:
                // El método en el Controlador debe pedir todos los datos (matrícula, desc, coste...)
                registrarReparacion(u); 
                break;

            case 2:
                // El método en el Controlador debe pedir la matrícula y el estado
               cambiarEstadoReparacion();
                break;

            case 0:
                u = null;
                return; // Vuelve al menu principal
                
            default:
                System.out.println("Opción incorrecta");
            }
        }
    }
	
	//Menu para el Administrador
	private void menuAdministrador() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n**** MENÚ ADMINISTRADOR ****");
            System.out.println("1. Gestionar Clientes y Vehículos"); // CU5
            System.out.println("2. Registrar nueva reparación"); // CU3
            System.out.println("3. Cambiar estado de reparación"); // CU4
            System.out.println("4. Gestión de Usuarios"); //CRUD
            System.out.println("5. Consultar estadísticas"); //CU6
            System.out.println("0. Cerrar sesión");

            opcion = entrada.nextInt();
            entrada.nextLine();
            
            switch (opcion) {

            case 1:
                gestionarClientesYVehiculos();
                break;

            case 2:
                // Llama al método de la vista que a su vez llama la del controlador, pasando el DNI del Admin
                registrarReparacion(u);
                break;
                
            case 3:
                // Llama al método del Controlador pasando la matricula y el estado
                cambiarEstadoReparacion();
                break;

            case 4:
                gestionUsuarios();
                break;
                
            case 5:
            	System.out.println("En preparacion");
                //controlador.mostrarEstadisticas();
                break;

            case 0:
                u = null;
                return;
                
            default:
                System.out.println("Opción incorrecta");
            }
        }
	}
	
	
	private void gestionarClientesYVehiculos() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n*** GESTIÓN DE CLIENTES Y VEHÍCULOS ***");
            System.out.println("1. Registrar nuevo Cliente");
            System.out.println("2. Modificar datos de Cliente");
            System.out.println("3. Eliminar Cliente");
            System.out.println("*******************************");
            System.out.println("4. Registrar nuevo Vehículo");
            System.out.println("5. Modificar Vehículo");
            System.out.println("6. Eliminar Vehículo");
            System.out.println("0. Volver al Menú de Administrador");
            
            opcion = entrada.nextInt();
            entrada.nextLine();

            switch (opcion) {

            case 1:
                controlador.altaCliente(); 
                break;

            case 2:
                controlador.modificarCliente(); 
                break;

            case 3:
                controlador.eliminarCliente(); 
                break;
                
            case 4:
                controlador.altaVehiculo(); 
                break;

            case 5:
                controlador.modificarVehiculo(); 
                break;
                
            case 6:
                controlador.eliminarVehiculo(); 
                break;
                
            case 0:
               return;
               
            default:
                System.out.println(" Opción incorrecta");
            }
        }
     }
	
	private void gestionUsuarios() {
		int opcion = -1;
		
		while(opcion != 0) {
			System.out.println("\n*** GESTIÓN DE USUARIOS ***");
            System.out.println("1. Registrar nuevo Usuario");
            System.out.println("2. Modificar datos de Usuario");
            System.out.println("3. Eliminar Usuario");
            System.out.println("0. Volver al Menú de Administrador");
            
            opcion = entrada.nextInt();
            entrada.nextLine();
            
            switch (opcion) {

            case 1:
                controlador.altaUsuario(); 
                break;

            case 2:
                controlador.modificarUsuario(); 
                break;

            case 3:
                controlador.eliminarUsuario(); 
                break;
            default:
                System.out.println(" Opción incorrecta");
            }
                
		}
	}
	
	
	//Metodo registrarReparacion.
	/**
	 * Tenemos que hacer aqui otro metodo complementario al que tenenmos en el controlador
	 * el del controlador se encarga de insertar la reparacion pero este se encarga de pedir los datos*/
	private void registrarReparacion(Usuario u) {
		System.out.println("Introduzca la matricula del vehiculo: ");
		String matricula = entrada.nextLine();
		
		System.out.println("Introduzca una descripcion: ");
		String descripcion = entrada.nextLine();
		
		System.out.println("Introduzca un coste estimado: ");
		double coste = entrada.nextDouble();
		entrada.nextLine();
		
		System.out.println("Introduzca una fecha : ");
		String fecha =  entrada.nextLine();
		
		String dni_usuario = u.getDni_usuario();
		
		//Ahora pasamos estos datos al controlador, éste los verifica y añade la reparacion a la BD
		controlador.registrarReparacion(matricula, descripcion,fecha , coste, dni_usuario);
	}
	
	//Metodo cambiarEstadoReparacion
	/**
	 * Como en el metodo anterior debemos hacer un metodo completemario al del controlador. 
	 * El de la vista se encarga de pedir los datos y el del controlador es el que realiza el cambio del estado*/
	private void cambiarEstadoReparacion() {
		//Se pide la matricula del vehiculo para cambiar su estado
		System.out.println("Introduzca una matricula: ");
		String matricula = entrada.nextLine();
		
		String estado = "";
		boolean verificacion = false;
		
		while(!verificacion) {
			System.out.println("Actualizar el estado. Opciones: ");
			System.out.println("1. REPARACION");
			System.out.println("2. FINALIZADO");
			System.out.println("Introduce la opción: ");
			int numOpcion = entrada.nextInt();
			entrada.nextLine();
			
			if(numOpcion == 1) {
				estado = "REPARACION";
				verificacion = true;
			}else if(numOpcion == 2) {
				estado = "FINALIZADO";
				verificacion = true;
			}else {
				System.out.println("Opción no válida");
			}
		}
		
		controlador.cambiarEstadoReparacion(matricula, estado);
	}
	
	
	public static void main(String[] args) {
		// Inicializamos DAO
        VehiculoDAOMySQL daoVehiculo = new VehiculoDAOMySQL();
        ClienteDAOMySQL daoCliente = new ClienteDAOMySQL();
        UsuarioDAOMySQL daoUsuario = new UsuarioDAOMySQL();
        ReparacionDAOMySQL daoReparacion = new ReparacionDAOMySQL();
        
        Usuario inicioAdmin = new Usuario("Gonzalo","12345678Z","gonzalo",Rol.ADMINISTRADOR);
        Usuario inicioMecanico = new Usuario("Gonzalo","12345678A","gonza",Rol.MECANICO);
        
        
        //Inserto tus usuarios
        daoUsuario.insert(inicioAdmin);
        daoUsuario.insert(inicioMecanico);

        // Inicializamos el controlador singleton.
        ControladorTaller.inicio(daoVehiculo, daoCliente, daoUsuario, daoReparacion);
        
        VistaTaller vista = new VistaTaller();
        
        vista.menuPrincipal();
	}
    
}
