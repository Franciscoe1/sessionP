package modelos;

/*
 * Clase modelo Producto
 * Representa un artículo que puede ser vendido y añadido a un carrito de compras.
 */
import java.util.Objects;

public class Producto {

    // --- Atributos de la clase Producto ---

    // Identificador único del producto (clave primaria).
    private Long id;
    // Nombre descriptivo del producto.
    private String nombre;
    // Categoría o tipo al que pertenece el producto (e.g., "Electrónica", "Ropa").
    private String tipo;
    // Precio de venta unitario del producto.
    private double precio;

    // --- Constructores ---

    /**
     * Constructor vacío por defecto.
     * Útil para frameworks que requieren un constructor sin argumentos (e.g., JPA, Spring).
     */
    public Producto() {
    }

    /**
     * Constructor para inicializar todos los atributos del producto.
     * * @param id El identificador único del producto.
     * @param nombre El nombre del producto.
     * @param tipo El tipo o categoría del producto.
     * @param precio El precio de venta del producto.
     */
    public Producto(Long id, String nombre, String tipo, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
    }

    // --- Métodos Getters ---

    /**
     * Obtiene el identificador único del producto.
     * @return El ID del producto.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtiene el nombre del producto.
     * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el tipo o categoría del producto.
     * @return El tipo.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene el precio de venta unitario del producto.
     * @return El precio.
     */
    public double getPrecio() {
        return precio;
    }

    // --- Métodos Setters ---
    // (Opcionales, pero incluidos por completitud para modificar el estado del objeto)

    /**
     * Establece el identificador único del producto.
     * @param id El nuevo ID.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Establece el nombre del producto.
     * @param nombre El nuevo nombre.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece el tipo o categoría del producto.
     * @param tipo El nuevo tipo.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Establece el precio de venta unitario del producto.
     * @param precio El nuevo precio.
     */
    public void setPrecio(double precio) { // Corregido tipo a double
        this.precio = precio;
    }

    // --- Métodos de Comparación (equals y hashCode) ---

    /**
     * Define la lógica de igualdad entre dos objetos Producto.
     * Dos productos se consideran iguales si tienen el mismo ID.
     * @param o El objeto con el que comparar.
     * @return true si los IDs son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Verifica nulidad y si la clase es la misma
        if (o == null || getClass() != o.getClass()) return false;

        Producto producto = (Producto) o;
        // La igualdad se basa exclusivamente en el ID
        return Objects.equals(id, producto.id);
    }

    /**
     * Genera un código hash para el objeto. Es esencial redefinirlo
     * junto con equals() para el correcto funcionamiento en colecciones
     * basadas en hash (como HashMap o HashSet).
     * El hash se basa **únicamente en el ID**.
     * @return El código hash basado en el ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}