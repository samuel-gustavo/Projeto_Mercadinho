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
    
    HashMap<String, Cliente> listaClientes = new HashMap<>();
    HashMap<String, Funcionario> listaFuncionarios = new HashMap<>();
    HashMap<String, Produto> listaProdutos = new HashMap<>();
    HashMap<Integer, Venda> listaVendas = new HashMap<>();
}
