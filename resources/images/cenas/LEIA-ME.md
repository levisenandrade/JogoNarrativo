# Imagens das cenas

Insira arquivos PNG nesta pasta. **Não é preciso editar os JSONs para usar as artes.**

- Uma imagem para todo um arquivo/cena: `prologo.png`, `ato_1.png`, `ato4_a_verdade.png` etc.
- Imagens diferentes por diálogo: `prologo_01.png`, `prologo_02.png`, `ato_1_01.png` etc.
- Primeiro o JavaFX tenta o nome específico do diálogo (`<id>_NN.png`), depois o nome da cena (`<id>.png`).
- Sem imagem, exibe apenas o painel textual provisório, **sem quebrar a execução**.
- O `id` é o campo `id` do JSON (ou o ID da Cena construída em Java), não necessariamente o nome do arquivo JSON.
- Use imagens 16:9 ou mais largas. PNGs com 1600x900 funcionam bem; não há tamanho obrigatório.

Exemplo: para `resources/cenas/prologo.json`, se o `id` for `prologo`, a arte `prologo_01.png`
será exibida no primeiro diálogo. Se só existir `prologo.png`, ela ficará em todos os diálogos.
