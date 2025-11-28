/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarias.sistema;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import modelos.ModeloTabela;

/**
 *
 * @author samuel
 */
public class FuncoesCamposTexto {
    
    public static <K, V> void pesquisaCampoTexto(JTable tabela, JTextField campo, HashMap<K, V> listaGenerica, String tipoDeOrdenacao) {
        campo.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtarItem(tabela, campo, listaGenerica, tipoDeOrdenacao);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filtarItem(tabela, campo, listaGenerica, tipoDeOrdenacao);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filtarItem(tabela, campo, listaGenerica, tipoDeOrdenacao);
            }
        });
    }
    
    public static <K, V> void filtarItem(JTable tabela, JTextField campo, HashMap<K, V> listaGenerica, String tipoDeOrdenacao) {
        String busca = campo.getText().trim().toLowerCase();

        HashMap<K, V> filtrados = listaGenerica;
        switch(tipoDeOrdenacao.toLowerCase()) {
            case "int":
                filtrados = listaGenerica.entrySet()
                    .stream()
                    .filter(item -> String.valueOf(item.getKey()).toLowerCase().contains(busca))
                    .sorted(Comparator.comparing(
                            e -> Integer.parseInt(String.valueOf(e.getKey()))
                    ))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));
                break;
               
            case "string":
                filtrados = listaGenerica.entrySet()
                    .stream()
                    .filter(item -> String.valueOf(item.getKey()).toLowerCase().contains(busca))
                    .sorted(Comparator.comparing(
                            e -> String.valueOf(e.getKey()).toLowerCase()
                    ))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));
                break;
            default:
                JOptionPane.showMessageDialog(null, "Informe o tipo correto no método de filtrar itens!");
        }
        

        ModeloTabela modelo = (ModeloTabela) tabela.getModel();
        modelo.getLista().clear();
        modelo.getLista().addAll(filtrados.values());

        tabela.revalidate();
        tabela.repaint();
    }
    
    public static void setTextoComReticencias(JTextField campo, String texto, int maxCaracteres) {
        if (texto.length() <= maxCaracteres) {
            campo.setText(texto);
        } else {
            campo.setText(texto.substring(0, maxCaracteres - 3) + "...");
        }
        campo.setToolTipText(texto);
    }
}
