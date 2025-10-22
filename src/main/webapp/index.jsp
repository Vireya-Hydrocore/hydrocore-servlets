<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vireya | HydroCore</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/style.css">
</head>

<body>
<header>
    <div class="title-logo">
        <h1>Hydrocore</h1>
    </div>
    <div class="topics-head" id="menu">
        <nav>
            <ul>
                <li><a href="#">Início</a></li>
                <li><a href="#funcionalidade-title">Funcionalidades</a></li>
                <li><a href="#sobre-nos">Sobre nós</a></li>
                <li><a href="#planos">Planos</a></li>
                <li><a id="bottom-cadastro" href="${pageContext.request.contextPath}/assets/pages/landingpage/login.jsp">Login</a></li>
            </ul>
        </nav>
    </div>
    <button id="btn-hamb">☰</button>
</header>
<main>
    <div class="frame-img">
        <div id="introducao">
            <div class="container-block">
                <h2>Gestão inteligente para tratamento de água industrial</h2>
                <p>Simplifique processos, reduza custos e aumente a eficiência da sua ETA com a tecnologia da
                    HydroCore.</p>
            </div>
        </div>
    </div>


    <div id="beneficios">
        <h2>Benefícios</h2>
        <p id="text-expln">Com a HydroCore, o tratamento de água se torna mais eficiente e organizado. O app reduz
            erros manuais, otimiza o uso de produtos químicos, <br>
            facilita a gestão de tarefas e gera relatórios confiáveis. Assim, indústrias ganham em economia,
            sustentabilidade e qualidade no processo.</p>
        <div class="container-beneficios">
            <div class="container-caixa">
                <h3>Calculadora Química</h3>
                <p>Controle preciso das dosagens de produtos químicos para manter a qualidade da água e reduzir
                    desperdícios.</p>
            </div>
            <div class="container-caixa">
                <h3>Reversão Química</h3>
                <p>Geração automática de relatórios de reversão de filtros, com base em parâmetros de consumo de
                    água e qualidade.</p>
            </div>
            <div class="container-caixa">
                <h3>Relatórios e Histórico</h3>
                <p>Tenha dados completos sobre conformidade ambiental e sanitária, sempre prontos para auditorias.
                </p>
            </div>
            <div class="container-caixa">
                <h3>Tarefas e Produtividade</h3>
                <p>Monitore atividades da equipe em tempo real e mantenha a operação da ETA sempre sob controle.</p>
            </div>
        </div>
    </div>

    <section class="funcionalidades-section">
        <h2 id="funcionalidade-title">Funcionalidades Principais</h2>
        <p id="funcionalidade-sub">Tornamos o tratamento de água mais eficiente ao integrar dados, processos e
            pessoas em uma única plataforma.</p>
        <div class="container-funcoes">
            <div class="linha-img-dir">
                <div class="infos">
                    <h2>Calculadora Química</h2>
                    <p>A calculadora química da HydroCore permite que os operadores insiram dados como volume de
                        água e parâmetros de qualidade
                        para que o sistema indique automaticamente o tipo e a quantidade correta de produto químico
                        a ser utilizado. Isso evita desperdícios,
                        garante mais precisão no tratamento e reduz o risco de falhas que poderiam comprometer a
                        qualidade da água.</p>
                </div>
                <div class="img">
                    <img src="<%= request.getContextPath() %>/assets/imgs/6853976-uma-cientista.jpg" alt="mulher fazendo calculos com quimicos">
                </div>
            </div>
            <div class="linha-img reverse">
                <div class="infos">
                    <h2>Controle de Estoque</h2>
                    <p>Com o controle de estoque, todos os produtos químicos da ETA ficam registrados e organizados
                        dentro do app.
                        O sistema atualiza em tempo real as quantidades disponíveis, avisa quando algum item está
                        acabando e ajuda a planejar compras com antecedência.
                        Assim, a indústria nunca corre o risco de ficar sem insumos importantes para o processo de
                        tratamento.</p>
                </div>
                <div class="img">
                    <img src="<%= request.getContextPath() %>/assets/imgs/Sistema-de-gestao-de-estoque-1024x489.jpg.webp" alt="Homen com tablete em um armazem de estoque">
                </div>
            </div>
            <div class="linha-img-dir">
                <div class="infos">
                    <h2>Gestão de Tarefas</h2>
                    <p>A funcionalidade de tarefas distribui automaticamente as atividades entre os funcionários,
                        deixando claro quem deve fazer o quê em cada etapa do processo.
                        Isso aumenta a organização da equipe, evita retrabalho e garante que todas as etapas do
                        tratamento sejam realizadas dentro do prazo, com acompanhamento do gestor em tempo real.</p>
                </div>
                <div class="img">
                    <img src="<%= request.getContextPath() %>/assets/imgs/gestao-de-tarefas-em-telas-diferntes-para-acompanhamento-de-projeto.jpg" alt="pessoa fazendo gestão de atividades">
                </div>
            </div>
            <div class="linha-img reverse">
                <div class="infos">
                    <h2>Histórico e Relatórios</h2>
                    <p>Todas as informações registradas no app ficam salvas em um histórico detalhado, que pode ser
                        transformado em relatórios e gráficos completos.
                        Essa ferramenta ajuda tanto no acompanhamento interno do desempenho da ETA quanto no
                        cumprimento de exigências legais,
                        já que os dados podem ser utilizados em auditorias e fiscalizações ambientais.</p>
                </div>
                <div class="img">
                    <img src="<%= request.getContextPath() %>/assets/imgs/analyst-uses-computer-dashboard-data-600nw-2285412737.webp" alt="">
                </div>
            </div>
        </div>
    </section>

    <section id="porque-section">
        <div id="porque">
            <h2>Por que escolher HydroCore?</h2>
            <p>Nossa solução foi desenvolvida especificamente para o setor de tratamento de água, atendendo às
                necessidades reais das ETAs.</p>
            <div class="grid-porques">
                <div class="block-porque">
                    <h3>Gestão Completa de ETAs</h3>
                    <br>
                    <p>A HydroCore integra todas as etapas do tratamento de água em um único sistema. Desde o
                        controle de qualidade da água até a organização das equipes, você acompanha cada detalhe em
                        tempo real, garantindo eficiência e visão completa da operação.</p>
                </div>
                <div class="block-porque">
                    <h3>Automatização de Processos</h3>
                    <br>
                    <p>Os registros manuais dão lugar a processos automatizados que reduzem erros, aceleram tarefas
                        e aumentam a segurança da operação. Com tudo centralizado, gestores ganham mais
                        tempo e confiabilidade nos resultados.</p>
                </div>
                <div class="block-porque">
                    <h3>Conformidade com a Legislação</h3>
                    <br>
                    <p>Nossa solução ajuda a manter sua ETA em conformidade com as normas ambientais e de saúde. Os
                        relatórios automáticos garantem transparência, evitam multas e reforçam a qualidade da água
                        entregue à comunidade.</p>
                </div>
                <div class="block-porque">
                    <h3>Escalabilidade</h3>
                    <br>
                    <p>Seja em pequenas ETAs ou em grandes indústrias, o HydroCore se adapta ao porte da operação. A
                        plataforma foi criada para crescer junto com o negócio, acompanhando a demanda sem perder
                        desempenho ou eficiência.</p>
                </div>
                <div class="block-porque">
                    <h3>Equipe Especializada</h3>
                    <br>
                    <p>O desenvolvimento do HydroCore combina a visão prática de quem vive o dia a dia das ETAs com
                        a inovação da tecnologia. Essa união garante soluções acessíveis, reais e eficientes para os
                        desafios do setor.</p>
                </div>
                <div class="block-porque">
                    <h3>Gestão Sustentável</h3>
                    <br>
                    <p>Com foco em sustentabilidade, promovemos o uso inteligente da água e o tratamento adequado
                        dos efluentes. O controle preciso reduz desperdícios, melhora a eficiência e minimiza os
                        impactos ambientais.</p>
                </div>
            </div>
        </div>
    </section>

    <section id="sobre-nos">
        <img id="img-nos" src="${pageContext.request.contextPath}/assets/imgs/Imagem do WhatsApp de 2025-09-05 à(s) 16.04.10_f34500b8.jpg" alt="">
        <div class="sobrenos-content">
            <h2>Sobre Nós</h2>
            <p>A HydroCore nasceu da união entre especialistas em tratamento de água e desenvolvedores de
                tecnologia. Nosso objetivo é oferecer uma solução que realmente atenda às necessidades das ETAs,
                simplificando processos, aumentando a produtividade e garantindo água de qualidade. Acreditamos que
                a tecnologia é a chave para preservar os recursos hídricos e construir um futuro sustentável</p>
            <a id="link-saibamais" href="${pageContext.request.contextPath}/assets/pages/landingpage/sobreNosReal.jsp">Saiba mais</a>
        </div>
    </section>

    <div id="planos">
        <div class="container-planos">
            <h2>Planos Preço</h2>
            <p>Escolha o plano ideal para as necessidades e velocidades da sua estação de tratamento de água.</p>
            <div class="grid-planos">
                <div class="card-assinar">
                    <h3>Starter</h3>
                    <p>Ideal para pequenas ETAs</p>
                    <p class="preco">R$ 199,00 /mês</p>
                    <ul>
                        <li>Gestão de usuários</li>
                        <li>Escolha e compra</li>
                        <li>Cadastro de funcionários</li>
                        <li>Permissões por tela</li>
                        <li>Parâmetros da água</li>
                        <li>Cálculo de químicos</li>
                        <li>Estoque de químicos</li>
                        <li>Entrada/saída estoque</li>
                        <li>Avisos na Home</li>
                    </ul>
                    <button onclick="window.location.href='${pageContext.request.contextPath}/assets/pages/landingpage/login.jsp'">//ao clicar no botão é enviado para a pagina de login
                        Assinar Agora
                    </button>
                </div>
                <div class="card-assinar-highlit">
                    <h3>Professional</h3>
                    <p>Ideal para médias e maiores ETAs</p>
                    <p class="preco">R$ 599,90 /mês</p>
                    <ul>
                        <li>Tudo do Starter</li>
                        <li>Dashboard com gráficos</li>
                        <li>Dados dinâmicos</li>
                        <li>Tarefas da equipe</li>
                        <li>Calendário de avisos</li>
                        <li>Exportar para Excel</li>
                        <li>Relatórios legais</li>
                        <li>Edição de perfil</li>
                        <li>Preferências do usuário</li>
                    </ul>
                    <button onclick="window.location.href='${pageContext.request.contextPath}/assets/pages/landingpage/login.jsp'">//ao clicar no botão é enviado para a pagina de login
                        Assinar Agora
                    </button>
                </div>
                <div class="card-assinar">
                    <h3>Enterprise</h3>
                    <p>Para grandes corporações</p>
                    <p class="preco">R$ 1599,00 /mês</p>
                    <ul>
                        <li>Tudo do Professional</li>
                        <li>Dashboard avançado</li>
                        <li>Análises preditivas</li>
                        <li>Comparações mensais</li>
                        <li>Relatórios automáticos</li>
                        <li>Alertas inteligentes</li>
                        <li>Integração do estoque</li>
                        <li>Controle de custos</li>
                        <li>Gestão avançada</li>
                    </ul>
                    <button onclick="window.location.href='${pageContext.request.contextPath}/assets/pages/landingpage/login.jsp'">//ao clicar no botão é enviado para a pagina de login
                        Assinar Agora
                    </button>
                </div>
            </div>
        </div>
    </div>

