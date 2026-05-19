package es.ubu.lsi.dao.conciertos;

import java.util.List;
import javax.persistence.EntityManager;
import es.ubu.lsi.dao.JpaDAO;
import es.ubu.lsi.model.conciertos.*;

/*
 * DAO para la entidad Cliente
 * 
 * @author ROE
 */
public class ClienteDAO extends JpaDAO<Cliente, String>{
	
	public ClienteDAO(EntityManager em) {
		super(em);
	}
	
	@Override
	public List<Cliente> findAll(){
		return getEntityManager()
				.createQuery("SELECT c FROM Cliente c", Cliente.class)
				.getResultList();
	}
	
	protected Class<Cliente> getEntityClass(){
		return Cliente.class;
	}
	
	/**
     * Busca un cliente por su NIF.
     * @param nif Identificador del cliente
     * @return Cliente encontrado o null si no existe
     */
    public Cliente findByNif(String nif) {
        return getEntityManager().find(Cliente.class, nif);
    }
    
    /**
     * Busca clientes por apellidos (coincidencia parcial).
     * @param apellidos Texto a buscar en apellidos
     * @return Lista de clientes que coinciden
     */
    public List<Cliente> findByApellidos(String apellidos) {
        return getEntityManager()
            .createQuery("SELECT c FROM Cliente c WHERE c.apellidos LIKE :apellidos", Cliente.class)
            .setParameter("apellidos", "%" + apellidos + "%")
            .getResultList();
    }
    
    /**
     * Busca clientes por ciudad.
     * @param ciudad Nombre de la ciudad
     * @return Lista de clientes de esa ciudad
     */
    public List<Cliente> findByCiudad(String ciudad) {
        return getEntityManager()
            .createQuery("SELECT c FROM Cliente c WHERE c.direccionPostal.ciudad = :ciudad", Cliente.class)
            .setParameter("ciudad", ciudad)
            .getResultList();
    }
    
    /**
     * Verifica si existe un cliente con el NIF dado.
     * @param nif Identificador del cliente
     * @return true si existe, false en caso contrario
     */
    public boolean existsById(String nif) {
        Long count = getEntityManager()
            .createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.nif = :nif", Long.class)
            .setParameter("nif", nif)
            .getSingleResult();
        return count > 0;
    }
	
}

