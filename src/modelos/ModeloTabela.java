/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.Function;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.ScrollPane;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author samuel
 */
public class ModeloTabela<T> extends AbstractTableModel {

    private final String[] colunasTabela;
    private final List<T> listaItens;
    private final Function<T, Object[]> extrator;
    
    public ModeloTabela(String[] colunas, List<T> lista, Function<T, Object[]> extrator) {
        this.colunasTabela = colunas;
        this.listaItens = lista;
        this.extrator = extrator;
    }
    
    @Override
    public int getRowCount() {
        return listaItens.size();
    }

    @Override
    public int getColumnCount() {
        return colunasTabela.length;
    }
    
    @Override
    public String getColumnName(int column) { 
        return colunasTabela[column];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return extrator.apply(listaItens.get(0))[columnIndex].getClass();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return extrator.apply(listaItens.get(rowIndex))[columnIndex];
    }
    
    public static void reconfigurarModelo(JTable tabela) {
        
        // Configurando o Header da Tabela
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                c.setFont(new Font("Dialog", Font.BOLD, 14));
                c.setBackground(new Color(51,153,0));
                c.setForeground(Color.WHITE);

                setHorizontalAlignment(SwingConstants.CENTER);

                return c;
            }
        };

        // aplica o renderizador em todas as colunas
        for (int i = 0; i < tabela.getColumnModel().getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }
        
        // Colocando Cor de Borda ao redor da Tabela
        tabela.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        // Colocando Cor de Borda ao redor da Tabela
        tabela.getTableHeader().setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        tabela.getTableHeader().repaint();
        
        // Configurando as linhas da Tabela
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                c.setFont(new Font("Dialog", Font.BOLD, 12));
                c.setForeground(Color.BLACK);
                ((DefaultTableCellRenderer) c).setHorizontalAlignment(SwingConstants.LEADING);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(51,153,0, 50)); 
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                
                return c;
            }
        });
        
        // Configurando a barra de Scroll da Tabela
        JScrollPane scroll = (JScrollPane) tabela.getParent().getParent();
        JScrollBar barra = scroll.getVerticalScrollBar();
        
        barra.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(51, 102, 0);   // cor do "polegar"
                this.trackColor = new Color(240, 240, 240); // cor do trilho
            }
        });
    }
    
    public static void ajustarColuna(JTable tabela, int coluna, int largura) {
//        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        TableColumn col = tabela.getColumnModel().getColumn(coluna);

        col.setPreferredWidth(largura);
        col.setMinWidth(largura);
        col.setMaxWidth(largura); // trava a largura
    }
}
