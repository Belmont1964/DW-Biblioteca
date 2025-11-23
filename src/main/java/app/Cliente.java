/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;



/**
 *
 * @author belmo
 */
public class Cliente {
    private int idCliente;
    private String nome;
    private String cpf;
    private String tel;
    private String ddd;
    private String email;
    private String senha;
    
    public Cliente (int idCliente, String nome, String cpf, String tel, String ddd, String email, String senha ){
        this.idCliente = idCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.tel = tel;
        this.ddd = ddd;
        this.email = email;
        this.senha = senha;
    }
    
    public Cliente (int idCliente, String nome, String cpf, String senha ){
        this.idCliente = idCliente;
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
    }
    
    public Cliente (){
        
    }
    
    public int idCliente (){
        return this.idCliente;
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public String getCpf(){
        return this.cpf;
    }
    
    public String getTelefone(){
        return this.tel;
    }
    
    public String getDdd(){
        return this.ddd;
    }
    
    public String getEmail(){
        return this.email;
    }
    
    public String getSenha(){
        return this.senha;
    }
    
        
}
