package es.ubu.lsi.model.conciertos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "CLIENTE")
public class Cliente implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "NIF")
	private String nif;
	
	@Column(name = "NOMBRE", nullable = false, length = 50)
	private String nombre;
	
	@Column(name = "APELLIDOS", nullable = false, length = 100)
	private String apellidos;
	
	@Embedded
	private DireccionPostal direccionPostal;
	
	@OneToMany
	private Set<Compra> compras = new HashSet<>();
	
	//Constructor por defecto
	public Cliente() {
		
	}
	
	public Cliente(String nif, String nombre, String apellidos, DireccionPostal dp) {
		setNif(nif);
		setNombre(nombre);
		setApellidos(apellidos);
		setDP(dp);
	}
	
	public String getNif() {
		return nif;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getApellidos() {
		return apellidos;
	}
	
	public DireccionPostal getDP() {
		return direccionPostal;
	}
	
	public Set<Compra> getCompras(){
		return compras;
	}
	
	public void setNif(String nif) {
		this.nif = nif;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public void setDp(DireccionPostal dp) {
		this.direccionPostal = dp;
	}
	
	public void setCompras(Set<Compras> compras) {
		this.compras = compras;
	}
	
	public void addCompra(Compra compra) {
		compras.add(compra);
		compra.setCliente(this);
	}
	
	public void removeCompra(Compra compra) {
		compras.remove(compra);
		compra.setCliente(null);
	}
	
	 @Override
	    public String toString() {
	        return "Cliente [nif=" + nif + ", nombre=" + nombre + ", apellidos=" + apellidos 
	                + ", direccionPostal=" + direccionPostal + "]";
	    }
}