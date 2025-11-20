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
     * Consulta SQL para mapear una Categoria desde un ResultSet.
     * @param rs El ResultSet de la consulta.
     * @return Objeto Categoria creado a partir de la fila actual del ResultSet.
     * @throws SQLException Si ocurre un error al acceder a los datos.
     */
    private Categoria getCategoria(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setid(rs.getLong("id"));
        c.setNombre(rs.getString("nombreCategoria"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setEstado(rs.getInt("estado"));
        return c;
    }

    // --- MÉTODOS DE LA INTERFAZ REPOSITORY ---

    /**
     * {@inheritDoc}
     * Lista todas las categorías en la base de datos.
     */
    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM categoria ORDER BY id ASC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()){
                categorias.add(getCategoria(rs));
            }
        }
        return categorias;
    }

    /**
     * {@inheritDoc}
     * Busca una categoría por su ID.
     */
    @Override
    public Categoria porId(Long id) throws SQLException {
        Categoria categoria = null;
        String sql = "SELECT * FROM categoria WHERE id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    categoria = getCategoria(rs);
                }
            }
        }
        return categoria;
    }

    /**
     * {@inheritDoc}
     * Guarda (inserta o actualiza) una categoría.
     * Asume que si el ID es nulo o 0, es una nueva categoría (INSERT);
     * de lo contrario, es una categoría existente (UPDATE).
     */
    @Override
    public void guardar(Categoria categoria) throws SQLException {
        String sql;
        if (categoria.getid() != null && categoria.getid() > 0) {
            // UPDATE
            sql = "UPDATE categoria SET nombreCategoria=?, descripcion=?, estado=? WHERE id=?";
        } else {
            // INSERT
            sql = "INSERT INTO categoria (nombreCategoria, descripcion, estado) VALUES (?, ?, ?)";
        }

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setString(2, categoria.getDescripcion());
            stmt.setInt(3, categoria.getEstado());

            if (categoria.getid() != null && categoria.getid() > 0) {
                // Para UPDATE, el ID es el último parámetro
                stmt.setLong(4, categoria.getid());
            }

            stmt.executeUpdate();
        }
    }

    /**
     * {@inheritDoc}
     * Elimina una categoría por su ID.
     */
    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}