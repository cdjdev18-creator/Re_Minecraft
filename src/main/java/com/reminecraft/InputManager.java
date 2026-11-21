package com.reminecraft;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {
    private final long window;
    private boolean[] keys = new boolean[512];
    private boolean[] mouseButtons = new boolean[8];
    private double mouseX, mouseY;

    public InputManager(long window) {
        this.window = window;
        glfwSetKeyCallback(window, new GLFWKeyCallback() {
            public void invoke(long w, int key, int scancode, int action, int mods) {
                if (key >= 0 && key < keys.length) keys[key] = (action != GLFW_RELEASE);
            }
        });
        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            public void invoke(long w, int button, int action, int mods) {
                if (button >= 0 && button < mouseButtons.length) {
                    mouseButtons[button] = (action != GLFW_RELEASE);
                    if (action == GLFW_PRESS) mouseClicked[button] = true;
                }
            }
        });
        glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            public void invoke(long w, double x, double y) { mouseX = x; mouseY = y; }
        });
    }

    public boolean isKeyPressed(int key) { return key >= 0 && key < keys.length && keys[key]; }
    public boolean isMouseButtonPressed(int b) { return b >=0 && b < mouseButtons.length && mouseButtons[b]; }
    public double getMouseX() { return mouseX; }
    public double getMouseY() { return mouseY; }
    private boolean[] mouseClicked = new boolean[8];
    // in callback: if action == GLFW_PRESS mouseClicked[button]=true;
    public boolean isMouseButtonClicked(int b){
        if(b<0||b>=mouseClicked.length) return false;
        if(mouseClicked[b]){ mouseClicked[b]=false; return true; }
        return false;
    }
}
