/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painel.carrinho;

import dados.BancoDados;
import utilitarias.classes.Produto;
import utilitarias.classes.Venda;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import painel.produto.PainelProduto;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import modelos.ModeloTabela;
import painel.cliente.PainelCliente;
import painel.funcionario.PainelFuncionario;
import painel.carrinho.PainelCarrinho;
import painel.historicovendas.HistoricoVendas;
import painel.venda.EfetuarVenda;
import utilitarias.sistema.CRUDHashMap;
import utilitarias.sistema.CampoMoedaFormatada;
import utilitarias.sistema.ControleAtalhos;
import utilitarias.sistema.DadosFicticios;
import utilitarias.sistema.FuncoesCamposTexto;
import utilitarias.sistema.FuncoesNaTabela;

/**
 *
 * @author samuel
 */
public class TelaInicial extends javax.swing.JFrame {

    /**
     * Creates new form TelaInicial
     */
    HashMap<String, Produto> listaProdutosHashMap = BancoDados.getHashmapProdutos();
    HashMap<String, Produto> listaCarrinhohashMap = BancoDados.getHashmapCarrinho();
    HashMap<Integer, Venda> listaVendashashmap = BancoDados.getHashmapVendas();
    
    List<Produto> listaCarrinhoArrayList = new ArrayList<>(listaCarrinhohashMap.values());
    
    Venda venda = null;
    Produto produtoPesquisado = null;
    private int linhaSelecionadaTabela;
    private String opcao = "cadastrar";
    
