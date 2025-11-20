package services;

import modelos.Producto;
import repositorio.ProductoRepositoryJdbcImplment;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoServiceJdbcImpl implements ProductoServices {
    //Declaramos una variable de tipo ProductoRepositoryJdbcImplemen
    private ProductoRepositoryJdbcImplment repositoryJdbc;
    //Implementamos un constuctor paa traer la conexion
    public ProductoServiceJdbcImpl(Connection connection) {
        this.repositoryJdbc = new ProductoRepositoryJdbcImplment(connection);
    }
    @Override
    public List<Producto> listar() {
        try{
            return repositoryJdbc.listar();
        }catch(SQLException throwables){
            throw new Exception(throwables.getMessage(), throwables.getCause());
        }
    }

    @Override
    public Optional<Producto> porId(Long id) {
        try{
            return Optional.ofNullable(repositoryJdbc.porId(id));
        }catch (SQLException throwables){
            throw new Exception(throwables.getMessage(), throwables.getCause());

        }
    }
}
