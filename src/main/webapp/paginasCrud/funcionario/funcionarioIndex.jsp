<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 04/10/2025
  Time: 23:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.servletsvireya.dto.FuncionarioDTO" %>
<%
    // Recupera a lista de funcionarios do request, que deve ser enviada pelo servlet
    List<FuncionarioDTO> lista = (List<FuncionarioDTO>) request.getAttribute("funcionarios");

    Boolean alterado = (Boolean) request.getAttribute("alteradoSucesso");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Funcionários</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/paginasCrud/css/styleProduto.css">
</head>

<body>
<header>
    <button class="menu" id="menu-toggle">☰</button>
    <h1 class="logo">HydroCore</h1>
    <div class="avatar-container">
        <div class="avatar" id="avatar">
            <div class="dropdown" id="dropdown">
                <ul>
                    <div>
                        <button class="logout">
                            <li><img src="/porta.png">Sair</li>
                        </button>
                    </div>
                </ul>
            </div>
        </div>
    </div>
</header>

<aside class="sidebar" id="sidebar">
    <ul>
        <a href="/index.html">
            <li><img src="/imagem 9.png"> Informações</li>
        </a>
        <a href="/funcionarios.html">
            <li><img src="/image 10.png"> Funcionarios</li>
        </a>
        <a href="/estoque.html">
            <li><img src="/image 11.png"> Estoque</li>
        </a>
        <a href="/produto.html">
            <li><img src="/image 12.png"> Produtos</li>
        </a>
        <a href="/cargo.html">
            <li><img src="/image 13.png"> Cargo</li>
        </a>
    </ul>
</aside>

<div class="plano-de-fundo">
    <main>
        <!-- CADASTRO DE FUNCIONÁRIOS -->
        <section class="cadastro">
            <h2>Cadastro de Funcionários</h2>
            <form name="frmFuncionario" action="${pageContext.request.contextPath}/ServletFuncionario" method="post" onsubmit="return validar();">
                <div class="campos">
                    <input type="hidden" name="action" value="createFuncionario"> <!-- envia esse parametro para o servlet ver q é create-->

                    <label>Nome</label>
                    <input type="text" name="nome" placeholder="Ex: Iago Eiken">
                </div>

                <div class="campos">
                    <label>E-mail</label>
                    <input type="text" name="email">
                </div>

                <div class="campos">
                    <label>Senha</label>
                    <input type="text" name="senha" placeholder="xxxxxxxxxxx">
                </div>

                <div class="campos">
                    <label>Data de Nascimento</label>
                    <input type="date" name="dataNascimento">
                </div>

                <div class="campos">
                    <label>Data de Admissão</label>
                    <input type="date" name="dataAdmissao">
                </div>

                <div class="campos">
                    <label>Cargo</label>
                    <input type="text" name="cargo" placeholder="Ex: Operador">
                </div>

                <div class="acoes">
                    <button type="button" class="botao-cancelar">Cancelar</button>
                    <input type="submit" value="Salvar" class="botao-salvar">
                </div>
            </form>
        </section>

        <!-- LISTA DE FUNCIONARIOS -->
        <section class="lista">
            <h2>Lista de Funcionários</h2>
            <table>
                <thead>
                <th>ID</th>
                <th>Nome</th>
                <th>E-mail</th>
                <th>Senha</th>
                <th>Data Admissão</th>
                <th>Data Nascimento</th>
                <th>Cargo</th>
                <th>Ações</th>
                </thead>
                <tbody>
                <% if (lista != null && !lista.isEmpty()) {
                    for (int i=0; i < lista.size(); i++) { %>
                <tr>
                    <td><%= lista.get(i).getId() %></td>
                    <td><%= lista.get(i).getNome() %></td>
                    <td><%= lista.get(i).getEmail() %></td>
                    <td><%= lista.get(i).getSenha()%></td>
                    <td><%= lista.get(i).getDataAdmissao()%></td>
                    <td><%= lista.get(i).getDataNascimento()%></td>
                    <td><%= lista.get(i).getNomeCargo()%></td>
                    <td>
                        <!-- Botão Editar -->
                        <a href="${pageContext.request.contextPath}/ServletFuncionario?action=editarFuncionario&id=<%= lista.get(i).getId() %>">
                            Editar
                        </a>
                        &nbsp;|&nbsp;
                        <!-- Botão Excluir -->
                        <form action="<%= request.getContextPath() %>/ServletFuncionario" method="get" style="display:inline;">
                            <input type="hidden" name="action" value="deleteFuncionario">
                            <input type="hidden" name="id" value="<%= lista.get(i).getId() %>">
                            <button class="botao-excluir" type="submit" onclick="return confirm('Tem certeza que deseja excluir este funcionário?');">
                                Excluir
                            </button>
                        </form>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="6">Nenhum produto encontrado!</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </section>
    </main>
</div>
<script src="/script.js"></script>
</body>

</html>
