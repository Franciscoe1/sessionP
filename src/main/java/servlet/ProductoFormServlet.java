package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import modelos.Categoria;
import modelos.Producto;
import services.ProductoServiceJdbcImpl;
import services.ProductoServices;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/producto/form")
public class ProductoFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Connection conn = (Connection) req.getAttribute("conn");
        ProductoServices service = new ProductoServiceJdbcImpl(conn);

        // Leer categorías de la base de datos
        List<Categoria> categorias = service.listarCategoria();
        req.setAttribute("categorias", categorias);

        // Producto vacío para no tener null en el JSP
        Producto producto = new Producto();
        req.setAttribute("producto", producto);

        req.getRequestDispatcher("/form.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Connection conn = (Connection) req.getAttribute("conn");
        ProductoServices service = new ProductoServiceJdbcImpl(conn);

        String nombre = req.getParameter("nombre");
        String descripcion = req.getParameter("descripcion");
        String codigo = req.getParameter("codigo"); // por si luego lo usas
        String fecha_elaboracion = req.getParameter("fecha_elaboracion");
        String fecha_caducidad = req.getParameter("fecha_caducidad");
        String precioParam = req.getParameter("precio");

        Long categoriaId;
        try {
            categoriaId = Long.parseLong(req.getParameter("categoria"));
        } catch (NumberFormatException e) {
            categoriaId = 0L;
        }

        Integer stock;
        try {
            stock = Integer.valueOf(req.getParameter("stock"));
        } catch (NumberFormatException e) {
            stock = 0;
        }

        Double precio = null;


        Map<String, String> errores = new HashMap<>();

        if (nombre == null || nombre.isBlank()) {
            errores.put("nombre", "El nombre no puede estar vacío");
        }

        if (categoriaId == 0L) {
            errores.put("categoria", "Debe seleccionar una categoría");
        }

        if (stock <= 0) {
            errores.put("stock", "El stock debe ser mayor que 0");
        }

        if (precioParam == null || precioParam.trim().isEmpty()) {
            errores.put("precio", "El precio no puede estar vacío");
        } else {
            precioParam = precioParam.trim().replace(",", ".");
            try {
                precio = Double.valueOf(precioParam);
                if (precio <= 0) {
                    errores.put("precio", "El precio debe ser mayor que 0");
                }
            } catch (NumberFormatException e) {
                errores.put("precio", "El precio debe ser un número válido");
            }
        }

        if (fecha_elaboracion == null || fecha_elaboracion.isBlank()) {
            errores.put("fecha_elaboracion", "La fecha de elaboración es obligatoria");
        }

        if (fecha_caducidad == null || fecha_caducidad.isBlank()) {
            errores.put("fecha_caducidad", "La fecha de caducidad es obligatoria");
        }


        LocalDate fechaElaboracion = null;
        LocalDate fechaCaducidad = null;

        try {
            fechaElaboracion = LocalDate.parse(fecha_elaboracion,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            fechaCaducidad = LocalDate.parse(fecha_caducidad,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            errores.put("fecha", "Las fechas no tienen un formato válido");
        }

        Long id;
        try {
            id = Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            id = 0L;
        }

        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre(nombre);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        producto.setCategoria(categoria);

        producto.setStock(stock);
        producto.setPrecio(precio);
        producto.setDescripcion(descripcion);
        producto.setFechaElaboracion(fechaElaboracion);
        producto.setFechaCaducidad(fechaCaducidad);
        // si decides usar 'codigo', también debes añadirlo a la clase Producto

        if (!errores.isEmpty()) {
            // volver a cargar categorías
            List<Categoria> categorias = service.listarCategoria();
            req.setAttribute("categorias", categorias);

            req.setAttribute("producto", producto);
            req.setAttribute("errores", errores);

            req.getRequestDispatcher("/form.jsp").forward(req, resp);
            return;
        }

        service.guardar(producto);

        resp.sendRedirect(req.getContextPath() + "/productos");
    }
}
