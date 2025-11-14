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
import services.ProductosServicesImplement;

import java.io.IOException;
import java.io.PrintWriter;
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

        // 1. Obtener los datos de los productos
        ProductoServices service = new ProductosServicesImplement();
        List<Producto> productos = service.listar();

        // 2. Verificar el estado de autenticación del usuario
        // Se utiliza el servicio de login para saber si hay un usuario en la sesión.
        LoginService auth = new LoginServiceSessionImpl();
        Optional<String> usernameOptional = auth.getUsername(req);

        // 3. Configurar la respuesta HTML
        resp.setContentType("text/html;charset=UTF-8");

        // 4. Generar el HTML dinámicamente
        try (PrintWriter out = resp.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Listado de Productos</title>");
            // Enlace a estilos CSS
            out.println("<link rel=\"stylesheet\" href=\"" + req.getContextPath() + "/estilos.css\">");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container'>"); // Contenedor principal
            out.println("<h1>Listado de Productos</h1>");

            // 5. Mensaje de bienvenida/login condicional
            if (usernameOptional.isPresent()) {
                out.println("<div class='welcome-message'>Hola " + usernameOptional.get() + " ¡Bienvenido!</div>");
            } else {
                // Mensaje si no está logueado, alentando a iniciar sesión para interactuar.
                out.println("<div class='alert'>Debes <a href='" + req.getContextPath() + "/Login.jsp'>Iniciar Sesión</a> para ver precios y comprar.</div>");
            }

            // 6. Generación de la tabla de productos
            out.println("<table class='styled-table'>");
            out.println("<tr>");
            out.println("<th>id</th>");
            out.println("<th>nombre</th>");
            out.println("<th>tipo</th>");

            // Las columnas 'precio' y 'Opciones' solo se muestran si el usuario está autenticado.
            if (usernameOptional.isPresent()) {
                out.println("<th>precio</th>");
                out.println("<th>Opciones</th>");
            }
            out.println("</tr>");

            // 7. Iterar y mostrar los productos
            productos.forEach(p -> {
                out.println("<tr>");
                out.println("<td>" + p.getId() + "</td>");
                out.println("<td>" + p.getNombre() + "</td>");
                out.println("<td>" + p.getTipo() + "</td>");

                if (usernameOptional.isPresent()) {
                    // Muestra el precio
                    out.println("<td>" + p.getPrecio() + "</td>");
                    // Muestra el botón para agregar al carrito, enlazando al AgregarCarroServlet
                    out.println("<td><a href=\""
                            + req.getContextPath() + "/agregar-carro?id=" + p.getId() + "\" title=\"Agregar al carro\" class='button success small'>🛒</a></td>");
                }
                out.println("</tr>");
            });

            out.println("</table>");

            // 8. Enlaces de navegación
            out.println("<div class='actions'>");
            out.println("<a class='button secondary' href='"+req.getContextPath()+"/Index.html'>Inicio</a>");
            // El enlace para ver el carro siempre está disponible, el servlet de ver-carro manejará el estado.
            out.println("<a class='button primary' href='"+req.getContextPath()+"/ver-carro'>Ver Carro</a>");
            out.println("</div>");

            out.println("</div>"); // Cierra contenedor
            out.println("</body>");
            out.println("</html>");
        }
    }
}