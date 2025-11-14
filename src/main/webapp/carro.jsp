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

    // NOTA: Se ha eliminado toda la lógica de generación de PDF de este JSP (la cual reside ahora en DescargarFacturaServlet).
%>

<html>
<head>
    <title>Carro de Compras</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/estilos.css">
</head>
<body>
<div class="container">
    <h1>🛒 Carro de Compras</h1>

    <%
        // --- Lógica Condicional de Renderizado ---
        // 2. Comprobar si el carrito está vacío o no existe.
        if (detalleCarro == null || detalleCarro.getItem().isEmpty()) {
    %>
    <p class="alert">Lo sentimos, no hay productos en el carro de compras!</p>
    <%
    } else {
        // 3. Si el carrito tiene ítems, se calculan los totales para mostrarlos.
        double total = detalleCarro.getTotal(); // Subtotal (sin IVA)
        double iva = detalleCarro.getIva();
        double totalConIva = detalleCarro.getTotalConIva(); // Total final
    %>
    <table class="styled-table">
        <tr>
            <th>Id Producto</th>
            <th>Nombre</th>
            <th>Precio Unitario</th>
            <th>Cantidad</th>
            <th>Subtotal Ítem</th>
        </tr>
        <%
            // 4. Iterar sobre la lista de ItemCarro y generar una fila por cada uno.
            for (ItemCarro item : detalleCarro.getItem()) {
        %>
        <tr>
            <td><%= item.getProducto().getId() %></td>
            <td><%= item.getProducto().getNombre() %></td>
            <td><%= df.format(item.getProducto().getPrecio()) %></td>
            <td><%= item.getCantidad() %></td>
            <td><%= df.format(item.getSubtotal()) %></td>
        </tr>
        <%
            }
        %>

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
    </table>

    <div class="actions">
        <%-- CRÍTICO: El enlace apunta al Servlet que genera el PDF --%>
        <a class="button success" href="<%=request.getContextPath()%>/descargar-factura">Generar Factura en PDF</a>
    </div>

    <%
        } // Cierra el "else" del if-empty
    %>

    <div class="actions">
        <a class="button primary" href="<%=request.getContextPath()%>/productos">SEGUIR COMPRANDO</a>
        <a class="button secondary" href="<%=request.getContextPath()%>/Index.html">Volver a Inicio</a>
    </div>
</div>
</body>
</html>