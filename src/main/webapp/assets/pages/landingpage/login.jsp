<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" type="image/x-icon">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body>
<main>
  <div class="container-esquerdo">
    <div class="logo">
      <img src="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" alt="Logo">
      <span>HydroCore</span>
    </div>
    <h1>Fazer Login (ETA)</h1>
    <form action="${pageContext.request.contextPath}/ServletLogin?action=logar" method="post">
      <label for="email">E-mail</label>
      <input type="text" id="email" name="email" placeholder="Digite seu e-mail" required>
      <label for="senha">Senha</label>
      <div class="input-senha">
        <input name="senha" type="password" id="senha" placeholder="Digite sua senha" required
               title="Tem que incluir pelo menos uma letra maiúscula e um caractere especial(!@#$%)">
        <span class="password-toggle" id="togglePassword">
                        <i class="fas fa-eye"></i>
        </span>
      </div>
      <button class="botao">Entrar</button>

      <p class="login-text"> Não possui conta? <a href="${pageContext.request.contextPath}/assets/pages/landingpage/cadastro.jsp">Clique aqui</a></p>
    </form>
  </div>

  <div class="container-direito">
    <div class="sobreposicao">
      <img src="${pageContext.request.contextPath}/assets/imgs/fundoDegrade.webp" alt="Background">
    </div>
    <div class="logo-direita">
      <img src="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" class="logo-girando" alt="App Preview">
    </div>
  </div>
</main>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/regex.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/mostrarSenha.js"></script>
</body>
</html>