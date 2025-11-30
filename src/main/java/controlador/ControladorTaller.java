package controlador;

import entities.*;
import dao.mysql.ClienteDAOMySQL;
import dao.mysql.ReparacionDAOMySQL;
import dao.mysql.UsuarioDAOMySQL;
import dao.mysql.VehiculoDAOMySQL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Controlador principal del Taller. Implementa el patrón Singleton.
 * Contiene la lógica de negocio y coordina las operaciones con las capas DAO.
 */
public class ControladorTaller {
	//Debemos crear un SINGLETON
	private static ControladorTaller instance;
	
	//Lamammos al usuario que se logueo
	private Usuario usuarioActivo;
	
	//Creamos el scanner 
	Scanner entrada = new Scanner(System.in);
	// Atributos para las capas de acceso a datos (DAO) - Inyección de dependencias
	private VehiculoDAOMySQL vehiculoDAO;
    private ClienteDAOMySQL clienteDAO;
    private UsuarioDAOMySQL usuarioDAO;
    private ReparacionDAOMySQL reparacionDAO;
    
    /**
     * Constructor privado para forzar el uso del patrón Singleton.
     * @param v DAO de Vehículos
     * @param c DAO de Clientes
     * @param u DAO de Usuarios
     * @param r DAO de Reparaciones
     */
    private ControladorTaller(VehiculoDAOMySQL v, ClienteDAOMySQL c,
                              UsuarioDAOMySQL u, ReparacionDAOMySQL r) {
        vehiculoDAO = v;
        clienteDAO = c;
        usuarioDAO = u;
        reparacionDAO = r;
    }
    
    /**
     * Método de inicialización estático para el Singleton.
     * Debe llamarse una sola vez al inicio de la aplicación.
     */
    public static void inicio(VehiculoDAOMySQL daoVehiculo, ClienteDAOMySQL daoCliente,
            UsuarioDAOMySQL daoUsuario, ReparacionDAOMySQL daoReparacion) {
    		instance = new ControladorTaller(daoVehiculo, daoCliente, daoUsuario, daoReparacion);
    }
    
    /**
     * Acceso a la instancia del Singleton.
     * @return La única instancia del ControladorTaller.
     */
  	public static ControladorTaller getInstance() {
          return instance;
      }
	
	
	//IMPLEMANTAMOS LA LOGICA DE NEGOCIO (RELATIVAS A LOS CASOS DE USO)
	
	//CU1 Ver reparaciones finalizadas (Invitado)
  	/**
	 * Recupera todas las reparaciones finalizadas y las visualiza directamente en la consola.
	 * @return El array con la lista de reparaciones finalizadas
	 */
    public ArrayList<Reparacion> visualizarReparacionesFinalizadas() {
    	ArrayList<Reparacion> listaReparaciones = reparacionDAO.repFinalizadas();
    	
    	
    	System.out.println("\n*** LISTADO DE REPARACIONES FINALIZADAS ***");
    	for(Reparacion rep : listaReparaciones) {
    		System.out.println(" Id: " + rep.getId_reparacion() + 
                    "\nDescripción: " + rep.getDescripcion() + 
                    "\nFecha entrada: " + rep.getFecha_entrada() + 
                    "\nCoste estimado: " + rep.getCoste_estimado());
    	}
    	return listaReparaciones;
    }
    
    //CU2 Login 
    /**
 	 * Intenta autenticar un usuario con el DNI y la contraseña proporcionados.
 	 * Si la autenticación es exitosa, establece el usuario como activo.
 	 * @param dni DNI del usuario.
 	 * @param pass Contraseña del usuario.
 	 * @return El objeto Usuario si el login es correcto, o  null si falla la verificación.
 	 */
    public Usuario login(String dni, String pass) {
        // Se comprueba que la contraseña es correcta en base al usuario. 
       boolean verificacion = usuarioDAO.login(dni, pass);
        
        if (!verificacion) {
        	return null;
        }

        Usuario u = usuarioDAO.findByDni(dni);
        this.usuarioActivo = u; // Guardamos el usuario activo
        return u;
    }

    
    
