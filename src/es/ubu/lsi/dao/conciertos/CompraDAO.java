package es.ubu.lsi.dao.conciertos;

import java.util.List;
import javax.persistence.EntityManager;
import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.conciertos.*;

/**
 * DAO para la entidad Compra.
 * 
 * @author ROE
 */
public class CompraDAO extends JpaDAO<Compra, Integer> {

    public CompraDAO(EntityManager em) {
        super(em);
    }
    
    public void insert(Compra compra) {
        getEntityManager().persist(compra);
    }

    @Override
    public List<Compra> findAll() {
        return getEntityManager()
            .createQuery("SELECT c FROM Compra c", Compra.class)
            .getResultList();
    }

    protected Class<Compra> getEntityClass() {
        return Compra.class;
    }

    // ============ MÉTODOS ADICIONALES ============
    
    /**
     * Busca una compra por su ID.
     * @param id Identificador de la compra
     * @return Compra encontrada o null
     */
    public Compra findById(Integer id) {
        return getEntityManager().find(Compra.class, id);
    }
    
    /**
     * Busca compras de un cliente.
     * @param cliente Cliente del que se quieren las compras
     * @return Lista de compras del cliente
     */
    public List<Compra> findByCliente(Cliente cliente) {
        return getEntityManager()
            .createQuery("SELECT c FROM Compra c WHERE c.cliente = :cliente", Compra.class)
            .setParameter("cliente", cliente)
            .getResultList();
    }
    
    /**
     * Busca compras de un cliente por su NIF.
     * @param nif Identificador del cliente
     * @return Lista de compras del cliente
     */
    public List<Compra> findByClienteNif(String nif) {
        return getEntityManager()
            .createQuery("SELECT c FROM Compra c WHERE c.cliente.nif = :nif", Compra.class)
            .setParameter("nif", nif)
            .getResultList();
    }
    
    /**
     * Busca compras de un concierto.
     * @param concierto Concierto del que se quieren las compras
     * @return Lista de compras para ese concierto
     */
    public List<Compra> findByConcierto(Concierto concierto) {
        return getEntityManager()
            .createQuery("SELECT c FROM Compra c WHERE c.concierto = :concierto", Compra.class)
            .setParameter("concierto", concierto)
            .getResultList();
    }
    
    /**
     * Busca compras de un concierto por su ID.
     * @param idConcierto Identificador del concierto
     * @return Lista de compras para ese concierto
     */
    public List<Compra> findByConciertoId(Integer idConcierto) {
        return getEntityManager()
            .createQuery("SELECT c FROM Compra c WHERE c.concierto.idconcierto = :idConcierto", Compra.class)
            .setParameter("idConcierto", idConcierto)
            .getResultList();
    }
    
    /**
     * Cuenta cuántas compras tiene un concierto.
     * @param idConcierto Identificador del concierto
     * @return Número de compras
     */
    public Long countByConciertoId(Integer idConcierto) {
        return getEntityManager()
            .createQuery("SELECT COUNT(c) FROM Compra c WHERE c.concierto.idconcierto = :idConcierto", Long.class)
            .setParameter("idConcierto", idConcierto)
            .getSingleResult();
    }
    
    /**
     * Obtiene el siguiente ID disponible para una nueva compra.
     * @return Siguiente ID (máximo actual + 1)
     */
    public Integer getNextId() {
        Integer maxId = getEntityManager()
            .createQuery("SELECT MAX(c.idcompra) FROM Compra c", Integer.class)
            .getSingleResult();
        return (maxId == null) ? 1 : maxId + 1;
    }
    
    /**
     * Elimina todas las compras de un concierto.
     * Usado en desactivar() antes de eliminar los conciertos.
     * 
     * @param idConcierto Identificador del concierto
     * @return Número de compras eliminadas
     */
    public int deleteByConciertoId(Integer idConcierto) {
        return getEntityManager()
            .createQuery("DELETE FROM Compra c WHERE c.concierto.idconcierto = :idConcierto")
            .setParameter("idConcierto", idConcierto)
            .executeUpdate();
    }
    
    /**
     * Elimina todas las compras de los conciertos de un grupo.
     * 
     * @param idGrupo Identificador del grupo
     * @return Número de compras eliminadas
     */
    public int deleteByGrupoId(Integer idGrupo) {
        return getEntityManager()
            .createNativeQuery("DELETE FROM COMPRA WHERE IDCONCIERTO IN (SELECT IDCONCIERTO FROM CONCIERTO WHERE IDGRUPO = ?)")
            .setParameter(1, idGrupo)
            .executeUpdate();
    }	
}