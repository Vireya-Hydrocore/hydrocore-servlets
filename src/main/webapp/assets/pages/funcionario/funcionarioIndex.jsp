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
<%@ page import="com.example.servletsvireya.dao.CargoDAO" %>
<%@ page import="com.example.servletsvireya.dto.CargoDTO" %>
<%@ page import="com.example.servletsvireya.dao.EtaDAO" %>
<%@ page import="com.example.servletsvireya.dto.EtaDTO" %>
<%
    // Recupera a lista de funcionarios do request, que deve ser enviada pelo servlet
    List<FuncionarioDTO> lista = (List<FuncionarioDTO>) request.getAttribute("funcionarios");

    boolean alterado = (Boolean) request.getAttribute("alteradoSucesso");
%>
<!DOCTYPE html>
<html lang="pt-BR">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Funcionário</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleCrud.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" type="image/x-icon">
</head>

<body>
<header>
    <button class="menu" id="menu-toggle">☰</button>
    <h1 class="logo">HydroCore</h1>

    <div class="logout-container">
        <form action="${pageContext.request.contextPath}/ServletAdmin" method="post">
            <input type="hidden" name="action" value="logout">
            <a href="${pageContext.request.contextPath}/assets/pages/landingpage/loginAdmin.jsp" class="logout-btn" type="submit" title="Sair">Sair</a>
        </form>
    </div>
</header>

<aside class="sidebar" id="sidebar">
    <ul>
        <a href="${pageContext.request.contextPath}/ServletDashboard">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/iconeDash.png"> Dashboard</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletEta?action=mainEta">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/iconeEtas.png"> ETAs</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletFuncionario?action=mainFuncionario">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/iconeFuncionarios.png"> Funcionários</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletEstoque?action=mainEstoque">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/iconeEstoque.png"> Estoque</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletProduto?action=mainProduto">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/iconeProdutos.png"> Produtos</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletCargo?action=mainCargo">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/iconeCargo.png"> Cargos</li>
        </a>
        <a href="${pageContext.request.contextPath}/ServletAdmin?action=mainAdmin">
            <li><img src="${pageContext.request.contextPath}/assets/imgs/iconeAdmins.png"> Admins</li>
        </a>
    </ul>
</aside>

