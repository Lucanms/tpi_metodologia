public record ResponsableArea(String nombre, String area) {
    public ResponsableArea {
        if (nombre == null || nombre.isBlank() || area == null || area.isBlank())
            throw new IllegalArgumentException("Nombre y área del responsable son obligatorios");
    }
}
