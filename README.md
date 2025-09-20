# Face SDK Demo App

Aplicación Android de ejemplo que integra **Regula FaceSDK** para realizar comparación de rostros.

---

## Funcionalidades
- Captura facial desde la cámara  
- Selección de imagen desde galería  
- Comparación de similitud entre dos imágenes  
- Visualización del porcentaje de similitud en una interfaz amigable  
- Reinicio del flujo para realizar nuevas pruebas  

---

## Arquitectura y Patrones

La aplicación sigue principios de **Clean Architecture** y **SOLID**:

### Capas
1. Presentation Layer → `FaceCompareScreen` implementado con Jetpack Compose  
2. Domain Layer → `CompareFacesUseCase` (lógica de negocio)  
3. Data Layer → `FaceComparisonRepository` (interacción con FaceSDK)  

### Patrones aplicados
- Dependency Inversion → la UI depende de casos de uso, no directamente del SDK  
- Single Responsibility Principle (SRP) → cada clase tiene una única responsabilidad  
- Activity Result API → manejo moderno de permisos, cámara y galería sin usar APIs obsoletas  

---

## Decisiones Técnicas
1. Uso de Jetpack Compose para una UI moderna y declarativa  
2. FileProvider para URIs seguros en Android 7+  
3. Almacenamiento interno → las fotos no aparecen en la galería del dispositivo  
4. Conversión segura de tipos → `similarity.toFloat() * 100` evita errores de tipado en el SDK  

---

## Posibles Mejoras
- Agregar pruebas unitarias para UseCase y Repository con mocks del SDK  
- Integrar un framework de inyección de dependencias como Hilt o Koin  
- Soporte para comparar múltiples rostros o generar una base de datos de caras  
- Configuración para guardar o no las fotos en la galería  
- Mejorar la UI con animaciones, loaders y manejo de errores detallado  
