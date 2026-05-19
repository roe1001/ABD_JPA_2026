package es.ubu.lsi.service.conciertos;

import java.util.Date;
import java.util.List;

import es.ubu.lsi.dao.conciertos.*;
import es.ubu.lsi.model.conciertos.*;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;
import es.ubu.lsi.service.PersistenceFactorySingleton;
import javax.persistence.EntityManager;

/**
 * Implementación del servicio de gestión de conciertos.
 * 
 * @author ROE
 */
public class ServiceImpl extends PersistenceService implements Service {

    // ============ MÉTODO 1: COMPRAR ============
    
	@Override
	public void comprar(Date fecha, String nif, int idGrupo, int tickets) 
	        throws PersistenceException {
	    
	    // Validaciones básicas de entrada
	    if (fecha == null) {
	        throw new IncidentException("La fecha no puede ser nula", IncidentError.NOT_EXIST_CONCERT);
	    }
	    if (nif == null || nif.trim().isEmpty()) {
	        throw new IncidentException("El NIF no puede ser nulo o vacío", IncidentError.NOT_EXIST_CLIENT);
	    }
	    if (tickets <= 0) {
	        throw new IncidentException("El número de tickets debe ser positivo", IncidentError.NOT_AVAILABLE_TICKETS);
	    }
	    
	    EntityManager em = null;
	    
	    try {
	        // 1. Obtener EntityManager y comenzar transacción
	        em = PersistenceFactorySingleton.getEntityManager();
	        beginTransaction(em);
	        
	        // 2. Verificar que el cliente existe
	        ClienteDAO clienteDAO = new ClienteDAO(em);
	        Cliente cliente = clienteDAO.findByNif(nif);
	        if (cliente == null) {
	            throw new IncidentException("Cliente con NIF " + nif + " no encontrado", 
	                                       IncidentError.NOT_EXIST_CLIENT);
	        }
	        
	        // 3. Verificar que el grupo existe
	        GrupoDAO grupoDAO = new GrupoDAO(em);
	        Grupo grupo = grupoDAO.findById(idGrupo);
	        if (grupo == null) {
	            throw new IncidentException("Grupo con ID " + idGrupo + " no encontrado", 
	                                       IncidentError.NOT_EXIST_MUSIC_GROUP);
	        }
	        
	        // 4. Buscar el concierto del grupo en esa fecha
	        ConciertoDAO conciertoDAO = new ConciertoDAO(em);
	        Concierto concierto = conciertoDAO.findByGrupoAndFecha(idGrupo, fecha);
	        if (concierto == null) {
	            throw new IncidentException("No hay concierto del grupo " + idGrupo + 
	                                       " en la fecha " + fecha, 
	                                       IncidentError.NOT_EXIST_CONCERT);
	        }
	        
	        // 5. Verificar que hay suficientes tickets disponibles
	        if (concierto.getTickets() < tickets) {
	            throw new IncidentException("Solo quedan " + concierto.getTickets() + 
	                                       " tickets, pero se solicitan " + tickets,
	                                       IncidentError.NOT_AVAILABLE_TICKETS);
	        }
	        
	        // 6. Crear la nueva compra
	        CompraDAO compraDAO = new CompraDAO(em);
	        Integer nuevoId = compraDAO.getNextId();
	        Compra compra = new Compra();
	        compra.setIdcompra(nuevoId);
	        compra.setnTickets(tickets);
	        compra.setCliente(cliente);
	        compra.setConcierto(concierto);
	        
	        // 7. Insertar la compra (persist)
	        em.persist(compra);
	        
	        // 8. Actualizar el concierto (restar tickets) - IMPORTANTE: hacer merge
	        concierto.setTickets(concierto.getTickets() - tickets);
	        em.merge(concierto);
	        
	        // 9. Commit de la transacción
	        commitTransaction(em);
	        
	    } catch (IncidentException e) {
	        if (em != null && em.getTransaction().isActive()) {
	            rollbackTransaction(em);
	        }
	        throw e;
	    } catch (Exception e) {
	        if (em != null && em.getTransaction().isActive()) {
	            rollbackTransaction(em);
	        }
	        // Loguear el error para depurar
	        e.printStackTrace();
	        throw new PersistenceException("Error en la operación comprar: " + e.getMessage(), e);
	    } finally {
	        if (em != null && em.isOpen()) {
	            close(em);
	        }
	    }
	}
	
    // ============ MÉTODO 2: DESACTIVAR ============
    
	@Override
	public void desactivar(int idGrupo) throws PersistenceException {
	    
	    EntityManager em = null;
	    
	    try {
	        em = PersistenceFactorySingleton.getEntityManager();
	        beginTransaction(em);
	        
	        GrupoDAO grupoDAO = new GrupoDAO(em);
	        Grupo grupo = grupoDAO.findById(idGrupo);
	        if (grupo == null) {
	            throw new IncidentException("Grupo con ID " + idGrupo + " no encontrado", 
	                                       IncidentError.NOT_EXIST_MUSIC_GROUP);
	        }
	        
	        // Si ya está inactivo, no hacer nada
	        if (grupo.getActivo() == 0) {
	            commitTransaction(em);
	            return;
	        }
	        
	        // PRIMERO: Eliminar compras
	        CompraDAO compraDAO = new CompraDAO(em);
	        compraDAO.deleteByGrupoId(idGrupo);
	        
	        // SEGUNDO: Eliminar conciertos
	        ConciertoDAO conciertoDAO = new ConciertoDAO(em);
	        conciertoDAO.deleteByGrupoId(idGrupo);
	        
	        // TERCERO: Desactivar el grupo
	        grupo.setActivo(0);
	        em.merge(grupo);
	        
	        commitTransaction(em);
	        
	    } catch (IncidentException e) {
	        if (em != null && em.getTransaction().isActive()) {
	            rollbackTransaction(em);
	        }
	        throw e;
	    } catch (Exception e) {
	        if (em != null && em.getTransaction().isActive()) {
	            rollbackTransaction(em);
	        }
	        e.printStackTrace();
	        throw new PersistenceException("Error en desactivar: " + e.getMessage(), e);
	    } finally {
	        if (em != null && em.isOpen()) {
	            close(em);
	        }
	    }
	}

    // ============ MÉTODO 3: CONSULTAR GRUPOS ============
    
    @Override
    public List<Grupo> consultarGrupos() throws PersistenceException {
        
        EntityManager em = null;
        
        try {
            em = PersistenceFactorySingleton.getEntityManager();
            beginTransaction(em);
            
            List<Grupo> grupos = em.createQuery(
                "SELECT DISTINCT g FROM Grupo g " +
                "LEFT JOIN FETCH g.conciertos c " +
                "LEFT JOIN FETCH c.compras cp " +
                "LEFT JOIN FETCH cp.cliente", 
                Grupo.class
            ).getResultList();
            
            commitTransaction(em);
            return grupos;
            
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                rollbackTransaction(em);
            }
            throw new PersistenceException("Error en la operación consultarGrupos: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                close(em);
            }
        }
    }
}