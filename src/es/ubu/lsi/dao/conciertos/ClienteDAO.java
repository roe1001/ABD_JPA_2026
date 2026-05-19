package es.ubu.lsi.dao.conciertos;

import java.util.List;
import javax.persistence.EntityManager;
import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.conciertos.Cliente;

/**
 * DAO para la entidad Cliente.
 */
public class ClienteDAO extends JpaDAO<Cliente, String> {

    public ClienteDAO(EntityManager em) {
        super(em);
    }

    @Override
    public List<Cliente> findAll() {
        return getEntityManager()
            .createQuery("SELECT c FROM Cliente c", Cliente.class)
            .getResultList();
    }

    protected Class<Cliente> getEntityClass() {
        return Cliente.class;
    }

    /**
     * Busca un cliente por su NIF (clave primaria).
     */
    public Cliente findById(String nif) {
        return getEntityManager().find(Cliente.class, nif);
    }
}