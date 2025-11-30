package vista;
import java.util.Scanner;
import entities.*;
import controlador.ControladorTaller;
import dao.mysql.ClienteDAOMySQL;
import dao.mysql.ReparacionDAOMySQL;
import dao.mysql.UsuarioDAOMySQL;
import dao.mysql.VehiculoDAOMySQL;

/**
 * Clase que representa la capa de Vista del Taller, encargada de toda la
 * interacción con el usuario (mostrar menús, solicitar datos e imprimir resultados).
 * Sigue el patrón MVC (Modelo-Vista-Controlador).
 *
 * @author Cristina Muñiz
 * @version 1.0
 */
public class VistaTaller {
	//Aqui se meten los menus interactivos
	
	private ControladorTaller controlador = ControladorTaller.getInstance();
	private Usuario u = null;
	
	//Entrada de datos por scanner
	private Scanner entrada = new Scanner(System.in);
	
	
	/**
	 * Constructor de la VistaTaller. 
	 * Inicializa la instancia única del Controlador (Singleton).
	 */
	public VistaTaller() {
		controlador = ControladorTaller.getInstance();
    }
	
	/**
	 * Muestra el menú principal de la aplicación.
	 * Permite iniciar sesión o visualizar las reparaciones que han finalizado.
	 * El ciclo se repite hasta que el usuario elige la opción de salir (0).
	 */
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


    
    //  Login (CU2) 
	/** * Solicita al usuario su DNI y contraseña para iniciar sesión.
	 * Llama al controlador para verificar las credenciales. Si la autenticación
	 * es exitosa, llama a rolActivo(Usuario) para mostrar el menú correspondiente.
	 * (Corresponde al CU2: Iniciar Sesión)
	 */
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
	
	/**
	 * Método auxiliar que determina qué submenú debe mostrarse al usuario
	 * después de un inicio de sesión exitoso, basado en su rol.
	 * * @param u El objeto Usuario autenticado.
	 */
	private void rolActivo(Usuario u) {
		if(u.getRol() == Rol.ADMINISTRADOR){
			menuAdministrador();
		}else if(u.getRol() == Rol.MECANICO) {
			menuMecanico();
		}else {
			System.out.println("Rol no reconocido");
		}
	}
	
	/**
	 * Muestra el menú específico para usuarios con el rol MECANICO.
	 * Permite registrar nuevas reparaciones y cambiar el estado de reparaciones existentes.
	 */
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
	
	/**
	 * Muestra el menú específico para usuarios con el rol ADMINISTRADOR.
	 * Permite acceder a la gestión de clientes/vehículos, reparaciones, usuarios y estadísticas.
	 */
	private void menuAdministrador() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n**** MENÚ ADMINISTRADOR ****");
            System.out.println("1. Gestionar Clientes y Vehículos"); // CU5
            System.out.println("2. Registrar nueva reparación"); // CU3
            System.out.println("3. Cambiar estado de reparación"); // CU4
            System.out.println("4. Gestión de Usuarios"); //CRUD
            System.out.println("5. Listar reparaciones"); 
            System.out.println("6. Consultar estadísticas"); //CU6
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
                controlador.listarReparaciones();
                break;
                
            case 6:
                mostrarEstadisticas();
                break;
            case 0:
                u = null;
                return;
                
