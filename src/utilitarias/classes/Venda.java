/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarias.classes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author samuel
 */
public class Venda {
    
    private static int proximoID = 1;
    private int id;
    private Cliente cliente;
    private Funcionario funcionario;
    private String valorTotal;
    private HashMap<String, Produto> listaProdutosComprados;

    public Venda(String valorTotal, HashMap<String, Produto> listaProdutosComprados) {
        this.id = proximoID++;
        this.valorTotal = valorTotal;
        this.listaProdutosComprados = listaProdutosComprados;
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

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public HashMap<String, Produto> getListaProdutosComprados() {
        return listaProdutosComprados;
    }

    public void setListaProdutosComprados(HashMap<String, Produto> listaProdutosComprados) {
        this.listaProdutosComprados = listaProdutosComprados;
    }
}
