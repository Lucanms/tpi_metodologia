# Checklist de Code Review - TPI Metodología de Sistemas II

## Criterios Evaluados

### 1. Nombres y Legibilidad
- [x] ¿Los nombres de clases, métodos y variables son descriptivos y pronunciables?
- [x] ¿Se eliminaron abreviaturas confusas (ej. `legajoEst`, `tipoSol`)?
- [x] ¿El código se lee como un lenguaje natural?

### 2. Funciones y Métodos
- [x] ¿Los métodos realizan una sola cosa (SRP)?
- [x] ¿Los métodos tienen pocos parámetros (idealmente menos de 3)?
- [x] ¿Se eliminaron los números y strings mágicos (reemplazados por Enums)?

### 3. Manejo de Errores
- [x] ¿Se eliminaron los `System.out.println("Error...")` y los retornos `-1`?
- [x] ¿Se utilizan excepciones del dominio, `IllegalArgumentException` y `IllegalStateException`?
- [x] ¿Las excepciones se lanzan en el momento y lugar correctos?

### 4. Diseño y Acoplamiento (SOLID)
- [x] ¿La lógica de negocio está separada de la persistencia?
- [x] ¿Se respeta la Inversión de Dependencias usando interfaces?
- [x] ¿Se eliminaron las "God Class"?

### 5. Pruebas
- [x] ¿Se escribieron pruebas unitarias para las reglas de negocio críticas?
- [x] ¿Las pruebas se ejecutan correctamente y sin errores?