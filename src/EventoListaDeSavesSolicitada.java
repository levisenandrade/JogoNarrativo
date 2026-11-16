import java.util.List;

/** Mostrar uma lista numerada de saves. Resposta: opção escolhida (0 = voltar). */
public class EventoListaDeSavesSolicitada extends EventoRequisicao<Integer> {

    private final String titulo;
    private final List<String> linhas;

    public EventoListaDeSavesSolicitada(String titulo, List<String> linhas) {
        this.titulo = titulo;
        this.linhas = linhas;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<String> getLinhas() {
        return linhas;
    }
}
