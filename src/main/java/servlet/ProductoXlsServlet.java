package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelos.Producto;
import services.LoginService;
import services.LoginServiceSessionImpl;
import services.ProductoServices;
import services.ProductoServiceJdbcImpl; // 1. Cambia la importación al servicio JDBC

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection; // 2. Nueva importación necesaria
import java.util.List;
import java.util.Optional;

/**
 * Servlet encargado de mostrar el listado de productos disponibles.
 * Mapeado a las rutas "/productos.html" y "/productos".
 * La presentación es condicional: muestra precios y opción de compra solo si el usuario está autenticado.
 */
@WebServlet({"/productos.html", "/productos"})
public class ProductoXlsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtener la conexión inyectada por el ConexionFilter
        // La conexión debe existir como atributo de la solicitud
        Connection conn = (Connection) req.getAttribute("conn");
        if (conn == null) {
            // Manejo de error si la conexión no está presente (aunque no debería pasar con el filtro)
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No se pudo obtener la conexión a la base de datos.");
            return;
        }

        // 2. Inicializar el servicio con la implementación JDBC, pasándole la conexión
        // Esto permite que el servicio acceda a la base de datos
        ProductoServices service = new ProductoServiceJdbcImpl(conn);
        List<Producto> productos = service.listar();

        // 3. Verificar el estado de autenticación del usuario (el resto del código sigue igual)
        LoginService auth = new LoginServiceSessionImpl();
        Optional<String> usernameOptional = auth.getUsername(req);

        // 4. Configuración de la respuesta HTTP
        resp.setContentType("text/html;charset=UTF-8");

        // ... el resto del método doGet (código HTML/salida) sigue aquí
        try (PrintWriter out = resp.getWriter()) {
            // ... (HTML de encabezado y tabla)

            out.println("<div class='container'>");

            // ... (lógica de bienvenida y login/logout)

            out.println("<h2>Listado de Productos</h2>");

            out.println("<table class='styled-table'>");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>Nombre</th>");
            out.println("<th>Categoría</th>");
            if (usernameOptional.isPresent()) {
                out.println("<th>Precio</th>");
                out.println("<th>Acción</th>");
            }
            out.println("</tr>");
            out.println("</thead>");

            // 7. Iterar y mostrar los productos
            productos.forEach(p -> {
                out.println("<tr>");
                out.println("<td>" + p.getid() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                // Asegúrate de que p.getCategoria() no sea null en ProductoServiceJdbcImpl.getProducto
                out.println("<td>" + p.getCategoria().getNombre() + "</td>");

                if (usernameOptional.isPresent()) {
                    // Muestra el precio
                    out.println("<td>" + p.getPrecio() + "</td>");
                    // Muestra el botón para agregar al carrito, enlazando al AgregarCarroServlet
                    out.println("<td><a href=\"\"\r\n                            + req.getContextPath() + \"/agregar-carro?id=\" + p.getid() + \"\\\" title=\\\"Agregar al carro\\\" class='button success small'>🛒</a></td>");
                }
                out.println("</tr>");
            });

            out.println("</table>");

            // 8. Enlaces de navegación
            out.println("<div class='actions'>");
            out.println("<a class='button secondary' href='"+req.getContextPath()+"/Index.html'>Inicio</a>");
            out.println("<a class='button primary' href='"+req.getContextPath()+"/ver-carro'>Ver Carro</a>");
            out.println("</div>");

            out.println("</div>"); // Cierra contenedor
            out.println("</body>");
            out.println("</html>");
        }
    }
}