    //CU3 Registrar reparacion (Mecanico y Administrador)
    /**
 	 * Registra una nueva reparación con estado PENDIENTE, asociándola a un vehículo y al usuario que la registra.
 	 * @param matricula Matrícula del vehículo asociado.
 	 * @param descripcion Descripción de la avería.
 	 * @param fecha_entrada Fecha de entrada en formato de cadena (asumiendo "YYYY-MM-DD").
 	 * @param coste_estimado Coste estimado inicial.
 	 * @param dni_usuario DNI del usuario que registra la reparación.
 	 */
    public void registrarReparacion(String matricula, String descripcion, String fecha_entrada, double coste_estimado, String dni_usuario) {
        System.out.println("\n*** REGISTRAR NUEVA REPARACIÓN ***");

        // Buscar Vehículo por matricula
        Vehiculo v = vehiculoDAO.findByMatricula(matricula);
        if (v == null) {
            System.out.println("Error: Vehículo con matrícula " + matricula + " no encontrado.");
            return;
        }

        // Buscar Usuario que registra
        Usuario u = usuarioDAO.findByDni(dni_usuario);
        if (u == null) {
            System.out.println("Error: Usuario que registra no encontrado.");
            return;
        }

        // Se crea la reparación, valor por defecto de "PENDIENTE" 
        Reparacion r = new Reparacion(descripcion, Date.valueOf(fecha_entrada), coste_estimado, Estado.PENDIENTE, 
                                        v.getId_vehiculo(), u.getId_usuario());
        
        reparacionDAO.insert(r);
        System.out.println(" Reparación registrada con éxito para el vehículo con matricula: " + matricula + ".");
    }
    
    //CU4 Cambiar estado de reparacion (Mecanico y Administrador)
    /**
 	 * Cambia el estado de una reparación. Muestra las reparaciones por matrícula,
 	 * pide el ID de la reparación a modificar por consola y aplica el nuevo estado.
 	 * @param matricula La matrícula del vehículo cuyas reparaciones se listarán.
 	 * @param estado La cadena que representa el nuevo estado (debe coincidir con un valor del Enum  Estado).
 	 */
    public void cambiarEstadoReparacion(String matricula, String estado) {
    	ArrayList<Reparacion> listaReparaciones = reparacionDAO.findByMatricula(matricula);
    	
    	//Debemos convertir el estado de String a Enum
    	Estado estadoNuevo = Estado.valueOf(estado);

        if (listaReparaciones.isEmpty()) {
            System.out.println("No se han encontrado reparaciones asociadas a esa matricula.");
            return;
        }

        System.out.println("***Lista de Reparaciones de la matricula:  " + matricula + " ***");

        for (Reparacion rep : listaReparaciones) {
            System.out.println(" Id: " + rep.getId_reparacion() + 
                               "\nDescripción: " + rep.getDescripcion() + 
                               "\nFecha entrada: " + rep.getFecha_entrada() + 
                               "\nCoste estimado: " + rep.getCoste_estimado());
        }

        System.out.println("Introduzca el id de la reparación a modificar: ");
        int id_reparacion = entrada.nextInt();
        entrada.nextLine();

        // Una vez que introducimos el id, volvemos a recorrer la lista para que solo nos muestre la reparación seleccionada
        Reparacion r = null;
        for (Reparacion rep : listaReparaciones) {
            if (rep.getId_reparacion() == id_reparacion) {
                r = rep;
                break;
            }
        }

        if (r == null) {
            System.out.println("No se encuentra la reparación");
            return;
        }

        r.setEstado(estadoNuevo);
        reparacionDAO.update(r);

        System.out.println("Estado de la reparación con id: " + id_reparacion + " actualizado a: " + estadoNuevo);
    	
    }
    
    
    //Metodos para el CU5, Gestion de clientes y vehiculos (Administrador)
    //Clientes
    /**
 	 * Solicita interactivamente todos los datos de un nuevo cliente por consola y lo inserta en la base de datos.
 	 */
    public void altaCliente() {
    	System.out.println("****ALTA NUEVO CLIENTE****");
    	
    	//Pedimos los datos
    	 String nombre;
         do {
             System.out.print("Introduce el nombre: ");
             nombre = entrada.nextLine().trim();
             if (nombre.isEmpty()) System.out.println("El campo nombre no puede estar vacío.");
         } while (nombre.isEmpty());

         String dni;
         do {
             System.out.print("Introduce el dni: ");
             dni = entrada.nextLine().trim();
             if (dni.isEmpty()) System.out.println("El campo dni no puede estar vacío.");
         } while (dni.isEmpty());
         
         String telefono ;
         do {
             System.out.print("Introduce el telefono: ");
             telefono = entrada.nextLine().trim();
             if (telefono.isEmpty()) System.out.println("El campo telefono no puede estar vacío.");
         } while (telefono.isEmpty());

         String email;
         do {
             System.out.print("Introduce el email: ");
             email = entrada.nextLine().trim();
             if (email.isEmpty()) System.out.println("El campo email no puede estar vacío.");
         } while (email.isEmpty());

         

         Cliente c = new Cliente(nombre, dni, telefono, email);
         clienteDAO.insert(c);
         
    }
    
