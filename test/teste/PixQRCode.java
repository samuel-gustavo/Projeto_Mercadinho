package teste;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

/**
 * Classe para gerar o Payload (código) e o QR Code de um pagamento Pix estático.
 */
public class PixQRCode {
    
    // ==================== CRC16 ====================
    public static String crc16(String payload) {
        int polinomio = 0x1021; 
        int resultado = 0xFFFF;
        
        byte[] bytes;
        try {
            bytes = payload.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            bytes = payload.getBytes(); 
        }

        for (byte b : bytes) {
            resultado ^= (b & 0xFF) << 8;

            for (int i = 0; i < 8; i++) {
                if ((resultado & 0x8000) != 0) {
                    resultado = (resultado << 1) ^ polinomio;
                } else {
                    resultado <<= 1;
                }
                resultado &= 0xFFFF;
            }
        }
        return String.format("%04X", resultado);
    }

    // ==================== GERAR PAYLOAD PIX (BR CODE) ====================
    public static String gerarPayloadPix(String chave, String nome, String valor, String cidade) {
        
        nome = removerAcentos(formatarString(nome, 25));
        cidade = removerAcentos(formatarString(cidade, 15)); // Adicionando limpeza para cidade

        // ID 00: Payload Format Indicator (000201)
        // O ID 01 (Point of Initiation Method) foi omitido, o que implica em '11'
        String payload = "000201"; 

        // ====================
        // ID 26: Merchant Account Information (Dados da Conta)
        // ====================
        String gui = "BR.GOV.BCB.PIX"; // Subcampo 00
        
        // Subcampo 00: GUI (BR.GOV.BCB.PIX)
        String campoGui = "00" + String.format("%02d", gui.length()) + gui; 
        
        // Subcampo 01: Chave Pix (a chave agora é "+5584987963710", 14 caracteres)
        String campoChave = "01" + String.format("%02d", chave.length()) + chave;
        
        // Conteúdo total do ID 26: 18 (GUI) + 18 (Chave: ID + Tamanho + Valor) = 36
        String conta = campoGui + campoChave; 

        // Montagem do ID 26: ID + Tamanho (36) + Valor
        payload += "26" + String.format("%02d", conta.length()) + conta;

        // ID 52: Merchant Category Code (MCC)
        payload += "52040000";
        
        // ID 53: Transaction Currency (986 = BRL)
        payload += "5303986";
        
        // ID 54: Transaction Amount
        payload += "54" + String.format("%02d", valor.length()) + valor;
        
        // ID 58: Country Code (BR)
        payload += "5802BR";
        
        // ID 59: Merchant Name
        payload += "59" + String.format("%02d", nome.length()) + nome;
        
        // ID 60: Merchant City
        // O tamanho deve ser dinâmico (15 para "Serrote dos Bez")
        payload += "60" + String.format("%02d", cidade.length()) + cidade; 

        // ID 62: Additional Data Field (Subcampo 05: Transaction ID)
        String txid = "***"; // Identificador de transação
        String campoTxid = "05" + String.format("%02d", txid.length()) + txid;
        // Montagem do ID 62: ID + Tamanho + Subcampos
        payload += "62" + String.format("%02d", campoTxid.length()) + campoTxid;

        // ID 63: CRC16 (Apenas o ID e o Tamanho)
        String payloadParaCrc = payload + "6304";
        
        String crc = crc16(payloadParaCrc);
        
        // Payload Final
        return payloadParaCrc + crc;
    }
    
    // ==================== GERAR QR CODE ====================
    public static void gerarQrCode(String payload, String caminho) throws Exception {

        BitMatrix matriz = new MultiFormatWriter()
                .encode(payload, BarcodeFormat.QR_CODE, 400, 400);

        BufferedImage imagem = MatrixToImageWriter.toBufferedImage(matriz);

        ImageIO.write(imagem, "png", new File(caminho));
    }

    // ==================== REMOVER ACENTOS ====================
    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
    
    // ==================== Formatador de Strings ====================
    public static String formatarString(String str, int tamanhoString) {
        return str.substring(0, tamanhoString);
    }
}