package services;

import modelos.Producto;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de Servicio para la Gestión de Productos (CRUD básico de lectura).
 * * Define el contrato que cualquier implementación debe seguir para interactuar
 * con la fuente de datos de los productos (e.g., base de datos, archivo JSON, etc.).
 * Esta interfaz asegura que el resto de la aplicación pueda obtener listas y
 * productos individuales sin preocuparse por los detalles de la persistencia.
 */
public interface ProductoServices {

    /**
     * Recupera y devuelve una lista de todos los productos disponibles.
     * * @return Una List de objetos Producto. Si no hay productos, devuelve una lista vacía.
     */
    List<Producto> listar();

    /**
     * Busca un producto específico por su identificador único (ID).
     * * Utiliza Optional para manejar de forma segura el caso en que el producto
     * no sea encontrado en la fuente de datos.
     * * @param id El Long ID del producto a buscar.
     * @return Un objeto Optional<Producto> que contendrá el Producto si existe,
     * o un Optional vacío si no se encuentra un producto con el ID dado.
     */
    Optional<Producto> porId(Long id);
}