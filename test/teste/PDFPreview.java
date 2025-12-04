/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.awt.Desktop;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 *
 * @author samuel
 */
public class PDFPreview {
    
    public static ByteArrayOutputStream gerarPdfEmMemoria() throws Exception {

        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream cs = new PDPageContentStream(document, page);

            float margemEsquerda = 40;
            float margemDireita = 570;
            float y = 760;
            float espacoLinha = 18;

            // =========================
            // TÍTULO
            // =========================
            {
                String titulo = "RELATÓRIO DE VENDAS";
                float tamanhoFonteTitulo = 18;
                cs.setFont(PDType1Font.HELVETICA_BOLD, tamanhoFonteTitulo);
                
                // Largura da Página
                float larguraPagina = page.getMediaBox().getWidth();
                
                // Largura do texto na fonte
                float larguraTexto = PDType1Font.HELVETICA_BOLD.getStringWidth(titulo) / 1000 * tamanhoFonteTitulo;
                
                float xCentral = (larguraPagina - larguraTexto) / 2;
                
                cs.beginText();
                cs.newLineAtOffset(xCentral, y);
                cs.showText(titulo);
                cs.endText();
            }

            // Linha abaixo do título
            y -= 10;
            cs.moveTo(margemEsquerda, y);
            cs.lineTo(margemDireita, y);
            cs.stroke();

            y -= 30;

            // =========================
            // BLOCO DE INFORMAÇÕES
            // =========================
            cs.setFont(PDType1Font.HELVETICA, 12);

            // Caixa ao redor do bloco
            cs.addRect(margemEsquerda - 10, y - 60, 510, 70);
            cs.stroke();

            // Cliente
            cs.beginText();
            cs.newLineAtOffset(margemEsquerda, y);
            cs.showText("Cliente: Samuel Gustavo");
            cs.endText();

            y -= espacoLinha;

            // Data
            cs.beginText();
            cs.newLineAtOffset(margemEsquerda, y);
            cs.showText("Data: 02/12/2025");
            cs.endText();

            y -= espacoLinha;

            // Valor
            cs.beginText();
            cs.newLineAtOffset(margemEsquerda, y);
            cs.showText("Valor Total: R$ 72,90");
            cs.endText();

            y -= 40;

            // =========================
            // DESCRIÇÃO / OBSERVAÇÃO
            // =========================
            cs.setFont(PDType1Font.HELVETICA_BOLD, 13);
            cs.beginText();
            cs.newLineAtOffset(margemEsquerda, y);
            cs.showText("Observações:");
            cs.endText();

            y -= espacoLinha;

            cs.setFont(PDType1Font.HELVETICA, 12);
            cs.beginText();
            cs.newLineAtOffset(margemEsquerda, y);
            cs.showText("Relatório gerado automaticamente pelo sistema.");
            cs.endText();


            // =========================
            // RODAPÉ
            // =========================
            y = 60;

            cs.moveTo(margemEsquerda, y + 15);
            cs.lineTo(margemDireita, y + 15);
            cs.stroke();

            cs.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
            cs.beginText();
            cs.newLineAtOffset(margemEsquerda, y);
            cs.showText("Sistema de Vendas - Documento gerado automaticamente");
            cs.endText();

            
            cs.close();


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            document.close();

            return baos;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public static void abrirPreview(ByteArrayOutputStream baos) throws IOException {
        File temp = File.createTempFile("preview", ".pdf");
        Files.write(temp.toPath(), baos.toByteArray());
        Desktop.getDesktop().open(temp);
    }
    
    public static void main(String[] args) {
        
        try {
            PDFPreview.abrirPreview(PDFPreview.gerarPdfEmMemoria());
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
