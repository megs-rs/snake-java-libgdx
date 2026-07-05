package com.badlogicgames.snake;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    
    // Cores Nokia
    private static final float[] NOKIA_BG = {0.607f, 0.737f, 0.058f};      // #9bbc0f
    private static final float[] NOKIA_DARK = {0.188f, 0.384f, 0.188f};     // #306230
    private static final float[] NOKIA_BLACK = {0.058f, 0.219f, 0.058f};    // #0f380f
    private static final float[] NOKIA_LIGHT = {0.768f, 0.811f, 0.631f};    // #c4cfa1
    
    // Configurações
    private static final int LARGURA = 320;
    private static final int ALTURA = 240;
    private static final int TAMANHO_BLOCO = 16;
    private static final float DELTA_TEMPO = 0.125f; // 8 FPS
    
    private ArrayList<Vector2> cobra;
    private Vector2 comida;
    private Vector2 direcao;
    private Vector2 novaDirecao;
    private int pontos;
    private float tempoAcumulado;
    private boolean gameOver;
    private Random random;
    
    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, LARGURA, ALTURA);
        random = new Random();
        resetGame();
    }
    
    private void resetGame() {
        // Posição inicial
        int centroX = (LARGURA / TAMANHO_BLOCO) / 2;
        int centroY = (ALTURA / TAMANHO_BLOCO) / 2;
        
        cobra = new ArrayList<>();
        cobra.add(new Vector2(centroX, centroY));
        cobra.add(new Vector2(centroX - 1, centroY));
        cobra.add(new Vector2(centroX - 2, centroY));
        
        direcao = new Vector2(1, 0); // Para direita
        novaDirecao = new Vector2(1, 0);
        pontos = 0;
        tempoAcumulado = 0;
        gameOver = false;
        
        gerarComida();
    }
    
    private void gerarComida() {
        int maxX = LARGURA / TAMANHO_BLOCO;
        int maxY = ALTURA / TAMANHO_BLOCO;
        
        do {
            comida = new Vector2(
                random.nextInt(maxX),
                random.nextInt(maxY)
            );
        } while (posicaoNaCobra(comida));
    }
    
    private boolean posicaoNaCobra(Vector2 pos) {
        for (Vector2 parte : cobra) {
            if (parte.epsilonEquals(pos, 0.01f)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void render() {
        // Input
        processarInput();
        
        // Atualização do jogo
        tempoAcumulado += Gdx.graphics.getDeltaTime();
        
        if (!gameOver && tempoAcumulado >= DELTA_TEMPO) {
            atualizar();
            tempoAcumulado = 0;
        }
        
        // Render
        Gdx.gl.glClearColor(NOKIA_BG[0], NOKIA_BG[1], NOKIA_BG[2], 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        
        desenhar();
        
        // Reinício no game over
        if (gameOver) {
            if (Gdx.input.isKeyJustPressed(Keys.R)) {
                resetGame();
            }
        }
    }
    
    private void processarInput() {
        if (Gdx.input.isKeyPressed(Keys.UP) && direcao.y != -1) {
            novaDirecao.set(0, 1);
        } else if (Gdx.input.isKeyPressed(Keys.DOWN) && direcao.y != 1) {
            novaDirecao.set(0, -1);
        } else if (Gdx.input.isKeyPressed(Keys.LEFT) && direcao.x != 1) {
            novaDirecao.set(-1, 0);
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT) && direcao.x != -1) {
            novaDirecao.set(1, 0);
        }
    }
    
    private void atualizar() {
        direcao.set(novaDirecao);
        
        Vector2 cabeca = cobra.get(0);
        Vector2 novaPos = new Vector2(cabeca.x + direcao.x, cabeca.y + direcao.y);
        
        int maxX = LARGURA / TAMANHO_BLOCO;
        int maxY = ALTURA / TAMANHO_BLOCO;
        
        // Verifica colisões
        if (novaPos.x < 0 || novaPos.x >= maxX || novaPos.y < 0 || novaPos.y >= maxY) {
            gameOver = true;
            return;
        }
        
        // Colisão com próprio corpo
        for (int i = 0; i < cobra.size(); i++) {
            if (novaPos.epsilonEquals(cobra.get(i), 0.01f)) {
                gameOver = true;
                return;
            }
        }
        
        // Move
        cobra.add(0, novaPos);
        
        // Comeu comida?
        if (novaPos.epsilonEquals(comida, 0.01f)) {
            pontos += 10;
            gerarComida();
        } else {
            cobra.remove(cobra.size() - 1);
        }
    }
    
    private void desenhar() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Desenha comida
        shapeRenderer.setColor(NOKIA_BLACK[0], NOKIA_BLACK[1], NOKIA_BLACK[2], 1);
        float comidaX = comida.x * TAMANHO_BLOCO + TAMANHO_BLOCO / 2;
        float comidaY = comida.y * TAMANHO_BLOCO + TAMANHO_BLOCO / 2;
        shapeRenderer.circle(comidaX, comidaY, 4);
        
        // Desenha corpo (exceto cabeça)
        for (int i = 1; i < cobra.size(); i++) {
            shapeRenderer.setColor(NOKIA_DARK[0], NOKIA_DARK[1], NOKIA_DARK[2], 1);
            Vector2 parte = cobra.get(i);
            shapeRenderer.rect(
                parte.x * TAMANHO_BLOCO + 2,
                parte.y * TAMANHO_BLOCO + 2,
                TAMANHO_BLOCO - 4,
                TAMANHO_BLOCO - 4
            );
        }
        
        // Desenha cabeça
        Vector2 cabeca = cobra.get(0);
        shapeRenderer.setColor(NOKIA_BLACK[0], NOKIA_BLACK[1], NOKIA_BLACK[2], 1);
        shapeRenderer.rect(
            cabeca.x * TAMANHO_BLOCO + 2,
            cabeca.y * TAMANHO_BLOCO + 2,
            TAMANHO_BLOCO - 4,
            TAMANHO_BLOCO - 4
        );
        
        // Olhos (direcionais)
        shapeRenderer.setColor(NOKIA_LIGHT[0], NOKIA_LIGHT[1], NOKIA_LIGHT[2], 1);
        float cx = cabeca.x * TAMANHO_BLOCO + TAMANHO_BLOCO / 2;
        float cy = cabeca.y * TAMANHO_BLOCO + TAMANHO_BLOCO / 2;
        
        // Posição dos olhos muda conforme direção
        float olhoOffsetX = direcao.x * 2;
        float olhoOffsetY = direcao.y * 2;
        
        if (direcao.x == 1) { // Direita
            shapeRenderer.circle(cx + 2, cy - 2, 1.5f);
            shapeRenderer.circle(cx + 2, cy + 2, 1.5f);
        } else if (direcao.x == -1) { // Esquerda
            shapeRenderer.circle(cx - 2, cy - 2, 1.5f);
            shapeRenderer.circle(cx - 2, cy + 2, 1.5f);
        } else if (direcao.y == 1) { // Cima
            shapeRenderer.circle(cx - 2, cy + 2, 1.5f);
            shapeRenderer.circle(cx + 2, cy + 2, 1.5f);
        } else { // Baixo
            shapeRenderer.circle(cx - 2, cy - 2, 1.5f);
            shapeRenderer.circle(cx + 2, cy - 2, 1.5f);
        }
        
        shapeRenderer.end();
    }
    
    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}