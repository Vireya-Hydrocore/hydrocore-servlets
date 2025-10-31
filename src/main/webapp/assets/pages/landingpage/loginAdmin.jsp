<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 10/10/2025
  Time: 19:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Página de Login</title>
  <style>
      body {
          font-family: Arial, sans-serif;
          background-color: #f0f2f5;

          /* várias imagens ao mesmo tempo */
          background-image:
                  url("${pageContext.request.contextPath}/assets/imgs/vireya_icon.png"),   /* flor */
                  url("${pageContext.request.contextPath}/assets/imgs/Subtract (1).png"),       /* onda esquerda */
                  url("${pageContext.request.contextPath}/assets/imgs/Subtract.png");         /* onda direita */

          /* controla posição de cada uma */
          background-position: center center, left top, right bottom;

          /* controla tamanho de cada uma */
          background-size: 700px, 400px, 400px;

          background-repeat: no-repeat, no-repeat, no-repeat;

          display: flex;
          justify-content: center;
          align-items: center;
          height: 100vh;
          margin: 0;
          position: relative;
      }

      .login-container {
          background-color: rgba(255, 255, 255, 0.90);
          padding: 30px 40px;
          border-radius: 10px;
          box-shadow: 0 2px 10px rgba(0,0,0,0.1);
          width: 320px;
          position: relative;
          z-index: 2;
      }

      h2 {
      text-align: center;
      margin-bottom: 25px;
      color: #333;
    }
    input[type="email"], input[type="password"] {
      width: 100%;
      padding: 10px;
      margin: 10px 0 20px 0;
      border: 1px solid #2a9fc5;
      border-radius: 5px;
    }
    button {
      width: 107%;
      padding: 10px;
      background-color: #33a28a;
      color: #fff;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      font-size: 16px;
    }
    button:hover {
      background-color: #1a7f87;
    }
    .erro {
      color: red;
      text-align: center;
      margin-bottom: 10px;
    }
    #email:hover{
        border: #45bada solid 2px;
    }
    #senha:hover{
        border: #2a9fc5 solid 2px;
    }
  </style>
</head>
<body>

<div class="login-container">
  <h2>Login (Área restrita)</h2>

  <%
    String erro = (String) request.getAttribute("erroLogin");
    if (erro != null) {
  %>
  <div class="erro"><%= erro %></div>
  <% } %>

  <form action="${pageContext.request.contextPath}/ServletLogin?action=logarAdmin" method="post">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" placeholder="Digite seu email" required>

    <label for="senha">Senha:</label>
    <input type="password" id="senha" name="senha" placeholder="Digite sua senha" required>

    <button type="submit">Entrar</button>
  </form>
</div>
</body>
</html>