<%--
  Created by IntelliJ IDEA.
  User: Usuario
  Date: 10/11/2025
  Time: 8:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="estilos.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h2>Iniciar sesión</h2>
        <span class="badge">Acceso</span>
    </div>

    <form method="post" action="login">

        <label>Usuario
            <input type="text" name="username" required autocomplete="username">
        </label>

        <label>Contraseña
            <input type="password" name="password" required autocomplete="current-password">
        </label>

        <div class="actions">
            <button type="submit" class="button primary">Entrar</button>
            <a class="button secondary" href="Index.html">Volver</a>
        </div>
    </form>

    <%
        // Recuperamos el atributo "error" que fue establecido por el Servlet (LoginServlet)
        // si la autenticación falló. Este error solo existe en el Request.
        String error = (String) request.getAttribute("error");

        // Si el atributo "error" no es nulo, significa que el login falló.
        if (error != null) {
    %>
    <p class="alert"><%= error %></p>
    <%
        }
    %>
</div>
</body>
</html>