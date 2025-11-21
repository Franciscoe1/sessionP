<%--
  Created by IntelliJ IDEA.
  User: Mateito
  Date: 20/11/2025
  Time: 8:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         import= "java.util.*, modelos.*"%>
<%-----%>
<%
    List<Producto> productos =(List<Producto>) request.getAttribute("productos");
    Optional<String> username =(Optional<String>) request.getAttribute("username");
%>
<html>
<head>
    <title>Listado Productos</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/estilos.css">
</head>
<body>
<h1>Listado de Productos</h1>
    <% if (username.isPresent()) { %>
<div> Hola <%=username.get()%>, Bienvenido! </div>
<p><a href="<%=request.getContextPath()%>" >Crear producto</a></p>
    <%}%>
<table>
    <tr>
        <th> ID Producto</th>
        <th> Nombre del Producto</th>
        <th> Categoria</th>
        <th> stock </th>
        <th> Descripcion </th>
        <th> Fecha Elaboracion</th>
        <th> Fecha Caducidad</th>
        <th> Condicion</th>
        <%if (username.isPresent()) { %>
        <th> Precio</th>
        <th> Accion</th>
        <%}%>
    </tr>
        <% for (Producto p : productos) { %>
    <tr>
        <td><%=p.getId()%>
        </td>
        <td><%=p.getNombre()%>
        </td>
        <td><%=p.getCategoria().getNombre()%>
        </td>
        <td><%=p.getStock()%>
        </td>
        <td><%=p.getDescripcion()%>
        </td>
        <td><%=p.getFechaElaboracion()%>
        </td>
        <td><%=p.getFechaCaducidad()%>
        </td>
        <td><%=p.getCondicion()%>
        </td>
            <%if (username.isPresent()) { %>
        <td><%=p.getPrecio()%>
        </td>
        <td>
            <a href="<%=request.getContextPath()%>/agregar-carro?id=<%=p.getId()%>Agregar al carro</a>
        </td>
        <%}%>
    </tr>
    <% } %>
</table>
</body>
</html>
