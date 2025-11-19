package services;
/*
    * Clase personalizada de excepción para manejar errores específicos en los servicios.
 */
public class Exception extends RuntimeException{
    public Exception(String mensaje){
        super(mensaje);
    }
    public Exception (String mensaje, Throwable cause){
        super(cause);
    }
}
