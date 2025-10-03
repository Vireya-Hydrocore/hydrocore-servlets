/*
    Validação de Formulário
*/

function validar(){
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