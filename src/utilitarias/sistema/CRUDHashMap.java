/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarias.sistema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import modelos.ModeloTabela;

/**
 *
 * @author samuel
 */
public class CRUDHashMap {
    
    public static <K, V> void adicionarItem(Map<K, V> hashmap, K chave, V valor) {
        if(!hashmap.containsKey(chave)) {
            hashmap.put(chave, valor);
            JOptionPane.showMessageDialog(null, "Adicionado com Sucesso!");
        } else {
            JOptionPane.showMessageDialog(null, "Esse dado já foi Cadastrado!");
        }
    }
    
    public static <K, V> void preencherTabela(Map<K, V> listaGenerica, JTable tabela, Function<V, Object[]> criadorDeLinhas) {
        ModeloTabela modelo = (ModeloTabela) tabela.getModel();
        
        modelo.getLista().clear();
        modelo.getLista().addAll(listaGenerica.values());
       
        modelo.fireTableDataChanged();
    }
    
    public static <K, V> V buscarItemTabela(Map<K, V> listaGenerica, JTable tabela, int linha) {
        ModeloTabela<V> modelo = (ModeloTabela<V>) tabela.getModel();
        V item = modelo.getItem(linha);
        if(item == null) {
            JOptionPane.showMessageDialog(null, "Selecione algum item na tabela para poder Editar ou Excluir!");
            return item;
        } 
        return item;
    }
    
    public static <K, V> void editarItem(JDialog dialog, Map<K, V> hashmap, K chaveAntiga, K chave, V valor) {
        if(!chaveAntiga.equals(chave) && hashmap.containsKey(chave)) {
            JOptionPane.showMessageDialog(null, "Esse dado não pode ser editado!");
        } else {
            hashmap.remove(chaveAntiga);
            hashmap.put(chave, valor);
            JOptionPane.showMessageDialog(null, "Editado com Sucesso!");
            dialog.dispose();    
        }
    }
    
    public static <K, V> void excluirItem(Map<K, V> hashmap, K chave) {
        int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Excluir?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(hashmap.containsKey(chave) && resposta == JOptionPane.YES_OPTION) {
            hashmap.remove(chave);
            JOptionPane.showMessageDialog(null, "Excluído com Sucesso!");
        }
    }
}
