/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classesutilitarias;

import java.util.ArrayList;

/**
 *
 * @author samuel
 */
public class Venda {
    
    private int id;
    private Cliente cliente;
    private Funcionario funcionario;
    private ArrayList<Produto> listaProdutos;

    public Venda(int id, Cliente cliente, ArrayList<Produto> listaProdutos) {
        this.id = id;
        this.cliente = cliente;
        this.listaProdutos = listaProdutos;
    }
    
    public Venda(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
    }
    
    public Venda(ArrayList<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<Produto> getListaProdutos() {
        return listaProdutos;
    }

    public void setListaProdutos(ArrayList<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }
}
