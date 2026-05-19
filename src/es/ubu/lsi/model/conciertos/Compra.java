package es.ubu.lsi.model.conciertos;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "COMPRA")
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "IDCOMPRA")
    private Integer idcompra;

    @Column(name = "N_TICKETS", nullable = false)
    private Integer nTickets;

    @ManyToOne
    @JoinColumn(name = "NIF", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "IDCONCIERTO", nullable = false)
    private Concierto concierto;

    // Constructor por defecto
    public Compra() {
    }

    // Constructor con parámetros
    public Compra(Integer idcompra, Integer nTickets, Cliente cliente, Concierto concierto) {
        setIdcompra(idcompra);
        setnTickets(nTickets);
        setCliente(cliente);
        setConcierto(concierto);
    }

    // Getters y Setters
    public Integer getIdcompra() {
        return idcompra;
    }

    public void setIdcompra(Integer idcompra) {
        this.idcompra = idcompra;
    }

    public Integer getnTickets() {
        return nTickets;
    }

    public void setnTickets(Integer nTickets) {
        this.nTickets = nTickets;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Concierto getConcierto() {
        return concierto;
    }

    public void setConcierto(Concierto concierto) {
        this.concierto = concierto;
    }

    @Override
    public String toString() {
        return "Compra [idcompra=" + idcompra + ", tickets=" + nTickets + "]";
    }

    // ToString completo (sin mostrar cliente y concierto para evitar recursión)
    public String toStringComplete() {
        return "Compra [idcompra=" + idcompra + ", cliente=" + cliente 
                + ", concierto=" + concierto + ", tickets=" + nTickets + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Compra compra = (Compra) obj;
        return idcompra != null && idcompra.equals(compra.idcompra);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}