package repositorio;

import modelos.Categoria;
import modelos.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryJdbcImplment implements Repository<Producto> {

    //Obtener la conexion a la BBDD
    private Connection conn;

    //Obtengo mi conexión mediante el constructor
    public ProductoRepositoryJdbcImplment(Connection conn) {
        this.conn = conn;
    }

    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select p.* , c.nombreCategoria as categoria FROM producto as p " +
                     " INNER JOIN categoria as c ON (p.idCategoria=c.id) order by p.id ASC")) {
            while(rs.next()){
                Producto p = getProducto(rs);
                productos.add(p);
            }
        }
        return productos;
    }


    //Implementamos un método para buescar un registro por ID
    @Override
    public Producto porId(Long id) throws SQLException {
        Producto producto = null;
        try(PreparedStatement stmt = conn.prepareStatement("select p.*, c.nombreCategoria as categoria from producto as p " +
                " inner join categoria as c ON (p.idCategoria = c.id)where p.id=?")) {
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
        if(producto.getid() != null && producto.getid() > 0){
            sql = "update producto set nombreCategoria=?, idCategoria=?, stock=?, precio=?, descripcion=?, codigo=? " +
                    " fecha_elaboracion=?, fecha_caducidad=? where id=?";
        }else{
            sql="insert into producto (nombreCategoria, idCategoria, stock, precio, descripcion, codigo, fecha_elaboracion, fecha_caducida, condicion) " +
                    " values (?,?,?,?,?,?,?,?,1)";
        }
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, producto.getNombre());
            stmt.setInt(2, producto.getStock());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setString(4, producto.getDescripcion());
            stmt.setInt(5, producto.getCondicion());
            stmt.setLong(6, producto.getCategoria().getid());

            if(producto.getid()!=null && producto.getid() > 0){
                stmt.setLong(7, producto.getid());
            }else{
                stmt.setDate(7, Date.valueOf(producto.getFechaElaboracion()));
                stmt.setDate(8, Date.valueOf(producto.getFechaCaducidad()));
            }
            stmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(Long id) throws SQLException {
        String sql = "delete from producto where id=?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
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
        //Creamos un nuevo objeto de tipo categoria
        Categoria c = new Categoria();
        c.setid(rs.getLong("id"));
        c.setNombre(rs.getString("categoria"));
        p.setCategoria(c);
        return p;
    }
}
