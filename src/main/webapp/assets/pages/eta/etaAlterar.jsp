<%--
  Created by IntelliJ IDEA.
  User: iagodiniz-ieg
  Date: 10/10/2025
  Time: 17:00
  To change this template use File | Settings | File Templates.
--%>

<%@ page import="com.example.servletsvireya.dto.EtaDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    EtaDTO eta = (EtaDTO) request.getAttribute("eta");
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>ETA</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleAlterar.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" type="image/x-icon">
</head>
<body>

<div class="form-container">
    <h2>Alterar ETA</h2>

    <form action="${pageContext.request.contextPath}/ServletEta" method="post">
        <input type="hidden" name="action" value="updateEta">
        <div class="container-maior">
            <div class="box-esquerda">
                <!-- ID do produto -->
                <div class="campos-readonly">
                    <label>ID</label>
                    <input type="number" name="id" value="${eta.id}" readonly>
                </div>

                <!-- Nome -->
                <div class="campos">
                    <label>Nome</label>
                    <input type="text" name="nome" value="${eta.nome}" required>
                </div>

                <!-- Capacidade -->
                <div class="campos">
                    <label>Capacidade</label>
                    <input type="number" name="capacidade" value="${eta.capacidade}">
                </div>

                <!-- Telefone -->
                <div class="campos">
                    <label>Telefone</label>
                    <input type="text" name="telefone" value="${eta.telefone}">
                </div>

                <!-- Telefone -->
                <div class="campos-readonly">
                    <label>CNPJ</label>
                    <input type="text" name="cnpj" value="${eta.cnpj}" readonly>
                </div>
            </div>
            <div class="box-direita">
                <!-- ENDEREÇO -->
                <br>
                <div class="campos">
                    <label>Rua</label>
                    <input type="text" name="rua" maxlength="50" value="${eta.rua}">
                </div>

                <!-- Bairro -->
                <div class="campos">
                    <label>Bairro</label>
                    <input type="text" name="bairro" maxlength="40" value="${eta.bairro}" required>
                </div>

                <!-- Cidade -->
                <div class="campos">
                    <label>Cidade</label>
                    <input type="text" name="cidade" maxlength="40" value="${eta.cidade}" required>
                </div>

                <!-- Estado -->
                <div class="campos">
                    <label for="estado">Estado</label>
                    <select id="estado" name="estado" required>
                        <option value="${eta.estado}">${eta.estado}</option>
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
                </div>

                <!-- Número -->
                <div class="campos">
                    <label>Número</label>
                    <input type="number" name="numero" value="${eta.numero}" required>
                </div>

                <!-- CEP -->
                <div class="campos">
                    <label for="cep">CEP</label>
                    <input type="text" id="cep" name="cep" value="${eta.cep}" pattern="[0-9]{5}-[0-9]{3}" required>
                </div>
            </div>
        </div>

        <!-- Botão -->
        <div class="acoes">
            <input type="submit" value="Salvar Alterações">
        </div>
    </form>
</div>
<script src="${pageContext.request.contextPath}/assets/js/regex.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/mascaraCep.js"></script>

</body>
</html>