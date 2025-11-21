<%--
  Created by IntelliJ IDEA.
  User: Mateito
  Date: 13/11/2025
  Time: 8:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page
        contentType="text/html; charset=UTF-8"
        language="java"
        import="modelos.*"
        trimDirectiveWhitespaces="true"
%>
<%@ page import="modelos.ItemCarro" %>
<%@ page import="modelos.DetalleCarro" %>
<%@ page import="java.text.DecimalFormat" %>

<%
    // --- Bloque de Scriptlet de Inicialización ---

    // Definición del formateador de decimales (Ejemplo: 1500.00)
    DecimalFormat df = new DecimalFormat("#,##0.00");

    // 1. Obtener el detalle del carro de la Sesión HTTP.
    // El objeto fue guardado previamente por AgregarCarroServlet bajo la clave "carro".
    DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");

    // Variables para el resumen del total
    double total = 0.0;
    double iva = 0.0;
    double totalConIva = 0.0;

    // Calcular los totales si existe el carrito
    if (detalleCarro != null && !detalleCarro.getItem().isEmpty()) {
        total = detalleCarro.getTotal();
        iva = detalleCarro.getIva();
        totalConIva = detalleCarro.getTotalConIva();
    }
%>

<html>
<head>
    <title>Carro de Compras</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/estilos.css">
</head>
<body>
<div class="container">

    <div class="header">
        <h2>🛍️ Carro de Compras</h2>
        <span class="badge">Detalle</span>
    </div>

    <%
        // Condición para mostrar el mensaje de carro vacío
        if (detalleCarro == null || detalleCarro.getItem().isEmpty()) {
    %>
    <div class="alert alert-info">
        Tu carro de compras está vacío. <a href="<%=request.getContextPath()%>/productos">Ver productos</a>.
    </div>
    <%
    } else {
    %>

    <p>A continuación se presenta el detalle de tu compra:</p>

    <%-- Tabla para mostrar los items del carrito --%>
    <table class="styled-table">
        <thead>
        <tr>
            <th>ID Producto</th>
            <th>Nombre</th>
            <th>Precio Unitario</th>
            <th>Cantidad</th>
            <th>Subtotal</th>
        </tr>
        </thead>
        <tbody>
        <%
            // Iterar sobre los items del carrito
            for (ItemCarro item : detalleCarro.getItem()) {
        %>
        <tr>
            <td><%= item.getProducto().getId() %></td>
            <td><%= item.getProducto().getNombre() %></td>
            <td><%= df.format(item.getProducto().getPrecio()) %></td>
            <td><%= df.format(item.getCantidad()) %></td>
            <td><%= df.format(item.getSubtotal()) %></td>
        </tr>
        <%
            }
        %>

        <%-- Filas de totales --%>
        <tr>
            <td colspan="4" style="text-align: right; font-weight: bold;">Total Base:</td>
            <td><%= df.format(total) %></td>
        </tr>
        <tr>
            <td colspan="4" style="text-align: right; font-weight: bold;">IVA (15%):</td>
            <td><%= df.format(iva) %></td>
        </tr>
        <tr class="final-total">
            <td colspan="4" style="text-align: right; font-weight: bold; background-color: #e9ecef;">TOTAL FINAL:</td>
            <td style="background-color: #e9ecef; font-weight: bold;"><%= df.format(totalConIva) %></td>
        </tr>
        </tbody>
    </table>

    <div class="actions">
        <%-- CRÍTICO: El enlace apunta al Servlet que genera el PDF --%>
        <a class="button success" href="<%=request.getContextPath()%>/descargar-factura">Generar Factura en PDF</a>
    </div>

    <%
        } // Cierra el "else" del if-empty
    %>

    <div class="actions">
        <a class="button secondary" href="<%=request.getContextPath()%>/productos">Seguir Comprando</a>
        <a class="button secondary" href="<%=request.getContextPath()%>/Index.html">Inicio</a>
    </div>

</div>
</body>
</html>