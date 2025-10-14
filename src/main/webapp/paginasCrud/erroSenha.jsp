<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%
    List<String> errosSenha = (List<String>) request.getAttribute("errosSenha");
    if (errosSenha != null && !errosSenha.isEmpty()) {
%>
<div class="alert alert-danger">
    <ul>
        <% for (String erro : errosSenha) { %>
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
