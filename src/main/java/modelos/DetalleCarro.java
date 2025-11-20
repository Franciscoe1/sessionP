package modelos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase que representa el detalle o contenido de un carrito de compras.
 * Administra la lista de productos (ItemCarro) y calcula los totales,
 * subtotal e IVA de la compra.
 */
public class DetalleCarro {
    // Lista que almacena los productos agregados al carrito (ItemCarro).
    private List<ItemCarro> items;

    // Definimos el IVA como una constante.
    /** Tasa del Impuesto al Valor Agregado (IVA), en este caso 15% (0.15). */
    private final double IVA_RATE = 0.15; // 15% de IVA

    /**
     * Constructor de la clase. Inicializa la lista de items del carrito
     * como un nuevo ArrayList vacío.
     */
    public DetalleCarro() {
        this.items = new ArrayList<>();
    }

    /**
     * Implementa un metodo para agregar un producto al carro.
     * Si el producto ya existe en el carrito, incrementa su cantidad;
     * de lo contrario, lo añade como un nuevo item.
     * * @param itemCarro El ItemCarro (producto y cantidad) a agregar o actualizar.
     */
    public void addItemCarro(ItemCarro itemCarro) {
        // Buscamos si el producto ya existe en el carrito usando su ID.
        // Se utiliza Stream y Optional para una búsqueda más concisa.
        Optional<ItemCarro> optionalItemCarro = items.stream()
                .filter(i -> i.getProducto().getid().equals(itemCarro.getProducto().getid()))
                .findAny();

        if (optionalItemCarro.isPresent()) {
            // Si el ItemCarro ya existe (mismo producto ID), incrementamos la cantidad.
            ItemCarro i = optionalItemCarro.get();
            i.setCantidad(i.getCantidad() + itemCarro.getCantidad());
        } else {
            // Si el ItemCarro no existe, lo añadimos como un nuevo elemento a la lista.
            this.items.add(itemCarro);
        }
    }

    /**
     * Método getter para obtener la lista completa de items en el carrito.
     * * @return La lista de ItemCarro.
     */
    public List<ItemCarro> getItem() {
        return items;
    }

    /**
     * Devuelve el total de la compra sin incluir el IVA.
     * Es el total base (suma de todos los subtotales de los ItemCarro).
     * * @return El total base de la compra.
     */
    public double getTotal() {
        // Usa Stream para iterar sobre los items, mapear cada uno a su subtotal (precio * cantidad),
        // y luego sumar todos esos subtotales.
        return items.stream().mapToDouble(ItemCarro::getSubtotal).sum();
    }

    /**
     * Calcula y devuelve el monto del IVA (15% del total base).
     * * @return El monto del IVA calculado.
     */
    public double getIva() {
        // Calcula el IVA aplicando la tasa (IVA_RATE) al total base (getTotal()).
        return this.getTotal() * IVA_RATE;
    }

    /**
     * Devuelve el total final de la compra incluyendo el IVA.
     * * @return El total final (Total base + IVA).
     */
    public double getTotalConIva() {
        // Suma el total base y el monto del IVA.
        return this.getTotal() + this.getIva();
    }
}