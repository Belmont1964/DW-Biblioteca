<%-- 
    Document   : Agenda
    Created on : 8 de nov. de 2025, 18:58:06
    Author     : belmo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- necessário para fazer iteração --%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BIBLIOTECA</title>
        <link rel="stylesheet" href="Biblioteca.css"> 
        
        <script>
            const t = 3000
            function removerMensagens (idElemento, tempo = t){
                setTimeout(function () {
                    var el = document.getElementById(idElemento);
                    if (el) {
                        el.remove();
                    }
                }, tempo);
            }
                
        </script>
    </head>
    <body>
        <div id = "page">
            <div id =  "div1">
                BIBLIOTECA
            </div>
            <div class = "container">

                <div class="div3">   <!-- MOSTRA O RESULTADO DA BUSCA -->
                    <c:forEach var="l" items="${livros}">
                        <FORM action ="Biblioteca" method ="POST">
                            ${l.idLivro} - ${l.titulo}  <br>  ${l.autor} - ${l.statusLivro}
                            
                            <c:if test="${choice == 'CONSULTAR'}">
                                <button class="${l.statusLivro == 'I' ? 'cinza' : 'branco'}" 
                                    type="submit" ${l.statusLivro == 'I' ? 'disabled' : ''}>EMPRESTAR</button>
                                <input type="hidden" name="choice" value="EMPRESTAR"><br>
                                <input type="hidden" name="idEmp" value="${l.idLivro}"> 
                                <input type="hidden" name="statusLivroEmp" value="${l.statusLivro}"> 
                            </c:if>
                            <c:if test="${choice == 'DEVOLVER'}">
                                <button class="branco" type="submit">DEVOLVER</button>
                                <input type="hidden" name="choice" value="EFETUAR-DEV">
                                <input type="hidden" name="idDev" value="${l.idLivro}">
                                <input type="hidden" name="idCliente" value="${idCliente}">
                                <input type="hidden" name="dataEmp" value="${l.dataEmprestimo}">
                                
                            </c:if>
                            <br>
                        </FORM>
                    </c:forEach>  
                    
                    <h1 id="msg1">${msg}</h1>
                    <script>
                        removerMensagens("msg1");
                    </script>
                    
                </div>

                <div class="div4"> <!-- MONTA A AREA DE CONSULTA -->
                    <div id = "div5">
                        <FORM action ="Biblioteca" method ="POST">                                                     
                            <input type ="text" name="book" maxlenght="50" placeholder="TITULO DO LIVRO" size=50><br><!-- comment -->
                            <input type="text" name="author" maxlength="30" placeholder="AUTOR" size=30><br><!-- comment -->
                            <button type="submit" name="choice" value="CONSULTAR">CONSULTAR</button><br><!-- comment -->
                            <button type="submit" name="choice" value="DEVOLVER">DEVOLUÇÃO</button><!-- comment -->
                            <button type="submit" name="choice" value="CADCLIENTE">CADASTRAR CLIENTE</button>
                            <button type="submit" name="choice" value="CADLIVRO">CADASTRAR LIVRO</button>
                            <h2 id="msg">${erro}</h2>
                            <script>
                                removerMensagens("msg");
                            </script>
                          
                        </FORM>
                    </div>                
                    <div id="div6">
                        <!-- ROTINA PARA MOSTRAR LIVRO PROCURADO OU LIVROS DE AUTOR PROCURADO -->
                        <c:if test="${choice == 'EMPRESTAR'}">
                            <form action="Biblioteca" method="POST">
                                <br>
                                <input type="text" name="cpf" placeholder="CPF" size="11"><br>
                                <input type="password" name="senha" placeholder="SENHA" size="11">
                                
                                <input type="hidden" name="idEmp" value="${param.idEmp}">  
                                <button class="branco" type="submit" name="choice" value="EMPRESTAR">ENVIAR</button>
                                <br><h2 id="msg2">${msg2}</h2>
                                <script>
                                    removerMensagens("msg2");
                                </script>
                            </form>
                        </c:if>
                        
                        <!-- ROTINA PARA MOSTRAR LIVROS EM POSSE DE UM CLIENTE E SELECIONAR A DEVOLUÇÃO -->
                        <c:if test="${choice == 'DEVOLVER'}">
                            <form action="Biblioteca" method="POST">
                                <input type="text" name="cpf" placeholder="CPF" size="11"><br>
                                <button class="branco" type="submit" name="choice" value="DEVOLVER">ENVIAR</button>
                                <br><h2 id="msg3">${msg3}</h2>
                                <script>
                                    removerMensagens("msg3");
                                </script>
                            </form>
                        </c:if>
                        
                        <!-- ROTINA PARA ENVIAR MENSAGEM DE DEVOLUÇÃO E EVENTUAL MULTA -->
                        <c:if test="${choice == 'EFETUAR-DEV'}">
                            <br><h2 style="color: red;"  id="msg5">${msg5}</h2>
                            <br><h2 id="msg4">${msg4}</h2>                           
                            <script>
                                removerMensagens("msg4");
                                removerMensagens("msg5",6000);
                            </script>
                        </c:if>
                        
                        <!-- ROTINA PARA CADASTRAR UM NOVO CLIENTE -->
                        <c:if test="${choice == 'CADCLIENTE'}">
                            <FORM action="Biblioteca" method="POST">
                                <BR><input type="text" name="nome" placeholder="NOME" size="50"><!-- comment -->
                                <BR><input type="text" name="cpf" placeholder="CPF" maxlength="11" size="11">
                                <BR><input type="text" name="ddd" placeholder="DDD" maxlength ="2" size="2">
                                 - <input type="text" name="tel"  placeholder="TELEFONE" maxlength="9" size="9">
                                <BR><input type="text" name="email" placeholder="EMAIL" size="50">
                                <BR><input type="password" name="senha" placeholder="SENHA" size="20">
                                <BR><input type="password" name="confirmaSenha" placeholder="CONFIRME A SENHA" size="20">
                                <button class="branco" type="submit" name="choice" value="CADCLIENTE">ENVIAR</button>
                                <br><h2 id="msg6"> ${msg6}</h2>
                                <script>
                                    removerMensagens("msg6");
                                </script>
                            </FORM>
                        </c:if>
                    </div>
                    

                </div>

            </div>
        </div>
    </body>
</html>
