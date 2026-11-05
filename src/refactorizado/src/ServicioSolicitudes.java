import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
public class ServicioSolicitudes {
    private final RepositorioSolicitudes repositorio;
    private final ReglaPrioridad reglaPrioridad;
    private final AtomicInteger siguienteId = new AtomicInteger(1);
    public ServicioSolicitudes(RepositorioSolicitudes repositorio, ReglaPrioridad reglaPrioridad) {
        this.repositorio = repositorio; this.reglaPrioridad = reglaPrioridad;
    }
    public int crearSolicitud(DatosSolicitud datos) {
        Prioridad prioridad = reglaPrioridad.calcular(datos);
        int id = siguienteId.getAndIncrement();
        repositorio.guardar(new Solicitud(id, datos, prioridad, LocalDate.now()));
        return id;
    }
    public boolean asignarResponsable(int id, ResponsableArea responsable) {
        return repositorio.buscarPorId(id).map(s -> { s.asignar(responsable); return true; }).orElse(false);
    }
    public boolean cambiarEstado(int id, EstadoSolicitud estado) {
        return repositorio.buscarPorId(id).map(s -> { s.cambiarEstado(estado); return true; }).orElse(false);
    }
    public List<SolicitudVista> buscar(TipoSolicitud tipo, EstadoSolicitud estado, String legajo) {
        return repositorio.listar().stream().filter(s -> tipo == null || s.datos().tipo() == tipo)
            .filter(s -> estado == null || s.estado() == estado)
            .filter(s -> legajo == null || legajo.equals(s.datos().legajo()))
            .map(SolicitudVista::desde).collect(Collectors.toUnmodifiableList());
    }
    public String generarReporte() {
        List<Solicitud> todas = repositorio.listar();
        long alta = todas.stream().filter(s -> s.prioridad() == Prioridad.ALTA).count();
        long pendientes = contar(todas, EstadoSolicitud.PENDIENTE);
        long proceso = contar(todas, EstadoSolicitud.EN_PROCESO);
        long resueltas = contar(todas, EstadoSolicitud.RESUELTA);
        long rechazadas = contar(todas, EstadoSolicitud.RECHAZADA);
        return "=== REPORTE DE SOLICITUDES ===\nTotal: " + todas.size() + " | Alta Prioridad: " + alta + "\n"
            + "Pendientes: " + pendientes + " | En Proceso: " + proceso + " | Resueltas: " + resueltas + " | Rechazadas: " + rechazadas;
    }
    private long contar(List<Solicitud> solicitudes, EstadoSolicitud estado) {
        return solicitudes.stream().filter(s -> s.estado() == estado).count();
    }
}
