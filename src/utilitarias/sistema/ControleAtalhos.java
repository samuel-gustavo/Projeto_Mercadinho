/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarias.sistema;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
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
}
