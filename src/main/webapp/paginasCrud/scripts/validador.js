/*
    Validação de Formulários
*/

function validarProduto(){
    let nome = frmProduto.nome.value
    let tipo = frmProduto.tipo.value
    let unidadeMedida = frmProduto.unidadeMedida.value
    let concentracao = frmProduto.concentracao.value

    //verificar se os campos estão preenchidos
    if (nome === ""){
        alert("Preencha o campo Nome")
        frmProduto.nome.focus() //Posiciona o cursor no campo
        return false;
    } else if (tipo === ""){
        alert("Preencha o campo Tipo")
        frmProduto.tipo.focus()
        return false;
    } else if (unidadeMedida === ""){
        alert("Preencha o campo Unidade de Medida")
        frmProduto.unidadeMedida.focus()
        return false;
    } else if (concentracao === ""){
        alert("Preencha o campo Concentração")
        frmProduto.concentracao.focus() 
        return false;
    } else{
        document.forms["frmProduto"].submit() //envia os dados do formulário
    }
}

function validarEstoque(){
    let nome = frmProduto.nome.value
    let tipo = frmProduto.tipo.value
    let unidadeMedida = frmProduto.unidadeMedida.value
    let concentracao = frmProduto.concentracao.value

    //verificar se os campos estão preenchidos
    if (nome === ""){
        alert("Preencha o campo Nome")
        frmProduto.nome.focus() //Posiciona o cursor no campo
        return false;
    } else if (tipo === ""){
        alert("Preencha o campo Tipo")
        frmProduto.tipo.focus()
        return false;
    } else if (unidadeMedida === ""){
        alert("Preencha o campo Unidade de Medida")
        frmProduto.unidadeMedida.focus()
        return false;
    } else if (concentracao === ""){
        alert("Preencha o campo Concentração")
        frmProduto.concentracao.focus()
        return false;
    } else{
        document.forms["frmProduto"].submit() //envia os dados do formulário
    }
}

function validarAdmin(){
    let nome = frmAdmin.nome.value
    let email = frmAdmin.email.value
    let senha = frmAdmin.senha.value

    //Verificar se os campos estão preenchidos
    if (nome === ""){
        alert("Preencha o campo Nome!")
        frmAdmin.nome.focus() //Posiciona o cursor no campo
        return false;
    } else if (email === ""){
        alert("Preencha o campo Email!")
        frmAdmin.email.focus()
        return false;
    } else if (senha === ""){
        alert("Preencha o campo Senha!")
        frmAdmin.senha.focus()
        return false;
    } else{
        document.forms["frmAdmin"].submit() //Envia os dados do formulário
    }
}