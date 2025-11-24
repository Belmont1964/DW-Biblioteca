/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package app;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author belmo
 */
@WebServlet (urlPatterns = ("/Biblioteca"))
public class Biblioteca extends HttpServlet {
        
    Connection conexao = null; // conectando o projeto com o banco de dados

    @Override
    public void init() throws ServletException{
        try{
            // fazendo a conexão com o BD
            conexao = DriverManager.getConnection("jdbc:derby://localhost:1527/Biblioteca","adminPrincipal","123");
        }
        catch (Exception ex){
            
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String escolha = request.getParameter("choice");
            String erro, msg, update = "";
            
            
            // ROTINA PARA BUSCAR LIVRO(S) PARA EMPRESTIMO POR AUTOR OU POR NOME DO LIVRO
            if ("CONSULTAR".equals(escolha)){
                request.setAttribute("choice",escolha);
                String titulo = request.getParameter("book");
                String autor = request.getParameter("author");
                if ("".equals(titulo) && "".equals(autor)){
                    erro = "CONSULTA INVÁLIDA";
                    request.setAttribute("erro",erro);
                }
                else {
                    String comandoSQL;
                    int decideBusca;
                    if ("".equals(titulo)){
                        comandoSQL ="SELECT * FROM Livro WHERE UPPER (autor) LIKE UPPER (?)";
                        decideBusca = 0;
                    }
                    else{
                        comandoSQL ="SELECT * FROM Livro WHERE UPPER (titulo) LIKE UPPER (?)";
                        decideBusca = 1;
                    }

                    try (PreparedStatement sql = conexao.prepareStatement(comandoSQL)){
                        if (decideBusca == 0){
                            sql.setString(1,"%" + autor + "%");
                        }
                        else {
                            sql.setString(1,"%" + titulo + "%");
                        }

                        ResultSet rs = sql.executeQuery();                      
                        List <Livro> livros = new ArrayList<>();

                        if (!rs.next()){
                            msg = "TITULO OU AUTOR NÃO ENCONTRADO";
                            request.setAttribute("msg",msg);
                        }
                        else{                                               
                            do{
                                livros.add(new Livro(
                                        rs.getInt("idLivro"),
                                        rs.getString("titulo"),
                                        rs.getString("autor"),
                                        rs.getString("edicao"),
                                        rs.getString("lugar"),
                                        rs.getString("statusLivro")
                                ));
                            }while(rs.next());
                            request.setAttribute("livros", livros);
                        }

                    }
                    catch (Exception e){
                        out.println("erro " + e );
                        msg = "erro "+e;
                        request.setAttribute("msg",msg);
                    } 
                }
            }

            // ROTINA PARA SELECIONAR LIVRO A SER EMPRESTADO A UM CLIENTE
            else if ("EMPRESTAR".equals(escolha)){
                request.setAttribute("choice",escolha);
                String cpf = request.getParameter("cpf");
                String senha = request.getParameter("senha");
                String idLivroEmp = request.getParameter("idEmp");
                String comandoSQL="select * from Cliente where cpf = ? and senha = ?";
                String msg2 = "";

                try(PreparedStatement sql = conexao.prepareStatement (comandoSQL)){
                    if (cpf != null && !cpf.isEmpty()){
                        sql.setString(1, cpf);
                        sql.setString(2, senha);                  
                        ResultSet rs = sql.executeQuery();

                        if(!rs.next()){
                            msg2 = "USUÁRIO OU SENHA INCORRETOS";

                        }
                        else{
                            int c = rs.getInt("idCliente");
                            int l = Integer.parseInt(idLivroEmp);
                            LocalDate hj = LocalDate.now();
                            java.sql.Date hjSQL = java.sql.Date.valueOf(hj);

                            comandoSQL = "update Livro set cliente = ?, statusLivro = ?, dataEmprestimo = ?, dataDevolucao = ? where idLivro = ?";
                            try(PreparedStatement sql1 = conexao.prepareStatement(comandoSQL)){
                                sql1.setInt(1, c);
                                sql1.setString (2,"I");
                                sql1.setDate(3, hjSQL);
                                sql1.setDate(4,null);
                                sql1.setInt(5, l);
                                sql1.executeUpdate();
                            }catch (SQLException e) {
                                out.println("erro " + e );
                                msg = "erro "+e;
                                request.setAttribute("msg",msg);
                            }   
                            msg2 = "EMPRÉSTIMO FEITO COM SUCESSO";
                        }

                    }
                } catch (SQLException e) {
                    out.println("erro " + e );
                    msg = "erro "+e;
                    request.setAttribute("msg",msg);
                }


                request.setAttribute("msg2",msg2);

            }

            // ROTINA PARA BUSCAR LIVRO(S) EMPRESTADO A UM CLIENTE 
            else if ("DEVOLVER".equals(escolha)){
                request.setAttribute("choice",escolha);
                String cpf = request.getParameter("cpf");
                String comandoSQL="select * from Cliente where cpf = ? ";
                String msg3 = cpf;

                if (cpf != null && !cpf.isEmpty()){                   
                    try (PreparedStatement sql = conexao.prepareStatement(comandoSQL)){                   
                        sql.setString(1, cpf);
                        ResultSet rsc = sql.executeQuery();
                        if (!rsc.next()){
                            msg3 = "CPF NÃO ENCONTRADO";
                        }
                        else{
                            int id = rsc.getInt("idCliente");  
                            request.setAttribute ("idCliente", id);
                            comandoSQL="select * from Livro where Cliente = ? ";
                            try (PreparedStatement sql1 = conexao.prepareStatement(comandoSQL)){
                                sql1.setInt(1,id);
                                ResultSet rsl = sql1.executeQuery();
                                List <Livro> livros = new ArrayList<>();
                                if (!rsl.next()){
                                    msg3 = "USUÁRIO NÃO POSSUI LIVRO A DEVOLVER";                                       
                                }
                                else{
                                    do{
                                        livros.add(new Livro(
                                            rsl.getInt("idLivro"),
                                            rsl.getString("titulo"),
                                            rsl.getString("autor"),
                                            rsl.getString("edicao"),
                                            rsl.getString("lugar"),
                                            rsl.getString("statusLivro"),
                                            rsl.getDate("dataEmprestimo")
                                       ));
                                    }while(rsl.next());
                                    request.setAttribute("livros", livros);

                                }


                            }catch (SQLException e) {
                                out.println("erro " + e );
                                msg = "erro "+e;
                                request.setAttribute("msg",msg);
                            }

                        }

                    } catch (SQLException e) {
                        out.println("erro " + e );
                        msg = "erro "+e;
                        request.setAttribute("msg",msg);
                    }
                }
                request.setAttribute("msg3", msg3);
            }

            // ROTINA PARA SELECIONAR LIVRO A SER DEVOLVIDO POR UM CLIENTE E ATUALIZAR HISTÓRICO DE EMPRESTIMO
            else if("EFETUAR-DEV".equals(escolha)){
                request.setAttribute("choice",escolha);
                int idL = Integer.parseInt(request.getParameter("idDev"));
                int idC = Integer.parseInt(request.getParameter("idCliente"));

                LocalDate dataEmp = LocalDate.parse(request.getParameter("dataEmp"));
                LocalDate hj = LocalDate.now();
                long diasAtraso = ChronoUnit.DAYS.between(dataEmp, hj);

                double multa = 0.0;               
                if (diasAtraso > 7){
                    multa = (diasAtraso - 7) * 20.0;
                    String msg5 = "VOCÊ TEM UMA MULTA DE: R$"+multa;
                    request.setAttribute("msg5",msg5);
                }

                java.sql.Date dataEmpSQL = java.sql.Date.valueOf(dataEmp);
                //LocalDate hj = LocalDate.now();
                java.sql.Date hjSQL = java.sql.Date.valueOf(hj);
                String comandoSQL = "update Livro set statusLivro = ?, dataDevolucao = ?, Cliente = ? where idLivro = ?";
                try(PreparedStatement sql = conexao.prepareStatement(comandoSQL)){
                    sql.setString (1,"D");
                    sql.setDate (2,hjSQL);
                    sql.setNull(3, java.sql.Types.INTEGER);
                    sql.setInt(4,idL);
                    sql.executeUpdate();
                    String msg4 = "DEVOLUÇÃO EFETUADA COM SUCESSO";
                    request.setAttribute("msg4",msg4);

                } catch (SQLException e) {
                    out.println("erro " + e );
                    msg = "erro "+e;
                    request.setAttribute("msg",msg);
                }

                comandoSQL = "insert into Emprestimo (idCliente, idLivro, dataEmp, dataDev, multa)"+
                        "values (?,?,?,?,?)";
                    try(PreparedStatement sql2 = conexao.prepareStatement(comandoSQL)){
                        sql2.setInt (1,idC);
                        sql2.setInt (2,idL);
                        sql2.setDate(3, dataEmpSQL);
                        sql2.setDate(4, hjSQL);
                        sql2.setDouble(5, multa);
                        sql2.executeUpdate();

                    } catch (SQLException e) {
                        out.println("erro " + e );
                        msg = "erro "+e;
                        request.setAttribute("msg",msg);
                    }


            }

            // ROTINA PARA CADASTRAR UM CLIENTE 
            else if ("CADCLIENTE".equals(escolha)){
                request.setAttribute("choice",escolha);
                String nome = request.getParameter("nome");
                String cpf = request.getParameter("cpf");
                String ddd = request.getParameter("ddd");
                String tel = request.getParameter("tel");
                String email = request.getParameter("email");
                String senha = request.getParameter("senha");
                String confirmaSenha = request.getParameter("confirmaSenha");
                String msg6="";

                
                if(senha == null || confirmaSenha==null || nome ==null || nome.trim().isEmpty() || cpf.trim().isEmpty() || cpf == null){
                    msg6 = "NOME, CPF E SENHA NÃO PODEM SER NULOS";
                }
                else if (!senha.equals(confirmaSenha)){
                    msg6 = "SENHA DIVERGE DA CONFIRMAÇÃO";
                }
                else{
                    
                    String comandoSQL = "insert into Cliente (nome, cpf, ddd, tel, email, senha) "+
                            "values (?,?,?,?,?,?)";
                    try(PreparedStatement sql = conexao.prepareStatement(comandoSQL)){
                        sql.setString(1,nome);
                        sql.setString(2,cpf);
                        sql.setString(3,ddd);
                        sql.setString(4,tel);
                        sql.setString(5,email);
                        sql.setString(6,senha);
                        sql.executeUpdate();
                        msg6 = "CLIENTE "+nome+" CADASTRADO COM SUCESSO";
                        
                        
                    } catch (SQLException e) {
                        out.println("erro " + e );
                        if (e.getErrorCode() == 30000){
                            msg6 = "CPF JÁ CADASTRADO";
                        }
                        else{
                            msg = "erro "+e.getErrorCode();
                            request.setAttribute("msg",msg);
                        }
                        
                    }
                }

                request.setAttribute("msg6",msg6);
            }
            
            // ROTINA PARA CADASTRAR UM LIVRO 
            else if ("CADLIVRO".equals(escolha)){
                request.setAttribute("choice",escolha);
                String titulo = request.getParameter("titulo");
                String autor = request.getParameter("autor");
                String edicao = request.getParameter("edicao");
                String c = request.getParameter("c");
                String p = request.getParameter("p");
                String e = request.getParameter("e");
                
                String lugar = "c"+c+"-p"+p+"-e"+e;
                String msg7 = lugar;

                
                if(titulo == null || autor ==null || titulo.trim().isEmpty() || autor.trim().isEmpty()){
                    msg7 = "TITULO E AUTOR NÃO PODEM SER NULOS";
                }
                else {                                 
                    String comandoSQL = "insert into Livro (titulo, autor, edicao, lugar, statusLivro) "+
                        "values (?, ?, ?, ?, ?)";
                    try(PreparedStatement sql = conexao.prepareStatement(comandoSQL)){
                        sql.setString(1,titulo);
                        sql.setString(2,autor);
                        sql.setString(3,edicao);
                        sql.setString(4,lugar);
                        sql.setString(5,"D");
                        sql.executeUpdate();
                        msg7 = "LIVRO CADASTRADO COM SUCESSO";
                        
                        
                } catch (SQLException ex) {
                    out.println("erro " + ex );
                    msg = "erro "+ex;
                    request.setAttribute("msg",msg);
                    }
                }
                request.setAttribute("msg7",msg7);
            }
            
            // ROTINA PARA MOSTRAR HISTÓRICO DE EMPRÉSTIMOS  
            else if ("HISTORICO".equals(escolha)){
                request.setAttribute("choice",escolha);
                String msg8 = "";
                String cpf = request.getParameter ("cpf");
                

                if (cpf != null && !cpf.isEmpty()){      //SELECIONA O CPF  
                    String comandoSQL="select * from Cliente where cpf = ? ";
                    try (PreparedStatement sql = conexao.prepareStatement(comandoSQL)){                   
                        sql.setString(1, cpf);
                        ResultSet rsc = sql.executeQuery();
                        if (!rsc.next()){
                            msg8 = "CPF NÃO ENCONTRADO";
                        }                        
                        
                        
                        else {                          // BUSCA OS LIVROS EMPRESTADOD AO CPF
                            String nomeHist = rsc.getString("nome");
                            String head = "Histórico de empréstimo de: "+nomeHist;
                            request.setAttribute("head",head);
                            
                            int idCliente = rsc.getInt("idCliente");
                            comandoSQL = "select l.titulo, l.autor, e.dataEmp, e.dataDev, e.multa "
                                    + "from Emprestimo e join Livro l on e.idLivro = l.idLivro "
                                    + "where e.idCliente = ? "
                                    + "order by e.dataEmp DESC";
                            
                            try(PreparedStatement sql1 = conexao.prepareStatement(comandoSQL)){
                                sql1.setInt(1, idCliente);
                                ResultSet rs = sql1.executeQuery();
                                List <Emprestimo> emprestimos = new ArrayList<>();
                                if (rs.next()){
                                    do{
                                       emprestimos.add(new Emprestimo(
                                               rs.getString("titulo"),
                                               rs.getString("autor"),
                                               rs.getDate("dataEmp"),
                                               rs.getDate("dataDev"),
                                               rs.getDouble("multa")
                                                                                     
                                       ));                                       
                                    }while (rs.next());
                                    request.setAttribute ("emprestimos", emprestimos);
                                    request.setAttribute("blockHead","N");
                                }
                                else{
                                    msg8 = "O cliente "+rsc.getString("nome")+" NAO POSSUI EMPRÉSTIMOS";
                                    request.setAttribute("blockHead","S");
                                }
                                    
                            }catch (SQLException e) {
                                out.println("erro " + e );
                                msg = "erro "+e.getMessage();
                                request.setAttribute("msg",msg);               
                            }
                        }
                            
                                
                    }catch (SQLException e) {
                    //out.println("erro " + e );
                        msg = "erro "+e;
                        request.setAttribute("msg",msg);
                
                    } 
                }
                request.setAttribute("msg8",msg8);
              
            }
            //REQUEST DISPATCHER PARA TODAS AS ROTINAS
            RequestDispatcher dispatcher = request.getRequestDispatcher("Biblioteca.jsp");
            if (dispatcher != null){
                dispatcher.forward(request, response);
            } 

        }

            
               
    
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

