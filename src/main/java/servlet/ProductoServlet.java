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
public class ProductoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtener la conexión inyectada por el ConexionFilter
        // La conexión debe existir como atributo de la solicitud
        Connection conn = (Connection) req.getAttribute("conn");
        // 2. Inicializar el servicio con la implementación JDBC, pasándole la conexión
        // Esto permite que el servicio acceda a la base de datos
        ProductoServices service = new ProductoServiceJdbcImpl(conn);
        List<Producto> productos = service.listar();

        // 3. Verificar el estado de autenticación del usuario (el resto del código sigue igual)
        LoginService auth = new LoginServiceSessionImpl();
        Optional<String> usernameOptional = auth.getUsername(req);

        // 4. Configuración de la respuesta HTTP
        /*resp.setContentType("text/html;charset=UTF-8");

        // ... el resto del método doGet (código HTML/salida) sigue aquí
        try (PrintWriter out = resp.getWriter()) {
            // ... (HTML de encabezado y tabla)

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Listado de Productos</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Listado de Productos!</h1>");

            if (usernameOptional.isPresent()) {
                out.println("<div style=\"color: blue;\">Hola " + usernameOptional.get() + " Bienvenido! </div>");
            }

            out.println("<table>");
            out.println("<tr>");
            out.println("<th>id</th>");
            out.println("<th>nombre</th>");
            out.println("<th>stock</th>");
            out.println("<th>fecha produccion</th>");
            if (usernameOptional.isPresent()) {
                out.println("<th>precio</th>");
                out.println("<th>opciones</th>");
            }
            out.println("</tr>");
            productos.forEach(p -> {
                out.println("<tr>");
                out.println("<td>" + p.getid() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + p.getStock() + "</td>");
                out.println("<td>" + p.getFechaElaboracion() + "</td>");
                if (usernameOptional.isPresent()) {
                    out.println("<td>" + p.getPrecio() + "</td>");
                    out.println("<td><a href=\""
                            + req.getContextPath()
                            + "/agregar-carro?id="
                            + p.getid()
                            + "\">Agregar Producto al carro</a></td>");
                }
                out.println("</tr>");
            });
            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        }*/
        req.setAttribute("productos", productos);
        req.setAttribute("username", usernameOptional);
        //Pasamos al servlet
        getServletContext().getRequestDispatcher("/producto.jsp").forward(req, resp);
    }
}