<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sobre Nós</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" type="image/x-icon">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styleSobreNos.css">
</head>
<body>
<header>
    <div class="title-logo">
        <img src="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png">
        <h1>HydroCore</h1>
    </div>
    <div class="topics-head" id="menu">
        <nav>
            <ul>
                <li><a href="${pageContext.request.contextPath}/index.jsp">Início</a></li>
                <li><a id="bottom-login" href="${pageContext.request.contextPath}/assets/pages/landingpage/login.jsp">Login</a></li>
            </ul>
        </nav>
    </div>
    <button id="btn-hamb">☰</button>
</header>

<section class="empresa">
    <h2>Sobre a Empresa</h2>
    <p>
        A Vireya é uma startup comprometida com a automação inteligente no tratamento de água,
        focando na sustentabilidade, redução de desperdícios e otimização dos processos industriais.
        Nosso objetivo é unir tecnologia e impacto ambiental positivo, garantindo eficiência e confiabilidade
        sem o uso de sensores físicos, mas com dados e inteligência.
    </p>
</section>

<section class="missao-visao-valores">
    <div class="card mvv">
        <h3>Missão</h3>
        <p>Promover inovação e sustentabilidade em processos de tratamento de água.</p>
    </div>
    <div class="card mvv">
        <h3>Visão</h3>
        <p>Ser referência em soluções tecnológicas que preservam recursos naturais e otimizam operações industriais.</p>
    </div>
    <div class="card mvv">
        <h3>Valores</h3>
        <p>Inovação, sustentabilidade, eficiência, inclusão e responsabilidade ambiental.</p>
    </div>
</section>

<section class="equipe">
    <h2>Nossa Equipe</h2>
    <div class="grid-equipe">
        <div class="membro" style="--delay: 0s">
            <img src="${pageContext.request.contextPath}/assets/imgs/erik.png" alt="Erik Felipe">
            <h3>Erik Santos</h3>
            <p><p>Desenvolvedor Back-end, responsável pelo tratamento dos dados usando Servlets e das telas para JSP.</p></p>
        </div>
        <div class="membro" style="--delay: 0.2s">
            <img src="${pageContext.request.contextPath}/assets/imgs/mayumi.png" alt="Mayumi Itikawa">
            <h3>Mayumi Kimura</h3>
            <p>Designer de UX, responsável por toda a experiência do usuário.</p>
        </div>
        <div class="membro" style="--delay: 0.4s">
            <img src="${pageContext.request.contextPath}/assets/imgs/guilherme.png" alt="Guilherme Guedes">
            <h3>Guilherme Guedes</h3>
            <p>Desenvolvedor BackEnd, responsável pelo JDBC, criação de Servlets e validação de dados da aplicação.</p>
        </div>
        <div class="membro" style="--delay: 0s">
            <img src="${pageContext.request.contextPath}/assets/imgs/herrera.png" alt="Enzo Herrera">
            <h3>Enzo Herrera</h3>
            <p>Desenvolvedor front-end, responsável por CSS, HTML e algumas telas no Figma. </p>
        </div>
        <div class="membro" style="--delay: 0.4s">
            <img src="${pageContext.request.contextPath}/assets/imgs/caio.png" alt="Caio">
            <h3>Caio Eiken</h3>
            <p>Engenheiro de dados, responsável por PROMPT e pseudominização.</p>
        </div>
        <div class="membro" style="--delay: 0.2s">
            <img src="${pageContext.request.contextPath}/assets/imgs/iago.png" alt="Iago">
            <h3>Iago Balbino</h3>
            <p>Especialista em banco de dados, responsável pelo modelo conceitual, scripts e data loader.</p>
        </div>
        <div class="membro" style="--delay: 0.4s">
            <img src="${pageContext.request.contextPath}/assets/imgs/casa.png" alt="Pedro Casarini">
            <h3>Pedro Casarini</h3>
            <p>Criador do modelo lógico e dos fluxogramas do projeto.</p>
        </div>
        <div class="membro" style="--delay: 0.2s">
            <img src="${pageContext.request.contextPath}/assets/imgs/gabriel.png" alt="Gabriel Andozia">
            <h3>Gabriel Andozia</h3>
            <p>Analista de sistemas operacionais e planilhas, responsável por SOP e Excel.</p>
        </div>
        <div class="membro" style="--delay: 0s">
            <img src="${pageContext.request.contextPath}/assets/imgs/clara.png" alt="Clara">
            <h3>Clara Bartolini</h3>
            <p>Desenvolvedora Mobile, Backend, responsável pelo fluxo do aplicativo</p>
        </div>
        <div class="membro" style="--delay: 0s">
            <img src="${pageContext.request.contextPath}/assets/imgs/vitor.png" alt="Vitor">
            <h3>Vitor Ponciano</h3>
            <p>Desenvolvedor web e responsável pela engenharia da solução.</p>
        </div>
        <div class="membro" style="--delay: 0.2s">
            <img src="${pageContext.request.contextPath}/assets/imgs/lins.png" alt="Leonardo Lins">
            <h3>Leonardo Lins</h3>
            <p>Desenvolvedor BackEnd, responsável por tratar informações vindas do banco e devolver para os usuário por meio de APIs</p>
        </div>
        <div class="membro" style="--delay: 0.4s">
            <img src="${pageContext.request.contextPath}/assets/imgs/rodrigo.png" alt="Rodrigo">
            <h3>Rodrigo</h3>
            <p>Desenvolvedor mobile e responsável pelo fluxo do aplicativo</p>
        </div>
        <div class="membro" style="--delay: 0s">
            <img src="${pageContext.request.contextPath}/assets/imgs/trindade.png" alt="Guilherme Trindade">
            <h3>Guilherme Trindade</h3>
            <p>Analista de Dados do projeto, responsável por conduzir a análise exploratória dos dados e desenvolver painéis interativos no Power BI.</p>
        </div>
        <div class="membro" style="--delay: 0s">
            <img src="${pageContext.request.contextPath}/assets/imgs/costa.png" alt="Guilherme Costa">
            <h3>Guilherme Costa</h3>
            <p>Cientista de dados do projeto, responsável pelo Robô de coleta de dados e criação do modelo de machine learning para o cálculo de potabilidade da água.</p>
        </div>
        <div class="membro" style="--delay: 0.4s">
            <img src="${pageContext.request.contextPath}/assets/imgs/frossard.png" alt="Pedro Frossard">
            <h3>Pedro Frossard</h3>
            <p>Interessado em análise e interpretação de dados, com foco em transformar informações em insights estratégicos.</p>
        </div>
        <div class="membro" style="--delay: 0.4s">
            <img src="${pageContext.request.contextPath}/assets/imgs/fernando.png" alt="Fernando">
            <h3>Fernando Henrique</h3>
            <p>Interessado em análise e interpretação de dados, com foco em transformar informações em insights estratégicos.</p>
        </div>
    </div>
