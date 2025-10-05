<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 09/09/2025
  Time: 20:55
  To change this template use File | Settings | File Templates.
--%>

<!------------------ MENU GERAL ------------------->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Área Restrita</title>
</head>
<body>
<h1>CRUD VIREYA</h1>
<h2>Selecione a tabela da qual você deseja realizar o CRUD: </h2>

<nav id="botoes">
    <a href="${pageContext.request.contextPath}/paginasCrud/estoque/index.jsp">Funcionario - Em Breve</a>
    <br><br>
    <a href="">Cargo - Em Breve</a>
    <br><br>
    <a href="${pageContext.request.contextPath}/ServletProduto?action=main">Produto</a>
    <br><br>
    <a href="${pageContext.request.contextPath}/ServletEstoque?action=mainEstoque">Estoque</a>
    <br><br>
    <a href="${pageContext.request.contextPath}/paginasCrud/eta/index.jsp">Eta - Em Breve</a>
    <br><br>
    <a href="${pageContext.request.contextPath}/paginasCrud/estoque/index.jsp">Admin - Em Breve</a>
</nav>

</body>
</html>