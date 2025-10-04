<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.servletsvireya.model.Produto" %>
<%
  // Produto passado pelo servlet via request.setAttribute("produto", produto);
  Produto produto = (Produto) request.getAttribute("produto");
%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
  <meta charset="UTF-8">
  <title>Editar Produto</title>
  <script>
    function validarEdicao() {
      let form = document.forms["frmEditar"];
      if(form.nome.value.trim() === "") { alert("Preencha o nome"); form.nome.focus(); return false; }
      if(form.tipo.value.trim() === "") { alert("Preencha o tipo"); form.tipo.focus(); return false; }
      if(form.unidadeMedida.value.trim() === "") { alert("Preencha a unidade de medida"); form.unidadeMedida.focus(); return false; }
      if(form.concentracao.value.trim() === "") { alert("Preencha a concentração"); form.concentracao.focus(); return false; }
      form.submit();
    }
  </script>
</head>
<body>
<h1>Editar Produto</h1>

<!-- Formulário envia para o servlet com POST -->
<form name="frmEditar" action="<%= request.getContextPath() %>/ServletProduto" method="post">
  <input type="hidden" name="action" value="update">
  <input type="hidden" name="id" value="<%= produto.getId() %>">

  <label for="nome">Nome:</label>
  <input type="text" id="nome" name="nome" value="<%= produto.getNome() %>"><br><br>

  <label for="tipo">Tipo:</label>
  <select id="tipo" name="tipo">
    <option value="Coagulante" <%= "Coagulante".equals(produto.getTipo()) ? "selected" : "" %>>Coagulante</option>
    <option value="Floculante" <%= "Floculante".equals(produto.getTipo()) ? "selected" : "" %>>Floculante</option>
    <option value="Outro" <%= "Outro".equals(produto.getTipo()) ? "selected" : "" %>>Outro</option>
    <!-- Adicione outros tipos conforme seu banco -->
  </select><br><br>

  <label for="unidadeMedida">Unidade de Medida:</label>
  <input type="text" id="unidadeMedida" name="unidadeMedida" value="<%= produto.getUnidadeMedida() %>"><br><br>

  <label for="concentracao">Concentração:</label>
  <input type="number" id="concentracao" name="concentracao" step="0.01" value="<%= produto.getConcentracao() %>"><br><br>

  <button type="button" onclick="validarEdicao()">Salvar Alterações</button>
</form>

<!-- Botão para excluir -->

</body>
</html>
