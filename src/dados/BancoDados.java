/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dados;

import classesutilitarias.Cliente;
import classesutilitarias.Funcionario;
import classesutilitarias.Produto;
import classesutilitarias.Venda;
import java.util.ArrayList;

/**
 *
 * @author samuel
 */
public class BancoDados {
    
    public static ArrayList<Funcionario> listaFuncionarios = new ArrayList<>();
    public static ArrayList<Cliente> listaClientes = new ArrayList<>();
    public static ArrayList<Produto> listaProdutos = new ArrayList<>();
    public static ArrayList<Produto> listaCarrinho = new ArrayList<>();
    public static ArrayList<Venda> listaVendas = new ArrayList<>();
}
