package utilitarias.sistema;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Classe para gerar o Payload (código) e o QR Code de um pagamento Pix estático.
 * Corrigido para garantir o padrão TLV (Tag-Length-Value) correto para todos
 * os tipos de chaves e inclui o ID 01 (Point of Initiation Method) e formatação
 * robusta para o campo de Valor (ID 54).
 */
public class PixQRCode {

    // ==================== CRC16 ====================
    /**
     * Calcula o CRC16/CCITT-A (0x1021) conforme exigido pelo padrão BR Code.
     * @param payload A string do payload antes do campo 63 (excluindo os 4 caracteres do CRC).
     * @return O valor do CRC16 em formato hexadecimal de 4 caracteres.
     */
    public static String crc16(String payload) {
        int polinomio = 0x1021; 
        int resultado = 0xFFFF; // Valor inicial
        
        byte[] bytes;
        try {
            // O BR Code deve ser codificado como US-ASCII
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
                resultado &= 0xFFFF; // Garante que o resultado fique em 16 bits
            }
        }
        return String.format("%04X", resultado);
    }

    // ==================== GERAR PAYLOAD PIX (BR CODE) ====================
    /**
     * Gera a string completa do Payload Pix no formato BR Code.
     * @param chave Chave Pix (CPF, CNPJ, Email, Telefone ou Chave Aleatória).
     * @param nome Nome do recebedor/empresa.
     * @param valor Valor da transação (ex: "12.05").
     * @param cidade Cidade do recebedor.
     * @return O payload completo (BR Code) com o CRC16.
     */
    public static String gerarPayloadPix(String chave, String nome, String valor, String cidade, boolean isCelular) {

        // PASSO 1: Normaliza a chave Pix
        chave = normalizarChavePix(chave, isCelular);

        // PASSO 2: Formata o valor para garantir o formato X.XX e um Length correto
        valor = formatarValorPix(valor);

        // PASSO 3: Formata e remove acentos do nome e cidade
        nome = removerAcentos(formatarString(nome, 25));
        cidade = removerAcentos(formatarString(cidade, 15));

        // ID 00: Payload Format Indicator (000201)
        String payload = "000201"; 
        
        // ID 01: Point of Initiation Method (010211)
        // 11 = Estático (valor fixo)
        payload += "010211";

        // ====================
        // ID 26: Merchant Account Information
        // ====================
        String gui = "BR.GOV.BCB.PIX"; // Subcampo 00

        // Subcampo 00: GUI (BR.GOV.BCB.PIX) - Tamanho 14
        String campoGui = "00" + String.format("%02d", gui.length()) + gui; 
        
        // Subcampo 01: Chave Pix - Tamanho dinâmico (agora correto após normalização)
        String campoChave = "01" + String.format("%02d", chave.length()) + chave;
        
        // Conteúdo total do ID 26
        String conta = campoGui + campoChave; 

        // Montagem do ID 26: ID + Tamanho dinâmico + Valor
        payload += "26" + String.format("%02d", conta.length()) + conta;

        // ID 52: Merchant Category Code (MCC)
        payload += "52040000";
        
        // ID 53: Transaction Currency (986 = BRL)
        payload += "5303986";
        
        // ID 54: Transaction Amount (o tamanho é dinâmico, ex: "12.05" tem length 5)
        payload += "54" + String.format("%02d", valor.length()) + valor;
        
        // ID 58: Country Code (BR)
        payload += "5802BR";
        
        // ID 59: Merchant Name
        payload += "59" + String.format("%02d", nome.length()) + nome;
        
        // ID 60: Merchant City
        payload += "60" + String.format("%02d", cidade.length()) + cidade; 

        // ID 62: Additional Data Field (Subcampo 05: Transaction ID)
        String txid = "***"; // Identificador de transação
        String campoTxid = "05" + String.format("%02d", txid.length()) + txid;
        // Montagem do ID 62: ID + Tamanho + Subcampos
        payload += "62" + String.format("%02d", campoTxid.length()) + campoTxid;

        // ID 63: CRC16 (Apenas o ID e o Tamanho, 6304)
        String payloadParaCrc = payload + "6304";
        
        String crc = crc16(payloadParaCrc);
        
        // Payload Final
        return payloadParaCrc + crc;
    }
    
    // ==================== GERAR QR CODE ====================
    /**
     * Gera o QR Code em uma imagem PNG a partir do payload.
     * @param payload A string do BR Code (Payload Pix).
     * @param caminho O caminho completo para salvar o arquivo PNG.
     * @throws Exception Se ocorrer um erro na geração do QR Code.
     */
    public static void gerarQrCode(String payload, String caminho) throws Exception {

        BitMatrix matriz = new MultiFormatWriter()
                .encode(payload, BarcodeFormat.QR_CODE, 400, 400);

        BufferedImage imagem = MatrixToImageWriter.toBufferedImage(matriz);

        ImageIO.write(imagem, "png", new File(caminho));
    }

    // ==================== REMOVER ACENTOS ====================
    /**
     * Remove acentos de uma string, transformando-a em caracteres ASCII.
     */
    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
    
    // ==================== Formatador de Strings ====================
    /**
     * Limita a string ao tamanho máximo especificado.
     * @param str A string a ser formatada.
     * @param max O tamanho máximo permitido.
     * @return A string formatada.
     */
    public static String formatarString(String str, int max) {
        if (str.length() > max) {
            return str.substring(0, max);
        }
        return str;
    }
    
    // ==================== Limpar String (Remove caracteres não numéricos) ====================
    /**
     * Remove todos os caracteres que não são dígitos de uma string.
     * @param str A string de entrada.
     * @return A string contendo apenas dígitos.
     */
    private static String limparString(String str) {
        return str.replaceAll("\\D", "");
    }
    
    // ==================== Formatador de Valor (Garante X.XX) ====================
    /**
     * Garante que o valor da transação esteja no formato "XX.XX" (duas casas decimais)
     * e usa o ponto como separador, conforme exigido pelo padrão BR Code.
     * @param valor O valor monetário de entrada (ex: "5", "12.5", "12,50").
     * @return O valor formatado como String (ex: "5.00", "12.50").
     */
    private static String formatarValorPix(String valor) {
        // Remove separador de milhar e substitui vírgula por ponto (se existir)
        String valorNumerico = valor.replaceAll("[^0-9,.]", "").replace(',', '.');

        try {
            // Tenta converter para Double
            double d = Double.parseDouble(valorNumerico);
            
            // Formata para garantir duas casas decimais e ponto como separador
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat df = new DecimalFormat("0.00", symbols);
            
            return df.format(d);
            
        } catch (NumberFormatException e) {
            // Em caso de erro de formatação/conversão, retorna um valor seguro
            return "0.00"; 
        }
    }


    // ==================== Normalizador de Chave PIX ====================
    /**
     * Normaliza a chave Pix, adicionando prefixos como "+55" e "+" para telefones,
     * e limpando caracteres de CPF/CNPJ, garantindo o formato correto para o BR Code.
     * * Prioriza a checagem de chaves de tamanho fixo (CPF/CNPJ) para evitar a ambiguidade
     * com números de celular de 11 dígitos, garantindo a formatação correta.
     * * @param chave A chave Pix de entrada.
     * @return A chave Pix normalizada.
     */
    private static String normalizarChavePix(String chave, boolean isCelular) {
        chave = chave.trim();
        
        // 1. Chaves não-numéricas (E-mail, Aleatória, ou Telefone já formatado com prefixo '+')
        // Retorna a chave original, pois já está no formato correto.
        if (chave.contains("@") || chave.length() == 32 || chave.startsWith("+")) {
            return chave;
        }

        String apenasDigitos = limparString(chave);
        int len = apenasDigitos.length();

        // 2. CNPJ (14 dígitos) - Chave puramente numérica de tamanho fixo
        if (len == 14) {
            return apenasDigitos;
        }
        
        // 3. CPF (11 dígitos) - Chave puramente numérica de tamanho fixo
        // Se a entrada for 11 dígitos puros, sem prefixo, o padrão BR Code exige que seja CPF
        // (o celular com 11 dígitos PRECISA ser fornecido com o '+' para ser distinguível).
        if (len == 11 && !isCelular) {
            return apenasDigitos; 
        }

        // 4. Telefone (10 dígitos - assumimos DD + 8 dígitos)
        // O Telefone (E.164) requer o prefixo '+55'. Se for 10 dígitos puros, adicionamos o prefixo.
        if (len == 11 && isCelular) {
            return "+55" + apenasDigitos;
        }
        
        // 5. Outros casos (incluindo telefones com 12+ dígitos, que não são padrão, ou formatos desconhecidos)
        // Retorna a chave original.
        return chave;
    }
}