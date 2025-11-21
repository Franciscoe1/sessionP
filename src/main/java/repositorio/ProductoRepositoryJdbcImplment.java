package repositorio;

import modelos.Categoria;
import modelos.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryJdbcImplment implements Repository<Producto> {

    // Obtener la conexión a la BBDD
    private Connection conn;

    // Obtengo mi conexión mediante el constructor
    public ProductoRepositoryJdbcImplment(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT p.*, c.nombreCategoria AS categoria " +
                             "FROM producto AS p " +
                             "INNER JOIN categoria AS c ON (p.idCategoria = c.id) " +
                             "ORDER BY p.id ASC")) {

            while (rs.next()) {
                Producto p = getProducto(rs);
                productos.add(p);
            }
        }
        return productos;
    }

    // Implementamos un método para buscar un registro por ID
    @Override
    public Producto porId(Long id) throws SQLException {
        Producto producto = null;
        String sql = "SELECT p.*, c.nombreCategoria AS categoria " +
                "FROM producto AS p " +
                "INNER JOIN categoria AS c ON (p.idCategoria = c.id) " +
                "WHERE p.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    producto = getProducto(rs);
                }
            }
        }
        return producto;
    }

    @Override
    public void guardar(Producto producto) throws SQLException {
        String sql;
        boolean isUpdate = producto.getId() != null && producto.getId() > 0;

        if (isUpdate) {

            sql = "UPDATE producto SET nombreProducto = ?, idCategoria = ?, stock = ?, " +
                    "precio = ?, descripcion = ?, fecha_elaboracion = ?, fecha_caducidad = ?, condicion = ? " +
                    "WHERE id = ?";
        } else {

            sql = "INSERT INTO producto (nombreProducto, idCategoria, stock, precio, descripcion, " +
                    "fecha_elaboracion, fecha_caducidad, condicion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Orden de parámetros para ambos casos
            stmt.setString(1, producto.getNombre());
            stmt.setLong(2, producto.getCategoria().getId());
            stmt.setInt(3, producto.getStock());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setString(5, producto.getDescripcion());
            stmt.setDate(6, Date.valueOf(producto.getFechaElaboracion()));
            stmt.setDate(7, Date.valueOf(producto.getFechaCaducidad()));
            stmt.setInt(8, producto.getCondicion());

            if (isUpdate) {
                // en UPDATE el 9° parámetro es el id
                stmt.setLong(9, producto.getId());
            }

            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private static Producto getProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getLong("id"));


        p.setNombre(rs.getString("nombreProducto"));

        p.setStock(rs.getInt("stock"));
        p.setPrecio(rs.getDouble("precio"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setFechaElaboracion(rs.getDate("fecha_elaboracion").toLocalDate());
        p.setFechaCaducidad(rs.getDate("fecha_caducidad").toLocalDate());
        p.setCondicion(rs.getInt("condicion"));

        // Creamos un nuevo objeto de tipo Categoria
        Categoria c = new Categoria();
        c.setId(rs.getLong("idCategoria"));
        c.setNombre(rs.getString("categoria"));
        p.setCategoria(c);

        return p;
    }
}
