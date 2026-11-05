public enum Prioridad {
    ALTA(1), MEDIA(2), BAJA(3);
    private final int codigo;
    Prioridad(int codigo) { this.codigo = codigo; }
    public int codigo() { return codigo; }
}
