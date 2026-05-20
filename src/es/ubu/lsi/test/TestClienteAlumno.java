package es.ubu.lsi.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.ubu.lsi.service.conciertos.IncidentError;
import es.ubu.lsi.service.conciertos.IncidentException;
import es.ubu.lsi.service.conciertos.Service;
import es.ubu.lsi.service.conciertos.ServiceImpl;

/**
 * Tests adicionales para la practica de conciertos.
 * 
 * @author Marwan Al Haddadine, Alvaro Allyon y Rodrigo Ortiz
 */
public class TestClienteAlumno {

    private static Service service;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private static int testCount = 0;
    private static int passedCount = 0;

    public static void main(String[] args) {
        System.out.println("=== TESTS ADICIONALES ===\n");
        
        try {
            service = new ServiceImpl();
            System.out.println("Servicio inicializado.\n");
            
            testCompraConTicketsCero();
            testCompraConTicketsNegativos();
            testCompraConFechaNula();
            testCompraConNifVacio();
            testCompraConNifNull();
            testCompraConGrupoNegativo();
            testCompraConGrupoCero();
            
            testRollbackPorTicketsInsuficientes();
            testRollbackPorGrupoInexistente();
            
            testCompraMultipleMismoConcierto();
            
            testDesactivarGrupoCorrecto();
            testDesactivarGrupoYaInactivo();
            testCompraDespuesDeDesactivar();
            
            testDesactivarGrupoInexistente();
            
            System.out.println("\n=== RESULTADOS ===");
            System.out.println("Tests ejecutados: " + testCount);
            System.out.println("Tests superados: " + passedCount);
            System.out.println("Tests fallados: " + (testCount - passedCount));
            
            if (testCount == passedCount) {
                System.out.println("\nTODOS LOS TESTS SUPERADOS");
            } else {
                System.out.println("\nAlgunos tests fallaron.");
            }
            
        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                es.ubu.lsi.service.PersistenceFactorySingleton.close();
                System.out.println("\nRecursos liberados.");
            } catch (Exception e) {
                System.err.println("Error al liberar recursos: " + e.getMessage());
            }
        }
    }
    
    // Verifica que comprar lance excepcion cuando tickets = 0
    private static void testCompraConTicketsCero() {
        testCount++;
        System.out.println("Test: tickets = 0");
        try {
            Date fecha = sdf.parse("01/11/2023 21:00:00");
            service.comprar(fecha, "1111111F", 1, 0);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_AVAILABLE_TICKETS) {
                System.out.println("OK: NOT_AVAILABLE_TICKETS");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que comprar lance excepcion cuando tickets son negativos
    private static void testCompraConTicketsNegativos() {
        testCount++;
        System.out.println("Test: tickets negativos (-5)");
        try {
            Date fecha = sdf.parse("01/11/2023 21:00:00");
            service.comprar(fecha, "1111111F", 1, -5);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_AVAILABLE_TICKETS) {
                System.out.println("OK: NOT_AVAILABLE_TICKETS");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que comprar lance excepcion cuando fecha es null
    private static void testCompraConFechaNula() {
        testCount++;
        System.out.println("Test: fecha = null");
        try {
            service.comprar(null, "1111111F", 1, 3);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_EXIST_CONCERT) {
                System.out.println("OK: NOT_EXIST_CONCERT");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que comprar lance excepcion cuando nif es vacio
    private static void testCompraConNifVacio() {
        testCount++;
        System.out.println("Test: nif vacio ('')");
        try {
            Date fecha = sdf.parse("01/11/2023 21:00:00");
            service.comprar(fecha, "", 1, 3);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_EXIST_CLIENT) {
                System.out.println("OK: NOT_EXIST_CLIENT");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que comprar lance excepcion cuando nif es null
    private static void testCompraConNifNull() {
        testCount++;
        System.out.println("Test: nif = null");
        try {
            Date fecha = sdf.parse("01/11/2023 21:00:00");
            service.comprar(fecha, null, 1, 3);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_EXIST_CLIENT) {
                System.out.println("OK: NOT_EXIST_CLIENT");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que comprar lance excepcion cuando idGrupo es negativo
    private static void testCompraConGrupoNegativo() {
        testCount++;
        System.out.println("Test: grupo con ID negativo (-1)");
        try {
            Date fecha = sdf.parse("01/11/2023 21:00:00");
            service.comprar(fecha, "1111111F", -1, 3);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_EXIST_MUSIC_GROUP) {
                System.out.println("OK: NOT_EXIST_MUSIC_GROUP");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que comprar lance excepcion cuando idGrupo es 0
    private static void testCompraConGrupoCero() {
        testCount++;
        System.out.println("Test: grupo con ID = 0");
        try {
            Date fecha = sdf.parse("01/11/2023 21:00:00");
            service.comprar(fecha, "1111111F", 0, 3);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_EXIST_MUSIC_GROUP) {
                System.out.println("OK: NOT_EXIST_MUSIC_GROUP");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que se haga rollback al intentar comprar mas tickets de los disponibles
    private static void testRollbackPorTicketsInsuficientes() {
        testCount++;
        System.out.println("Test: Rollback por tickets insuficientes");
        try {
            Date fecha = sdf.parse("01/11/2023 21:00:00");
            service.comprar(fecha, "1111111F", 1, 1000);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_AVAILABLE_TICKETS) {
                System.out.println("OK: NOT_AVAILABLE_TICKETS");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que se haga rollback al intentar comprar de un grupo inexistente
    private static void testRollbackPorGrupoInexistente() {
        testCount++;
        System.out.println("Test: Rollback por grupo inexistente");
        try {
            Date fecha = sdf.parse("01/11/2023 21:00:00");
            service.comprar(fecha, "1111111F", 999, 3);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_EXIST_MUSIC_GROUP) {
                System.out.println("OK: NOT_EXIST_MUSIC_GROUP");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que desactivar un grupo inexistente lanza excepcion
    private static void testDesactivarGrupoInexistente() {
        testCount++;
        System.out.println("Test: Desactivar grupo inexistente");
        try {
            service.desactivar(999);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_EXIST_MUSIC_GROUP) {
                System.out.println("OK: NOT_EXIST_MUSIC_GROUP");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Desactiva el grupo 2 correctamente
    private static void testDesactivarGrupoCorrecto() {
        testCount++;
        System.out.println("Test: Desactivar grupo correcto (grupo 2)");
        try {
            service.desactivar(2);
            System.out.println("OK: Grupo desactivado");
            passedCount++;
        } catch (IncidentException e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getError());
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que desactivar un grupo ya inactivo no lanza excepcion
    private static void testDesactivarGrupoYaInactivo() {
        testCount++;
        System.out.println("Test: Desactivar grupo ya inactivo (grupo 2)");
        try {
            service.desactivar(2);
            service.desactivar(2);
            System.out.println("OK: No lanzo excepcion");
            passedCount++;
        } catch (IncidentException e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getError());
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Verifica que no se puede comprar de un grupo desactivado
    private static void testCompraDespuesDeDesactivar() {
        testCount++;
        System.out.println("Test: Comprar despues de desactivar grupo (grupo 2)");
        try {
            service.desactivar(2);
            Date fecha = sdf.parse("15/11/2023 22:30:00");
            service.comprar(fecha, "1111111F", 2, 5);
            System.err.println("ERROR: Deberia haber lanzado excepcion");
        } catch (IncidentException e) {
            if (e.getError() == IncidentError.NOT_EXIST_CONCERT) {
                System.out.println("OK: NOT_EXIST_CONCERT");
                passedCount++;
            } else {
                System.err.println("ERROR: Codigo incorrecto: " + e.getError());
            }
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
    
    // Realiza multiples compras del mismo concierto para verificar que resta tickets correctamente
    private static void testCompraMultipleMismoConcierto() {
        testCount++;
        System.out.println("Test: Multiples compras mismo concierto (grupo 2)");
        try {
            Date fecha = sdf.parse("01/11/2023 22:00:00");
            service.comprar(fecha, "1111111F", 2, 10);
            System.out.println("Primera compra: 10 tickets");
            service.comprar(fecha, "7352353T", 2, 5);
            System.out.println("Segunda compra: 5 tickets");
            System.out.println("OK: Compras realizadas");
            passedCount++;
        } catch (IncidentException e) {
            System.err.println("ERROR: Fallo por: " + e.getError());
        } catch (Exception e) {
            System.err.println("ERROR: Excepcion inesperada: " + e.getClass().getSimpleName());
        }
    }
}
