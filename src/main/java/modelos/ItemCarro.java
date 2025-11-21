package modelos;

import java.util.Objects;

/**
 * Clase que representa un ítem individual dentro de un carrito de compras.
 * Combina un Producto específico con la cantidad deseada de ese producto.
 */
public class ItemCarro {
    // La cantidad de unidades del producto que se desean comprar.
    private int cantidad;

    // El objeto Producto asociado a este ítem del carrito.
    private Producto producto;

    /**
     * Constructor para crear un ItemCarro.
     * @param cantidad La cantidad de unidades del producto.
     * @param producto El Producto que se está agregando al carrito.
     */
    public ItemCarro(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    // --- Métodos Getters y Setters ---

    /**
     * Obtiene la cantidad de unidades de este producto.
     * @return La cantidad.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece una nueva cantidad de unidades para este producto.
     * @param cantidad La nueva cantidad.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el objeto Producto asociado a este ítem.
     * @return El Producto.
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece un nuevo Producto para este ítem.
     * @param producto El nuevo Producto.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // --- Métodos de Comparación y Subtotal ---

    /**
     * Metodo para comparar si ya un producto está en la lista del carrito.
     * La comparación se realiza **SÓLO por el ID del Producto**. Esto es clave
     * para que la clase `DetalleCarro` sepa si debe añadir un nuevo ítem o
     * solo incrementar la cantidad de un ítem existente.
     *
     * @param o El objeto con el que se va a comparar (debe ser otro ItemCarro).
     * @return true si los ID de los Productos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        // 1. Verificación de identidad: si son el mismo objeto en memoria, son iguales.
        if (this == o) return true;
        // 2. Verificación de nulidad y clase: si el objeto es nulo o de otra clase, no son iguales.
        if (o == null || getClass() != o.getClass()) return false;

        // Conversión segura al tipo ItemCarro.
        ItemCarro itemCarro = (ItemCarro) o;

        // Comparamos si el ID del producto es el mismo.
        // Se utiliza Objects.equals() para manejar posibles IDs nulos de forma segura.
        return Objects.equals(producto.getId(), itemCarro.producto.getId());
    }

    /**
     * Genera el código hash para el objeto. Es necesario redefinir hashCode()
     * para mantener la consistencia con el método equals(), especialmente cuando
     * se usan colecciones basadas en hash (como HashMap o HashSet).
     * El hash se basa **únicamente en el ID del Producto**.
     *
     * @return El código hash basado en el ID del Producto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(producto.getId());
    }

    /**
     * Calcula y devuelve el subtotal de este ítem (cantidad * precio unitario).
     * @return El subtotal del ítem.
     */
    public double getSubtotal() {
        // Multiplica la cantidad por el precio unitario del producto asociado.
        return cantidad * producto.getPrecio();
    }
}