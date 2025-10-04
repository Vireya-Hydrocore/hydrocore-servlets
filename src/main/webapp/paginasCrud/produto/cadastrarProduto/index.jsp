<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <title>Cadastro de Produtos</title>
    <script>
        // Validação de Formulário
        function validar() {
            let nome = frmProduto.nome.value;
            let tipo = frmProduto.tipo.value;
            let unidadeMedida = frmProduto.unidadeMedida.value;
            let concentracao = frmProduto.concentracao.value;

            if (nome === "") {
                alert("Preencha o campo Nome");
                frmProduto.nome.focus();
                return false;
            } else if (tipo === "") {
                alert("Preencha o campo Tipo");
                frmProduto.tipo.focus();
                return false;
            } else if (unidadeMedida === "") {
                alert("Preencha o campo Unidade de Medida");
                frmProduto.unidadeMedida.focus();
                return false;
            } else if (concentracao === "") {
                alert("Preencha o campo Concentração");
                frmProduto.concentracao.focus();
                return false;
            }
            return true; // envia o formulário
        }
    </script>
</head>
<body>
<h1>Cadastrar novo produto no sistema</h1>
<br><br><br>

<form name="frmProduto"
      action="${pageContext.request.contextPath}/ServletProduto"
      method="post"
      onsubmit="return validar()">

    <!-- Campo oculto para informar ao servlet que é CREATE -->
    <input type="hidden" name="action" value="create">

    <label for="nome">Nome: </label>
    <input type="text" id="nome" name="nome" placeholder="Digite o nome do produto" required>
    <br><br>

    <label for="tipo">Tipo: </label>
    <input type="text" name="tipo" id="tipo" placeholder="Digite o tipo do produto (Coagulante, Desinfetante)" required>
    <br><br>


    <label for="unidadeMedida">Unidade de Medida: </label>
    <input type="text" name="unidadeMedida" id="unidadeMedida" placeholder="Digite a unidade de medida (mg/L, %, m³)" required>
    <br><br>

    <label for="concentracao">Concentração: </label>
    <input type="number" name="concentracao" id="concentracao" placeholder="Digite o valor da concentração" required>
    <br><br><br>

    <input type="submit" value="Enviar Resultados">
</form>

</body>
</html>

