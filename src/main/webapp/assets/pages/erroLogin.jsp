<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%
    List<String> erros = (List<String>) request.getAttribute("erros");
    if (erros != null && !erros.isEmpty()) {
%>
<div class="alert alert-danger">
    <ul>
        <% for (String erro : erros) { %>
        <li><%= erro %></li>
        <% } %>
    </ul>
</div>
<%
} else if (request.getAttribute("erro") != null) {
%>
<div class="alert alert-danger">
    <p><%= request.getAttribute("erro") %></p>
</div>
<% } %>
