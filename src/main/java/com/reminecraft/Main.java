package com.reminecraft;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    private Window window;
    private Game game;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to init GLFW");
        NativeBridge.init();
        window = new Window(1280, 720, "Re_Minecraft");
        window.create();
        GL.createCapabilities();
        game = new Game(window);
        game.init();
    }

    private void loop() {
        while (!window.shouldClose()) {
            game.update();
            game.render();
            window.swapBuffers();
            glfwPollEvents();
            game.postUpdate();
        }
    }

    private void cleanup() {
        game.cleanup();
        window.destroy();
        glfwTerminate();
    }
}