</section>

<footer>
    <div class="grid-footer">
        <div class="links-grid">
            <img id="img-foot" src="${pageContext.request.contextPath}/assets/imgs/vireya_icon.png" alt="Logo Vireya">
            <nav class="nav-footer">
                <p class="title-points">Ferramentas</p>
                <ul>
                    <li>Dashboard</li>
                    <li>Calculadora Química</li>
                    <li>Controle de Estoque</li>
                    <li>Tratamento de Água</li>
                    <li>Criação de Relatórios</li>
                    <li>Controle de Atividades e Avisos</li>
                </ul>
            </nav>

            <nav class="nav-footer">
                <p class="title-points">Contatos</p>
                <ul>
                    <li>(11) 99286-4575</li>
                    <li>Instituto J&F</li>
                    <li>@vireyabrasil</li>
                    <li>R. Irineu José Bordon, 335 - Parque Anhanguera, São Paulo - SP, 05120-060</li>
                </ul>
            </nav>

            <nav class="nav-footer">
                <p class="title-points">Outros</p>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/assets/pages/landingpage/sobreNos.jsp">Sobre Nós</a></li>
                    <li><a href="${pageContext.request.contextPath}/assets/pages/landingpage/loginAdmin.jsp">Área Restrita</a></li>
                </ul>
            </nav>
        </div>

        <hr>

        <div class="rodape-footer">
            <div id="direitos-cont">
                <p>© 2025 Vireya. Todos os direitos reservados.</p>
            </div>
            <div id="icones-midias">
                <!-- Instagram -->
                <div id="dist-icon">
                    <a href="https://instagram.com" class="icon" target="_blank">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path d="M7.8 2h8.4A5.8 5.8 0 0 1 22 7.8v8.4A5.8 5.8 0 0 1 16.2 22H7.8A5.8 5.8 0 0 1 2 16.2V7.8A5.8 5.8 0 0 1 7.8 2Zm0 2C6 4 4 6 4 7.8v8.4C4 18 6 20 7.8 20h8.4c1.8 0 3.8-2 3.8-3.8V7.8C20 6 18 4 16.2 4H7.8Zm8.7 1.6a1.2 1.2 0 1 1 0 2.4 1.2 1.2 0 0 1 0-2.4Zm-4.5 2.4a4.2 4.2 0 1 1 0 8.4 4.2 4.2 0 0 1 0-8.4Zm0 2a2.2 2.2 0 1 0 0 4.4 2.2 2.2 0 0 0 0-4.4Z"/>
                        </svg>
                    </a>
                </div>

                <!-- LinkedIn -->
                <div class="dist-icon">
                    <a href="https://linkedin.com" class="icon" target="_blank">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path d="M4.98 3.5c0 1.38-1.11 2.5-2.48 2.5A2.5 2.5 0 0 1 0 3.5C0 2.12 1.11 1 2.5 1S5 2.12 5 3.5zM.5 23h4V7.98h-4V23zM8 7.98h3.8v2.06h.05c.53-1 1.83-2.06 3.77-2.06 4.03 0 4.77 2.65 4.77 6.1V23h-4v-6.52c0-1.56-.03-3.57-2.17-3.57-2.18 0-2.51 1.7-2.51 3.46V23H8V7.98z"/>
                        </svg>
                    </a>
                </div>

                <!-- WhatsApp -->
                <div class="dist-icon">
                    <a href="https://wa.me/5511999999999" class="icon" target="_blank">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path d="M17.47 14.3c-.27-.14-1.63-.8-1.88-.9-.25-.09-.43-.14-.62.14-.19.27-.71.9-.88 1.08-.16.18-.33.2-.6.07-.27-.14-1.14-.42-2.18-1.35-.8-.71-1.34-1.6-1.5-1.87-.16-.27-.02-.42.12-.55.12-.12.27-.33.41-.49.14-.16.18-.27.27-.45.09-.18.05-.34-.02-.48-.07-.14-.62-1.5-.85-2.05-.22-.53-.45-.46-.62-.47h-.53c-.18 0-.48.07-.73.34-.25.27-.96.94-.96 2.3 0 1.36.98 2.67 1.12 2.85.14.18 1.93 2.95 4.68 4.14.65.28 1.15.45 1.55.58.65.21 1.23.18 1.69.11.52-.08 1.63-.67 1.86-1.31.23-.64.23-1.19.16-1.31-.07-.12-.25-.2-.52-.34Z"/>
                        </svg>
                    </a>
                </div>

                <!-- GitHub -->
                <div class="dist-icon">
                    <a href="https://github.com" target="_blank" id="icon" class="icon">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path d="M12 .5C5.7.5.5 5.7.5 12c0 5.1 3.3 9.4 7.9 10.9.6.1.8-.2.8-.5v-1.7c-3.2.7-3.9-1.5-3.9-1.5-.5-1.3-1.2-1.7-1.2-1.7-1-.7.1-.7.1-.7 1.1.1 1.7 1.2 1.7 1.2 1 .1.7 2.2 3.7 1.6v-1.6c-2.6-.3-5.3-1.3-5.3-6 0-1.3.5-2.3 1.2-3.2-.1-.3-.5-1.4.1-2.9 0 0 1-.3 3.3 1.2a11.5 11.5 0 0 1 6 0c2.3-1.5 3.3-1.2 3.3-1.2.6 1.5.2 2.6.1 2.9.8.9 1.2 1.9 1.2 3.2 0 4.7-2.7 5.7-5.3 6v1.7c0 .3.2.6.8.5 4.6-1.5 7.9-5.8 7.9-10.9C23.5 5.7 18.3.5 12 .5Z"/>
                        </svg>
                    </a>
                </div>
            </div>
        </div>
    </div>
</footer>

<script>
    document.addEventListener("DOMContentLoaded", () => {
        const cards = document.querySelectorAll(".membro");
        cards.forEach((card, index) => {
            card.style.animationDelay = ${index * 0.2}s;
        });
    });
</script>

</body>
</html>