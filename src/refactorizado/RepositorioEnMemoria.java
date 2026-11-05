import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class RepositorioEnMemoria implements RepositorioSolicitudes {
    private final List<Solicitud> solicitudes = new ArrayList<>();
    public void guardar(Solicitud solicitud) { solicitudes.add(solicitud); }
    public Optional<Solicitud> buscarPorId(int id) {
        return solicitudes.stream().filter(s -> s.id() == id).findFirst();
    }
    public List<Solicitud> listar() { return List.copyOf(solicitudes); }
}
