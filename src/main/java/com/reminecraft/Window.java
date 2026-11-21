package com.reminecraft;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private final String title;
    private long handle;
    private int fbWidth, fbHeight;

    public Window(int width, int height, String title) {
        this.width = width; this.height = height; this.title = title;
    }

    public void create() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (handle == NULL) throw new RuntimeException("Failed to create window");
        GLFWVidMode vm = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(handle, (vm.width()-width)/2, (vm.height()-height)/2);
        glfwMaximizeWindow(handle);
        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);
        glfwSetFramebufferSizeCallback(handle, (w, fw, fh) -> { fbWidth = fw; fbHeight = fh; });
        int[] w = new int[1], h = new int[1];
        glfwGetWindowSize(handle, w, h);
        width = w[0]; height = h[0];
        int[] fw = new int[1], fh = new int[1];
        glfwGetFramebufferSize(handle, fw, fh);
        fbWidth = fw[0]; fbHeight = fh[0];
        glfwSetWindowSizeCallback(handle, (w2, nw, nh) -> { width = nw; height = nh; });
        glfwShowWindow(handle);
    }

    public boolean shouldClose() { return glfwWindowShouldClose(handle); }
    public void swapBuffers() { glfwSwapBuffers(handle); }
    public long getHandle() { return handle; }
    public void destroy() { glfwDestroyWindow(handle); }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getFramebufferWidth() { return fbWidth; }
    public int getFramebufferHeight() { return fbHeight; }
}