    public TelaInicial() {
        initComponents();
        
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        List<Produto> listaProdutosArrayList = new ArrayList<>(listaProdutosHashMap.values());
        
        DadosFicticios dados = new DadosFicticios();
        for(String[] p: dados.getProdutos()) {
            Produto produto = new Produto(
                    p[0],
                    p[1],
                    Integer.parseInt(p[2]),
                    p[3]
            );
            
            listaProdutosHashMap.put(p[0], produto);
            listaProdutosArrayList.add(produto);
        }
        
        ModeloTabela<Produto> mt = new ModeloTabela<>(
                new String[]{"Código", "Descrição", "Quantidade"},
                listaProdutosArrayList,
                p -> new Object[]{
                    p.getCodigo(),
                    p.getDescricao(),
                    p.getQuatidade()
                }
        );
        
        jtProdutosVenda.setModel(mt);
        
        mostrarDescricaoDosProdutosDaTabela(jtProdutosVenda);
        ModeloTabela.reconfigurarModelo(jtProdutosVenda);
        
        ModeloTabela<Produto> mt2 = new ModeloTabela<>(
                new String[]{"Código", "Descrição", "Valor Unitário", "Quantidade"},
                listaCarrinhoArrayList,
                p -> new Object[]{
                    p.getCodigo(),
                    p.getDescricao(),
                    p.getValorUnitario(),
                    p.getQuatidade()
                }
        );
        
        jtCarrinho.setModel(mt2);
        
        mostrarDescricaoDosProdutosDaTabela(jtCarrinho);
        ModeloTabela.reconfigurarModelo(jtCarrinho);
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F1", () -> jtCodigoProduto.requestFocus());
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F2", () -> {
            int resposta = JOptionPane.showConfirmDialog(null, "Deseja mesmo cancelar essa Venda?", "Cancerlar Venda", JOptionPane.YES_NO_OPTION);
            if(resposta == JOptionPane.YES_OPTION) {
                listaCarrinhoArrayList.clear();
                listaCarrinhohashMap.clear();
                CRUDHashMap.preencherTabela(listaCarrinhohashMap, jtCarrinho, p -> new Object[] {
                    p.getCodigo(),
                    p.getDescricao(),
                    p.getQuatidade(),
                    p.getValorUnitario()
                });
                FuncoesNaTabela.informarQuantidadeEPreencharArrayListDaTabela(listaCarrinhohashMap, listaCarrinhoArrayList);
                FuncoesCamposTexto.filtarItem(jtProdutosVenda, jtCodigoProduto, listaProdutosHashMap, "int");
                jtValortotal.setText(FuncoesNaTabela.somarValoresDaTabelaCarrinho(listaCarrinhohashMap));
            }
        });
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F3", () -> {
            if(listaCarrinhohashMap.size() > 0) {
                EfetuarVenda efetuarVenda = null;
                if(this.venda == null) {
                    this.venda = new Venda(jtValortotal.getText(), listaCarrinhohashMap);
                    efetuarVenda = new EfetuarVenda(this, this.venda);
                    efetuarVenda.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                            boolean vendaFinalizada = venda.getTipoDePagamento() != null;
                            if(vendaFinalizada) {
                                CRUDHashMap.preencherTabela(listaProdutosHashMap, jtProdutosVenda, p -> new Object[] {
                                    p.getCodigo(),
                                    p.getDescricao(),
                                    p.getQuatidade()
                                });
                                FuncoesNaTabela.informarQuantidadeEPreencharArrayListDaTabela(listaProdutosHashMap, listaProdutosArrayList);
                                CRUDHashMap.preencherTabela(listaCarrinhohashMap, jtCarrinho, p -> new Object[] {
                                    p.getCodigo(),
                                    p.getDescricao(),
                                    p.getQuatidade(),
                                    p.getValorUnitario()
                                });
                                FuncoesNaTabela.informarQuantidadeEPreencharArrayListDaTabela(listaCarrinhohashMap, listaCarrinhoArrayList);
                                FuncoesCamposTexto.filtarItem(jtProdutosVenda, jtCodigoProduto, listaProdutosHashMap, "int");
                                jtValortotal.setText(FuncoesNaTabela.somarValoresDaTabelaCarrinho(listaCarrinhohashMap));
                            }
                            listaVendashashmap.put(venda.getId(), venda);
                            venda = null;
                        }
                    });
                    efetuarVenda.setVisible(true);
                } else if(this.venda != null && this.venda.getTipoDePagamento() == null) {
                    efetuarVenda = new EfetuarVenda(this, this.venda);
                    efetuarVenda.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                            boolean vendaFinalizada = venda.getTipoDePagamento() != null;
                            if(vendaFinalizada) {
                                CRUDHashMap.preencherTabela(listaProdutosHashMap, jtProdutosVenda, p -> new Object[] {
                                    p.getCodigo(),
                                    p.getDescricao(),
                                    p.getQuatidade()
                                });
                                FuncoesNaTabela.informarQuantidadeEPreencharArrayListDaTabela(listaProdutosHashMap, listaProdutosArrayList);
                                CRUDHashMap.preencherTabela(listaCarrinhohashMap, jtCarrinho, p -> new Object[] {
                                    p.getCodigo(),
                                    p.getDescricao(),
                                    p.getQuatidade(),
                                    p.getValorUnitario()
                                });
                                FuncoesNaTabela.informarQuantidadeEPreencharArrayListDaTabela(listaCarrinhohashMap, listaCarrinhoArrayList);
                                FuncoesCamposTexto.filtarItem(jtProdutosVenda, jtCodigoProduto, listaProdutosHashMap, "int");
                                jtValortotal.setText(FuncoesNaTabela.somarValoresDaTabelaCarrinho(listaCarrinhohashMap));
                                listaVendashashmap.put(venda.getId(), venda);
                                venda = null;
                            }
                        }
                    });
                    efetuarVenda.setVisible(true);
                } 
            } else {
                JOptionPane.showMessageDialog(null, "Adicione pelo menos um item no Carrinho para realizar a Venda!");
            }
        });
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F4", () -> {
            Produto produtoCarrinho = CRUDHashMap.buscarItemTabela(listaCarrinhohashMap, jtCarrinho, linhaSelecionadaTabela);
            if(produtoCarrinho != null) {
                prepararCampoEdicao(produtoCarrinho);
                this.opcao = "editar";
            }
        });
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F5", () -> {
            Produto produtoCarrinho = CRUDHashMap.buscarItemTabela(listaCarrinhohashMap, jtCarrinho, linhaSelecionadaTabela);
            if(produtoCarrinho != null) {
                listaCarrinhohashMap.remove(produtoCarrinho.getCodigo());
                CRUDHashMap.preencherTabela(listaCarrinhohashMap, jtCarrinho, 
                            p -> new Object[]{
                                p.getCodigo(),
                                p.getDescricao(),
                                p.getValorUnitario(),
                                p.getQuatidade()
                            }
                    );
                FuncoesNaTabela.informarQuantidadeEPreencharArrayListDaTabela(listaCarrinhohashMap, listaCarrinhoArrayList);
                jtValortotal.setText(FuncoesNaTabela.somarValoresDaTabelaCarrinho(listaCarrinhohashMap));
            }
        });
        
        FuncoesNaTabela.pegarSelecaoDaTabela(jtCarrinho, linha -> {
            this.linhaSelecionadaTabela = linha;
        });
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F6", () -> {
            PainelProduto painelProduto = new PainelProduto(this);
            painelProduto.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    CRUDHashMap.preencherTabela(listaProdutosHashMap, jtProdutosVenda, p -> new Object[] {
                        p.getCodigo(),
                        p.getDescricao(),
                        p.getQuatidade()
                    });
                    FuncoesNaTabela.informarQuantidadeEPreencharArrayListDaTabela(listaProdutosHashMap, listaProdutosArrayList);
                }
            });
            painelProduto.setVisible(true);
        });
        ControleAtalhos.addKeyBinding(getRootPane(), "F7", () -> new PainelFuncionario(this).setVisible(true));
        ControleAtalhos.addKeyBinding(getRootPane(), "F8", () -> new PainelCliente(this).setVisible(true));
        ControleAtalhos.addKeyBinding(getRootPane(), "F9", () -> new HistoricoVendas(this).setVisible(true));
        ControleAtalhos.addKeyBinding(getRootPane(), "F10", () -> dispose());
        
        FuncoesCamposTexto.pesquisaCampoTexto(jtProdutosVenda, jtCodigoProduto, listaProdutosHashMap, "int");
    }
    
    private void limparCampos() {
        jtCodigoProduto.setText("");
        jtValorUnitario.setText("R$ 0,00");
        jtQuantidadeProduto.setText("0");
    }
    
    private void prepararCampoEdicao(Produto produto) {
        jtCodigoProduto.setText(produto.getCodigo());
        jtCodigoProduto.setEditable(false);
        jtQuantidadeProduto.setText("" + produto.getQuatidade());
        jtQuantidadeProduto.requestFocus();
    }
    
    private void pesquisarProdutoPorCodigo() {
        String codigo = jtCodigoProduto.getText();
        if(listaProdutosHashMap.containsKey(codigo)) {
            produtoPesquisado = new Produto(listaProdutosHashMap.get(codigo));
            jtValorUnitario.setText(produtoPesquisado.getValorUnitario());
            jtQuantidadeProduto.setText("1");
            jtQuantidadeProduto.requestFocus();
        } else {
            JOptionPane.showMessageDialog(null, "Produto inexistente!");
        }
    }
    
    private void verificarQuantidadeInformada(String opcao) {
        int quantidade = -1;
        try {
            quantidade = Integer.parseInt(jtQuantidadeProduto.getText());
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Informe um valor numérico para a quantidade!");
            jtQuantidadeProduto.setText("1");
        }
        switch(opcao) {
            case "cadastrar":
                if(produtoPesquisado.getQuatidade() >= quantidade && quantidade >= 1) {
                    produtoPesquisado.setQuatidade(quantidade);
                    listaCarrinhohashMap.put(produtoPesquisado.getCodigo(), produtoPesquisado);
                    CRUDHashMap.preencherTabela(listaCarrinhohashMap, jtCarrinho, 
                            p -> new Object[]{
                                p.getCodigo(),
                                p.getDescricao(),
                                p.getValorUnitario(),
                                p.getQuatidade()
                            }
                    );
                    FuncoesNaTabela.informarQuantidadeEPreencharArrayListDaTabela(listaCarrinhohashMap, listaCarrinhoArrayList);
                    jtValortotal.setText(FuncoesNaTabela.somarValoresDaTabelaCarrinho(listaCarrinhohashMap));
                    produtoPesquisado = null;
                    limparCampos();
                    jtCodigoProduto.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "Quantidade inválida para que o estoque apresenta!");
                }
                break;
            case "editar":
                Produto produtoCarrinho = CRUDHashMap.buscarItemTabela(listaCarrinhohashMap, jtCarrinho, linhaSelecionadaTabela);
                Produto produtoOriginal = listaProdutosHashMap.get(produtoCarrinho.getCodigo());
                if(produtoCarrinho != null) {
                    quantidade = Integer.parseInt(jtQuantidadeProduto.getText());
                    if(produtoOriginal.getQuatidade() >= quantidade) {
                        produtoCarrinho.setQuatidade(quantidade);
                        listaCarrinhohashMap.put(produtoCarrinho.getCodigo(), produtoCarrinho);
                        CRUDHashMap.preencherTabela(listaCarrinhohashMap, jtCarrinho, 
                                p -> new Object[]{
                                    p.getCodigo(),
                                    p.getDescricao(),
                                    p.getValorUnitario(),
                                    p.getQuatidade()
                                }
                        );
                        FuncoesNaTabela.informarQuantidadeEPreencharArrayListDaTabela(listaCarrinhohashMap, listaCarrinhoArrayList);
                        jtValortotal.setText(FuncoesNaTabela.somarValoresDaTabelaCarrinho(listaCarrinhohashMap));
                        produtoPesquisado = null;
                        jtCodigoProduto.setEditable(true);
                        jtCodigoProduto.requestFocus();
                        limparCampos();
                        this.opcao = "cadastrar";
                    } else {
                        JOptionPane.showMessageDialog(null, "Quantidade Inexistente para o Estoque!");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione algum Item do Carrinho para Editar ou Deletar!");
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Erro ao verificar a quantidade informada!");
        }
        
    }
    
    private void mostrarDescricaoDosProdutosDaTabela(JTable jtable) {
        jtable.setToolTipText(""); // ativa tooltip

        jtable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                int row = jtable.rowAtPoint(e.getPoint());

                if (row >= 0) {
                    int modelRow = jtable.convertRowIndexToModel(row);

                    ModeloTabela<Produto> modelo = 
                        (ModeloTabela<Produto>) jtable.getModel();

                    Produto p = modelo.getLista().get(modelRow);

                    jtable.setToolTipText(p.getDescricao());
                } else {
                    jtable.setToolTipText(null);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlImagemCactoyce = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtProdutosVenda = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtCarrinho = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jtCodigoProduto = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jtValorUnitario = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jtQuantidadeProduto = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jtValortotal = new javax.swing.JTextField();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 102, 0));

        jPanel6.setOpaque(false);
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jlImagemCactoyce.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Cactoyce_Menor.png"))); // NOI18N
        jlImagemCactoyce.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jlImagemCactoyceMouseClicked(evt);
            }
        });
        jPanel6.add(jlImagemCactoyce, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 40)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Floricultura Cacto da Serra");
        jPanel6.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 630, 40));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(615, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(240, 240, 240));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 102, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Caixa Aberto");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 4, 1280, 70));

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 1280, 80));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel16.setBackground(new java.awt.Color(51, 102, 0));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Lista de Produtos");
        jPanel16.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 30));

        jLabel14.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Lista de Produtos");
        jPanel16.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 30));

        jPanel4.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 30));

        jtProdutosVenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtProdutosVenda.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jtProdutosVenda);
        if (jtProdutosVenda.getColumnModel().getColumnCount() > 0) {
            jtProdutosVenda.getColumnModel().getColumn(0).setResizable(false);
            jtProdutosVenda.getColumnModel().getColumn(1).setResizable(false);
            jtProdutosVenda.getColumnModel().getColumn(2).setResizable(false);
        }

        jPanel4.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-3, 27, 335, 305));

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 330, 330));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel16.setText("F8- Painel de Clientes");
        jPanel5.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, -1, -1));

        jLabel17.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel17.setText("F9- Histórico Vendas");
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 40, -1, -1));

        jLabel18.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel18.setText("F2- Cancelar Venda");
        jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        jLabel19.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel19.setText("F6- Painel de Produtos");
        jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, -1, -1));

        jLabel20.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel20.setText("F7- Painel de Funcionários");
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));

        jPanel18.setBackground(new java.awt.Color(0, 0, 0));
        jPanel18.setPreferredSize(new java.awt.Dimension(20, 110));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 2, 120));

        jPanel19.setBackground(new java.awt.Color(0, 0, 0));
        jPanel19.setPreferredSize(new java.awt.Dimension(20, 110));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 2, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        jPanel5.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 2, 120));

        jLabel22.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel22.setText("F5- Deletar item");
        jPanel5.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        jLabel23.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel23.setText("F1- Iniciar Venda");
        jPanel5.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        jLabel24.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel24.setText("F3- Finalizar Venda");
        jPanel5.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, -1));

        jLabel25.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel25.setText("F4- Editar item");
        jPanel5.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        jLabel21.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel21.setText("F10- Sair do Sistema");
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, -1, -1));

        jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 480, 570, 120));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel17.setBackground(new java.awt.Color(51, 102, 0));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Carrinho de Compras");
        jPanel17.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 30));

        jPanel7.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, -1, -1));

        jPanel24.setBackground(new java.awt.Color(255, 255, 255));
        jPanel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtCarrinho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jtCarrinho);

        jPanel24.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-3, -2, 695, 305));

        jPanel7.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 690, 300));

        jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 130, 690, 330));

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9.setBackground(new java.awt.Color(51, 102, 0));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Código");
        jPanel9.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 4, -1, -1));

        jPanel8.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 30));

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtCodigoProduto.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jtCodigoProduto.setForeground(new java.awt.Color(51, 102, 0));
        jtCodigoProduto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtCodigoProduto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtCodigoProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtCodigoProdutoActionPerformed(evt);
            }
        });
        jPanel21.add(jtCodigoProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 50));

        jPanel8.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 200, 50));

        jPanel2.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 130, 200, 80));

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(51, 102, 0));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Valor Unitário");
        jPanel11.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 4, -1, -1));

        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 30));

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtValorUnitario.setEditable(false);
        jtValorUnitario.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jtValorUnitario.setForeground(new java.awt.Color(51, 102, 0));
        jtValorUnitario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtValorUnitario.setText("R$ 0,00");
        jtValorUnitario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel20.add(jtValorUnitario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 50));

        jPanel10.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 200, 50));

        jPanel2.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 260, 200, 80));

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(51, 102, 0));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Quantidade");
        jPanel13.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 4, -1, -1));

        jPanel12.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 30));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtQuantidadeProduto.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jtQuantidadeProduto.setForeground(new java.awt.Color(51, 102, 0));
        jtQuantidadeProduto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtQuantidadeProduto.setText("0");
        jtQuantidadeProduto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtQuantidadeProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtQuantidadeProdutoActionPerformed(evt);
            }
        });
        jPanel23.add(jtQuantidadeProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 50));

        jPanel12.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 200, 50));

        jPanel2.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 380, 200, 80));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(51, 102, 0));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Valor Total");
        jPanel15.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, -1, -1));

        jPanel14.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 40));

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel22.setToolTipText("");
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtValortotal.setEditable(false);
        jtValortotal.setFont(new java.awt.Font("Dialog", 1, 26)); // NOI18N
        jtValortotal.setForeground(new java.awt.Color(51, 102, 0));
        jtValortotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtValortotal.setText("R$ 0,00");
        jtValortotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel22.add(jtValortotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 80));

        jPanel14.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 690, 80));

        jPanel2.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 480, 690, 120));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1375, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jLabel3)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(layout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(284, 284, 284)
                .addComponent(jLabel3))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jlImagemCactoyceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlImagemCactoyceMouseClicked
        ImageIcon imagem = new ImageIcon(getClass().getResource("/icons/Cactoyce.png"));
        JOptionPane.showMessageDialog(null, "", "Cactoyce --> Dona do Negócio", JOptionPane.INFORMATION_MESSAGE, imagem);
    }//GEN-LAST:event_jlImagemCactoyceMouseClicked

    private void jtCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtCodigoProdutoActionPerformed
        pesquisarProdutoPorCodigo();
    }//GEN-LAST:event_jtCodigoProdutoActionPerformed

    private void jtQuantidadeProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtQuantidadeProdutoActionPerformed
        verificarQuantidadeInformada(this.opcao);
    }//GEN-LAST:event_jtQuantidadeProdutoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInicial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlImagemCactoyce;
    private javax.swing.JTable jtCarrinho;
    private javax.swing.JTextField jtCodigoProduto;
    private javax.swing.JTable jtProdutosVenda;
    private javax.swing.JTextField jtQuantidadeProduto;
    private javax.swing.JTextField jtValorUnitario;
    private javax.swing.JTextField jtValortotal;
    // End of variables declaration//GEN-END:variables
}
