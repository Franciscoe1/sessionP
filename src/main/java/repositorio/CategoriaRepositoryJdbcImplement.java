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

    }

    @Override
    public void eliminar(Long id) throws SQLException {

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