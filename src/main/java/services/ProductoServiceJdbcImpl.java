package services;

import modelos.Categoria;
import modelos.Producto;
import repositorio.ProductoRepositoryJdbcImplment;
import repositorio.CategoriaRepositoryJdbcImplement;
import repositorio.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductoServiceJdbcImpl implements ProductoServices {

    // Repositorios JDBC
    private Repository<Producto> repositoryJdbc;
    private Repository<Categoria> repositoryCategoriaJdbc;

    public ProductoServiceJdbcImpl(Connection connection) {
        this.repositoryJdbc = new ProductoRepositoryJdbcImplment(connection);
        this.repositoryCategoriaJdbc = new CategoriaRepositoryJdbcImplement(connection);
    }

    @Override
    public List<Producto> listar() {
        try {
            return repositoryJdbc.listar();
        } catch (SQLException e) {
            throw new ServicesException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Producto> porId(Long id) {
        try {
            return Optional.ofNullable(repositoryJdbc.porId(id));
        } catch (SQLException e) {
            throw new ServicesException(e.getMessage(), e);
        }
    }

    @Override
    public void guardar(Producto producto) {
        try {
            repositoryJdbc.guardar(producto);
        } catch (SQLException e) {
            throw new ServicesException(e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            repositoryJdbc.eliminar(id);
        } catch (SQLException e) {
            throw new ServicesException(e.getMessage(), e);
        }
    }


    @Override
    public List<Categoria> listarCategoria() {
        try {
            return repositoryCategoriaJdbc.listar();
        } catch (SQLException e) {
            throw new ServicesException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Categoria> getCategoria(Long id) {
        try {
            return Optional.ofNullable(repositoryCategoriaJdbc.porId(id));
        } catch (SQLException e) {
            throw new ServicesException(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Categoria> porIdCategoria(Long id) {
        try {
            return Optional.ofNullable(repositoryCategoriaJdbc.porId(id));
        } catch (SQLException e) {
            throw new ServicesException(e.getMessage(), e);
        }
    }
}
