# Sistema de Gestión de Solicitudes Académicas — versión refactorizada

Requisitos: JDK 17 o superior. No requiere dependencias externas.

## Compilar
```bash
javac -encoding UTF-8 -d out src/*.java test/*.java
```

## Ejecutar pruebas
```bash
java -cp out PruebasServicio
```

Resultado verificado: 19 pruebas superadas.

## Diseño
- `ServicioSolicitudes`: coordinación de casos de uso.
- `Solicitud`: reglas de estado y asignación.
- `DatosSolicitud`: objeto parámetro inmutable.
- `ReglaPrioridad` / `CalculadorPrioridad`: política de prioridad.
- `RepositorioSolicitudes` / `RepositorioEnMemoria`: persistencia en memoria.
- Enums para tipo, estado y prioridad.
- `SolicitudVista`: resultado de búsqueda de solo lectura.

## Limitaciones
Persistencia en memoria; IDs locales a la instancia del servicio. La revisión cruzada humana, el repositorio remoto y las pruebas de caracterización contra la clase inicial deben completarse por el equipo.
