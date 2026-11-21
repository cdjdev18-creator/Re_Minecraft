package com.reminecraft;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {
    public void clear(float r, float g, float b) {
        glClearColor(r,g,b,1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void drawRect(float x, float y, float w, float h, float r, float g, float b) {
        glColor3f(r,g,b);
        glBegin(GL_QUADS);
        glVertex2f(x,y); glVertex2f(x+w,y); glVertex2f(x+w,y+h); glVertex2f(x,y+h);
        glEnd();
    }

    public void drawText(String text, float x, float y, float size, Color color) {
        // Render via AWT to texture each call (simple)
        Font font = new Font("SansSerif", Font.BOLD, (int)size);
        FontMetrics fm = new Canvas().getFontMetrics(font);
        int tw = fm.stringWidth(text)+4, th = fm.getHeight()+4;
        BufferedImage img = new BufferedImage(tw, th, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setFont(font);
        g.setColor(color);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawString(text, 2, fm.getAscent()+2);
        g.dispose();
        int[] pixels = img.getRGB(0,0,tw,th,null,0,tw);
        ByteBuffer buf = ByteBuffer.allocateDirect(tw*th*4);
        for (int p : pixels) { buf.put((byte)((p>>16)&0xFF)); buf.put((byte)((p>>8)&0xFF)); buf.put((byte)(p&0xFF)); buf.put((byte)((p>>24)&0xFF)); }
        buf.flip();
        glDisable(GL_DEPTH_TEST);
        int tex = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, tex);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, tw, th, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor3f(1,1,1);
        glBegin(GL_QUADS);
        glTexCoord2f(0,0); glVertex2f(x,y);
        glTexCoord2f(1,0); glVertex2f(x+tw,y);
        glTexCoord2f(1,1); glVertex2f(x+tw,y+th);
        glTexCoord2f(0,1); glVertex2f(x,y+th);
        glEnd();
        glDisable(GL_TEXTURE_2D);
        glDeleteTextures(tex);
        glEnable(GL_DEPTH_TEST);
    }
}
