# Snake Nokia 🐍

Recriação do clássico Snake do Nokia 3310 feita com **libGDX 1.12.1** e **Java 21**.

> Jogue diretamente no desktop ou instale no Android.

---

## Requisitos

- Java 21+
- Gradle 8.6 (via wrapper incluso)

## Como Executar

```bash
# Desktop
./gradlew desktop:run

# Android (dispositivo conectado)
./gradlew android:installDebug
```

## Como Jogar

| Tecla | Ação |
|---|---|
| ⬆ ⬇ ⬅ ➡ | Movimentar a cobra |
| R | Reiniciar (após game over) |

## Build

```bash
# Gerar JAR executável do desktop
./gradlew desktop:dist
# O JAR estará em desktop/build/libs/
```

## Estrutura do Projeto

```
snake-java-libgdx/
├── core/                          # Lógica do jogo
│   └── src/main/java/.../SnakeGame.java
├── desktop/                       # Inicializador desktop
│   └── src/main/java/.../DesktopLauncher.java
├── android/                       # Inicializador Android
│   └── src/main/java/.../AndroidLauncher.java
├── build.gradle                   # Build principal
└── settings.gradle                # Configuração dos módulos
```

## Características

- Grid 20×15, bloco 16 px
- 8 atualizações por segundo
- Paleta de cores Nokia 3310 (4 cores)
- Renderização 100% procedural (sem sprites)
- Desktop (LWJGL3) e Android

## Licença

MIT
