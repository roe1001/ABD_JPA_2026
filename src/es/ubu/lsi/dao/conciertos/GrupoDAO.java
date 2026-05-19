package es.ubu.lsi.dao.conciertos;

import java.util.List;
import javax.persistence.EntityManager;
import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.conciertos.Grupo;

/**
 * DAO para la entidad Grupo.
 */
public class GrupoDAO extends JpaDAO<Grupo, Integer> {

    public GrupoDAO(EntityManager em) {
        super(em);
    }

    @Override
    public List<Grupo> findAll() {
        return getEntityManager()
            .createQuery("SELECT g FROM Grupo g", Grupo.class)
            .getResultList();
    }

    protected Class<Grupo> getEntityClass() {
        return Grupo.class;
    }

    /**
     * Busca un grupo por su ID.
     */
    public Grupo findById(Integer id) {
        return getEntityManager().find(Grupo.class, id);
    }

    /**
     * Desactiva el grupo poniendo ACTIVO = 0.
     * Usado en la transacción desactivar().
     */
    public void desactivar(Integer idGrupo) {
        Grupo grupo = findById(idGrupo);
        if (grupo != null) {
            grupo.setActivo(0);
            getEntityManager().merge(grupo);
        }
    }
}