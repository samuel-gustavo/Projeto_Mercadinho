/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dados;

import utilitarias.classes.Cliente;
import utilitarias.classes.Funcionario;
import utilitarias.classes.Produto;
import utilitarias.classes.Venda;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author samuel
 */
public class BancoDados {
    
    private static HashMap<String, Cliente> hashmapClientes = new HashMap<String, Cliente>(){{
        put("000.000.000-00", new Cliente("000.000.000-00", "Usuário Padrão", "Usuário Padrão", "Usuário Padrão"));
    }};
    private static HashMap<String, Funcionario> hashmapFuncionarios = new HashMap<String, Funcionario>(){{
        put("000.000.000-01", new Funcionario("000.000.000-01", "Funcionário Padrão"));
    }};
    private static HashMap<String, Produto> hashmapProdutos = new HashMap<>();
    private static HashMap<String, Produto> hashmapCarrinho = new HashMap<>();
    private static HashMap<Integer, Venda> hashmapVendas = new HashMap<>();

    public static HashMap<String, Cliente> getHashmapClientes() {
        return hashmapClientes;
    }

    public static void setHashmapClientes(HashMap<String, Cliente> hashmapClientes) {
        BancoDados.hashmapClientes = hashmapClientes;
    }

    public static HashMap<String, Funcionario> getHashmapFuncionarios() {
        return hashmapFuncionarios;
    }

    public static void setHashmapFuncionarios(HashMap<String, Funcionario> hashmapFuncionarios) {
        BancoDados.hashmapFuncionarios = hashmapFuncionarios;
    }

    public static HashMap<String, Produto> getHashmapProdutos() {
        return hashmapProdutos;
    }

    public static void setHashmapProdutos(HashMap<String, Produto> hashmapProdutos) {
        BancoDados.hashmapProdutos = hashmapProdutos;
    }

    public static HashMap<String, Produto> getHashmapCarrinho() {
        return hashmapCarrinho;
    }

    public static void setHashmapCarrinho(HashMap<String, Produto> hashmapCarrinho) {
        BancoDados.hashmapCarrinho = hashmapCarrinho;
    }

    public static HashMap<Integer, Venda> getHashmapVendas() {
        return hashmapVendas;
    }

    public static void setHashmapVendas(HashMap<Integer, Venda> hashmapVendas) {
        BancoDados.hashmapVendas = hashmapVendas;
    }
}
