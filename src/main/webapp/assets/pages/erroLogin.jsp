<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%
    String erro = (String) request.getAttribute("erros");
    if (erro != null) {
%>
<div class="alert alert-danger">
    <ul>
        <li><%= erro %></li>
    </ul>
</div>
<%
} else if (request.getAttribute("erro") != null) {
%>
<div class="alert alert-danger">
    <p><%= request.getAttribute("erro") %></p>
</div>
<% } %>
