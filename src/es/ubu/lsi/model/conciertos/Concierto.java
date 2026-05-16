package es.ubu.lsi.model.conciertos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "CONCIERTO")
public class Concierto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "IDCONCIERTO")
    private Integer idconcierto;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "CIUDAD", nullable = false, length = 100)
    private String ciudad;

    @Column(name = "FECHA", nullable = false)
    private Timestamp fecha;

    @Column(name = "TICKETS", nullable = false)
    private Integer tickets;

    @Column(name = "PRECIO", nullable = false)
    private Double precio;

    // Relación con Grupo (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "IDGRUPO", nullable = false)
    private Grupo grupo;

    // Relación bidireccional con Compra
    @OneToMany(mappedBy = "concierto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Compra> compras = new HashSet<>();

    // Constructor por defecto
    public Concierto() {
    }

    // Constructor con parámetros
    public Concierto(Integer idconcierto, String nombre, String ciudad, Timestamp fecha, 
                     Integer tickets, Double precio, Grupo grupo) {
    	
    	setIdconcierto(idconcierto);
    	setNombre(nombre);
        setCiudad(ciudad);
        setFecha(fecha);
        setTickets(tickets);
        setPrecio(precio);;
        setGrupo(grupo);
    }

    public Integer getIdconcierto() {
        return idconcierto;
    }

    public void setIdconcierto(Integer idconcierto) {
        this.idconcierto = idconcierto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Integer getTickets() {
        return tickets;
    }

    public void setTickets(Integer tickets) {
        this.tickets = tickets;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Set<Compra> getCompras() {
        return compras;
    }

    public void setCompras(Set<Compra> compras) {
        this.compras = compras;
    }

    // Método helper para relación bidireccional
    public void addCompra(Compra compra) {
        compras.add(compra);
        compra.setConcierto(this);
    }

    public void removeCompra(Compra compra) {
        compras.remove(compra);
        compra.setConcierto(null);
    }

    // Método para restar tickets (usado en la transacción comprar)
    public void restarTickets(int cantidad) throws IllegalArgumentException {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de tickets debe ser positiva");
        }
        if (this.tickets < cantidad) {
            throw new IllegalArgumentException("No hay suficientes tickets disponibles");
        }
        this.tickets -= cantidad;
    }

    @Override
    public String toString() {
        return "Concierto [idconcierto=" + idconcierto + ", nombre=" + nombre + ", ciudad=" + ciudad 
                + ", fecha=" + fecha + ", tickets=" + tickets + ", precio=" + precio + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Concierto concierto = (Concierto) obj;
        return idconcierto != null && idconcierto.equals(concierto.idconcierto);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}