</main>

<footer>
    <div class="grid-footer">
        <div class="links-grid">
            <img id="img-foot" src="<%= request.getContextPath() %>/assets/imgs/Group 1.png" alt="">
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
                    <li>(11) 95795-9848</li>
                    <li>Institutuo J&F</li>
                    <li>@vireyabrasil</li>
                    <li>Rua Dolore Duran 38, Rochdale - SP</li>
                </ul>
            </nav>

            <nav class="nav-footer">
                <p class="title-points">Outros</p>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/assets/pages/landingpage/sobreNosReal.jsp">Sobre nós</a></li>
                    <li><a href="${pageContext.request.contextPath}/assets/pages/landingpage/loginAdmin.jsp">Área Restrita</a></li>
                </ul>
            </nav>
        </div>

        <hr>
        <div class="rodape-footer">
            <div id="direitos-cont">
                <p>© 2025 HydroCore. Todos os direitos reservados.</p>
            </div>
            <div id="icones-midias">
                <div class="dist-icon">
                    <a href="https://instagram.com" id="icon" target="_blank" class="icon">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path
                                    d="M7.8 2h8.4A5.8 5.8 0 0 1 22 7.8v8.4A5.8 5.8 0 0 1 16.2 22H7.8A5.8 5.8 0 0 1 2 16.2V7.8A5.8 5.8 0 0 1 7.8 2Zm0 2C6 4 4 6 4 7.8v8.4C4 18 6 20 7.8 20h8.4c1.8 0 3.8-2 3.8-3.8V7.8C20 6 18 4 16.2 4H7.8Zm8.7 1.6a1.2 1.2 0 1 1 0 2.4 1.2 1.2 0 0 1 0-2.4Zm-4.5 2.4a4.2 4.2 0 1 1 0 8.4 4.2 4.2 0 0 1 0-8.4Zm0 2a2.2 2.2 0 1 0 0 4.4 2.2 2.2 0 0 0 0-4.4Z" />
                        </svg>
                    </a>
                </div>

                <div class="dist-icon">
                    <a href="https://linkedin.com" class="icon" target="_blank">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path
                                    d="M4.98 3.5c0 1.38-1.11 2.5-2.48 2.5A2.5 2.5 0 0 1 0 3.5C0 2.12 1.11 1 2.5 1S5 2.12 5 3.5zM.5 23h4V7.98h-4V23zM8 7.98h3.8v2.06h.05c.53-1 1.83-2.06 3.77-2.06 4.03 0 4.77 2.65 4.77 6.1V23h-4v-6.52c0-1.56-.03-3.57-2.17-3.57-2.18 0-2.51 1.7-2.51 3.46V23H8V7.98z" />
                        </svg>
                    </a>
                </div>

                <div class="dist-icon">
                    <a href="https://wa.me/5511999999999" class="icon" target="_blank">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path
                                    d="M17.47 14.3c-.27-.14-1.63-.8-1.88-.9-.25-.09-.43-.14-.62.14-.19.27-.71.9-.88 1.08-.16.18-.33.2-.6.07-.27-.14-1.14-.42-2.18-1.35-.8-.71-1.34-1.6-1.5-1.87-.16-.27-.02-.42.12-.55.12-.12.27-.33.41-.49.14-.16.18-.27.27-.45.09-.18.05-.34-.02-.48-.07-.14-.62-1.5-.85-2.05-.22-.53-.45-.46-.62-.47h-.53c-.18 0-.48.07-.73.34-.25.27-.96.94-.96 2.3 0 1.36.98 2.67 1.12 2.85.14.18 1.93 2.95 4.68 4.14.65.28 1.15.45 1.55.58.65.21 1.23.18 1.69.11.52-.08 1.63-.67 1.86-1.31.23-.64.23-1.19.16-1.31-.07-.12-.25-.2-.52-.34ZM12.01 2a9.95 9.95 0 0 0-8.5 15.1l-1.8 5.28 5.44-1.78a9.96 9.96 0 0 0 4.86 1.23h.01c5.5 0 9.97-4.47 9.98-9.96A9.95 9.95 0 0 0 12 2Zm0 18.2c-1.53 0-3.04-.41-4.36-1.18l-.31-.18-3.23 1.05 1.06-3.12-.2-.32A7.96 7.96 0 0 1 12 4.03a7.95 7.95 0 0 1 0 15.9Z" />
                        </svg>
                    </a>
                </div>

                <div class="dist-icon">
                    <a href="https://github.com" target="_blank" class="icon">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                            <path
                                    d="M12 .5C5.7.5.5 5.7.5 12c0 5.1 3.3 9.4 7.9 10.9.6.1.8-.2.8-.5v-1.7c-3.2.7-3.9-1.5-3.9-1.5-.5-1.3-1.2-1.7-1.2-1.7-1-.7.1-.7.1-.7 1.1.1 1.7 1.2 1.7 1.2 1 .1.7 2.2 3.7 1.6v-1.6c-2.6-.3-5.3-1.3-5.3-6 0-1.3.5-2.3 1.2-3.2-.1-.3-.5-1.4.1-2.9 0 0 1-.3 3.3 1.2a11.5 11.5 0 0 1 6 0c2.3-1.5 3.3-1.2 3.3-1.2.6 1.5.2 2.6.1 2.9.8.9 1.2 1.9 1.2 3.2 0 4.7-2.7 5.7-5.3 6v1.7c0 .3.2.6.8.5 4.6-1.5 7.9-5.8 7.9-10.9C23.5 5.7 18.3.5 12 .5Z" />
                        </svg>
                    </a>
                </div>
            </div>
        </div>
    </div>
</footer>
</body>

</html>

<script>
    const btn = document.getElementById("btn-hamb");
    const menu = document.getElementById("menu");
    btn.addEventListener("click", () => {
        menu.classList.toggle("mostrar");
    });
</script>
