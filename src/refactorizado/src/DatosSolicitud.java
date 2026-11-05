public record DatosSolicitud(String legajo, String nombre, String email, String carrera,
                             TipoSolicitud tipo, String descripcion, boolean regular,
                             double promedio, int diasVencimiento) {
    public DatosSolicitud {
        if (legajo == null || legajo.isBlank()) throw new IllegalArgumentException("El legajo es obligatorio");
        if (nombre == null || nombre.isBlank()) throw new IllegalArgumentException("El nombre es obligatorio");
        if (tipo == null) throw new IllegalArgumentException("El tipo de solicitud es obligatorio");
    }
}
