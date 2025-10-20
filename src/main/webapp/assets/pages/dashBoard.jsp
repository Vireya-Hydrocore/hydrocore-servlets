<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 20/10/2025
  Time: 06:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%
    Map<String, Integer> lista = (Map<String, Integer>) request.getAttribute("dashboard");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vireya</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleDash.css">
</head>

<body>
<header>
    <button class="menu" id="menu-toggle">☰</button>
    <h1 class="logo">HydroCore</h1>

    <div class="logout-container">
        <form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
            <input type="hidden" name="action" value="logout">
            <a href="${pageContext.request.contextPath}/assets/pages/landingpage/loginAdmin.jsp" class="logout-btn" type="submit"
               title="Sair">Sair</a>
        </form>
    </div>
</header>

<aside class="sidebar" id="sidebar">
    <ul>
        <a href="${pageContext.request.contextPath}/ServletEta?action=mainEta">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/imagem9.png"> ETAs</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletFuncionario?action=mainFuncionario">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/image10.png"> Funcionarios</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletEstoque?action=mainEstoque">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/image11.png"> Estoque</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletProduto?action=mainProduto">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/image12.png"> Produtos</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletCargo?action=mainCargo">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/image13.png"> Cargo</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletAdmin?action=mainAdmin">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/icons8-admin-settings-male-16.png"> Admin</li>
        </a>
        <a href="${pageContext.request.contextPath}/dashAnalise">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/icons8-painel-de-controle-16.png"> DashBoard</li>
        </a>
    </ul>
</aside>

<div class="plano-de-fundo">

    <main>
        <section class="dashboard">
            <h1>DashBoard</h1>
            <br>
            <hr>
            <br>

            <div class="container">
                <div class="blocos">
                    <h2>Quantidade Admin</h2>
                    <hr>
                    <p><%= lista.get("admin") %></p>
                </div>

                <div class="blocos">
                    <h2>Total Cargos</h2>
                    <hr>
                    <p><%= lista.get("cargo")%></p>
                </div>

                <div class="blocos">
                    <h2>Quantidade Estoques</h2>
                    <hr>
                    <p><%= lista.get("estoque")%></p>
                </div>

                <div class="blocos">
                    <h2>Total ETAs</h2>
                    <hr>
                    <p><%= lista.get("eta")%></p>
                </div>

                <div class="blocos">
                    <h2>Quantidade Funcionários</h2>
                    <hr>
                    <p><%= lista.get("funcionario")%></p>
                </div>

                <div class="blocos">
                    <h2>Quantidade Produtos</h2>
                    <hr>
                    <p><%= lista.get("produto")%></p>
                </div>
            </div>

        </section>
    </main>
</div>
<!-- Script -->
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>

</html>