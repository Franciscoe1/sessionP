package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import services.Exception;
import util.ConexionBDD;

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
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        /*
        * request: petición que hace el cliente
        * response: respuesta del servidor
        * filterChain: es una clase de filtro que representa el flujo
        * de procesamiento, este método llama al método chain.doFilter(request, response)
        * dentro de un filtro pasa la solicitud, el siguiente paso la clase filtra o te devuelve
        * el recurso destino que puede ser un servlet o jsp
         */

        // Obtenemos la conexión
        try (Connection conn= ConexionBDD.getConnection()){
            //Verificamos que la conexión realizada o se cambien a autocommit
            //(configuración automática a la base de datos y cada instrucción SQL)
            if (conn.getAutoCommit()){
                conn.setAutoCommit(false);
            }
            try {
                // Agregamos la conexión como un atributo en la solicitud
                // esto nos permite que otros componentes como sertvlets o DAOs
                // puedan acceder a la conexión
                request.setAttribute("conn", conn);
                //Pasa la solicitud y la respuesta al siguiente filtro o recurso destino
                filterChain.doFilter(request, response);
                conn.commit();
            } catch (SQLException | Exception e){
            }

        }
    }
}
