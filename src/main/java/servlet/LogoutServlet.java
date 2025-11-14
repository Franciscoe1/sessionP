package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import services.LoginService;
import services.LoginServiceSessionImpl;
import java.io.IOException;
import java.util.Optional;

/**
 * Servlet encargado de cerrar la sesión activa del usuario (logout).
 * Mapeado a la ruta "/logout".
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Maneja las peticiones GET para cerrar la sesión.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 1. Verificar si el usuario está actualmente logueado.
        // Se utiliza el servicio de login para obtener el nombre de usuario de la sesión.
        LoginService auth = new LoginServiceSessionImpl();
        Optional<String> Username = auth.getUsername(req);

        // Si el usuario está logueado, procedemos a invalidar la sesión.
        if (Username.isPresent()) {

            // 2. Obtener la Sesión HTTP.
            // Es vital usar 'false' como argumento:
            // req.getSession(false) - Obtiene la sesión existente, pero NO crea una nueva si no existe.
            HttpSession session = req.getSession(false);

            // 3. Invalidar la Sesión.
            if (session != null) {
                // El método invalidate() destruye la sesión, borrando todos los atributos
                // (incluyendo "username", "carro", etc.) y la hace inutilizable.
                session.invalidate();
            }
        }

        // 4. Redirigir al usuario.
        // Tras el logout, redirigimos al usuario a la página de bienvenida.
        // Es una práctica estándar para confirmar el cierre de sesión y llevar al usuario a un estado conocido.
        resp.sendRedirect(req.getContextPath() + "/Index.html");
    }
}