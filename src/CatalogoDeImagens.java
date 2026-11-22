/**
 * catálogo das imagens de fundo da tela da narrativa (parte da View)
 *
 * Cada diálogo de uma cena é identificado por "idDaCena_NN" (NN = posição do diálogo na cena, começando
 * em 01) e associado ao nome de um arquivo PNG da pasta resources/images/cenas (sem o ".png").
 * Assim, vários diálogos podem usar a mesma imagem sem precisar copiar o arquivo.
 *
 * Para trocar a arte de um diálogo, basta mudar o nome do arquivo na linha correspondente.
 * Se um diálogo não estiver aqui, o JogoController tenta "idDaCena_NN.png" e depois "idDaCena.png".
 * O teste CatalogoDeImagensTest confere se todos os diálogos têm imagem e se os arquivos existem.
 */

import java.util.HashMap;
import java.util.Map;

public class CatalogoDeImagens {

    private static final Map<String, String> IMAGENS = new HashMap<>();

    static {
        // ---------------------------------------------------------------- prólogo
        IMAGENS.put("prologo_01", "preto");
        IMAGENS.put("prologo_02", "entrada-cidade");
        IMAGENS.put("prologo_03", "cidade-de-cima-noite");
        IMAGENS.put("prologo_04", "radio-noite");
        IMAGENS.put("prologo_05", "bairro-residencial");
        IMAGENS.put("prologo_06", "cena-de-crime");
        IMAGENS.put("prologo_07", "banca-de-jornal");
        IMAGENS.put("prologo_08", "preto");

        // ---------------------------------------------------------------- ato I
        IMAGENS.put("ato_1_01", "onibus-interior");
        IMAGENS.put("ato_1_02", "rodoviaria");
        IMAGENS.put("ato_1_03", "rodoviaria");
        IMAGENS.put("ato_1_04", "bairro-residencial");
        IMAGENS.put("ato_1_05", "bairro-residencial");
        IMAGENS.put("ato_1_06", "bairro-residencial");
        IMAGENS.put("ato_1_07", "beco-delegacia");
        IMAGENS.put("fim_ato_1_01", "banca-de-jornal");

        // ---------------------------------------------------------------- ato II
        IMAGENS.put("ato2_opcao1a_01", "delegacia-fundos");
        IMAGENS.put("ato2_opcao1a_02", "delegacia-fundos");
        IMAGENS.put("ato2_opcao1a_03", "sala-detetive");

        IMAGENS.put("ato2_opcao1b_01", "escritorio-detetive");
        IMAGENS.put("ato2_opcao1b_02", "escritorio-detetive");

        IMAGENS.put("ato2_rota2a_01", "fabrica-exterior");
        IMAGENS.put("ato2_rota2a_02", "fabrica-subsolo");
        IMAGENS.put("ato2_rota2a_03", "fabrica-subsolo");
        IMAGENS.put("ato2_rota2a_04", "fabrica-subsolo");

        IMAGENS.put("ato2_rota2b_01", "arquivo-municipal-fachada");
        IMAGENS.put("ato2_rota2b_02", "arquivo-municipal-interior");
        IMAGENS.put("ato2_rota2b_03", "arquivo-municipal-interior");

        IMAGENS.put("ato2_rota3a_01", "beco-delegacia");
        IMAGENS.put("ato2_rota3a_02", "sala-interrogatorio");
        IMAGENS.put("ato2_rota3a_03", "delegacia-fundos");

        IMAGENS.put("ato2_rota3b_01", "delegacia-fundos");

        // ---------------------------------------------------------------- ato III
        IMAGENS.put("ato3_perspectiva1a_01", "delegacia-fundos");
        IMAGENS.put("ato3_perspectiva1a_02", "jornal-capa");
        IMAGENS.put("ato3_perspectiva1a_03", "beco-escuro");
        IMAGENS.put("ato3_perspectiva1a_04", "prefeitura-fachada");

        IMAGENS.put("ato3_perspectiva1b_01", "delegacia-fundos");
        IMAGENS.put("ato3_perspectiva1b_02", "jornal-capa");
        IMAGENS.put("ato3_perspectiva1b_03", "viela-barbearia");
        IMAGENS.put("ato3_perspectiva1b_04", "cidade-de-cima-noite");

        IMAGENS.put("ato3_perspectiva2a_01", "arquivo-municipal-interior");
        IMAGENS.put("ato3_perspectiva2a_02", "jornal-capa");
        IMAGENS.put("ato3_perspectiva2a_03", "viela-barbearia");
        IMAGENS.put("ato3_perspectiva2a_04", "cidade-de-cima-noite");

        IMAGENS.put("ato3_perspectiva3a_01", "arquivo-municipal-fachada");
        IMAGENS.put("ato3_perspectiva3a_02", "jornal-capa");
        IMAGENS.put("ato3_perspectiva3a_03", "praca-matriz-amanhecer");
        IMAGENS.put("ato3_perspectiva3a_04", "prefeitura-saguao");

        // ---------------------------------------------------------------- ato IV
        IMAGENS.put("ato4_a_verdade_01", "prefeitura-saguao");
        IMAGENS.put("ato4_a_verdade_02", "prefeitura-saguao");
        IMAGENS.put("ato4_a_verdade_03", "camara-plenario");

        // ---------------------------------------------------------------- ato V
        IMAGENS.put("ato5_rota1a_01", "camara-plenario");
        IMAGENS.put("ato5_rota1a_02", "policia-federal");
        IMAGENS.put("ato5_rota1a_03", "policia-federal");

        IMAGENS.put("ato5_rota1b_01", "camara-multidao-manha");
        IMAGENS.put("ato5_rota1b_02", "radio-manha");
        IMAGENS.put("ato5_rota1b_03", "praca-multidao");

        IMAGENS.put("ato5_rota1c_01", "camara-plenario");
        IMAGENS.put("ato5_rota1c_02", "delegacia-fundos");
        IMAGENS.put("ato5_rota1c_03", "rua-de-dia");

        // ---------------------------------------------------------------- ato VI e finais
        IMAGENS.put("ato6_o_preco_01", "cemiterio");

        IMAGENS.put("final1_balanca_01", "policia-federal");
        IMAGENS.put("final1_balanca_02", "cemiterio");

        IMAGENS.put("final2_nevoa_01", "praca-matriz-dia");
        IMAGENS.put("final2_nevoa_02", "jornal-capa");

        IMAGENS.put("final3_sangue_01", "caminhonete-1");
        IMAGENS.put("final3_sangue_02", "caminhonete-2");
    }

    private CatalogoDeImagens() {
    }

    /**
     * @param cenaId id da cena (ex.: "prologo")
     * @param indice posição do diálogo na cena, começando em 1
     * @return o nome do arquivo (sem ".png") ou null se esse diálogo não tem imagem no catálogo
     */
    public static String arquivoDe(String cenaId, int indice) {
        return IMAGENS.get(String.format("%s_%02d", cenaId, indice));
    }

    /**
     * @return os nomes de todos os arquivos usados no catálogo (usado para validar que eles existem)
     */
    public static java.util.Collection<String> arquivos() {
        return IMAGENS.values();
    }
}