            default:
                System.out.println("Opción incorrecta");
            }
        }
	}
	
	/**
	 * Muestra el submenú para la gestión de Clientes y Vehículos.
	 * Permite realizar operaciones CRUD (Alta, Modificación, Eliminación, Listado) sobre ambas entidades.
	 * (Corresponde al CU5: Gestión de Clientes y Vehículos)
	 */
	private void gestionarClientesYVehiculos() {
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n*** GESTIÓN DE CLIENTES Y VEHÍCULOS ***");
            System.out.println("1. Registrar nuevo Cliente");
            System.out.println("2. Modificar datos de Cliente");
            System.out.println("3. Eliminar Cliente");
            System.out.println("4. Listar clientes");
            System.out.println("*******************************");
            System.out.println("5. Registrar nuevo Vehículo");
            System.out.println("6. Modificar Vehículo");
            System.out.println("7. Eliminar Vehículo");
            System.out.println("8. Listar vehiculos");
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
            	controlador.listarClientes();
            	break;
                
            case 5:
                controlador.altaVehiculo(); 
                break;

            case 6:
                controlador.modificarVehiculo(); 
                break;
                
            case 7:
                controlador.eliminarVehiculo(); 
                break;
            
            case 8:
            	controlador.listarVehiculos();
            	break;
                
            case 0:
               return;
               
            default:
                System.out.println(" Opción incorrecta");
            }
        }
     }
	
	/**
	 * Muestra el submenú para la gestión de Usuarios.
	 * Permite realizar operaciones CRUD sobre los usuarios del sistema.
	 */
	private void gestionUsuarios() {
		int opcion = -1;
		
		while(opcion != 0) {
			System.out.println("\n*** GESTIÓN DE USUARIOS ***");
            System.out.println("1. Registrar nuevo Usuario");
            System.out.println("2. Modificar datos de Usuario");
            System.out.println("3. Eliminar Usuario");
            System.out.println("4. Listar usuarios");
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
               
            case 4:
                controlador.listarUsuarios(); 
                break;
            default:
                System.out.println(" Opción incorrecta");
            }
                
		}
	}
	
	
	/**
	 * Solicita los datos necesarios al usuario para registrar una nueva reparación (CU3).
	 * Una vez recopilados, delega la validación e inserción al Controlador.
	 * * @param u El usuario (Mecánico o Administrador) que realiza el registro.
	 */
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
	
	/**
	 * Solicita la matrícula de un vehículo y el nuevo estado de la reparación (CU4).
	 * Muestra un menú para que el usuario elija entre 'REPARACION' y 'FINALIZADO'.
	 * Llama a otro metodo dentro del controladorTaller para realizar la lógica de los datos.
	 */
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
	
	/**
	 * Muestra las estadísticas del taller (CU6), como el coste promedio de las reparaciones finalizadas.
	 * Delega el cálculo al Controlador y se encarga únicamente de la presentación y formato.
	 */
	private void mostrarEstadisticas() {
		System.out.println("\n*** CONSULTA DE ESTADÍSTICAS ***");
	    
	    // 1. Llamar al Controlador para obtener el dato
	    double promedio = controlador.calcularCostePromedio();
	    
	    // 2. Lógica de presentación y formato
	    if (promedio > 0) {
	        System.out.println("Coste Promedio de Reparaciones Finalizadas: " + 
	                           String.format("%.2f", promedio) + " €");
	    } else {
	        System.out.println("No hay datos de reparaciones finalizadas.");
	    }
	}
	
	/**
	 * Método principal (main) que inicializa los DAOs, el Controlador y la Vista,
	 * y arranca la aplicación con la presentación del menú principal.
	 * * @param args Argumentos de la línea de comandos.
	 */
	public static void main(String[] args) {
		// Inicializamos DAO
        VehiculoDAOMySQL daoVehiculo = new VehiculoDAOMySQL();
        ClienteDAOMySQL daoCliente = new ClienteDAOMySQL();
        UsuarioDAOMySQL daoUsuario = new UsuarioDAOMySQL();
        ReparacionDAOMySQL daoReparacion = new ReparacionDAOMySQL();
        
        Usuario administrador = new Usuario("Marcos","12121212Z","marcosAd",Rol.ADMINISTRADOR);
        Usuario mecanico = new Usuario("Ana","23232323A","anaMc",Rol.MECANICO);
        
        
        //Inserccion de usuarios de prueba
        daoUsuario.insert(administrador);
        daoUsuario.insert(mecanico);

        // Inicializamos el controlador singleton.
        ControladorTaller.inicio(daoVehiculo, daoCliente, daoUsuario, daoReparacion);
        
        VistaTaller vista = new VistaTaller();
        
        vista.menuPrincipal();
	}
    
}
