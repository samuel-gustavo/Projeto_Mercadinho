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
    
    private static HashMap<String, Cliente> hashClientes = new HashMap<>();
    private static HashMap<String, Funcionario> hashFuncionarios = new HashMap<>();
    private static HashMap<String, Produto> hashProdutos = new HashMap<>();
    private static HashMap<Integer, Venda> hashVendas = new HashMap<>();

    public static HashMap<String, Cliente> getHashClientes() {
        return hashClientes;
    }

    public static void setHashClientes(HashMap<String, Cliente> hashClientes) {
        BancoDados.hashClientes = hashClientes;
    }

    public static HashMap<String, Funcionario> getHashFuncionarios() {
        return hashFuncionarios;
    }

    public static void setHashFuncionarios(HashMap<String, Funcionario> hashFuncionarios) {
        BancoDados.hashFuncionarios = hashFuncionarios;
    }

    public static HashMap<String, Produto> getHashProdutos() {
        return hashProdutos;
    }

    public static void setHashProdutos(HashMap<String, Produto> hashProdutos) {
        BancoDados.hashProdutos = hashProdutos;
    }

    public static HashMap<Integer, Venda> getHashVendas() {
        return hashVendas;
    }

    public static void setHashVendas(HashMap<Integer, Venda> hashVendas) {
        BancoDados.hashVendas = hashVendas;
    }
}
