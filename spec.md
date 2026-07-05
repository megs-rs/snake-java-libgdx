# Snake Nokia — Especificação Técnica

## Visão Geral

Recriação do clássico jogo Snake do Nokia 3310 usando libGDX 1.12.1. O jogo utiliza renderização por primitivas (`ShapeRenderer`) — sem assets externos.

---

## Stack Tecnológica

| Componente | Tecnologia |
|---|---|
| Engine | libGDX 1.12.1 |
| Linguagem | Java 21 |
| Desktop | LWJGL3 |
| Android | SDK 21–34, GLES 2.0 |
| Build | Gradle 8.6 (multi-projeto) |

## Módulos

| Módulo | Função |
|---|---|
| `:core` | Lógica do jogo e renderização |
| `:desktop` | Inicializador desktop (LWJGL3) |
| `:android` | Inicializador Android |

---

## Configurações

### Tela

| Parâmetro | Valor |
|---|---|
| Resolução | 320 × 240 px |
| Tamanho do bloco | 16 px |
| Grid | 20 × 15 células |
| Tela cheia | Não |
| Redimensionável | Não |

### Jogabilidade

| Parâmetro | Valor |
|---|---|
| Tick rate | 8 atualizações/s |
| Delta fixo | 0,125 s |
| Tamanho inicial da cobra | 3 segmentos |
| Pontuação por comida | +10 |
| Velocidade | Constante |

### Cores (paleta Nokia)

| Nome | RGB | Uso |
|---|---|---|
| `NOKIA_BG` | #9bbc0f | Fundo |
| `NOKIA_DARK` | #306230 | Corpo da cobra |
| `NOKIA_BLACK` | #0f380f | Cabeça e comida |
| `NOKIA_LIGHT` | #c4cfa1 | Olhos |

---

## Arquitetura

### Estrutura de Classes

Toda a lógica está em `SnakeGame.java` (~225 linhas):

```
SnakeGame (ApplicationAdapter)
├── create()          → inicializa renderer, câmera, resetGame()
├── render()          → processa input, atualiza, desenha
├── resetGame()       → reinicia estado
├── processarInput()  → setas direcionais (sem reversão 180°)
├── atualizar()       → mover, colisão, comer
├── gerarComida()     → posição aleatória livre
├── posicaoNaCobra()  → verifica ocupação
├── desenhar()        → ShapeRenderer (retângulos e círculos)
└── dispose()          → libera ShapeRenderer
```

### Dependências

```groovy
:core → gdx:1.12.1
:desktop → :core + gdx-backend-lwjgl3 + gdx-platform:natives-desktop
:android → :core + gdx-backend-android + natives (armeabi-v7a, arm64-v8a)
```

---

## Mecânicas

### Movimento
- Direção é armazenada como vetor unitário (x, y)
- Input é bufferizado em `novaDirecao` para não ser perdido entre ticks
- Reversão de 180° é bloqueada (ex.: movendo direita não pode virar esquerda)

### Colisão
- **Parede**: cabeça fora de `[0, 20)` × `[0, 15)` → game over
- **Auto**: cabeça colide com qualquer segmento do corpo → game over

### Comida
- Uma comida por vez, posicionada aleatoriamente em célula vazia
- Ao ser comida: cobra cresce (cauda não é removida), nova comida gerada

### Game Over
- Estado congelado (renderização para mas não reinicia)
- Tecla `R` reinicia o jogo

### Renderização
- Fundo: glClear com cor Nokia
- Comida: círculo preto (raio 4 px)
- Corpo: retângulos escuros com padding de 2 px
- Cabeça: retângulo preto com olhos direcionais (2 círculos claros)

---

## Plataformas

### Desktop
- Janela 320×240 não redimensionável
- Título: "Snake Nokia 🐍"
- VSync ativado, 60 FPS
- Entrada: teclado (setas)

### Android
- Landscape, fullscreen, imersivo
- Wake lock ativo
- GLES 2.0
- Entrada: apenas teclado físico (sem suporte touch)

---

## Observações

- Sem display de pontuação (variável `pontos` existe mas não é renderizada)
- Sem som
- Sem power-ups ou aumento de velocidade
- Sem suporte iOS ou HTML (embora plugins estejam no buildscript)
- Sem assets externos (tudo é procedural)
