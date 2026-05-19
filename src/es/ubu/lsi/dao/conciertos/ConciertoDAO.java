package es.ubu.lsi.dao.conciertos;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.conciertos.Concierto;
import es.ubu.lsi.model.conciertos.Grupo;

/**
 * DAO para la entidad Concierto.
 * 
 * @author ROE
 */
public class ConciertoDAO extends JpaDAO<Concierto, Integer> {

    public ConciertoDAO(EntityManager em) {
        super(em);
    }

    @Override
    public List<Concierto> findAll() {
        return getEntityManager()
            .createQuery("SELECT c FROM Concierto c", Concierto.class)
            .getResultList();
    }

    protected Class<Concierto> getEntityClass() {
        return Concierto.class;
    }

    // ============ MÉTODOS ADICIONALES ============
    
    /**
     * Busca un concierto por su ID.
     * @param id Identificador del concierto
     * @return Concierto encontrado o null
     */
    public Concierto findById(Integer id) {
        return getEntityManager().find(Concierto.class, id);
    }
    
    /**
     * Busca conciertos de un grupo específico.
     * @param grupo Grupo del que se quieren los conciertos
     * @return Lista de conciertos del grupo
     */
    public List<Concierto> findByGrupo(Grupo grupo) {
        return getEntityManager()
            .createQuery("SELECT c FROM Concierto c WHERE c.grupo = :grupo", Concierto.class)
            .setParameter("grupo", grupo)
            .getResultList();
    }
    
    /**
     * Busca conciertos de un grupo por su ID.
     * @param idGrupo Identificador del grupo
     * @return Lista de conciertos del grupo
     */
    public List<Concierto> findByGrupoId(Integer idGrupo) {
        return getEntityManager()
            .createQuery("SELECT c FROM Concierto c WHERE c.grupo.idgrupo = :idGrupo", Concierto.class)
            .setParameter("idGrupo", idGrupo)
            .getResultList();
    }
    
    /**
     * Busca el concierto de un grupo en una fecha específica.
     * Método CRUCIAL para la transacción comprar().
     * 
     * @param idGrupo Identificador del grupo
     * @param fecha Fecha del concierto (solo se compara la parte de fecha, no hora)
     * @return Concierto encontrado o null
     */
    public Concierto findByGrupoAndFecha(Integer idGrupo, Date fecha) {
        try {
            // Opción 2: Usar el ID del grupo con c.grupo = :grupo
            // Primero carga el grupo
            Grupo grupo = getEntityManager().find(Grupo.class, idGrupo);
            if (grupo == null) {
                return null;
            }
            
            TypedQuery<Concierto> query = getEntityManager()
                .createQuery("SELECT c FROM Concierto c WHERE c.grupo = :grupo AND TRUNC(c.fecha) = TRUNC(:fecha)", Concierto.class);
            query.setParameter("grupo", grupo);
            query.setParameter("fecha", fecha);
            
            List<Concierto> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca conciertos por ciudad.
     * @param ciudad Ciudad donde se celebra el concierto
     * @return Lista de conciertos en esa ciudad
     */
    public List<Concierto> findByCiudad(String ciudad) {
        return getEntityManager()
            .createQuery("SELECT c FROM Concierto c WHERE c.ciudad = :ciudad", Concierto.class)
            .setParameter("ciudad", ciudad)
            .getResultList();
    }
    
    /**
     * Busca conciertos posteriores a una fecha.
     * @param fecha Fecha límite (inclusive)
     * @return Lista de conciertos desde esa fecha
     */
    public List<Concierto> findDesdeFecha(Date fecha) {
        return getEntityManager()
            .createQuery("SELECT c FROM Concierto c WHERE c.fecha >= :fecha ORDER BY c.fecha", Concierto.class)
            .setParameter("fecha", fecha)
            .getResultList();
    }
    
    /**
     * Busca conciertos con tickets disponibles.
     * @return Lista de conciertos con tickets > 0
     */
    public List<Concierto> findConTicketsDisponibles() {
        return getEntityManager()
            .createQuery("SELECT c FROM Concierto c WHERE c.tickets > 0", Concierto.class)
            .getResultList();
    }
    
    /**
     * Reduce el número de tickets de un concierto.
     * Método usado en la transacción comprar().
     * 
     * @param idConcierto Identificador del concierto
     * @param cantidad Cantidad de tickets a restar
     * @return true si se pudo restar, false si no hay suficientes
     */
    public boolean restarTickets(Integer idConcierto, int cantidad) {
        Concierto concierto = findById(idConcierto);
        if (concierto != null && concierto.getTickets() >= cantidad) {
            concierto.setTickets(concierto.getTickets() - cantidad);
            getEntityManager().merge(concierto);
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si existe un concierto con el ID dado.
     * @param id Identificador del concierto
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Integer id) {
        Long count = getEntityManager()
            .createQuery("SELECT COUNT(c) FROM Concierto c WHERE c.idconcierto = :id", Long.class)
            .setParameter("id", id)
            .getSingleResult();
        return count > 0;
    }
    
    /**
     * Elimina todos los conciertos de un grupo.
     * Usado en desactivar() antes de desactivar el grupo.
     * 
     * @param idGrupo Identificador del grupo
     * @return Número de conciertos eliminados
     */
    public int deleteByGrupoId(Integer idGrupo) {
        return getEntityManager()
            .createNativeQuery("DELETE FROM CONCIERTO WHERE IDGRUPO = ?")
            .setParameter(1, idGrupo)
            .executeUpdate();
    }
}