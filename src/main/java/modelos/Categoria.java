package modelos;

public class Categoria {
    private Long id;
    private String nombre;
    private String descripcion;
    private int estado;

    // Implementamos el constructor vacio
    public Categoria() {
    }

    public Categoria(Long id, int estado, String descripcion, String nombre) {
        this.id = id;
        this.estado = estado;
        this.descripcion = descripcion;
        this.nombre = nombre;
    }

    // Implementamos los métodos Getter and setter
    public Long getid() {
        return id;
    }

    public void setid(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}