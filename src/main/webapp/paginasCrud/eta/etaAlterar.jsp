<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 10/10/2025
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.example.servletsvireya.dto.EtaDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  EtaDTO eta = (EtaDTO) request.getAttribute("eta");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>ETA</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/paginasCrud/css/styleAlterar.css">
</head>
<body>

<div class="form-container">
  <h2>Alterar ETA</h2>

  <form action="${pageContext.request.contextPath}/ServletEta" method="post">
    <input type="hidden" name="action" value="updateEta">

    <!-- ID do produto -->
    <div class="campos-readonly">
      <label>ID</label>
      <input type="number" name="id" value="${eta.id}" readonly>
    </div>

    <!-- Nome -->
    <div class="campos">
      <label>Nome</label>
      <input type="text" name="nome" value="${eta.nome}" required>
    </div>

    <!-- Capacidade -->
    <div class="campos">
      <label>Capacidade</label>
      <input type="number" name="capacidade" value="${eta.capacidade}">
    </div>

    <!-- Telefone -->
    <div class="campos">
      <label>Telefone</label>
      <input type="text" name="telefone" value="${eta.telefone}">
    </div>

    <!-- Telefone -->
    <div class="campos-readonly">
      <label>CNPJ</label>
      <input type="text" name="cnpj" value="${eta.cnpj}" readonly>
    </div>

    <!-- ENDEREÇO -->
    <br>
    <h3>Endereço</h3>

    <div class="campos">
      <label>Rua</label>
      <input type="text" name="rua" value="${eta.rua}">
    </div>

    <!-- Bairro -->
    <div class="campos">
      <label>Bairro</label>
      <input type="text" name="bairro" value="${eta.bairro}" required>
    </div>

    <!-- Cidade -->
    <div class="campos">
      <label>Cidade</label>
      <input type="text" name="cidade" value="${eta.cidade}" required>
    </div>

    <!-- Estado -->
    <div class="campos">
      <label>Estado</label>
      <input type="text" name="estado" value="${eta.estado}" required>
    </div>

    <!-- Número -->
    <div class="campos">
      <label>Número</label>
      <input type="number" name="numero" value="${eta.numero}" required>
    </div>

    <!-- CEP -->
    <div class="campos">
      <label>CEP</label>
      <input type="text" name="cep" value="${eta.cep}" required>
    </div>

    <!-- Botão -->
    <div class="acoes">
      <input type="submit" value="Salvar Alterações">
    </div>
  </form>
</div>

</body>
</html>