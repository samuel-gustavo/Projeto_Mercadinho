/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classesutilitarias;

/**
 *
 * @author samuel
 */
public class Produto {
    
    private long codigo;
    private String descricao;
    private int quatidade;
    private double valorUnitario;

    public Produto(long codigo, String descricao, int quatidade, double valorUnitario) {
        this.codigo = codigo;     
        this.descricao = descricao;
        this.quatidade = quatidade;
        this.valorUnitario = valorUnitario;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
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

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
