package servlet;

/*
 * Servlet para el manejo del login y el conteo
 * de cuántas veces el usuario ha iniciado sesión
 * correctamente, aun cuando se cierre la sesión (logout).
 * Utiliza la **Sesión HTTP** para la autenticación y las **Cookies** para el contador persistente.
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import services.LoginService;
import services.LoginServiceSessionImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet({"/login", "/login.html"})
public class LoginServlet extends HttpServlet {

    // Credenciales estáticas de ejemplo para el login. En un entorno real, vendrían de una base de datos.
    final static String USERNAME = "admin";
    final static String PASSWORD = "1234";

    /**
     * Método auxiliar para obtener el contador desde la cookie "loginCounter".
     * Si la cookie no existe, devuelve 0. Si hay un error de formato, también devuelve 0.
     * @param req La solicitud HTTP.
     * @return El número de veces que el usuario ha iniciado sesión correctamente.
     */
    private int obtenerContadorDesdeCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("loginCounter".equals(c.getName())) {
                    try {
                        // Intenta convertir el valor de la cookie a entero.
                        return Integer.parseInt(c.getValue());
                    } catch (NumberFormatException e) {
                        // Si el valor no es un número válido, se ignora y se trata como 0.
                        return 0;
                    }
                }
            }
        }
        // Si no se encuentra la cookie, el contador inicia en 0.
        return 0;
    }

    /**
     * Método GET:
     * - Si el usuario ya está autenticado (tiene "username" en sesión),
     * muestra un mensaje de bienvenida y el contador de inicios de sesión.
     * - Si no está autenticado, redirige al formulario de login.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Verificar autenticación
        // Servicio que obtiene el username desde la sesión (usando LoginServiceSessionImpl).
        LoginService auth = new LoginServiceSessionImpl();
        Optional<String> usernameOptional = auth.getUsername(req);

        if (usernameOptional.isPresent()) {
            // El usuario está autenticado.

            // 2. Obtener y preparar el contador de la cookie
            int counter = obtenerContadorDesdeCookie(req);
            String counterText = String.valueOf(counter);

            // 3. Generar la respuesta HTML
            resp.setContentType("text/html;charset=UTF-8");

            try (PrintWriter out = resp.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("  <meta charset=\"UTF-8\">");
                out.println("  <title>Hola " + usernameOptional.get() + "</title>");
                // Añadido enlace a hoja de estilos para mejor presentación
                out.println("<link rel=\"stylesheet\" href=\"" + req.getContextPath() + "/estilos.css\">");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container'>"); // Contenedor principal
                out.println("<h1>Hola " + usernameOptional.get() + " has iniciado sesión con éxito!</h1>");
                out.println("<p>Veces que has iniciado sesión en este navegador: <strong>" + counterText + "</strong></p>");
                out.println("<div class='actions'>"); // Contenedor de acciones
                out.println("<a class='button secondary' href='" + req.getContextPath() + "/Index.html'>Volver</a>");
                out.println("<a class='button alert-button' href='" + req.getContextPath() + "/logout'>Cerrar sesión</a>"); // Enlace al Servlet de logout
                out.println("</div>");
                out.println("</div>"); // Cierra contenedor
                out.println("</body>");
                out.println("</html>");
            }
        } else {
            // 4. Si no hay usuario en sesión, enviamos al formulario de Login.jsp
            getServletContext().getRequestDispatcher("/Login.jsp").forward(req, resp);
        }
    }

    /**
     * Método POST:
     * - Procesa el formulario de login.
     * - Valida el usuario y contraseña.
     * - Si es exitoso, guarda el username en la Sesión e incrementa el contador en la Cookie.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Obtener los parámetros enviados en el formulario
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // 2. Validar credenciales
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {

            // --- Lógica de Autenticación (Sesión) ---
            HttpSession session = req.getSession();
            // Guardamos el username en la sesión para mantener el estado de autenticación
            session.setAttribute("username", username);

            // --- Lógica del Contador (Cookie Persistente) ---
            // 3. Obtener, incrementar y actualizar la cookie del contador
            int counter = obtenerContadorDesdeCookie(req);
            counter++; // Incrementamos el contador

            // Creamos la nueva cookie con el valor incrementado
            Cookie loginCounterCookie = new Cookie("loginCounter", String.valueOf(counter));
            // Establecemos la duración de la cookie (ejemplo: 30 días)
            loginCounterCookie.setMaxAge(60 * 60 * 24 * 30);
            // Establecemos el path de la cookie para que esté disponible en toda la aplicación
            loginCounterCookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());

            // Agregamos la cookie a la respuesta para que el navegador la almacene/actualice
            resp.addCookie(loginCounterCookie);

            // 4. Redirigir
            // Se usa Post/Redirect/Get: Redirigimos al GET del mismo servlet para mostrar la bienvenida
            // y evitar que el usuario reenvíe el formulario al recargar la página.
            resp.sendRedirect(req.getContextPath() + "/login.html");

        } else {
            // 5. Credenciales inválidas
            // Enviamos un mensaje de error como atributo de la solicitud
            req.setAttribute("error", "Usuario o contraseña inválidos.");
            // Usamos RequestDispatcher para reenviar al formulario, manteniendo el mensaje de error
            getServletContext().getRequestDispatcher("/Login.jsp").forward(req, resp);
        }
    }
}