<div class="plano-de-fundo">
    <main>

        <!-- CADASTRO DE FUNCIONÁRIOS -->

        <section class="cadastro">
            <h2>Cadastro de Funcionários</h2>
            <form name="frmFuncionario" action="${pageContext.request.contextPath}/ServletFuncionario" method="post">
                <input type="hidden" name="action" value="createFuncionario"> <!-- envia esse parametro para o servlet ver q é create-->

                <div class="campos">
                    <label>Nome</label>
                    <input type="text" name="nome" maxlength="80" placeholder="Ex: Iago Eiken" required>
                </div>

                <div class="campos">
                    <label>E-mail</label>
                    <input type="text" name="email" maxlength="40" placeholder="exemplo@gmail.com" required>
                </div>

                <div class="campos">
                    <label>Senha</label>
                    <input type="password" name="senha" maxlength="36" placeholder="senhaForte01@" required>
                </div>

                <div class="campos">
                    <label>Data de Nascimento</label>
                    <input type="date" name="dataNascimento" required>
                </div>

                <div class="campos">
                    <label>Data de Admissão</label>
                    <input type="date" name="dataAdmissao" required>
                </div>

                <div class="campos">
                    <label for="cargo">Cargo</label>
                    <select id="cargo" name="cargo">
                        <option value="">Selecione um cargo</option> <%-- Valor inicial vazio --%>
                        <%-- Listando os cargos disponíveis com cargoDAO --%>
                        <%CargoDAO cargoDAO = new CargoDAO();
                        List<CargoDTO> cargosList = cargoDAO.listarCargos();

                        for (CargoDTO cargo : cargosList){%>
                            <option value="<%= cargo.getId() %>"> <%= cargo.getNome() %> </option> <%-- Mostra o nome e pega o id do cargo --%>
                        <%}%>
                    </select>
                </div>

                <div class="campos">
                    <label for="nomeEta">Nome da ETA</label>
                    <select id="nomeEta" name="nomeEta">
                        <option value="">Selecione uma ETA</option> <%-- Valor inicial vazio --%>
                        <%-- Listando os cargos disponíveis com EtaDAO --%>
                        <%
                            EtaDAO etaDAO = new EtaDAO();
                            List<EtaDTO> etaList = etaDAO.listarEtas();

                            for (EtaDTO eta : etaList){%>
                        <option value="<%= eta.getId() %>"> <%= eta.getNome() %> </option> <%-- Mostra o nome e pega o id da ETA --%>
                        <%}%>
                    </select>
                </div>

                <div class="acoes">
                    <input type="reset" class="botao-redefinir" value="Limpar">
                    <input type="submit" value="Salvar" class="botao-salvar">
                </div>
            </form>
        </section>

        <!-- LISTA DE FUNCIONARIOS -->

        <section class="lista">

            <!-- FILTRO DE FUNCIONÁRIOS -->

            <div class="filtro-topo">
                <h2 class="titulo-lista">Lista de Funcionários</h2>

                <form class="filtro-form" action="${pageContext.request.contextPath}/ServletFuncionario" method="get">
                    <input type="hidden" name="action" value="filtroFuncionario">

                    <label for="colunaSelect">Coluna</label>
                    <select name="nome_coluna" id="colunaSelect">
                        <option value="nome">Nome</option>
                        <option value="email">E-mail</option>
                        <option value="data_admissao">Data de Admissão</option>
                        <option value="data_nascimento">Data de Nascimento</option>
                        <option value="nome_cargo">Cargo</option>
                        <option value="nome_eta">ETA</option>
                    </select>

                    <label for="pesquisa">Pesquisa</label>
                    <input type="text" id="pesquisa" name="pesquisa" placeholder="Buscar...">

                    <a class="botao-redefinir" href="${pageContext.request.contextPath}/ServletFuncionario?action=mainFuncionario">
                        Redefinir filtragem
                    </a>
                    <button type="submit" class="botao-salvar">Aplicar Filtro</button>
                </form>
            </div>

            <script>
                    const colunaSelect = document.getElementById('colunaSelect');
                    const inputPesquisa = document.getElementById('pesquisa');

                    colunaSelect.addEventListener('change', function() {
                        const valor = colunaSelect.value;

                        // Se for uma das opções de data, muda o tipo para "date"
                        if (valor === 'data_admissao' || valor === 'data_nascimento') {
                            inputPesquisa.type = 'date';
                            inputPesquisa.value = ''; // limpa o campo para evitar erro de formato
                        } else {
                            inputPesquisa.type = 'text';
                            inputPesquisa.value = '';
                        }
                    });
                </script>

            <!--LISTA-->
            <div class="tabela-container">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome</th>
                        <th>E-mail</th>
                        <th>Data Admissão</th>
                        <th>Data Nascimento</th>
                        <th>Cargo</th>
                        <th>ETA</th>
                        <th>Ações</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (lista != null && !lista.isEmpty()) {
                        for (int i=0; i < lista.size(); i++) { %>
                    <tr>
                        <td><%= lista.get(i).getId() %></td>
                        <td><%= lista.get(i).getNome() %></td>
                        <td><%= lista.get(i).getEmail() %></td>
                        <td><%= lista.get(i).getDataAdmissao()%></td>
                        <td><%= lista.get(i).getDataNascimento()%></td>
                        <td><%= lista.get(i).getNomeCargo()%></td>
                        <td><%= lista.get(i).getNomeEta()%></td>
                        <td>
                            <a class="botao-editar"
                               href="${pageContext.request.contextPath}/ServletFuncionario?action=selectFuncionario&id=<%= lista.get(i).getId() %>">Editar</a>
                            &nbsp;|&nbsp;
                            <form action="<%= request.getContextPath() %>/ServletFuncionario" method="post" style="display:inline;">
                                <input type="hidden" name="action" value="deleteFuncionario">
                                <input type="hidden" name="id" value="<%= lista.get(i).getId() %>">
                                <button class="botao-excluir" type="submit"
                                        onclick="return confirm('Tem certeza que deseja excluir este funcionário?');">
                                    Excluir
                                </button>
                            </form>
                        </td>
                    </tr>
                    <% }
                    } else { %>
                    <tr>
                        <td colspan="8">Nenhum funcionário encontrado!</td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>

</html>