    /**
 	 * Permite modificar interactivamente los datos de un cliente (Nombre, DNI, Teléfono, Email)
 	 * buscando por DNI y solicitando los nuevos valores por consola.
 	 */
    public void modificarCliente() {
    	System.out.println("****MODIFICAR CLIENTE****");
    	System.out.println("Introduce el dni del cliente a modificar: ");
    	String dniModificar = entrada.nextLine();
    	
    	Cliente c = clienteDAO.findByDni(dniModificar);
    	if(c == null) {
    		System.out.println("Error: no se encuentra el cliente con dni: " + dniModificar);
    		return;
    	}
    	
    	//Necesitamos obtener los datos del cliente que hemos buscado para poder cambair los datos
    	boolean resultado = true;
        while (resultado) {
            System.out.println("\nDatos del cliente:");
            System.out.println("1. Nombre: " + c.getNombre());
            System.out.println("2. dni: " + c.getDni_cliente());
            System.out.println("3. Teléfono: " + c.getTelefono());
            System.out.println("4. Email: " + c.getEmail());
            System.out.println("0. Guardar");
            System.out.print("Selecciona el campo a modificar: ");

            String entradaUsuario = entrada.nextLine();
            int opcion;
            try {
            	opcion = Integer.parseInt(entradaUsuario);
            }catch(NumberFormatException e) {
            	System.out.println("Opcion invalida. " +e.getMessage());
            	continue;
            }

            switch (opcion) {
                case 1:
                	System.out.println("Introduce el nuevo nombre: ");
                    c.setNombre(entrada.nextLine());
                    break;
                case 2:
                	System.out.println("Introduce el nuevo dni: ");
                    c.setDni_cliente(entrada.nextLine()); 
                    break;
                case 3:
                	System.out.println("Introduce el nuevo telefono: ");
                    c.setTelefono(entrada.nextLine());;
                    break;
                case 4:
                    System.out.print("Introduce el nuevo email: ");
                    c.setEmail(entrada.nextLine());;
                    break;
                case 0:
                    resultado = false;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }

        clienteDAO.update(c);
        System.out.println("Cliente actualizado correctamente.");
    }
    
    /**
 	 * Solicita el DNI por consola y elimina el cliente correspondiente de la base de datos.
 	 */
    public void eliminarCliente() {
    	System.out.println("****ELIMINAR CLIENTE****");
    	System.out.println("Introduce el dni del cliente a eliminar: ");
    	String dniEliminar = entrada.nextLine();
    	clienteDAO.delete(dniEliminar);
    	
    }
    
    //Vehiculos
    /**
 	 * Solicita interactivamente los datos de un nuevo vehículo (matrícula, marca, modelo) 
 	 * y el DNI de un cliente para asociarlo, insertándolo en la base de datos.
 	 */
    public void altaVehiculo() {
    	System.out.println("****ALTA VEHICULO****");
        String matricula;
        do {
            System.out.print("Introduce la matrícula: ");
            matricula = entrada.nextLine().trim();
            if (matricula.isEmpty()) System.out.println("El campo matrícula no puede estar vacío.");
        } while (matricula.isEmpty());

        String marca;
        do {
            System.out.print("Introduce la marca: ");
            marca = entrada.nextLine().trim();
            if (marca.isEmpty()) System.out.println("El campo marca no puede estar vacío.");
        } while (marca.isEmpty());

        String modelo;
        do {
            System.out.print("Introduce el modelo: ");
            modelo = entrada.nextLine().trim();
            if (modelo.isEmpty()) System.out.println("El campo modelo no puede estar vacío.");
        } while (modelo.isEmpty());

        String dni_cliente;
        Cliente cliente = null;
        do {
            System.out.print("Introduce el dni del cliente: ");
            dni_cliente = entrada.nextLine().trim();
            if (dni_cliente.isEmpty()) {
                System.out.println("El campo dni no puede estar vacío.");
                continue;
            }
            // Necesitamos buscar el dni que nos proporcionan para verificar que esta en nuestra BD
            cliente = clienteDAO.findByDni(dni_cliente);
            if (cliente == null) {
                System.out.println("Cliente no encontrado.");
            }
        } while (cliente == null); 

        // Creamos un vehiculo nuevo con los datos y lo insertamos
        Vehiculo v = new Vehiculo(matricula, marca, modelo, cliente.getId_cliente());
        vehiculoDAO.insert(v);
        
    }
    
    /**
 	 * Permite modificar interactivamente los datos de un vehículo (matrícula, marca, modelo)
 	 * o reasignarlo a un nuevo cliente, buscando por matrícula y utilizando la consola para la interacción.
 	 */
    public void modificarVehiculo() {
    	System.out.println("****MODIFICAR VEHICULO****");
    	System.out.print("Introduce la matrícula del vehículo a modificar: ");
        String matricula = entrada.nextLine();

        Vehiculo v = vehiculoDAO.findByMatricula(matricula);
        if (v == null) {
            System.out.println("Error: vehículo no encontrado.");
            return;
        }
        
        Cliente c = clienteDAO.findById(v.getCliente_id());

        boolean resultado = true;

        while (resultado) {
            System.out.println("\nDatos del vehículo:");
            System.out.println("1. Matrícula: " + v.getMatricula());
            System.out.println("2. Marca: " + v.getMarca());
            System.out.println("3. Modelo: " + v.getModelo());
            System.out.println("4. Cliente actual: " + c.getDni_cliente());
            System.out.println("0. Guardar");
            System.out.print("Selecciona el campo a modificar: ");

            String entradaUsuario = entrada.nextLine();
            int opcion;

            try {
                opcion = Integer.parseInt(entradaUsuario);
            } catch (NumberFormatException e) {
                System.out.println("Opción inválida. " + e.getMessage());
                continue;
            }

            switch (opcion) {
                case 1:
                    System.out.print("Introduce la nueva matrícula: ");
                    String nuevaMatricula = entrada.nextLine().trim();
                    if (!nuevaMatricula.isEmpty()) {
                        v.setMatricula(nuevaMatricula);
                    } else {
                        System.out.println("el campo matrícula no puede estar vacío.");
                    }
                    break;

                case 2:
                    System.out.print("Introduce la nueva marca: ");
                    String nuevaMarca = entrada.nextLine().trim();
                    if (!nuevaMarca.isEmpty()) {
                        v.setMarca(nuevaMarca);
                    } else {
                        System.out.println("El campo marca no puede estar vacío.");
                    }
                    break;

                case 3:
                    System.out.print("Introduce el nuevo modelo: ");
                    String nuevoModelo = entrada.nextLine().trim();
                    if (!nuevoModelo.isEmpty()) {
                        v.setModelo(nuevoModelo);
                    } else {
                        System.out.println("> El campo modelo no puede estar vacío.");
                    }
                    break;

                case 4:
                    System.out.print("Introduce el dni del nuevo cliente: ");
                    String dniNuevoCliente = entrada.nextLine().trim();

                    if (dniNuevoCliente.isEmpty()) {
                        System.out.println("El campo dni no puede estar vacío.");
                        break;
                    }

                    Cliente nuevoCliente = clienteDAO.findByDni(dniNuevoCliente);
                    if (nuevoCliente == null) {
                        System.out.println("Error: Cliente no encontrado.");
                    } else {
                        v.setCliente_id(nuevoCliente.getId_cliente());
                        System.out.println("Cliente actualizado correctamente.");
                    }
                    break;

                case 0:
                    resultado = false;
                    break;

                default:
                    System.out.println("Opción inválida");
            }
        }

        vehiculoDAO.update(v);
        System.out.println("El vehículo se ha actualizado correctamente.");
    }
	
    /**
 	 * Solicita la matrícula por consola y elimina el vehículo correspondiente de la base de datos.
 	 */
    public void eliminarVehiculo() {
    	System.out.println("****ELIMINAR Vehiculo****");
    	System.out.println("Introduce la matricula del vehiculo a eliminar: ");
    	String matriculaEliminar = entrada.nextLine();
    	vehiculoDAO.delete(matriculaEliminar);
    	
    }
    
    //Metodos para la gestion de los usuarios
    /**
 	 * Solicita interactivamente todos los datos de un nuevo usuario (nombre, DNI, contraseña y rol)
 	 * por consola y lo registra.
 	 */
    public void altaUsuario() {
    	System.out.println("****ALTA NUEVO USUARIO****");
    	
    	//Pedimos los datos
    	 String nombre;
         do {
             System.out.print("Introduce el nombre: ");
             nombre = entrada.nextLine().trim();
             if (nombre.isEmpty()) System.out.println("El campo nombre no puede estar vacío.");
         } while (nombre.isEmpty());

         String dni;
         do {
             System.out.print("Introduce el dni: ");
             dni = entrada.nextLine().trim();
             if (dni.isEmpty()) System.out.println("El campo dni no puede estar vacío.");
         } while (dni.isEmpty());
         
         String password ;
         do {
             System.out.print("Introduce la contraseña: ");
             password = entrada.nextLine().trim();
             if (password.isEmpty()) System.out.println("El campo contraseña no puede estar vacío.");
         } while (password.isEmpty());

         String rol;
         do {
             System.out.print("Introduce el rol: ");
             rol = entrada.nextLine().trim().toUpperCase();
             if (rol.isEmpty()) System.out.println("El campo rol no puede estar vacío.");
         } while (rol.isEmpty());

         

         Usuario u = new Usuario (nombre, dni, password, Rol.valueOf(rol));
         usuarioDAO.insert(u);
    }
    
    /**
 	 * Permite modificar interactivamente los datos de un usuario existente (Nombre, DNI, Contraseña, Rol)
 	 * buscando por DNI y solicitando los nuevos valores por consola.
 	 */
    public void modificarUsuario() {
    	System.out.println("****MODIFICAR USUARIO****");
    	System.out.println("Introduce el dni del usuario a modificar: ");
    	String dniModificar = entrada.nextLine();
    	
    	Usuario u = usuarioDAO.findByDni(dniModificar);
    	if(u == null) {
    		System.out.println("Error: no se encuentra el usuaio con dni: " + dniModificar);
    		return;
    	}
    	
    	//Necesitamos obtener los datos del usuario que hemos buscado para poder cambair los datos
    	boolean resultado = true;
        while (resultado) {
            System.out.println("\nDatos del usuario:");
            System.out.println("1. Nombre: " + u.getNombre_usuario());
            System.out.println("2. dni: " + u.getDni_usuario());
            System.out.println("3. Password: " + u.getPassword());
            System.out.println("4. Rol: " + u.getRol());
            System.out.println("0. Guardar");
            System.out.print("Selecciona el campo a modificar: ");

            String entradaUsuario = entrada.nextLine();
            int opcion;
            try {
            	opcion = Integer.parseInt(entradaUsuario);
            }catch(NumberFormatException e) {
            	System.out.println("Opcion invalida. " +e.getMessage());
            	continue;
            }

            switch (opcion) {
                case 1:
                	System.out.println("Introduce el nuevo nombre: ");
                    u.setNombre_usuario(entrada.nextLine());;
                    break;
                case 2:
                	System.out.println("Introduce el nuevo dni: ");
                    u.setDni_usuario(entrada.nextLine()); 
                    break;
                case 3:
                	System.out.println("Introduce la nueva contraseña: ");
                    u.setPassword(entrada.nextLine());
                    break;
                case 4:
                    System.out.print("Introduce el nuevo rol: ");
                    String nuevoRolStr = entrada.nextLine().trim().toUpperCase();
                    Rol nuevoRol = Rol.valueOf(nuevoRolStr);
                    u.setRol(nuevoRol);
                    break;
                case 0:
                    resultado = false;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }

        usuarioDAO.update(u);;
        System.out.println("Usuario actualizado correctamente.");
    }
    
    /**
 	 * Solicita el DNI por consola y elimina el usuario correspondiente de la base de datos.
 	 */
    public void eliminarUsuario() {
    	System.out.println("****ELIMINAR Usuario****");
    	System.out.println("Introduce el dni del usuario a eliminar: ");
    	String dniEliminar = entrada.nextLine();
    	usuarioDAO.delete(dniEliminar);
    	
    }
    
    //CU6, calcular el coste promedio de las reparaciones finalizadas
    /**
 	 * Muestra por consola el coste promedio de todas las reparaciones cuyo estado es FINALIZADA.
 	 */
    public double calcularCostePromedio() {
    	return reparacionDAO.costePromedio();
    }
    
    //Listar vehiculos
    /**
 	 * Lista por consola todos los vehículos registrados en el sistema.
 	 */
    public ArrayList<Vehiculo> listarVehiculos() {
    	ArrayList<Vehiculo> listaVehiculos = vehiculoDAO.findAll();
    	
    	
    	System.out.println("\n*** LISTADO DE VEHICULOS ***");
    	for(Vehiculo v : listaVehiculos) {
    		System.out.println(" Id: " + v.getId_vehiculo() + 
                    "\nMatricula: " + v.getMatricula() + 
                    "\nMarca: " + v.getMarca()+ 
                    "\nModelo: " + v.getModelo() +
                    "\nCliente id: " + v.getCliente_id());
    		
    	}
    	return listaVehiculos;
    }
    
    //Listar clientes
    /**
 	 * Lista por consola todos los clientes registrados en el sistema.
 	 */
    public ArrayList<Cliente> listarClientes() {
    	ArrayList<Cliente> listaClientes = clienteDAO.findAll();
    	
    	
    	System.out.println("\n*** LISTADO DE CLIENTES ***");
    	for(Cliente c : listaClientes) {
    		System.out.println(" Id: " + c.getId_cliente() + 
                    "\nNombre: " + c.getNombre() + 
                    "\nDni: " + c.getDni_cliente()+ 
                    "\nTelefono: " + c.getTelefono() +
                    "\nEmail: " + c.getEmail());
    		
    	}
    	return listaClientes;
    }
    
    //Listar usuarios
    /**
 	 * Lista por consola todos los usuarios (mecánicos/administradores) registrados en el sistema.
 	 */
    public ArrayList<Usuario> listarUsuarios() {
    	ArrayList<Usuario> listaUsuarios = usuarioDAO.findAll();
    	
    	
    	System.out.println("\n*** LISTADO DE USUARIOS ***");
    	for(Usuario u : listaUsuarios) {
    		System.out.println(" Id: " + u.getId_usuario() + 
                    "\nNombre: " + u.getNombre_usuario() + 
                    "\nDni: " + u.getDni_usuario()+ 
                    "\nRol: " + u.getRol());
    		
    	}
    	return listaUsuarios;
    }
    
    //Listar reparaciones
    /**
 	 * Lista por consola todas las reparaciones registradas en el sistema.
 	 */
    public ArrayList<Reparacion> listarReparaciones() {
    	ArrayList<Reparacion> listarReparaciones = reparacionDAO.findAll();
    	
    	
    	System.out.println("\n*** LISTADO DE REPARACIONES ***");
    	for(Reparacion rep : listarReparaciones) {
    		System.out.println(" Id: " + rep.getId_reparacion() + 
                    "\nDescripcion: " + rep.getDescripcion() + 
                    "\nFecha entrada: " + rep.getFecha_entrada()+ 
                    "\ncoste estimado: " + rep.getCoste_estimado() +
                    "\nEstado: " + rep.getEstado() +
                    "\nVehiculo id: " + rep.getVehiculo_id() +
                    "\nUsuario id: " + rep.getUsuario_id());
    		
    	}
    	return listarReparaciones;
    }
}
