<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 05/10/2025
  Time: 01:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <title>Alterar Estoque</title>
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
  <h2>Alterar Estoque</h2>

  <form action="${pageContext.request.contextPath}/ServletEstoque" method="post">
    <input type="hidden" name="action" value="updateEstoque"> <!-- Servlet enxerga que o action é updateEstoque -->

    <div class="campos">
      <label>ID</label>
      <input type="number" name="id" value="${id}" readonly>
    </div>

    <div class="campos">
      <label>Quantidade</label>
      <input type="number" min="0" name="quantidade" value="${quantidade}">
    </div>

    <div class="campos">
      <label>Data de Validade</label>
      <input type="date" name="dataValidade" value="${dataValidade}">
    </div>

    <div class="campos">
      <label>Minímo possível estocado</label>
      <input type="number" name="minPossivelEstocado" value="${minPossivelEstocado}" required>
    </div>

    <div class="acoes">
      <input type="submit" value="Salvar Alterações">
    </div>
  </form>
</div>

</body>
</html>