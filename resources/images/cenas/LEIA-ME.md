# Imagens das cenas

Caixa de imagem do jogo: 982 x 327 px (proporção 3:1).
Tamanho recomendado das imagens: 1964 x 654 px (ou 2172 x 724 px). Formato PNG.

## Como o jogo escolhe a imagem de cada diálogo
1. `src/CatalogoDeImagens.java` — tabela "cena_NN -> nome do arquivo" (NN = número do diálogo, começando em 01).
2. Se não estiver na tabela: arquivo `idDaCena_NN.png` nesta pasta.
3. Se não existir: arquivo `idDaCena.png`.
4. Se nada existir: aparece um quadro com o nome da cena (placeholder).

## Para trocar uma imagem
- Troque o arquivo PNG mantendo o nome, ou
- Mude o nome do arquivo na tabela de `CatalogoDeImagens.java`.

O teste `CatalogoDeImagensTest` confere se todo diálogo tem imagem e se todos os arquivos existem.
Imagem sem uso no momento: `viela-barbearia-corpo.png` (game over do Ato III 1B).
