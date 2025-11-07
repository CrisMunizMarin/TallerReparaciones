package entities;

class Usuario {
	private int id_usuario;
	private String nombre_usuario;
	private String dni_usuario;
	private String password;
	Rol rol;
	
	public Usuario(int id_usuario, String nombre_usuario, String dni_usuario, String password, Rol rol) {
		this.id_usuario = id_usuario;
		this.nombre_usuario = nombre_usuario;
		this.dni_usuario = dni_usuario;
		this.password = password;
		this.rol = rol;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getNombre_usuario() {
		return nombre_usuario;
	}

	public void setNombre_usuario(String nombre_usuario) {
		this.nombre_usuario = nombre_usuario;
	}

	public String getDni_usuario() {
		return dni_usuario;
	}

	public void setDni_usuario(String dni_usuario) {
		this.dni_usuario = dni_usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	
	
}
