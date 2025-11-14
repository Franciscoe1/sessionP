package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelos.DetalleCarro;
import modelos.ItemCarro;
import modelos.Producto;
import services.ProductoServices;
import services.ProductosServicesImplement;

import java.io.IOException;
import java.util.Optional;

/**
 * Servlet encargado de agregar un producto al carrito de compras.
 * Mapeado a la ruta "/agregar-carro".
 */
@WebServlet ("/agregar-carro")
public class AgregarCarroServlet extends HttpServlet {

    /**
     * Maneja las peticiones GET para añadir un producto al carrito.
     * Espera un parámetro 'id' en la URL.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Obtener y validar el ID del producto
        // Se asume que el ID se pasa como parámetro de consulta (e.g., /agregar-carro?id=1)
        // Se convierte el String del parámetro a Long. Se debe manejar NumberFormatException en un entorno de producción.
        Long id = Long.parseLong(req.getParameter("id"));

        // 2. Buscar el producto en el servicio
        // Se inicializa el servicio para acceder a los datos de los productos.
        ProductoServices service = new ProductosServicesImplement();
        // Se busca el producto por ID, devolviendo un Optional.
        Optional <Producto> producto = service.porId(id);

        // 3. Procesar la adición si el producto existe
        if (producto.isPresent()) {

            // Crear el ItemCarro para agregar. Siempre se comienza agregando 1 unidad.
            // La lógica de sumar la cantidad si el producto ya existe reside en DetalleCarro.addItemCarro.
            ItemCarro item = new ItemCarro(1, producto.get());

            // 4. Obtener o crear el carrito en la Sesión
            HttpSession session = req.getSession();
            // Se intenta recuperar el objeto DetalleCarro (que se almacena bajo la clave "carro") de la sesión.
            DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");

            // Si no hay carrito en la sesión, se crea uno nuevo y se guarda en la sesión.
            if(detalleCarro == null){
                detalleCarro = new DetalleCarro();
                session.setAttribute("carro", detalleCarro);
            }

            // 5. Añadir el ItemCarro al carrito.
            // DetalleCarro maneja si debe añadir un nuevo ítem o incrementar la cantidad existente.
            detalleCarro.addItemCarro(item);
        }

        // 6. Redirigir al Servlet que muestra el contenido del carrito
        // Esto previene el problema del doble envío del formulario/petición al recargar la página
        // (patrón Post/Redirect/Get simulado con GET/Redirect/Get).
        // Se redirige a la ruta del Servlet que tiene la lógica de mostrar el carrito.
        resp.sendRedirect(req.getContextPath()+"/ver-carro");
    }
}