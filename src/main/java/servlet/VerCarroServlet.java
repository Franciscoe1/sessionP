package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet encargado de mostrar la vista del carrito de compras.
 * Mapeado a la ruta "/ver-carro".
 * Su única responsabilidad es dirigir la petición a la página JSP que
 * se encarga de renderizar el contenido del carrito (DetalleCarro)
 * almacenado en la sesión.
 */
@WebServlet("/ver-carro")
public class VerCarroServlet extends HttpServlet {

    /**
     * Maneja las peticiones GET para mostrar la vista del carrito.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Simplemente redirige la petición y la respuesta a la vista JSP.
        // Se utiliza RequestDispatcher.forward() para transferir el control internamente
        // dentro del servidor. Esto permite que el JSP acceda a todos los atributos
        // de la solicitud y, más importante, a los atributos de la sesión (como el carrito).
        getServletContext().getRequestDispatcher("/carro.jsp").forward(req, resp);
    }
}