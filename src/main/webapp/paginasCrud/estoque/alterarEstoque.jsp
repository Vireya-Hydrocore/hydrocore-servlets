<%--
  Created by IntelliJ IDEA.
  User: eriksilva-ieg
  Date: 05/10/2025
  Time: 01:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Estoque</title>
    <style>
        /* Tela escurecida de fundo */
        .modal-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            display: none; /* fica escondido inicialmente */
            justify-content: center;
            align-items: center;
            z-index: 1000;
        }

        /* Quadrado central */
        .modal-content {
            background: #fff;
            padding: 20px 30px; /* Menos padding vertical para mais espaço para inputs */
            border-radius: 8px;
            width: 600px; /* Um pouco menor que antes */
            max-width: 90%; /* Responsivo para telas pequenas */
            box-shadow: 0 5px 15px rgba(0,0,0,0.3);
            position: relative;
            max-height: 90%; /* Garante que caiba na tela */
            overflow-y: auto; /* Scroll interno se necessário */
        }

        .modal-content h2 {
            margin-top: 0;
        }

        .campos {
            margin-bottom: 15px;
        }

        .campos label {
            display: block;
            margin-bottom: 5px;
        }

        .campos input {
            width: 100%;
            padding: 10px;
            margin-top: 2px;
            box-sizing: border-box;
            font-size: 14px;
        }

        .acoes {
            text-align: right;
            margin-top: 20px; /* Dá um espaçamento do último input */
        }

        .acoes button {
            padding: 8px 16px;
            margin-left: 10px;
        }

        .close-btn {
            position: absolute;
            top: 10px;
            right: 15px;
            cursor: pointer;
            font-size: 18px;
            background: none;
            border: none;
        }
    </style>
</head>
<body>

<!-- Modal -->
<div class="modal-overlay" id="modal">
    <div class="modal-content">
        <button class="close-btn" id="closeModal">&times;</button>
        <h2>Editar Estoque</h2>
        <form>
            <div class="campos">
                <label for="id">ID</label>
                <input type="text" id="id" name="id" readonly value="<% request.getAttribute("id"); %>">
            </div>

            <div class="campos">
                <label for="nomeProduto">Quantidade</label>
                <input type="number" id="nomeProduto" min="0" name="nomeProduto" <% request.getAttribute("quantidade"); %>>
            </div>

            <div class="campos">
                <label for="dataValidade">Data Validade</label>
                <input type="date" id="dataValidade" name="dataValidade" readonly <% request.getAttribute("dataValidade"); %>>
            </div>

            <div class="campos">
                <label for="minPossivelEstocado">Minímo possível estocado</label>
                <input type="number" id="minPossivelEstocado" min="0" name="minPossivelEstocado" <% request.getAttribute("minPossivelEstocado"); %>>
            </div>

            <div class="acoes">
                <button type="button" id="cancelBtn">Cancelar</button>
                <button type="submit">Salvar</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/paginasCrud/scripts/modal.js"></script>

</body>
</html>

