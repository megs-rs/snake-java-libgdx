package com.badlogicgames.snake.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogicgames.snake.SnakeGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Snake Nokia 🐍");
        config.setWindowedMode(320, 240);
        config.setResizable(false);
        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setIdleFPS(60);
        
        new Lwjgl3Application(new SnakeGame(), config);
    }
}