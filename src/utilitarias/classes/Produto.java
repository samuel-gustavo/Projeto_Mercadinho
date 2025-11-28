/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarias.classes;

/**
 *
 * @author samuel
 */
public class Produto {
    
    private String codigo;
    private String descricao;
    private int quatidade;
    private String valorUnitario;
    
    public Produto(Produto produto) {
        this(produto.getCodigo(), produto.getDescricao(), produto.getQuatidade(), produto.getValorUnitario());
    }

    public Produto(String codigo, String descricao, int quatidade, String valorUnitario) {
        this.codigo = codigo;     
        this.descricao = descricao;
        this.quatidade = quatidade;
        this.valorUnitario = valorUnitario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuatidade() {
        return quatidade;
    }

    public void setQuatidade(int quatidade) {
        this.quatidade = quatidade;
    }

    public String getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(String valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
