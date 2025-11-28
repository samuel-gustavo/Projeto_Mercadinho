/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarias.sistema;

import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author samuel
 */
public class ControleAtalhos {
    
    public static void addKeyBinding(JComponent componente, String tecla, Runnable acao) {
        InputMap im = componente.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = componente.getActionMap();

        im.put(KeyStroke.getKeyStroke(tecla), tecla);

        am.put(tecla, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                acao.run();
            }
        });
    }
    
    public static void addKeyBindings(JComponent componente, Map<String, Runnable> atalhos) {
        InputMap im = componente.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = componente.getActionMap();

        atalhos.forEach((tecla, acao) -> {
            im.put(KeyStroke.getKeyStroke(tecla), tecla);
            am.put(tecla, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    acao.run();
                }
            });
        });
    }
    /*
    
    public static void passadorDeCampoComEnter(JComponent... componentes) {

        for (int i = 0; i < componentes.length; i++) {
            JComponent atual = componentes[i];
            int proximoIndex = i + 1;

            // Se for o último componente
            if (proximoIndex == componentes.length) {

                // Se for botão → aperta o botão
                if (atual instanceof JButton) {
                    JButton botao = (JButton) atual;
                    atual.getInputMap(JComponent.WHEN_FOCUSED)
                            .put(KeyStroke.getKeyStroke("ENTER"), "pressionarBotao");

                    atual.getActionMap().put("pressionarBotao", new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            botao.doClick();
                        }
                    });
                } 
                else {
                    // apenas não avança mais, ou faz uma ação específica se quiser
                }

                continue;
            }

            // Para os demais: ENTER avança para o próximo
            JComponent proximo = componentes[proximoIndex];

            atual.getInputMap(JComponent.WHEN_FOCUSED)
                    .put(KeyStroke.getKeyStroke("ENTER"), "focarProximo");

            atual.getActionMap().put("focarProximo", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    proximo.requestFocus();
                }
            });
        }
    } */
    
    public static void passadorDeCampoComEnter(JComponent... componentes) {
        for (int i = 0; i < componentes.length; i++) {
            final JComponent atual = componentes[i];
            final int proximoIndex = i + 1;

            // último componente
            if (proximoIndex >= componentes.length) {
                if (atual instanceof JButton) {
                    final JButton botao = (JButton) atual;
                    InputMap imBot = botao.getInputMap(JComponent.WHEN_FOCUSED);
                    ActionMap amBot = botao.getActionMap();
                    imBot.put(KeyStroke.getKeyStroke("ENTER"), "pressionarBotao");
                    amBot.put("pressionarBotao", new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            botao.doClick();
                        }
                    });
                }
                continue;
            }

            final JComponent proximo = componentes[proximoIndex];

            // não altere botões que estão no meio (serão clicados só se for o último)
            if (atual instanceof JButton) continue;

            InputMap im = atual.getInputMap(JComponent.WHEN_FOCUSED);
            ActionMap am = atual.getActionMap();

            // caso especial: JFormattedTextField -> commit, postActionEvent (se houver), e avançar
            if (atual instanceof JFormattedTextField) {
                final JFormattedTextField f = (JFormattedTextField) atual;
                im.put(KeyStroke.getKeyStroke("ENTER"), "commitAndFireAndNext");
                am.put("commitAndFireAndNext", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            f.commitEdit(); // valida/commit do formatted field
                        } catch (Exception ex) {
                            // se não for válido, não avança
                            return;
                        }
                        // dispara ActionListeners do campo (se houver)
                        f.postActionEvent();
                        proximo.requestFocusInWindow();
                    }
                });
                continue;
            }

            // caso: JTextField que tem ActionListener -> dispara action e avança
            if (atual instanceof JTextField) {
                final JTextField tf = (JTextField) atual;
                if (tf.getActionListeners().length > 0) {
                    im.put(KeyStroke.getKeyStroke("ENTER"), "fireActionAndNext");
                    am.put("fireActionAndNext", new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // dispara a action existente
                            tf.postActionEvent();
                            // avança o foco
                            proximo.requestFocusInWindow();
                        }
                    });
                    continue;
                }
            }

            // caso padrão: apenas avança para o próximo componente
            im.put(KeyStroke.getKeyStroke("ENTER"), "focarProximo");
            am.put("focarProximo", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    proximo.requestFocusInWindow();
                }
            });
        }
    }
}
