<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletsvireya.dto.FuncionarioDTO" %>
<%@ page import="com.example.servletsvireya.dao.CargoDAO" %>
<%@ page import="com.example.servletsvireya.dto.CargoDTO" %>
<%@ page import="java.util.List" %>

<%
  FuncionarioDTO funcionario = (FuncionarioDTO) request.getAttribute("funcionario");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Funcionário</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleAlterar.css">
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" type="image/x-icon">
</head>
<body>

<div class="form-container">
  <h2>Alterar Funcionário</h2>

    <form action="${pageContext.request.contextPath}/ServletFuncionario" method="post">
      <input type="hidden" name="action" value="updateFuncionario">

      <!-- ID do funcionário -->
      <div class="campos-readonly">
        <label>ID</label>
        <input type="number" name="id" value="${funcionario.id}" readonly>
      </div>

      <!-- Nome -->
      <div class="campos">
        <label>Nome</label>
        <input type="text" name="nome" maxlength="80" value="${funcionario.nome}" required>
      </div>

      <!-- Email -->
      <div class="campos">
        <label>E-mail</label>
        <input type="email" name="email" maxlength="40" value="${funcionario.email}">
      </div>

      <!-- Senha (escondido) -->
      <input type="hidden" name="senha" value="${funcionario.senha}">

      <!-- Data de Admissão -->
      <div class="campos">
        <label>Data de Admissão</label>
        <input type="date" name="dataAdmissao" value="${funcionario.dataAdmissao}">
      </div>

      <!-- Data de Nascimento -->
      <div class="campos">
        <label>Data de Nascimento</label>
        <input type="date" name="dataNascimento" value="${funcionario.dataNascimento}">
      </div>

      <!-- Cargo -->
      <div class="campos">
        <label for="cargo">Cargo</label>
        <select id="cargo" name="nomeCargo">
          <option value="${funcionario.nomeCargo}">${funcionario.nomeCargo}</option>
          <%-- Valor inicial do cargo --%>
          <%-- Listando os cargos disponíveis com cargoDAO --%>
          <%
            CargoDAO cargoDAO = new CargoDAO();
            List<CargoDTO> cargosList = cargoDAO.listarCargos();

            for (CargoDTO cargo : cargosList) {
          %>
          <option value="<%= cargo.getNome() %>"><%= cargo.getNome() %>
          </option>
          <%-- Mostra o nome e pega o id do cargo --%>
          <%}%>
        </select>
      </div>

      <!-- Nome ETA -->
      <div class="campos-readonly">
        <label>ETA</label>
        <input type="text" name="nomeEta" value="${funcionario.nomeEta}" readonly>
      </div>

      <!-- Botão -->
      <div class="acoes">
        <input type="button" value="Cancelar" onclick="history.back()">
        <input type="submit" value="Salvar Alterações">
      </div>
  </form>

</div>

</body>
</html>

