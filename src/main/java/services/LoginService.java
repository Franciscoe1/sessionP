package services;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Interfaz de Servicio para la Lógica de Autenticación (Login).
 * * Define el contrato para cualquier clase que implemente la funcionalidad
 * de obtener el nombre de usuario (username) a partir de una solicitud HTTP.
 * Es fundamental para desacoplar la lógica de negocio de la capa de presentación
 * (servlets/controladores).
 */
public interface LoginService {

    /**
     * Intenta recuperar el nombre de usuario (username) del cliente a partir de
     * la información contenida en la solicitud HTTP.
     * * La implementación de este método se encargará de buscar el username
     * en lugares comunes como:
     * 1. La Sesión HTTP (HttpSession).
     * 2. Cookies enviadas por el cliente.
     * 3. Parámetros de la solicitud (aunque es menos seguro para autenticación).
     * * @param request El objeto HttpServletRequest que contiene la solicitud del cliente.
     * @return Un objeto Optional que contiene el username si se encuentra (usuario autenticado),
     * o un Optional vacío si el usuario no está logueado o no se puede determinar.
     */
    Optional<String> getUsername(HttpServletRequest request);
}