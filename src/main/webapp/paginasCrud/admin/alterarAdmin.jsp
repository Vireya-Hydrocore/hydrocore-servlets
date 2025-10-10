<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 05/10/2025
  Time: 20:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Alterar Admin</title>
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
        .campos-readonly input{
            width: 100%;
            padding: 8px;
            border: 1px solid #E00;
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
    <h2>Alterar Admin</h2>

    <form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
        <input type="hidden" name="action" value="updateAdmin"> <!-- Servlet enxerga que o action é updateEstoque -->

        <div class="campos-readonly">
            <label>ID</label>
            <input type="number" name="id" value="${id}" readonly>
        </div>

        <div class="campos">
            <label>Nome</label>
            <input type="text" name="nome" value="${nome}" required>
        </div>

        <div class="campos">
            <label>E-mail</label>
            <input type="email" name="email" value="${email}" required>
        </div>

        <div class="campos">
            <label>Senha</label>
            <input type="password" name="senha" value="${senha}" required>
        </div>

        <div class="acoes">
            <input type="submit" value="Salvar Alterações">
        </div>
    </form>
</div>

</body>
</html>
