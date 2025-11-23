/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import java.sql.Date;

/**
 *
 * @author belmo
 */
public class Emprestimo {
    private String  titulo;
    private String autor ;
    private Date dataEmp;
    private Date dataDev;
    private double multa;
    
    public Emprestimo ( String titulo, String autor, Date dataEmp, Date dataDev, double multa){

        this.titulo = titulo;
        this.autor = autor;
        this.dataEmp = dataEmp;
        this.dataDev = dataDev;
        this.multa = multa;
    }


    public String getTitulo(){
        return titulo;
    }
    public String getAutor(){
        return autor;
    }
    public Date getDataEmp(){
        return dataEmp;
    }
    public Date getDataDev(){
        return dataDev;
    }
    public double getMulta(){
        return multa;
    }
}