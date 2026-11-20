import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RepositorioEmMemoriaTest {

    private RepositorioDeSaves repositorio; // usado pela interface, como o Controlador faz
    private EstadoPartida estado;

    @BeforeEach
    public void prepara() {
        repositorio = new RepositorioEmMemoria();

        Protagonista protagonista = new Protagonista("Levi", 23, "Masculino");
        protagonista.cadastrarItens();
        estado = EstadoPartida.nova(protagonista);
        estado.setAto(Ato.ATO_II);
        estado.setRotaAtoII(1);
    }

    @Test
    public void slotVazioNaoDeveExistirENaoTemMetadados() throws Exception {
        assertFalse(repositorio.existe(1));
        assertNull(repositorio.lerMetadados(1));
    }

    @Test
    public void deveSalvarECarregarOEstado() throws Exception {
        repositorio.salvar(1, estado);

        assertTrue(repositorio.existe(1));
        EstadoPartida carregado = repositorio.carregar(1);
        assertEquals("Levi", carregado.getProtagonista().getNome());
        assertEquals(Ato.ATO_II, carregado.getAto());
        assertEquals("Levi", repositorio.lerMetadados(1).getNomeJogador());
    }

    @Test
    public void deveExcluirUmSave() throws Exception {
        repositorio.salvar(2, estado);
        repositorio.excluir(2);

        assertFalse(repositorio.existe(2));
    }

    @Test
    public void carregarSlotVazioDeveLancarExcecao() {
        assertThrows(CarregamentoException.class, () -> repositorio.carregar(3));
    }

    @Test
    public void slotInvalidoDeveLancarExcecaoAoSalvar() {
        assertThrows(SaveException.class, () -> repositorio.salvar(9, estado));
    }
}
