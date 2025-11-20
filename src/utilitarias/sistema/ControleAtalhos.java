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
    }
}
