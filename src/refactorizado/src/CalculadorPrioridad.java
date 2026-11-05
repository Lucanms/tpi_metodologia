public class CalculadorPrioridad implements ReglaPrioridad {
    @Override public Prioridad calcular(DatosSolicitud datos) {
        return switch (datos.tipo()) {
            case CERTIFICADO_EXAMEN -> datos.diasVencimiento() <= 2 ? Prioridad.ALTA : Prioridad.MEDIA;
            case EQUIVALENCIA -> datos.promedio() >= 8.0 && datos.regular() ? Prioridad.ALTA : Prioridad.MEDIA;
            case REVISION_NOTA -> Prioridad.ALTA;
            case BAJA_MATERIA -> Prioridad.BAJA;
        };
    }
}
