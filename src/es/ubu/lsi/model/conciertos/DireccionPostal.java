package es.ubu.lsi.model.conciertos;

import java.io.Serializable;
import javax.persistence.*;

@Embeddable
public class DireccionPostal implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String direccion;
	private String cp;
	private String ciudad;
	
	//Constructor por defecto
	public DireccionPostal() {
		
	}
	
	//Constructor con parametros
	public DireccionPostal(String direccion, String cp, String ciudad) {
		setDireccion(direccion);
		setCp(cp);
		setCiudad(ciudad);
	}
	
	public String getDireccion() {
		return direccion;
	}
	
	public String getCp() {
		return cp;
	}
	
	public String getCiudad() {
		return ciudad;
	}
	
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	public void setCp(String cp) {
		this.cp = cp;
	}
	
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	
	@Override
    public String toString() {
        return "DireccionPostal [direccion=" + direccion + ", codigoPostal=" + cp + ", ciudad=" + ciudad + "]";
    }
}