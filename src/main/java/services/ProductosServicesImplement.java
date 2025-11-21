package services;

/*
 * Implementación de la interfaz ProductoServices
 * */
import modelos.Producto;
import modelos.Categoria;
import repositorio.Repository;
import repositorio.CategoriaRepositoryJdbcImplement;
import repositorio.ProductoRepositoryJdbcImplment;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.sql.SQLException;

public class ProductosServicesImplement implements ProductoServices {

    // --- Implementación de los métodos de Producto ---

    /**
     * Devuelve una lista de productos predefinidos de forma estática.
     */
    @Override
    public List<Producto> listar() {
        // Implementación con datos estáticos (Mock Data)
        return Arrays.asList(
                new Producto(1L, 1, LocalDate.of(2026,11,19), LocalDate.of(2025,11,19), "Leche fresca y pasteurizada.", 50, 2000.0, "Leche", "Lacteos"),
                new Producto(2L, 2, LocalDate.of(2026,5,10), LocalDate.of(2025,5,10), "Pan integral recién horneado.", 30, 1500.0, "Pan", "Panaderia"),
                new Producto(3L, 3, LocalDate.of(2027,1,5), LocalDate.of(2026,1,5), "Huevos orgánicos de gallinas libres.", 100, 3000.0, "Huevos", "Proteinas")
        );
    }

    /**
     * Busca un producto por su ID en la lista estática.
     */
    @Override
    public Optional<Producto> porId(Long id) {
        return listar().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    /**
     * Implementación Mock para guardar un producto.
     */
    @Override
    public void guardar(Producto producto) {
        // Implementación Mock: En una implementación real, se guardaría en la BDD.
        System.out.println("Guardando producto: " + producto.getNombre() + " (Mock)");
    }

    /**
     * Implementación Mock para eliminar un producto.
     */
    @Override
    public void eliminar(Long id) {
        // Implementación Mock: En una implementación real, se eliminaría de la BDD.
        System.out.println("Eliminando producto con ID: " + id + " (Mock)");
    }

    // --- Implementación de los métodos de Categoría ---

    /**
     * Devuelve una lista de categorías predefinidas.
     */
    @Override
    public List<Categoria> listarCategoria() {
        // Implementación Mock: Devuelve una lista de Categorías estáticas
        return Arrays.asList(
                new Categoria(1L, 1, "Lácteos", "Bebidas y Lácteos"),
                new Categoria(2L, 1, "Panadería", "Cereales y Panadería"),
                new Categoria(3L, 1, "Huevos", "Proteínas y Huevos")
        );
    }

    /**
     * Implementación del método faltante. Busca una categoría por ID.
     * @param id El ID de la categoría a buscar.
     * @return Un Optional<Categoria> con la categoría si existe.
     */
    @Override
    public Optional<Categoria> getCategoria(Long id) {
        // Implementación Mock: Busca la categoría en la lista estática.
        return listarCategoria().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    /**
     * Implementación del segundo método de búsqueda por ID (delegando a getCategoria).
     * @param id El ID de la categoría a buscar.
     * @return Un Optional<Categoria> con la categoría si existe.
     */
    @Override
    public Optional<Categoria> porIdCategoria(Long id) {
        // Reutilizamos la lógica del método getCategoria, ya que ambos buscan por ID.
        return getCategoria(id);
    }
}