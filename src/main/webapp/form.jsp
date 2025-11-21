<%--
  Created by IntelliJ IDEA.
  User: Mateito
  Date: 21/11/2025
  Time: 8:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="java.util.*, modelos.*" %>

<%
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    if (categorias == null) categorias = new ArrayList<>();

    Map<String, String> errores = (Map<String, String>) request.getAttribute("errores");
    if (errores == null) errores = new HashMap<>();

    Producto producto = (Producto) request.getAttribute("producto");
    if (producto == null) producto = new Producto();

    String fechaElaboracion = producto.getFechaElaboracion() != null
            ? producto.getFechaElaboracion().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            : "";

    String fechaCaducidad = producto.getFechaCaducidad() != null
            ? producto.getFechaCaducidad().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            : "";
%>

<html>
<head>
    <title>Formulario Producto</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/estilos.css">
</head>
<body>

<div class="container">
    <h1>Crear / Editar Producto</h1>

    <%
        if (!errores.isEmpty()) {
    %>
    <div class="alert error">
        Por favor corrige los errores del formulario.
    </div>
    <%
        }
    %>

    <form action="<%=request.getContextPath()%>/producto/form" method="post">

        <input type="hidden" name="id"
               value="<%= producto.getId() != null ? producto.getId() : "" %>"/>

        <label>Nombre:</label>
        <input type="text" name="nombre"
               value="<%= producto.getNombre() != null ? producto.getNombre() : "" %>"/>
        <span class="error"><%= errores.getOrDefault("nombre", "") %></span>

        <label>Categoría:</label>
        <select name="categoria">
            <option value="0">-- Seleccione --</option>
            <% for (Categoria c : categorias) { %>
            <option value="<%= c.getId() %>"
                    <%
                        if (producto.getCategoria() != null
                                && producto.getCategoria().getId() != null
                                && producto.getCategoria().getId().equals(c.getId())) {
                    %> selected <% } %>>
                <%= c.getNombre() %>
            </option>
            <% } %>
        </select>
        <span class="error"><%= errores.getOrDefault("categoria", "") %></span>

        <label>Stock:</label>
        <input type="number" name="stock"
               value="<%= producto.getStock() %>"/>
        <span class="error"><%= errores.getOrDefault("stock", "") %></span>

        <label>Precio:</label>
        <input type="text" name="precio"
               value="<%= producto.getPrecio() %>"/>
        <span class="error"><%= errores.getOrDefault("precio", "") %></span>

        <label>Descripción:</label>
        <textarea name="descripcion"><%= producto.getDescripcion() != null ? producto.getDescripcion() : "" %></textarea>
        <span class="error"><%= errores.getOrDefault("descripcion", "") %></span>

        <label>Fecha Elaboración:</label>
        <input type="date" name="fecha_elaboracion" value="<%= fechaElaboracion %>"/>
        <span class="error"><%= errores.getOrDefault("fecha_elaboracion", "") %></span>

        <label>Fecha Caducidad:</label>
        <input type="date" name="fecha_caducidad" value="<%= fechaCaducidad %>"/>
        <span class="error"><%= errores.getOrDefault("fecha_caducidad", "") %></span>

        <div class="actions">
            <button type="submit" class="button primary">Guardar</button>
            <a href="<%=request.getContextPath()%>/productos" class="button secondary">Volver al listado</a>
        </div>
    </form>
</div>

</body>
</html>
