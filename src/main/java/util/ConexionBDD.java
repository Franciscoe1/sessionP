package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBDD {

    // 1. Configuración de la base de datos (Ajusta estos valores si es necesario)
    private static String url = "jdbc:mysql://localhost:3306/sistemaventas?serverTimezone=UTC";
    private static String username = "root";
    private static String password = ""; // Asegúrate de que esta sea la contraseña correcta si la tienes

    /**
     * Devuelve una nueva conexión a la base de datos.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Método para probar la conexión y mostrar el resultado en consola.
     */
    public static void probarConexion() {
        Connection conn = null;
        try {
            // Intentar establecer la conexión
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Conexión a la base de datos exitosa!");

        } catch (SQLException e) {
            System.err.println("❌ Conexión a la base de datos fallida.");
            // Mostrar el código de error SQL y el mensaje
            System.err.println("Error SQL: " + e.getErrorCode() + ", Mensaje: " + e.getMessage());

        } finally {
            // Asegurarse de cerrar la conexión si se abrió
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Método main de prueba para ejecutar directamente y ver el resultado en la consola.
     */
    public static void main(String[] args) {
        System.out.println("--- Iniciando prueba de conexión ---");
        ConexionBDD.probarConexion();
        System.out.println("--- Prueba de conexión finalizada ---");
    }
}