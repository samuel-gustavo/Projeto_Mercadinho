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
import java.util.HashMap;

/**
 *
 * @author samuel
 */
public class BancoDados {
    
    HashMap<String, Cliente> listaClientes = new HashMap<>();
    HashMap<String, Funcionario> listaFuncionarios = new HashMap<>();
    HashMap<Long, Produto> listaProdutos = new HashMap<>();
    HashMap<Integer, Venda> listaVendas = new HashMap<>();
}
