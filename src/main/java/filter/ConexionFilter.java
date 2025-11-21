package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import util.ConexionBDD;
import jakarta.servlet.http.HttpServletResponse;
import services.ServicesException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

// Implementamos una anotación. Esta anotación
// me sirve para poder utilizar la conexión en cualquier parte de mi aplicación.
@WebFilter("/*")
public class ConexionFilter implements Filter {
    /*
     * Una clase Filter en java es un objeto que realiza tarea
     * de filtrado en las solicitudes cliente, servidor
     * respuesta a un recurso: los filtros se pueden ejecutar
     * en servidores compatibles con Jakarta EE
     * Los filtros interceptan solicitudes y respuestas de manera
     * dinámica para transformar o utilizar la información que contienen.
     * El filtrado se realiza mediante el método doFilter
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        /*
         * request: petición que hace el cliente
         * response: respuesta del servidor
         * filterChain: es una clase de filtro que representa el flujo
         * del procesamiento. Este método llama al método chain.doFilter(request, response)
         * dentro de un filtro pasa la solicitud, el siguiente paso la clase
         * filtro o te devuelve el recurso destino que puede ser un servlet
         * o jsp
         * **/

        // Obtenemos la conexión
        try (Connection conn = ConexionBDD.getConnection()) {
            // Verificamos que la conexión realizada o se cambien a autocommit
            // (configuración automática a la base de datos y cada instrucción
            // SQL)
            if (conn.getAutoCommit()) {
                conn.setAutoCommit(false);
            }

            try {
                // Agregamos la conexión como un atributo en la solicitud
                // Esto permite que otros componentes como setrvlet o DAOS
                // puedan acceder a la conexión
                request.setAttribute("conn", conn);

                // Pasa la solicitud y la resouesta al siguiente filtro o al recurso
                // destino
                filterChain.doFilter(request, response);


                /*
                 * Si el procesamineto se realizo correctamente sin lanzar excepciones, se confirma
                 * la solicitud, y se aplica todos los cambios a la base de datos
                 **/
                conn.commit();

                /*
                 * Si corre algun error durante el procesamiento (dentro del doFilter), se
                 * captura la excepción
                 **/
            } catch (SQLException | ServicesException e) {
                // Se deshace los cambios con un rollbaxx y de esa forma se matiene la integridad de los
                // datos
                conn.rollback();

                // Enviamos un código de error HTTP 500 al cliente
                // indicando un problema interno del servido
                ((HttpServletResponse)
                response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                       e.getMessage());
                e.printStackTrace();

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}