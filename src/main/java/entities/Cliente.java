package entities;

public class Cliente {
	private int id_cliente;
	private String nombre;
	private String dni_cliente;
	private String telefono;
	private String email;
	
	public Cliente(String nombre, String dni_cliente, String telefono, String email) {
		this.nombre = nombre;
		this.dni_cliente = dni_cliente;
		this.telefono = telefono;
		this.email = email;
	}

	public int getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDni_cliente() {
		return dni_cliente;
	}

	public void setDni_cliente(String dni_cliente) {
		this.dni_cliente = dni_cliente;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
