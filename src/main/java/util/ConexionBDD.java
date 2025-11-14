package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBDD {
    private static String url= "jdbc:mysql://localhost:3306/sistemaventas?servreTimezone=UTC";
    private static String username="root";
    private static String password="";
    public static Connection getConnection()throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
