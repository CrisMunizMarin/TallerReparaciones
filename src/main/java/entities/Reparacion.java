package entities;

import java.sql.Date;

class Reparacion {
	 private int id_reparacion;
	 private String descripcion;
	 private Date fecha_entrada;
	 private double coste_estimado;
	 Estado estado;
	 private int vehiculo_id;
	 private int usuario_id;
	 
	 public Reparacion(int id_reparacion, String descripcion, Date fecha_entrada, double coste_estimado, Estado estado ) {
		 this.id_reparacion = id_reparacion;
		 this.descripcion = descripcion;
		 this.fecha_entrada = fecha_entrada;
		 this.coste_estimado = coste_estimado;
		 this.estado = estado;
		 
		 
	 }

	 public int getId_reparacion() {
		 return id_reparacion;
	 }

	 public void setId_reparacion(int id_reparacion) {
		 this.id_reparacion = id_reparacion;
	 }

	 public String getDescripcion() {
		 return descripcion;
	 }

	 public void setDescripcion(String descripcion) {
		 this.descripcion = descripcion;
	 }

	 public Date getFecha_entrada() {
		 return fecha_entrada;
	 }

	 public void setFecha_entrada(Date fecha_entrada) {
		 this.fecha_entrada = fecha_entrada;
	 }

	 public double getCoste_estimado() {
		 return coste_estimado;
	 }

	 public void setCoste_estimado(double coste_estimado) {
		 this.coste_estimado = coste_estimado;
	 }

	 public Estado getEstado() {
		 return estado;
	 }

	 public void setEstado(Estado estado) {
		 this.estado = estado;
	 }

	 public int getVehiculo_id() {
		 return vehiculo_id;
	 }

	 public void setVehiculo_id(int vehiculo_id) {
		 this.vehiculo_id = vehiculo_id;
	 }

	 public int getUsuario_id() {
		 return usuario_id;
	 }

	 public void setUsuario_id(int usuario_id) {
		 this.usuario_id = usuario_id;
	 }
	 
	 
}
