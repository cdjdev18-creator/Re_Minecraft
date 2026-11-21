package com.reminecraft;

import org.lwjgl.stb.STBImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class TextureLoader {
    public static int loadTexture(String path) {
        IntBuffer w = org.lwjgl.BufferUtils.createIntBuffer(1);
        IntBuffer h = org.lwjgl.BufferUtils.createIntBuffer(1);
        IntBuffer c = org.lwjgl.BufferUtils.createIntBuffer(1);
        ByteBuffer data = stbi_load(path, w, h, c, 4);
        if (data == null) { System.err.println("Failed to load texture: " + path); return 0; }
        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w.get(0), h.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        stbi_image_free(data);
        return tex;
    }
}
