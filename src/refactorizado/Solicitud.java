import java.time.LocalDate;

public class Solicitud {
    private final int id;
    private final DatosSolicitud datos;
    private final Prioridad prioridad;
    private final LocalDate fecha;
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;
    private ResponsableArea responsable;

    public Solicitud(int id, DatosSolicitud datos, Prioridad prioridad, LocalDate fecha) {
        this.id = id; this.datos = datos; this.prioridad = prioridad; this.fecha = fecha;
    }
    public int id() { return id; }
    public DatosSolicitud datos() { return datos; }
    public Prioridad prioridad() { return prioridad; }
    public LocalDate fecha() { return fecha; }
    public EstadoSolicitud estado() { return estado; }
    public ResponsableArea responsable() { return responsable; }
    public void asignar(ResponsableArea nuevo) {
        if (estado == EstadoSolicitud.RESUELTA || estado == EstadoSolicitud.RECHAZADA)
            throw new IllegalStateException("No se puede asignar una solicitud terminal");
        responsable = nuevo;
        estado = EstadoSolicitud.EN_PROCESO;
    }
    public void cambiarEstado(EstadoSolicitud nuevo) {
        if (nuevo == null) throw new IllegalArgumentException("El estado es obligatorio");
        if (estado == EstadoSolicitud.RESUELTA || estado == EstadoSolicitud.RECHAZADA)
            throw new IllegalStateException("La solicitud ya está cerrada");
        if (nuevo == EstadoSolicitud.RESUELTA && responsable == null)
            throw new IllegalStateException("No se puede resolver sin responsable asignado");
        estado = nuevo;
    }
}
