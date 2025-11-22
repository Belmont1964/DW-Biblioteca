/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import java.util.Date;


/**
 *
 * @author belmo
 */
public class Livro {
    private int idLivro;
    private String titulo;
    private String autor;
    private String lugar;
    private String statusLivro;
    private String edicao;
    private int idCliente;
    private Date dataEmprestimo;
    private Date dataDevolucao;
    
    public Livro (int idLivro, String titulo, String autor, String lugar, String statusLivro, String edicao, int idCliente, Date dataEmprestimo, Date dataDevolucao){
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.autor = autor;
        this.edicao = edicao;
        this.lugar = lugar;
        this.idCliente = idCliente;
        this.statusLivro = statusLivro;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;      
    }
    
    public Livro (int idLivro, String titulo, String autor, String edicao, String lugar, String statusLivro){
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.autor = autor;
        this.edicao = edicao;
        this.lugar = lugar;
        this.statusLivro = statusLivro;
    }
    
    public Livro (int idLivro, String titulo, String autor, String edicao, String lugar){
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.autor = autor;
        this.edicao = edicao;
        this.lugar = lugar;
        this.statusLivro = "D";
    }
    
    public Livro (int idLivro, String titulo, String autor, String edicao, String lugar, String statusLivro, Date dataEmp){
        this.idLivro = idLivro;
        this.titulo = titulo;
        this.autor = autor;
        this.edicao = edicao;
        this.lugar = lugar;
        this.statusLivro = statusLivro;
        this.dataEmprestimo = dataEmp;
    }
    
    public Livro (){
        
    }
    
    public int getIdLivro(){
        return this.idLivro;
    }
    
    public String getTitulo(){
        return this.titulo;
    }
    
    public String getAutor(){
        return this.autor;
    }
    
    public String getLugar(){
        return this.lugar;
    }
    
    public String getEdicao(){
        return this.edicao;
    }
    
    public int getLeitor(){
        return this.idCliente;
    }
    
    public String getStatusLivro(){
        return this.statusLivro;
    }
    
    public Date getDataEmprestimo(){
        return this.dataEmprestimo;
    }
    
    public Date getDataDevolucao(){
        return this.dataDevolucao;
    }
    
    public void setIdLivro(int x){
        this.idLivro = x;
    }
    
    public void setTitulo(String x){
        this.titulo = x;
    }
    
    public void setAutor(String x){
        this.autor = x;
    }
    
    public void setEdicao(String x){
        this.edicao = x;
    }
    
    public void setLugar(String x){
        this.lugar = x;
    }
    
    public void setLeitor(int x){
        this.idCliente = x;
    }
    
    public void setStatusLivro(String x){
        this.statusLivro = x;
    }
    
    public void setDataEmprestimo(Date x){
        this.dataEmprestimo = x;
    }
    
    public void setDataDevolucao(Date x){
        this.dataDevolucao = x;
    }
    
    
}
