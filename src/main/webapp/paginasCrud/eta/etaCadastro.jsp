<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 07/10/2025
  Time: 15:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Cadastro de ETA</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f6f7fb;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 50px auto;
            background-color: #fff;
            padding: 30px 40px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.15);
        }

        h2 {
            text-align: center;
            color: #003366;
        }

        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 6px;
        }

        button {
            margin-top: 25px;
            width: 100%;
            padding: 10px;
            border: none;
            border-radius: 6px;
            background-color: #003366;
            color: white;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background-color: #00509e;
        }

        .mensagem {
            text-align: center;
            margin-top: 20px;
            color: green;
            font-weight: bold;
        }

        .erro {
            text-align: center;
            margin-top: 20px;
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Cadastro de ETA</h2>

    <form action="${pageContext.request.contextPath}/ServletEta" method="post">
        <input type="hidden" name="action" value="cadastrar">

        <label>Nome da ETA:</label>
        <input type="text" name="nome" required id="name">

        <label>Capacidade (m³/dia):</label>
        <input type="number" name="capacidade" step="0.10" required>

        <label>Cnpj:</label>
        <input type="text" name="cnpj" id="cnpj" required>

        <label>Telefone</label>
        <input type="text" name="telefone" id="telefone">

        <h3>Endereço</h3>
        <label>Rua:</label>
        <input type="text" name="rua" required>

        <label>Número:</label>
        <input type="number" name="numero" required>

        <label>Bairro:</label>
        <input type="text" name="bairro" required>

        <label>Cidade:</label>
        <input type="text" name="cidade" required>

        <label>Estado:</label>
        <input type="text" name="estado" required>

        <label>Cep:</label>
        <input type="text" name="cep" required>

        <h3>Administrador</h3>
        <label>Email:</label>
        <input type="email" name="adminEmail" required>

        <label for="senha">Senha</label>
        <div class="input-senha">
            <input type="password" id="senha" placeholder="Digite sua senha" name="senha" required
                   pattern="^(?=.*[A-Z])(?=.*[!@#$%]).{8,}$"
                   title="Tem que incluir pelo menos uma letra maiúscula e um caractere especial(!@#$%)">
            <span class="password-toggle" id="togglePassword">
                        <i class="fas fa-eye"></i>
                    </span>
        </div>

        <button type="submit">Cadastrar</button>
    </form>

</div>
<script src="${pageContext.request.contextPath}/paginasCrud/eta/script.js"></script>
<script src="${pageContext.request.contextPath}/paginasCrud/eta/regex.js"></script>
</body>
</html>
