/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package painel.venda;

import dados.BancoDados;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import painel.cliente.CadastrarCliente;
import painel.venda.formaspagamento.PainelPagamentoCartao;
import painel.venda.formaspagamento.PainelPagamentoDinheiro;
import painel.venda.formaspagamento.PainelPagamentoPix;
import utilitarias.classes.Cliente;
import utilitarias.classes.Funcionario;
import utilitarias.classes.Produto;
import utilitarias.classes.Venda;
import utilitarias.sistema.ControleAtalhos;
import utilitarias.sistema.FuncoesCamposTexto;

/**
 *
 * @author samuel
 */
public class EfetuarVenda extends javax.swing.JDialog {

    /**
     * Creates new form EfetuarVenda
     */
    HashMap<String, Produto> listaCarrinhohashMap = BancoDados.getHashmapCarrinho();
    HashMap<String, Cliente> listaClienteshashMap = BancoDados.getHashmapClientes();
    HashMap<String, Funcionario> listaFuncionarioshashMap = BancoDados.getHashmapFuncionarios();
    Venda venda;
    
    public EfetuarVenda(JFrame parent, Venda venda) {
        super(parent, true);
        initComponents();
        
        setLocationRelativeTo(null);
        this.venda = venda;
        this.venda.setCliente(null);
        this.venda.setFuncionario(null);
        
        iniciarTela();
        
        ControleAtalhos.passadorDeCampoComEnter(jfCPFCliente, jcFuncionario);
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F1", () -> {
            pagarComPix();
        });
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F2", () -> {
            pagarComCartao();
        });
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F3", () -> {
            pagarComDinheiro();
        });
        
        ControleAtalhos.addKeyBinding(getRootPane(), "F4", () -> {
            sairPainelEfetuarVenda();
        });
    }
    
    private void iniciarTela() {
        jtValortotal.setText(this.venda.getValorTotal());
        jfCPFCliente.requestFocus();
        
        JComboBox<Funcionario> combox = jcFuncionario;
        for(Funcionario funcionario: listaFuncionarioshashMap.values()) {
            combox.addItem(funcionario);
        }
    }
    
    private void verificarClienteExistente() {
        String cpfCliente = jfCPFCliente.getText();
        if(listaClienteshashMap.containsKey(cpfCliente)) {
            FuncoesCamposTexto.setTextoComReticencias(jtNomeCliente, listaClienteshashMap.get(cpfCliente).getNome(), 26);
            jtNomeCliente.setForeground(Color.BLACK);
        } else {
            int reposta = JOptionPane.showConfirmDialog(null, 
                    "O CPF informado não foi cadastrado. Deseja cadastrar esse CPF?",
                    "Usar CPF Padrão",
                    JOptionPane.YES_NO_OPTION);
            
            if(reposta == JOptionPane.YES_OPTION) {
                CadastrarCliente cadastrarCliente = new CadastrarCliente(new JFrame(), jfCPFCliente.getText());
                cadastrarCliente.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) { 
                        listaClienteshashMap = BancoDados.getHashmapClientes();
                    }
                });
                cadastrarCliente.setVisible(true);
                verificarClienteExistente();
            } else {
                Cliente cliente = listaClienteshashMap.get("000.000.000-00");
                jfCPFCliente.setText(cliente.getCpf());
                jtNomeCliente.setText(cliente.getNome());
            }
        }
    }
    
    private boolean verificarPossivelVenda() {
        Funcionario funcionario = (Funcionario) jcFuncionario.getSelectedItem();
        Cliente cliente = listaClienteshashMap.get(jfCPFCliente.getText());
        if(funcionario != null && cliente != null) {
            this.venda.setCliente(cliente);
            this.venda.setFuncionario(funcionario);
            return true;
        } else {
            return false;
        }
    }
    
    private void pagarComPix() {
        if(verificarPossivelVenda()) {
            new PainelPagamentoPix(new JFrame(), venda).setVisible(true); 
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos para poder efetuar o pagamento!");
        }
    }
    
    private void pagarComCartao() {
        if(verificarPossivelVenda()) {
            new PainelPagamentoCartao(new JFrame(), venda).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos para poder efetuar o pagamento!");
        }
    }
    
    private void pagarComDinheiro() {
        if(verificarPossivelVenda()) {
            new PainelPagamentoDinheiro(new JFrame(), venda).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Preencha todos os campos para poder efetuar o pagamento!");
        }
    }
    
    private void sairPainelEfetuarVenda() {
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jfCPFCliente = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jtNomeCliente = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jtValortotal = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jcFuncionario = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        btnPagamentoPix = new javax.swing.JButton();
        btnPagamentoCartao = new javax.swing.JButton();
        btnPagamentoDinheiro = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        btnSair = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 102, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Finalização da Venda");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 69));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, -1));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel5.setBackground(new java.awt.Color(51, 102, 0));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("CPF do Cliente");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 30));

        jfCPFCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        try {
            jfCPFCliente.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jfCPFCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jfCPFCliente.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jfCPFCliente.setPreferredSize(new java.awt.Dimension(60, 35));
        jfCPFCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfCPFClienteActionPerformed(evt);
            }
        });
        jPanel5.add(jfCPFCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 240, -1));

        jPanel6.setBackground(new java.awt.Color(51, 102, 0));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Nome");
        jPanel6.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 30));

        jtNomeCliente.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jtNomeCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtNomeCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jtNomeCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtNomeClienteActionPerformed(evt);
            }
        });
        jPanel6.add(jtNomeCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 240, 35));

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel15.setBackground(new java.awt.Color(102, 153, 0));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Valor Total");
        jPanel15.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 40));

        jPanel14.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 40));

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel22.setToolTipText("");
        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jtValortotal.setEditable(false);
        jtValortotal.setBackground(new java.awt.Color(255, 255, 255));
        jtValortotal.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jtValortotal.setForeground(new java.awt.Color(51, 102, 0));
        jtValortotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtValortotal.setText("R$ 0,00");
        jtValortotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel22.add(jtValortotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 70));

        jPanel14.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, -1, 70));

        jPanel3.setBackground(new java.awt.Color(51, 102, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jcFuncionario.setBackground(new java.awt.Color(255, 255, 255));
        jcFuncionario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.add(jcFuncionario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 240, 36));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Funcionário");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 240, 30));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel16.setBackground(new java.awt.Color(51, 102, 0));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setBackground(new java.awt.Color(51, 102, 0));
        jLabel10.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Formas de Pagamento");
        jPanel16.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 40));

        jPanel4.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 830, 40));

        btnPagamentoPix.setBackground(new java.awt.Color(102, 153, 0));
        btnPagamentoPix.setForeground(new java.awt.Color(255, 255, 255));
        btnPagamentoPix.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/imagem_pix (1).png"))); // NOI18N
        btnPagamentoPix.setText("Pix");
        btnPagamentoPix.setBorder(null);
        btnPagamentoPix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagamentoPixActionPerformed(evt);
            }
        });
        jPanel4.add(btnPagamentoPix, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 120, 40));

        btnPagamentoCartao.setBackground(new java.awt.Color(102, 153, 0));
        btnPagamentoCartao.setForeground(new java.awt.Color(255, 255, 255));
        btnPagamentoCartao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/imagem_cartao (1).png"))); // NOI18N
        btnPagamentoCartao.setText("Cartão");
        btnPagamentoCartao.setBorder(null);
        btnPagamentoCartao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagamentoCartaoActionPerformed(evt);
            }
        });
        jPanel4.add(btnPagamentoCartao, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 120, 40));

        btnPagamentoDinheiro.setBackground(new java.awt.Color(102, 153, 0));
        btnPagamentoDinheiro.setForeground(new java.awt.Color(255, 255, 255));
        btnPagamentoDinheiro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/imagem_dinheiro (1).png"))); // NOI18N
        btnPagamentoDinheiro.setText("Dinheiro");
        btnPagamentoDinheiro.setBorder(null);
        btnPagamentoDinheiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagamentoDinheiroActionPerformed(evt);
            }
        });
        jPanel4.add(btnPagamentoDinheiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 80, 120, 40));

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(51, 102, 0));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("(F1)");
        jTextField1.setBorder(null);
        jPanel4.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 50, 120, 30));

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(51, 102, 0));
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("(F3)");
        jTextField2.setBorder(null);
        jPanel4.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 50, 120, 30));

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(255, 255, 255));
        jTextField3.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(51, 102, 0));
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("(F2)");
        jTextField3.setBorder(null);
        jPanel4.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, 120, 30));

        btnSair.setBackground(new java.awt.Color(102, 153, 0));
        btnSair.setForeground(new java.awt.Color(255, 255, 255));
        btnSair.setText("Sair (F4)");
        btnSair.setBorder(null);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(69, 69, 69))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(356, 356, 356)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 960, 490));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jfCPFClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfCPFClienteActionPerformed
        verificarClienteExistente();
    }//GEN-LAST:event_jfCPFClienteActionPerformed

    private void jtNomeClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtNomeClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtNomeClienteActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        sairPainelEfetuarVenda();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnPagamentoPixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagamentoPixActionPerformed
        pagarComPix();
    }//GEN-LAST:event_btnPagamentoPixActionPerformed

    private void btnPagamentoCartaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagamentoCartaoActionPerformed
        pagarComCartao();
    }//GEN-LAST:event_btnPagamentoCartaoActionPerformed

    private void btnPagamentoDinheiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagamentoDinheiroActionPerformed
        pagarComDinheiro();
    }//GEN-LAST:event_btnPagamentoDinheiroActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPagamentoCartao;
    private javax.swing.JButton btnPagamentoDinheiro;
    private javax.swing.JButton btnPagamentoPix;
    private javax.swing.JButton btnSair;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JComboBox<Funcionario> jcFuncionario;
    private javax.swing.JFormattedTextField jfCPFCliente;
    private javax.swing.JTextField jtNomeCliente;
    private javax.swing.JTextField jtValortotal;
    // End of variables declaration//GEN-END:variables
}
