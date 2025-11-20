package services;
/*
 * Implementación de la interfaz ProductoServices
 * Esta clase simula el acceso a datos (DAO) usando una lista estática en memoria.
 * */
import modelos.Producto;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Importación sugerida si se usa 'Collectors'

/**
 * Implementación de ProductoServices que utiliza una lista estática de Productos
 * cargada en memoria.
 * Es útil para pruebas o aplicaciones pequeñas sin una base de datos persistente.
 */
public class ProductosServicesImplement implements ProductoServices {

    /**
     * Devuelve una lista de productos predefinidos de forma estática.
     * En una aplicación real, este método se conectaría a una base de datos.
     * @return Una List inmutable de objetos Producto.
     */
    @Override
    public List<Producto> listar() {
        // Implementación con datos estáticos (Mock Data)
        return Arrays.asList(
                new Producto(1L, 1, LocalDate.of(2026,11,19), LocalDate.of(2025,11,19), "Leche fresca y pasteurizada.", 50, 2000.0, "telefono", "inteligencia"),
                new Producto(2L, 2, LocalDate.of(2026,5,10), LocalDate.of(2025,5,10), "Pan integral recién horneado.", 30, 1500.0, "telefono", "inteligencia"),
                new Producto(3L, 3, LocalDate.of(2027,1,5), LocalDate.of(2026,1,5), "Huevos orgánicos de gallinas libres.", 100, 3000.0, "telefono", "inteligencia")
                );
    }

    /**
     * Busca un producto por su ID en la lista estática.
     * Utiliza la API Stream de Java 8 para realizar una búsqueda eficiente.
     * @param id El ID del producto a buscar.
     * @return Un Optional<Producto> que contiene el producto si se encuentra.
     */
    @Override
    public Optional<Producto> porId(Long id) {
        // 1. Llama a listar() para obtener la lista completa de productos.
        // 2. Crea un stream para procesar los elementos.
        // 3. Filtra el stream: solo mantiene los productos cuyo ID sea igual al ID buscado.
        // 4. findAny(): Devuelve el primer elemento que coincide (envuelto en Optional)
        //    y detiene el procesamiento del stream.
        return listar().stream().filter(p -> p.getid().equals(id)).findAny();
    }
}