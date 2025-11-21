package modelos;

import java.time.LocalDate;

public class Producto {
    private Long id;
    private String nombre;
    // implementamos las variables de la base de datos
    private Categoria categoria;
    private double precio;
    // declaramos la variable stock
    private int stock;
    // Cambiamos la variable tipo por descripcion
    private String descripcion;
    // declaramos las variables fecha de elaboración, caducidad y condición
    private LocalDate fechaElaboracion;
    private LocalDate fechaCaducidad;
    private int condicion;

    public Producto() {
    }

    // Modificamos el constructor con las variables añadidas
    public Producto(Long id, int condicion, LocalDate fechaCaducidad, LocalDate fechaElaboracion,
                    String descripcion, int stock, double precio, String nombre, String tipo) {
        this.id = id;
        this.condicion = condicion;
        // Instanciamos un objeto de tipo Categoria
        Categoria categoria = new Categoria();
        categoria.setNombre(tipo);
        this.categoria = categoria; // Asumiendo que quieres asignar la nueva categoria a this.categoria
        this.fechaCaducidad = fechaCaducidad;
        this.fechaElaboracion = fechaElaboracion;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;

        this.nombre = nombre;
    }

    // Implementamos los métodos setter and getter
    // de las valables añadidas
public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public int getCondicion() {
    return condicion;
}

public void setCondicion(int condicion) {
    this.condicion = condicion;
}

public LocalDate getFechaCaducidad() {
    return fechaCaducidad;
}

public void setFechaCaducidad(LocalDate fechaCaducidad) {
    this.fechaCaducidad = fechaCaducidad;
}

public LocalDate getFechaElaboracion() {
    return fechaElaboracion;
}

public void setFechaElaboracion(LocalDate fechaElaboracion) {
    this.fechaElaboracion = fechaElaboracion;
}

public String getDescripcion() {
    return descripcion;
}

public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
}

public int getStock() {
    return stock;
}

public void setStock(int stock) {
    this.stock = stock;
}

public double getPrecio() {
    return precio;
}
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}