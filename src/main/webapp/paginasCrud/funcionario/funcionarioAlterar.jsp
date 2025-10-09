<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletsvireya.dto.FuncionarioDTO" %>

<%
  FuncionarioDTO funcionario = (FuncionarioDTO) request.getAttribute("funcionario");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Alterar Funcionário</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #f5f5f5;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .form-container {
      background: #fff;
      padding: 20px 30px;
      border-radius: 10px;
      box-shadow: 0 2px 6px rgba(0,0,0,0.2);
      width: 400px;
    }
    .form-container h2 {
      text-align: center;
      margin-bottom: 20px;
    }
    .campos {
      margin-bottom: 15px;
    }
    .campos label {
      display: block;
      margin-bottom: 5px;
      font-weight: bold;
    }
    .campos input {
      width: 100%;
      padding: 8px;
      border: 1px solid #bbb;
      border-radius: 6px;
    }
    .acoes {
      text-align: center;
      margin-top: 20px;
    }
    .acoes input[type="submit"] {
      background-color: #4460F6;
      color: #fff;
      border: none;
      padding: 10px 15px;
      border-radius: 6px;
      cursor: pointer;
    }
    .acoes input[type="submit"]:hover {
      background-color: #2f45b5;
    }
  </style>
</head>
<body>

<div class="form-container">
  <h2>Alterar Funcionário</h2>

  <form action="${pageContext.request.contextPath}/ServletFuncionario" method="post">
    <input type="hidden" name="action" value="alterarFuncionario">
    <input type="hidden" name="id" value="${funcionario.id}">

    <label for="nome">Nome:</label>
    <input type="text" id="nome" name="nome" value="${funcionario.nome}" required><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="${funcionario.email}" required><br>

    <label for="senha">Senha:</label>
    <input type="password" id="senha" name="senha" value="${funcionario.senha}" required><br>

    <label for="nomeCargo">Cargo:</label>
    <input type="text" id="nomeCargo" name="nomeCargo"
           value="${funcionario.nomeCargo}" required><br>

    <button type="submit">Salvar</button>
  </form>

</div>

</body>
</html>

