/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitarias.sistema;

import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 *
 * @author samuel
 */
public class CampoMoedaFormatada extends JTextField {
    
    public static void aplicarFormatoMoeda(JTextField campo) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        ((AbstractDocument) campo.getDocument()).setDocumentFilter(new DocumentFilter() {

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                atualizar(fb, offset, 0, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                atualizar(fb, offset, length, text, attrs);
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length)
                    throws BadLocationException {
                atualizar(fb, offset, length, "", null);
            }

            private void atualizar(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {

                // texto atual do campo
                String atual = fb.getDocument().getText(0, fb.getDocument().getLength());

                // remove o trecho que será substituído
                StringBuilder sb = new StringBuilder(atual);
                sb.replace(offset, offset + length, text);

                // mantém apenas números
                String numeros = sb.toString().replaceAll("[^0-9]", "");

                if (numeros.isEmpty()) {
                    super.replace(fb, 0, fb.getDocument().getLength(), "", attrs);
                    return;
                }

                // converte número para valor em reais
                double valor = Double.parseDouble(numeros) / 100.0;
                String formatado = formato.format(valor);

                // atualiza o campo inteiro
                super.replace(fb, 0, fb.getDocument().getLength(), formatado, attrs);
            }
        });

        // Deixa alinhado à direita (opcional, mas recomendado)
        campo.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    public static String formantarValor(Double valor) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formato.format(valor);
    }
    
    public static double desformatarFormatoMoeda(String campo) {
        if (campo == null || campo.isEmpty()) return 0.0;

        campo = campo
                .replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .trim();

        try {
            return Double.parseDouble(campo);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
