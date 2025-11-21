package repositorio;

import modelos.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz Repository para la entidad Categoria
 * utilizando la API JDBC de Java.
 */
public class CategoriaRepositoryJdbcImplement implements Repository<Categoria>{

    // La conexión a la base de datos es inyectada (obtenida del ConexionFilter)
    private Connection conn;

    /**
     * Constructor que recibe la conexión a la base de datos.
     * @param conn La conexión JDBC.
     */

    public CategoriaRepositoryJdbcImplement(Connection conn) {
        this.conn = conn;
    }

    /**
     * Devuelve una lista de todas las categorías presentes en la base de datos.
     * @return Lista de objetos Categoria.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        try(Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from categoria")) {
            while(rs.next()){
                Categoria categoria = getClass(rs);
                categorias.add(categoria);
            }
        }
        return categorias;
    }

    @Override
    public Categoria porId(Long id) throws SQLException {
        Categoria categoria = null;
        try(PreparedStatement stmt = conn.prepareStatement("Select * from categoria where id=?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    categoria = getClass(rs);
                }
            }
        }
        return categoria;
    }

    @Override
    public void guardar(Categoria categoria) throws SQLException {
        // 1. Determinar si es INSERT o UPDATE
        String sql;
        boolean isUpdate = categoria.getId() != null && categoria.getId() > 0;

        if (isUpdate) {
            // UPDATE: Actualiza nombre y descripción basándose en el ID
            sql = "UPDATE categoria SET nombreCategoria=?, descripcion=? WHERE id=?";
        } else {
            // INSERT: Inserta un nuevo registro
            sql = "INSERT INTO categoria(nombreCategoria, descripcion) VALUES(?, ?)";
        }

        // 2. Ejecutar la sentencia SQL dentro de un try-with-resources
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // 3. Setear los parámetros
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());

            if (isUpdate) {
                // Para UPDATE, el ID es el tercer parámetro
                stmt.setLong(3, categoria.getId());
            }

            // 4. Ejecutar la actualización (INSERT o UPDATE)
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id=?";

        // Ejecutar la sentencia SQL
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Setear el parámetro (el ID a eliminar)
            stmt.setLong(1, id);

            // Ejecutar la eliminación
            stmt.executeUpdate();
        }
    }

    /**
     * Consulta SQL para mapear una Categoria desde un ResultSet.
     * @param rs El ResultSet de la consulta.
     * @return Objeto Categoria creado a partir de la fila actual del ResultSet.
     * @throws SQLException Si ocurre un error al acceder a los datos.
     */
    private Categoria getClass(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setNombre(rs.getString("nombreCategoria"));
        categoria.setDescripcion(rs.getString("descripcion"));
        categoria.setId(rs.getLong("id"));
        return categoria;
    }
}