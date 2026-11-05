public class PruebasServicio {
    private static int pasaron = 0;
    private static void verificar(boolean condicion, String nombre) {
        if (!condicion) throw new AssertionError("FALLÓ: " + nombre);
        pasaron++; System.out.println("OK: " + nombre);
    }
    private static void esperarExcepcion(Runnable accion, String nombre) {
        try { accion.run(); throw new AssertionError("FALLÓ (no lanzó excepción): " + nombre); }
        catch (IllegalArgumentException | IllegalStateException esperada) { pasaron++; System.out.println("OK: " + nombre); }
    }
    private static DatosSolicitud datos(TipoSolicitud tipo, int dias, double promedio, boolean regular, String legajo) {
        return new DatosSolicitud(legajo, "Estudiante", "e@u.edu", "Sistemas", tipo, "Prueba", regular, promedio, dias);
    }
    public static void main(String[] args) {
        var repo = new RepositorioEnMemoria();
        var servicio = new ServicioSolicitudes(repo, new CalculadorPrioridad());
        int certificado = servicio.crearSolicitud(datos(TipoSolicitud.CERTIFICADO_EXAMEN, 2, 6, true, "100"));
        verificar(repo.buscarPorId(certificado).orElseThrow().prioridad() == Prioridad.ALTA, "certificado con vencimiento en 2 días: alta");
        int certificadoMedio = servicio.crearSolicitud(datos(TipoSolicitud.CERTIFICADO_EXAMEN, 3, 6, true, "101"));
        verificar(repo.buscarPorId(certificadoMedio).orElseThrow().prioridad() == Prioridad.MEDIA, "certificado con vencimiento mayor a 2 días: media");
        int equivalenciaAlta = servicio.crearSolicitud(datos(TipoSolicitud.EQUIVALENCIA, 0, 8, true, "102"));
        verificar(repo.buscarPorId(equivalenciaAlta).orElseThrow().prioridad() == Prioridad.ALTA, "equivalencia con promedio 8 y regular: alta");
        int equivalenciaMedia = servicio.crearSolicitud(datos(TipoSolicitud.EQUIVALENCIA, 0, 9, false, "103"));
        verificar(repo.buscarPorId(equivalenciaMedia).orElseThrow().prioridad() == Prioridad.MEDIA, "equivalencia no regular: media");
        int revision = servicio.crearSolicitud(datos(TipoSolicitud.REVISION_NOTA, 0, 0, false, "104"));
        verificar(repo.buscarPorId(revision).orElseThrow().prioridad() == Prioridad.ALTA, "revisión de nota: alta");
        int baja = servicio.crearSolicitud(datos(TipoSolicitud.BAJA_MATERIA, 0, 0, false, "105"));
        verificar(repo.buscarPorId(baja).orElseThrow().prioridad() == Prioridad.BAJA, "baja de materia: baja");
        verificar(repo.buscarPorId(certificado).orElseThrow().estado() == EstadoSolicitud.PENDIENTE, "nueva solicitud inicia pendiente");
        verificar(!servicio.cambiarEstado(999, EstadoSolicitud.RESUELTA), "id inexistente no cambia estado");
        esperarExcepcion(() -> servicio.cambiarEstado(certificado, EstadoSolicitud.RESUELTA), "no resolver sin responsable");
        verificar(servicio.asignarResponsable(certificado, new ResponsableArea("Ana", "Alumnos")), "asignar responsable existente");
        verificar(repo.buscarPorId(certificado).orElseThrow().estado() == EstadoSolicitud.EN_PROCESO, "asignar responsable pasa a en proceso");
        verificar(servicio.cambiarEstado(certificado, EstadoSolicitud.RESUELTA), "resolver con responsable");
        esperarExcepcion(() -> servicio.asignarResponsable(certificado, new ResponsableArea("Luis", "Alumnos")), "no reasignar solicitud resuelta");
        verificar(servicio.buscar(TipoSolicitud.EQUIVALENCIA, null, null).size() == 2, "filtro por tipo");
        verificar(servicio.buscar(null, null, "105").size() == 1, "filtro por legajo");
        verificar(servicio.buscar(null, EstadoSolicitud.PENDIENTE, null).size() == 5, "filtro por estado");
        verificar(repo.listar().size() == 6, "repositorio mantiene seis solicitudes");
        String reporte = servicio.generarReporte();
        verificar(reporte.contains("Total: 6") && reporte.contains("Resueltas: 1"), "reporte contiene totales y estados");
        esperarExcepcion(() -> datos(TipoSolicitud.BAJA_MATERIA, 0, 0, false, " "), "rechazar legajo vacío");
        System.out.println("RESULTADO: " + pasaron + " pruebas superadas.");
    }
}
