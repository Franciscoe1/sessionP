package services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Implementación concreta de la interfaz LoginService.
 * * Esta clase gestiona la autenticación del usuario basándose en la Sesión HTTP (HttpSession).
 * Se asume que, tras un login exitoso, el nombre de usuario ('username') se ha guardado en la sesión.
 */
public class LoginServiceSessionImpl implements LoginService {

    /**
     * Implementa el método para obtener el nombre de usuario de la solicitud HTTP.
     * Busca el nombre de usuario en la Sesión HTTP.
     * * @param request El objeto HttpServletRequest que contiene la solicitud del cliente.
     * @return Un Optional que contiene el username si está presente en la sesión,
     * o un Optional vacío si no hay sesión o si el atributo 'username' no existe.
     */
    @Override
    public Optional<String> getUsername(HttpServletRequest request) {
        // Obtiene la sesión actual asociada a esta solicitud.
        // Se usa 'false' como argumento para **evitar crear una nueva sesión** // si el cliente aún no tiene una.
        HttpSession session = request.getSession(false);

        // 1. Verificación de la Sesión
        if (session != null) {
            // La sesión existe. Intentamos recuperar el atributo de autenticación.

            // 2. Recuperación del Atributo
            // El atributo de la sesión debe ser casteado a String, ya que getAttribute()
            // devuelve un Object.
            String username = (String) session.getAttribute("username");

            // 3. Verificación del Username
            if (username != null) {
                // El username fue encontrado y no es nulo. Lo devolvemos envuelto en Optional.
                return Optional.of(username);
            }
            // Si el username es nulo, la ejecución continúa para devolver Optional.empty().
        }

        // Retorna un Optional vacío si:
        // a) No había sesión (session == null).
        // b) La sesión existía, pero el atributo "username" era nulo.
        return Optional.empty();
    }
}