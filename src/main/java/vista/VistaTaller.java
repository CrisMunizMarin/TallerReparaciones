package vista;
import java.util.Scanner;
import entities.Rol;

public class VistaTaller {
	//Aqui se meten los menus interactivos
	private Scanner entrada;
	
	public VistaTaller() {
        // Inicializar el Scanner al crear la vista
        this.entrada = new Scanner(System.in);
    }
	
	//Meenu principal, por defecto sale el menu de invitado
	public int mostrarMenuInvitado() {
        mostrarMensaje("\n--- MENÚ PRINCIPAL ---");
        mostrarMensaje("1. Iniciar Sesión");
        mostrarMensaje("0. Salir de la Aplicación");
        return pedirOpcion();
    }
	
	
	//Menu para el mecanico
	public int mostrarMenuMecanico() {
        mostrarMensaje("\n--- MENÚ MECÁNICO ---");
        mostrarMensaje("1. Ver listado de Reparaciones Pendientes"); 
        mostrarMensaje("2. Cambiar Estado de Reparación (CU4)");
        mostrarMensaje("3. Registrar Nueva Reparación (CU3)");
        mostrarMensaje("9. Cerrar Sesión");
        mostrarMensaje("0. Salir de la Aplicación");
        return pedirOpcion();
    }

	//Menui para el Administrador
	public int mostrarMenuAdmin() {
        mostrarMensaje("\n--- MENÚ ADMINISTRADOR ---");
        mostrarMensaje("1. Gestionar Clientes y Vehículos (CU5)");
        mostrarMensaje("2. Gestión de Usuarios (CRUD)");
        mostrarMensaje("3. Ver Estadísticas/Reportes");
        mostrarMensaje("9. Cerrar Sesión");
        mostrarMensaje("0. Salir de la Aplicación");
        return pedirOpcion();
    }
	//Se hace un submenú para la opcion de gestion de clientes y vehiculos
	public int mostrarMenuGestion() {
	    System.out.println("\n--- GESTIÓN DE CLIENTES Y VEHÍCULOS (ADMIN) ---");
	    System.out.println("1. Registrar nuevo Cliente");
	    System.out.println("2. Modificar datos de Cliente");
	    System.out.println("3. Eliminar Cliente");
	    System.out.println("4. Registrar nuevo Vehículo");
	    System.out.println("5. Eliminar Vehículo");
	    System.out.println("0. Volver al Menú Principal");
	    return pedirOpcion(); 
	}
	
	
	
	//Metodos auc¡xiliares
	public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
	
	public int pedirOpcion() {
        System.out.print("Seleccione una opción: ");
        while (!entrada.hasNextInt()) {
            mostrarMensaje("Entrada no válida. Por favor, introduzca un número.");
            entrada.next(); // Consumir la entrada no válida
            System.out.print("Seleccione una opción: ");
        }
        int opcion = entrada.nextInt();
        entrada.nextLine(); // Consumir el salto de línea pendiente
        return opcion;
    }
	
	//Metodos para realizar los insert, update, deñlte
	//Metodo par pedir un String al usuairo
	public String pedirString(String mensaje) {
        System.out.print(mensaje + ": ");
        return entrada.nextLine();
    }
	//Metodo para pedir un int al usuairo
	public int pedirInt(String mensaje) {
        System.out.print(mensaje + ": ");
        while (!entrada.hasNextInt()) {
            mostrarMensaje("Entrada no válida. Por favor, introduzca un número entero.");
            entrada.next();
            System.out.print(mensaje + ": ");
        }
        int valor = entrada.nextInt();
        entrada.nextLine(); 
        return valor;
    }
	
	//Pedir un double al usuairo
	public double pedirDouble(String mensaje) {
        System.out.print(mensaje + ": ");
        while (!entrada.hasNextDouble()) {
            mostrarMensaje("Entrada no válida. Por favor, introduzca un número decimal (ej. 150.50).");
            entrada.next();
            System.out.print(mensaje + ": ");
        }
        double valor = entrada.nextDouble();
        entrada.nextLine();
        return valor;
    }
	
// --- CU4: Cambiar Estado ---
    
    public int pedirIdReparacion() {
        return pedirInt("Introduzca el ID de la Reparación");
    }

    public String pedirNuevoEstado() {
        mostrarMensaje("Estados Válidos: PENDIENTE, EN_REPARACION, FINALIZADO");
        return pedirString("Introduzca el nuevo ESTADO");
    }

    // --- CU5: Gestión de Clientes ---
    
    public String pedirDni() {
        return pedirString("DNI del Cliente");
    }
    
    // Los datos individuales (nombre, teléfono, email, matrícula, etc.) se piden
    // directamente con pedirString() y pedirInt() en el Controlador
    // para simplificar la lógica de modificación (UPDATE).

    
    // --- Login ---
    
    public String pedirUsuario() {
        return pedirString("Usuario");
    }
    
    public String pedirPassword() {
        return pedirString("Contraseña");
    }
    
    // Cierre del Scanner
    public void cerrar() {
        if (entrada != null) {
            entrada.close();
        }
    }
}
