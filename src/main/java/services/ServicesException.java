package services;
/*
    * Clase personalizada de excepción para manejar errores específicos en los servicios.
 */
public class ServicesException extends RuntimeException{
    public ServicesException(String mensaje){
        super(mensaje);
    }
    public ServicesException(String mensaje, Throwable cause){
        super(cause);
    }
}
