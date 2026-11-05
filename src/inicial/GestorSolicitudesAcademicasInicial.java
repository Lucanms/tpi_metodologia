import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorSolicitudesAcademicasInicial {

    // Persistencia mediante mapas sin tipado
    private List<Map<String, Object>> dbSolicitudes = new ArrayList<>();
    private int contadorId = 1;

    // Operación 1: Registrar solicitud 
    public int crearSolicitud(String legajoEst, String nombreEst, String emailEst, String carreraEst,
                              String tipoSol, String descripcion, boolean esRegular,
                              double promedioEst, int diasVencimiento) {
        if (legajoEst == null || legajoEst.isEmpty() || nombreEst == null || nombreEst.isEmpty()
                || tipoSol == null || tipoSol.isEmpty()) {
            System.out.println("Error: Datos vacíos");
            return -1;
        }

        // Cálculo de prioridad 
        int prioridad = 3;
        if (tipoSol.equals("CERTIFICADO_EXAMEN")) {
            if (diasVencimiento <= 2) {
                prioridad = 1;
            } else {
                prioridad = 2;
            }
        } else if (tipoSol.equals("EQUIVALENCIA")) {
            if (promedioEst >= 8.0 && esRegular) {
                prioridad = 1;
            } else {
                prioridad = 2;
            }
        } else if (tipoSol.equals("REVISION_NOTA")) {
            prioridad = 1;
        } else if (tipoSol.equals("BAJA_MATERIA")) {
            prioridad = 3;
        } else {
            System.out.println("Error: Tipo no válido");
            return -1;
        }

        Map<String, Object> nueva = new HashMap<>();
        nueva.put("id", contadorId);
        nueva.put("legajo", legajoEst);
        nueva.put("nombreEst", nombreEst);
        nueva.put("emailEst", emailEst);
        nueva.put("carrera", carreraEst);
        nueva.put("tipo", tipoSol);
        nueva.put("desc", descripcion);
        nueva.put("regular", esRegular);
        nueva.put("promedio", promedioEst);
        nueva.put("prioridad", prioridad);
        
        // Estados 0 = Pendiente, 1 = En Proceso, 2 = Resuelta, 3 = Rechazada
        nueva.put("estado", 0);
        nueva.put("respNombre", "SIN_ASIGNAR");
        nueva.put("respArea", "NINGUNA");
        nueva.put("fecha", LocalDate.now().toString());

        dbSolicitudes.add(nueva);
        contadorId++;
        return (int) nueva.get("id");
    }

    // Operación 2: Asignar responsable
    public boolean asignarResponsable(int idSol, String nombreResp, String areaResp) {
        for (Map<String, Object> s : dbSolicitudes) {
            if ((int) s.get("id") == idSol) {
                int estadoActual = (int) s.get("estado");
                if (estadoActual == 2 || estadoActual == 3) {
                    return false;
                }
                s.put("respNombre", nombreResp);
                s.put("respArea", areaResp);
                s.put("estado", 1);
                return true;
            }
        }
        return false;
    }

    // Operación 3: Cambiar estado
    public boolean cambiarEstado(int idSol, int nuevoEstado) {
        for (Map<String, Object> s : dbSolicitudes) {
            if ((int) s.get("id") == idSol) {
                // No puede pasar a resuelta (2) si no tiene responsable asignado
                if (nuevoEstado == 2 && s.get("respNombre").equals("SIN_ASIGNAR")) {
                    return false;
                }
                s.put("estado", nuevoEstado);
                return true;
            }
        }
        return false;
    }

    // Operación 4: Buscar y filtrar solicitudes
    public List<Map<String, Object>> buscar(String filtroTipo, Integer filtroEstado, String filtroLegajo) {
        List<Map<String, Object>> res = new ArrayList<>();
        for (Map<String, Object> s : dbSolicitudes) {
            boolean cumple = true;
            if (filtroTipo != null && !s.get("tipo").equals(filtroTipo)) {
                cumple = false;
            }
            if (filtroEstado != null && (int) s.get("estado") != filtroEstado) {
                cumple = false;
            }
            if (filtroLegajo != null && !s.get("legajo").equals(filtroLegajo)) {
                cumple = false;
            }
            if (cumple) {
                res.add(s);
            }
        }
        return res;
    }

    // Operación 5: Generar reporte básico
    public String generarReporte() {
    int total = dbSolicitudes.size();
    int pend = 0, proc = 0, res = 0, rech = 0, alta = 0;

    for (Map<String, Object> s : dbSolicitudes) {
        int est = (int) s.get("estado");
        int prio = (int) s.get("prioridad");
        if (est == 0) pend++;
        else if (est == 1) proc++;
        else if (est == 2) res++;
        else if (est == 3) rech++;

        if (prio == 1) alta++;
    }

    return "=== REPORTE DE SOLICITUDES ===\n"
            + "Total: " + total + " | Alta Prioridad: " + alta + "\n"
            + "Pendientes: " + pend + " | En Proceso: " + proc
            + " | Resueltas: " + res + " | Rechazadas: " + rech;
    }
}
