package es.ubu.lsi.model.conciertos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;


@Entity
@Table(name = "GRUPO")
public class Grupo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "IDGRUPO")
	private Integer idGrupo;
	
	@Column(name = "NOMBRE", nullable = false, length = 50)
	private String nombre;
	
	@Column(name = "ESTILO", length = 20)
	private String estilo;
	
	@Column(name = "ACTIVO", nullable = false)
	private Integer activo; //1 activo, 0 falso
	
	@OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Concierto> conciertos = new HashSet<>();
	
	public Grupo() {
		
	}
	
	public Grupo(Integer idGrupo, String nombre, String estilo, Integer activo) {
		setID(idGrupo);
		setNombre(nombre);
		setEstilo(estilo);
		setActivo(activo);
	}
	
	public Integer getID() {
		return idGrupo;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getEstilo() {
		return estilo;
	}
	
	public Integer getActivo() {
		return activo;
	}
	
	public Set<Concierto> getConciertos(){
		return conciertos;
	}
	
	public void setID(Integer idGrupo) {
		this.idGrupo = idGrupo;
	}
	
	public void setNombre(String nombre) {
        this.nombre = nombre;
    }
	
	public void setEstilo(String estilo) {
        this.estilo = estilo;
    }
	
	public void setActivo(Integer activo) {
	    this.activo = activo;
	}
	
	public void setConciertos(Set<Concierto> conciertos) {
		this.conciertos = conciertos;
	}
	
	public void addConcierto(Concierto concierto) {
		conciertos.add(concierto);
		concierto.setGrupo(this);
	}
	
	public void removeConcierto(Concierto concierto) {
		conciertos.remove(concierto);
		concierto.setGrupo(null);
	}
	
	@Override
    public String toString() {
        return "Grupo [idgrupo=" + idgrupo + ", nombre=" + nombre + ", estilo=" + estilo + ", activo=" + activo + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Grupo grupo = (Grupo) obj;
        return idgrupo != null && idgrupo.equals(grupo.idgrupo);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
	
	
}