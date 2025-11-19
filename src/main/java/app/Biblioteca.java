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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.*;

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            String titulo = request.getParameter("book");
            String autor = request.getParameter("author");
            String escolha = request.getParameter("choice");
            String erro, msg, update = "";
            
            
            if ("CONSULTAR".equals(escolha)){
                request.setAttribute("choice",escolha);
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
                /*
                RequestDispatcher dispatcher = request.getRequestDispatcher("Biblioteca.jsp");
                if (dispatcher != null){
                    dispatcher.forward(request, response);
                }
                */
            }
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

                            comandoSQL = "update Livro set cliente = ?, statusLivro = ?, dataEmprestimo = ? where idLivro = ?";
                            try(PreparedStatement sql1 = conexao.prepareStatement(comandoSQL)){
                                sql1.setInt(1, c);
                                sql1.setString (2,"I");
                                sql1.setDate(3, hjSQL);
                                sql1.setInt(4, l);
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
                                            rsl.getString("statusLivro")                                    
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
