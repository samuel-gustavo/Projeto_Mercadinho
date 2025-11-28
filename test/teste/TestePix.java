package teste;

/**
 *
 * @author samuel
 */
public class TestePix {
    
    public static void main(String[] args) throws Exception {
        
        // Dados para gerar o Payload:
        String chave = "71424858410"; // Chave de telefone
        String nome = "Samuel Gustavo Lima da Silva";
        String valor = "4007.78";
        String cidade = "Serrote dos Bezerras"; // Novo parâmetro

        // Chama a função para gerar o payload (ajustada para 4 parâmetros)
        // OBS: Você precisará ajustar a assinatura do método no TestePix para aceitar a cidade
        String payload = PixQRCode.gerarPayloadPix(chave, nome, valor, cidade); 

        System.out.println(payload);

        PixQRCode.gerarQrCode(payload, "pix.png");
    }
}