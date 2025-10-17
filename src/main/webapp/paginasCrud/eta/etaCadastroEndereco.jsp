<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 11/10/2025
  Time: 11:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Cadastro de Endereço</title>

  <!-- Ícone e estilos -->
  <link rel="shortcut icon" href="${pageContext.request.contextPath}img/vireya icon.png" type="image/x-icon">
  <link rel="stylesheet" href="${pageContext.request.contextPath}pagina/cadastro2.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>

<body>
<main>
  <div class="container-esquerdo">
    <div class="logo">
      <img src="${pageContext.request.contextPath}img/vireya icon.png" alt="Logo HydroCore">
      <span>HydroCore</span>
    </div>

    <h1>Cadastrar Endereço</h1>

    <!-- AQUI: action configurada para o Servlet -->
    <form id="form-endereco" action="${pageContext.request.contextPath}/ServletEndereco" method="post">
      <input type="hidden" name="action" value="createEndereco">

      <label for="cep">CEP</label>
      <input type="text" id="cep" name="cep" placeholder="Digite seu CEP" pattern="[0-9]{5}-[0-9]{3}" required>
      <div id="erroCEP" class="erro"></div>

      <label for="logradouro">Logradouro</label>
      <input type="text" id="logradouro" name="logradouro" placeholder="Ex: Rua das Flores" required>
      <div id="erroLogradouro" class="erro"></div>

      <label for="numero">Número</label>
      <input type="text" id="numero" name="numero" placeholder="Ex: 123" required>
      <div id="erroNumero" class="erro"></div>

      <label for="complemento">Complemento</label>
      <input type="text" id="complemento" name="complemento" placeholder="Apartamento, bloco, etc.">

      <label for="bairro">Bairro</label>
      <input type="text" id="bairro" name="bairro" placeholder="Ex: Centro" required>
      <div id="erroBairro" class="erro"></div>

      <label for="cidade">Cidade</label>
      <input type="text" id="cidade" name="cidade" placeholder="Ex: São Paulo" required>
      <div id="erroCidade" class="erro"></div>

      <label for="estado">Estado</label>
      <select id="estado" name="estado" required>
        <option value="">Selecione o estado</option>
        <option value="AC">Acre</option>
        <option value="AL">Alagoas</option>
        <option value="AP">Amapá</option>
        <option value="AM">Amazonas</option>
        <option value="BA">Bahia</option>
        <option value="CE">Ceará</option>
        <option value="DF">Distrito Federal</option>
        <option value="ES">Espírito Santo</option>
        <option value="GO">Goiás</option>
        <option value="MA">Maranhão</option>
        <option value="MT">Mato Grosso</option>
        <option value="MS">Mato Grosso do Sul</option>
        <option value="MG">Minas Gerais</option>
        <option value="PA">Pará</option>
        <option value="PB">Paraíba</option>
        <option value="PR">Paraná</option>
        <option value="PE">Pernambuco</option>
        <option value="PI">Piauí</option>
        <option value="RJ">Rio de Janeiro</option>
        <option value="RN">Rio Grande do Norte</option>
        <option value="RS">Rio Grande do Sul</option>
        <option value="RO">Rondônia</option>
        <option value="RR">Roraima</option>
        <option value="SC">Santa Catarina</option>
        <option value="SP">São Paulo</option>
        <option value="SE">Sergipe</option>
        <option value="TO">Tocantins</option>
      </select>
      <div id="erroEstado" class="erro"></div>

      <button class="botao" type="submit">Salvar Endereço</button>
    </form>
  </div>

  <div class="container-direito">
    <div class="sobreposicao">
      <img src="${pageContext.request.contextPath}img/abstract geometric.png" alt="Fundo">
    </div>
    <div class="celular">
      <img src="${pageContext.request.contextPath}img/modelo_frontal.png" alt="Celular">
    </div>
  </div>
  <script src="${pageContext.request.contextPath}/paginasCrud/eta/js/regex.js"></script>
  <script src="${pageContext.request.contextPath}/paginasCrud/eta/js/script.js"></script>
</main>
</body>
</html>

