import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Garante que toda cena da história tem uma imagem para cada diálogo
 * e que todo arquivo citado no catálogo existe em resources/images/cenas.
 */
public class CatalogoDeImagensTest {

    private List<Cena> todasAsCenas() throws Exception {
        ConstrutorDeCenas construtor = new ConstrutorDeCenas(EstadoPartida.nova(new Protagonista("Teste", 30, "Outro")));

        List<Cena> cenas = new ArrayList<>();
        cenas.add(construtor.criarPrologo());
        cenas.add(construtor.criarAtoI());
        cenas.add(construtor.criarFinalAtoI());
        cenas.add(construtor.criarAtoIIRota1A());
        cenas.add(construtor.criarAtoIIRota1B());
        cenas.add(construtor.criarAtoIIRota2A());
        cenas.add(construtor.criarAtoIIRota2B());
        cenas.add(construtor.criarAtoIIRota3A());
        cenas.add(construtor.criarAtoIIRota3B());
        cenas.add(construtor.criarAtoIIIPerspectiva1A());
        cenas.add(construtor.criarAtoIIIPerspectiva1B());
        cenas.add(construtor.criarAtoIIIPerspectiva2A());
        cenas.add(construtor.criarAtoIIIPerspectiva3A());
        cenas.add(construtor.criarAtoIV());
        cenas.add(construtor.criarAtoVRota1A());
        cenas.add(construtor.criarAtoVRota1B());
        cenas.add(construtor.criarAtoVRota1C());
        cenas.add(construtor.criarAtoVI());
        cenas.add(construtor.criarFinalBalanca());
        cenas.add(construtor.criarFinalNevoa());
        cenas.add(construtor.criarFinalSangue());
        return cenas;
    }

    @Test
    public void todoDialogoDeTodaCenaDeveTerImagemNoCatalogo() throws Exception {
        for (Cena cena : todasAsCenas()) {
            int quantidade = cena.getDialogos().size();

            for (int indice = 1; indice <= quantidade; indice++) {
                assertNotNull(CatalogoDeImagens.arquivoDe(cena.getCenaId(), indice),
                        "Sem imagem para " + cena.getCenaId() + " diálogo " + indice);
            }

            // e nenhuma entrada do catálogo sobra além do último diálogo da cena
            assertNull(CatalogoDeImagens.arquivoDe(cena.getCenaId(), quantidade + 1),
                    "Catálogo tem diálogo a mais em " + cena.getCenaId());
        }
    }

    @Test
    public void todoArquivoDoCatalogoDeveExistirNaPastaDeImagens() {
        for (String arquivo : CatalogoDeImagens.arquivos()) {
            assertNotNull(getClass().getResource("/images/cenas/" + arquivo + ".png"),
                    "Arquivo não encontrado: " + arquivo + ".png");
        }
    }

    @Test
    public void dialogoSemImagemNoCatalogoDeveDevolverNull() {
        assertNull(CatalogoDeImagens.arquivoDe("cena_que_nao_existe", 1));
    }
}
