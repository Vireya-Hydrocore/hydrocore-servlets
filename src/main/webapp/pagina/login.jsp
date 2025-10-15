<!DOCTYPE html>
<html lang="pt-br">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Login</title>
  <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/img/vireya_icon.png" type="image/x-icon">
  <link rel="stylesheet" href="/assets/css/login.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body>
<main>
  <div class="container-esquerdo">
    <div class="logo">
      <img src="/assets/img/vireya_icon.png" alt="Logo">
      <span>HydroCore</span>
    </div>
    <h1>Fazer Login</h1>
    <form>
      <label for="email">E-mail</label>
      <input type="text" id="email" name="email" placeholder="Digite seu e-mail" required>

      <label for="senha">Senha</label>
      <div class="input-senha">
        <input type="password" id="senha" name="senha" placeholder="Digite sua senha" required>
        <span class="password-toggle" id="togglePassword">
                        <i class="fas fa-eye"></i>
                    </span>
      </div>
      <button class="botao">Entrar</button>
    </form>
  </div>

  <div class="container-direito">
    <div class="sobreposicao">
      <img src="/assets/img/abstract geometric.png" alt="Background">
    </div>
    <div class="celular">
      <img src="/assets/img/modelo_frontal.png" alt="App Preview">
    </div>
  </div>
</main>
<script src="/assets/js/script.js"></script>
</body>

</html>