import java.util.HashMap;
import java.util.Map;

/**
 * Segunda implementação de RepositorioDeSaves: guarda os saves num mapa, sem gravar arquivos.
 * Mostra o padrão Strategy na prática: o Controlador só conhece a interface e funciona
 * com qualquer uma das duas (RepositorioJson ou esta). Também serve para testes rápidos.
 *
 * Os dados são guardados já convertidos em SaveData, igual ao que o JSON faz,
 * então o estado salvo não muda se a partida continuar a ser jogada.
 */
public class RepositorioEmMemoria implements RepositorioDeSaves {

    private final Map<Integer, SaveData> saves = new HashMap<>();

    @Override
    public void salvar(int slot, EstadoPartida estado) throws SaveException {
        validarSlot(slot);
        saves.put(slot, SaveData.de(estado, slot));
    }

    @Override
    public EstadoPartida carregar(int slot) throws CarregamentoException {
        return lerDados(slot).paraEstado();
    }

    @Override
    public MetadadosSave lerMetadados(int slot) throws CarregamentoException {
        if (!existe(slot)) {
            return null;
        }

        SaveData dados = lerDados(slot);
        dados.validar();
        return dados.getMetadados();
    }

    @Override
    public boolean existe(int slot) {
        return saves.containsKey(slot);
    }

    @Override
    public void excluir(int slot) throws SaveException {
        validarSlot(slot);
        saves.remove(slot);
    }

    private SaveData lerDados(int slot) throws CarregamentoException {
        if (!existe(slot)) {
            throw new CarregamentoException("O slot " + slot + " está vazio.");
        }

        return saves.get(slot);
    }

    private void validarSlot(int slot) throws SaveException {
        if (slot < SLOT_AUTOMATICO || slot > ULTIMO_SLOT_MANUAL) {
            throw new SaveException("Slot inválido: " + slot);
        }
    }
}
