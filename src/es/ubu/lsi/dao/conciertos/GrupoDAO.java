package es.ubu.lsi.dao.conciertos;

import java.util.List;
import javax.persistence.EntityManager;
import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.conciertos.Grupo;

/**
 * DAO para la entidad Grupo.
 * 
 * @author ROE
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

    // ============ MÉTODOS ADICIONALES ============
    
    /**
     * Busca un grupo por su ID.
     * @param id Identificador del grupo
     * @return Grupo encontrado o null si no existe
     */
    public Grupo findById(Integer id) {
        return getEntityManager().find(Grupo.class, id);
    }
    
    /**
     * Busca grupos por nombre (coincidencia exacta).
     * @param nombre Nombre del grupo
     * @return Grupo encontrado o null
     */
    public Grupo findByNombreExacto(String nombre) {
        List<Grupo> resultados = getEntityManager()
            .createQuery("SELECT g FROM Grupo g WHERE g.nombre = :nombre", Grupo.class)
            .setParameter("nombre", nombre)
            .getResultList();
        return resultados.isEmpty() ? null : resultados.get(0);
    }
    
    /**
     * Busca grupos por nombre (coincidencia parcial).
     * @param nombre Parte del nombre del grupo
     * @return Lista de grupos que contienen el texto
     */
    public List<Grupo> findByNombreLike(String nombre) {
        return getEntityManager()
            .createQuery("SELECT g FROM Grupo g WHERE g.nombre LIKE :nombre", Grupo.class)
            .setParameter("nombre", "%" + nombre + "%")
            .getResultList();
    }
    
    /**
     * Busca grupos por estilo musical.
     * @param estilo Estilo musical (ej: Rock, Romantico)
     * @return Lista de grupos de ese estilo
     */
    public List<Grupo> findByEstilo(String estilo) {
        return getEntityManager()
            .createQuery("SELECT g FROM Grupo g WHERE g.estilo = :estilo", Grupo.class)
            .setParameter("estilo", estilo)
            .getResultList();
    }
    
    /**
     * Busca grupos activos (activo = 1).
     * @return Lista de grupos activos
     */
    public List<Grupo> findActivos() {
        return getEntityManager()
            .createQuery("SELECT g FROM Grupo g WHERE g.activo = 1", Grupo.class)
            .getResultList();
    }
    
    /**
     * Busca grupos inactivos (activo = 0).
     * @return Lista de grupos inactivos
     */
    public List<Grupo> findInactivos() {
        return getEntityManager()
            .createQuery("SELECT g FROM Grupo g WHERE g.activo = 0", Grupo.class)
            .getResultList();
    }
    
    /**
     * Verifica si existe un grupo con el ID dado.
     * @param id Identificador del grupo
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(Integer id) {
        Long count = getEntityManager()
            .createQuery("SELECT COUNT(g) FROM Grupo g WHERE g.idgrupo = :id", Long.class)
            .setParameter("id", id)
            .getSingleResult();
        return count > 0;
    }
    
    /**
     * Desactiva un grupo (cambia activo a 0).
     * @param id Identificador del grupo
     * @return true si se desactivó, false si no existía
     */
    public boolean desactivar(Integer id) {
        Grupo grupo = findById(id);
        if (grupo != null) {
            grupo.setActivo(0);
            getEntityManager().merge(grupo);
            return true;
        }
        return false;
    }
}