<%-- 
    Document   : Agenda
    Created on : 8 de nov. de 2025, 18:58:06
    Author     : belmo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- necessário para fazer iteração --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>BIBLIOTECA</title>
        <link rel="stylesheet" href="Biblioteca.css"> 
    </head>
    <body>
        <div id = "page">
            <div id =  "div1">
                BIBLIOTECA
            </div>
            <div class = "container">

                <div class="div3">  
                    <h1 id="msg1">${msg}</h1>
                    <script>
                        setTimeout(function () {
                            var el = document.getElementById("msg1");
                            if (el) {
                                el.remove();
                            }
                        }, 3000);
                    </script>
                    <!--
                    <c:forEach var="c" items="${contatos}">
                        <FORM action ="Agenda" method ="POST">
                            ${c.nome}  -  ${c.tel}  -  ${c.id} - 
                            <button type="submit">APAGAR</button>
                            <br>
                            <input type="hidden" name="choice" value="APAGAR">
                            <input type="hidden" name="idSai" value="${c.id}"> 
                        </FORM>
                    </c:forEach>   -->
                    
                </div>

                <div class="div4">
                    <div id = "div5">
                        <FORM action ="Biblioteca" method ="POST">                          
                            <br> 
                            <input type ="text" name="book" maxlenght="50" placeholder="TITULO DO LIVRO" size=50><br><!-- comment -->
                            <input type="text" name="author" maxlength="30" placeholder="AUTOR" size=30><br><!-- comment -->
                            <button type="submit" name="choice" value="CONSULTAR">CONSULTAR</button>
                            <button type="submit" name="choice" value="EMPRESTAR">EMPRESTAR</button>
                            <br><br><h2 id="msg">${erro}</h2>
                            <script>
                                setTimeout(function () {
                                    var el = document.getElementById("msg");
                                    if (el) {
                                        el.remove();
                                    }
                                }, 3000);
                            </script>
                          
                        </FORM>
                    </div>

                    <div id="div6"></div>
                </div>

            </div>
        </div>
    </body>
</html>
