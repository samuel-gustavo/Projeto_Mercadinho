/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarias.sistema;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author samuel
 */
public class FuncoesNaTabela {
    
    public static void pegarSelecaoDaTabela(JTable tabela, Consumer<Integer> callback) {
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabela.getSelectedRow();
                callback.accept(linha);
            }
        });
    }
    
    public static <K, V> void informarQuantidadeEPreencharArrayListDaTabela(Map<K, V> hashmap, List<V> arrayList, JTextField jtextField) {
        jtextField.setText("" + hashmap.size());
        arrayList.clear();
        arrayList.addAll(hashmap.values());
    }
}
