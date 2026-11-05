import java.time.LocalDate;
public record SolicitudVista(int id, String legajo, String nombreEstudiante, String email,
                             String carrera, TipoSolicitud tipo, String descripcion,
                             boolean regular, double promedio, Prioridad prioridad,
                             EstadoSolicitud estado, String responsable, String area,
                             LocalDate fecha) {
    public static SolicitudVista desde(Solicitud s) {
        ResponsableArea r = s.responsable();
        return new SolicitudVista(s.id(), s.datos().legajo(), s.datos().nombre(), s.datos().email(),
            s.datos().carrera(), s.datos().tipo(), s.datos().descripcion(), s.datos().regular(),
            s.datos().promedio(), s.prioridad(), s.estado(), r == null ? "SIN_ASIGNAR" : r.nombre(),
            r == null ? "NINGUNA" : r.area(), s.fecha());
    }
}
