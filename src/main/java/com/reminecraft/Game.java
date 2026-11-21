package com.reminecraft;

import java.awt.Color;
import motor.player.Player;
import motor.world.blokvertices;
import motor.sky.Cicle;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Game {
    public enum State { MAIN_MENU, PLAYING, PAUSED, OPTIONS }
    private Window window;
    private InputManager input;
    private ConfigManager config;
    private Renderer renderer;
    private World world;
    private OptionsMenu optionsMenu;
    private int atlasTex;
    private State state = State.MAIN_MENU;
    private State optionsReturn = State.MAIN_MENU;
    private boolean escPrev = false;
    private Cicle cicle = new Cicle();
    private Player player = new Player();
    private float yaw = 0, pitch = 0;
    private double lastMouseX, lastMouseY;
    private boolean firstMouse = true;
    private long lastTime = System.nanoTime();

    public Game(Window window) { this.window = window; }

    public void init() {
        GameLogger.init();
        input = new InputManager(window.getHandle());
        config = new ConfigManager();
        renderer = new Renderer();
        world = new World();
        optionsMenu = new OptionsMenu(config);
        atlasTex = TextureLoader.loadTexture("src/main/resources/textures/AtlasTexutre.png");
        System.out.println("[Re_Minecraft] atlasTex=" + atlasTex);
        glEnable(GL_DEPTH_TEST);
    }

    public void update() {
        boolean esc = input.isKeyPressed(GLFW_KEY_ESCAPE);
        long now = System.nanoTime();
        float dt = (now-lastTime)/1e9f;
        lastTime = now;
        if (state == State.PLAYING) {
            glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            double mx = input.getMouseX(), my = input.getMouseY();
            if(firstMouse){ lastMouseX=mx; lastMouseY=my; firstMouse=false; }
            float dx = (float)(mx-lastMouseX)*0.1f;
            float dy = (float)(my-lastMouseY)*0.1f;
            lastMouseX=mx; lastMouseY=my;
            yaw -= dx; pitch += dy;
            if(pitch>89) pitch=89; if(pitch<-89) pitch=-89;
            float speed = input.isKeyPressed(GLFW_KEY_LEFT_SHIFT)?8f:4f;
            float f=0,s=0;
            if(input.isKeyPressed(GLFW_KEY_W)) f+=1;
            if(input.isKeyPressed(GLFW_KEY_S)) f-=1;
            if(input.isKeyPressed(GLFW_KEY_A)) s-=1;
            if(input.isKeyPressed(GLFW_KEY_D)) s+=1;
            player.move(-f*speed, -s*speed);
            if(input.isKeyPressed(GLFW_KEY_SPACE)) player.jump();
            player.update(dt);
            cicle.update(dt);
        } else {
            glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            firstMouse=true;
        }
        if (esc && !escPrev) {
            if (state == State.PLAYING) state = State.PAUSED;
            else if (state == State.PAUSED) state = State.PLAYING;
            else if (state == State.OPTIONS) state = optionsReturn;
        }
        escPrev = esc;
        if (state == State.PLAYING) world.update();
        if (clickCooldown > 0) clickCooldown -= 0.016f;
    }

    public void render() {
        glViewport(0,0,window.getFramebufferWidth(),window.getFramebufferHeight());
        glMatrixMode(GL_PROJECTION); glLoadIdentity();
        glOrtho(0, window.getWidth(), window.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW); glLoadIdentity();

        if (state == State.MAIN_MENU) renderMainMenu();
        else if (state == State.OPTIONS) renderOptions();
        else if (state == State.PAUSED) { renderWorld3D(); renderPause(); }
        else { renderWorld3D(); drawCrosshair(); }
    }

    private void renderMainMenu() {
        renderer.clear(0.1f,0.1f,0.2f);
        renderer.drawText("Re_Minecraft", window.getWidth()/2f-144, window.getHeight()/2f-20, 40, Color.WHITE);
        // textured test quad
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, atlasTex);
        float u0=0, v0=0, u1=16f/256f, v1=16f/256f;
        glBegin(GL_QUADS);
        glTexCoord2f(u0,v0); glVertex2f(10,10);
        glTexCoord2f(u1,v0); glVertex2f(100,10);
        glTexCoord2f(u1,v1); glVertex2f(100,100);
        glTexCoord2f(u0,v1); glVertex2f(10,100);
        glEnd();
        glDisable(GL_TEXTURE_2D);
        float cx = window.getWidth()/2f-100, y = window.getHeight()/2f+60;
        drawButton("JUGAR", cx, y, 200, 40, () -> state = State.PLAYING);
        drawButton("OPCIONES", cx, y+60, 200, 40, () -> { optionsReturn = State.MAIN_MENU; state = State.OPTIONS; });
        drawButton("SALIR", cx, y+120, 200, 40, () -> glfwSetWindowShouldClose(window.getHandle(), true));
        renderer.drawText("By " + dates.DEVELOPER, 10, window.getHeight()-40, 14, Color.LIGHT_GRAY);
        renderer.drawText(dates.COPYRIGHT, 10, window.getHeight()-20, 14, Color.LIGHT_GRAY);
    }

    private void renderPause() {
        glMatrixMode(GL_PROJECTION); glLoadIdentity();
        glOrtho(0, window.getWidth(), window.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW); glLoadIdentity();
        renderer.drawRect(0,0,window.getWidth(),window.getHeight(),0,0,0);
        // overlay
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glColor4f(0,0,0,0.6f);
        glBegin(GL_QUADS);
        glVertex2f(0,0); glVertex2f(window.getWidth(),0); glVertex2f(window.getWidth(),window.getHeight()); glVertex2f(0,window.getHeight());
        glEnd();
        glDisable(GL_BLEND);
        renderer.drawText("PAUSA", window.getWidth()/2f-40, 120, 40, Color.WHITE);
        float cx = window.getWidth()/2f-100, y = 220;
        drawButton("REANUDAR", cx, y, 200, 40, () -> state = State.PLAYING);
        drawButton("OPCIONES", cx, y+60, 200, 40, () -> { optionsReturn = State.PAUSED; state = State.OPTIONS; });
        drawButton("VOLVER AL MENU", cx, y+120, 200, 40, () -> state = State.MAIN_MENU);
        renderer.drawText(GameLogger.getLogs().length()>200?GameLogger.getLogs().substring(GameLogger.getLogs().length()-200):GameLogger.getLogs(), 10, y+180, 12, Color.WHITE);
        drawButton("COPY", cx, y+320, 90, 30, () -> { java.awt.datatransfer.StringSelection s = new java.awt.datatransfer.StringSelection(GameLogger.getLogs()); java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, null); });
        drawButton("PASTE", cx+110, y+320, 90, 30, () -> { try { java.awt.datatransfer.Clipboard c = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard(); String d = (String)c.getData(java.awt.datatransfer.DataFlavor.stringFlavor); GameLogger.log("INFO", "Pasted: " + d); } catch(Exception e){ GameLogger.log("WARN","Clipboard empty"); } });
    }

    private void renderOptions() {
        optionsMenu.render(this, renderer, window, input);
    }

    private void renderWorld3D() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        glMatrixMode(GL_PROJECTION); glLoadIdentity();
        float aspect = (float)window.getWidth()/window.getHeight();
        // perspective
        float f = 1.0f/(float)Math.tan(Math.toRadians(60)/2);
        float[] m = new float[16];
        // simple perspective via glu-like manual
        glFrustum(-aspect, aspect, -1, 1, 1, 100);
        glMatrixMode(GL_MODELVIEW); glLoadIdentity();
        glRotatef(-pitch, 1, 0, 0);
        glRotatef(-yaw, 0, 1, 0);
        glTranslatef(-player.x, -player.y-1.62f, -player.z);
        // draw 16x16 chunk, 4 high
        for(int x=0;x<16;x++) for(int z=0;z<16;z++){
            drawTexturedCube(x,0,z,
                blokvertices.GRASS.TOP.x,blokvertices.GRASS.TOP.y,
                blokvertices.GRASS.WEST.x,blokvertices.GRASS.WEST.y,
                blokvertices.GRASS.BOTTOM.x,blokvertices.GRASS.BOTTOM.y); // grass top
            for(int y=1;y<4;y++) drawTexturedCube(x,-y,z,
                blokvertices.DIRT.TOP.x,blokvertices.DIRT.TOP.y,
                blokvertices.DIRT.WEST.x,blokvertices.DIRT.WEST.y,
                blokvertices.DIRT.BOTTOM.x,blokvertices.DIRT.BOTTOM.y); // dirt
        }
    }

    private void drawTexturedCube(float x,float y,float z,int topCellX,int topCellY,int sideCellX,int sideCellY,int bottomCellX,int bottomCellY){
        float ts=16f/256f;
        float tu0=topCellX*ts, tv0=topCellY*ts, tu1=tu0+ts, tv1=tv0+ts;
        float su0=sideCellX*ts, sv0=sideCellY*ts, su1=su0+ts, sv1=sv0+ts;
        float bu0=bottomCellX*ts, bv0=bottomCellY*ts, bu1=bu0+ts, bv1=bv0+ts;
        glPushMatrix();
        glTranslatef(x,y,z);
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, atlasTex);
        glBegin(GL_QUADS);
        // top
        glTexCoord2f(tu0,tv0); glVertex3f(0,1,0);
        glTexCoord2f(tu1,tv0); glVertex3f(1,1,0);
        glTexCoord2f(tu1,tv1); glVertex3f(1,1,1);
        glTexCoord2f(tu0,tv1); glVertex3f(0,1,1);
        // bottom
        glTexCoord2f(bu0,bv0); glVertex3f(0,0,0);
        glTexCoord2f(bu1,bv0); glVertex3f(1,0,0);
        glTexCoord2f(bu1,bv1); glVertex3f(1,0,1);
        glTexCoord2f(bu0,bv1); glVertex3f(0,0,1);
        // north (z=0)
        glTexCoord2f(su0,sv0); glVertex3f(0,0,0);
        glTexCoord2f(su1,sv0); glVertex3f(1,0,0);
        glTexCoord2f(su1,sv1); glVertex3f(1,1,0);
        glTexCoord2f(su0,sv1); glVertex3f(0,1,0);
        // south (z=1)
        glTexCoord2f(su0,sv0); glVertex3f(0,0,1);
        glTexCoord2f(su1,sv0); glVertex3f(1,0,1);
        glTexCoord2f(su1,sv1); glVertex3f(1,1,1);
        glTexCoord2f(su0,sv1); glVertex3f(0,1,1);
        // east (x=1)
        glTexCoord2f(su0,sv0); glVertex3f(1,0,0);
        glTexCoord2f(su1,sv0); glVertex3f(1,0,1);
        glTexCoord2f(su1,sv1); glVertex3f(1,1,1);
        glTexCoord2f(su0,sv1); glVertex3f(1,1,0);
        // west (x=0)
        glTexCoord2f(su0,sv0); glVertex3f(0,0,0);
        glTexCoord2f(su1,sv0); glVertex3f(0,0,1);
        glTexCoord2f(su1,sv1); glVertex3f(0,1,1);
        glTexCoord2f(su0,sv1); glVertex3f(0,1,0);
        glEnd();
        glDisable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    private double clickCooldown = 0;
    private void drawButton(String label, float x, float y, float w, float h, Runnable action) {
        float mx = (float)input.getMouseX(), my = (float)input.getMouseY();
        boolean hover = mx>=x && mx<=x+w && my>=y && my<=y+h;
        renderer.drawRect(x,y,w,h, hover?0.4f:0.25f, hover?0.4f:0.25f, 0.6f);
        renderer.drawText(label, x+10, y+8, 18, Color.WHITE);
        if (hover && input.isMouseButtonPressed(0) && clickCooldown <= 0) {
            action.run();
            clickCooldown = 0.3f;
        }
    }

    public void setStateFromOptions(){ state = optionsReturn; }
    private void drawCrosshair(){
        glMatrixMode(GL_PROJECTION); glLoadIdentity();
        glOrtho(0, window.getWidth(), window.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW); glLoadIdentity();
        glDisable(GL_DEPTH_TEST);
        glColor3f(1,1,1);
        float cx=window.getWidth()/2f, cy=window.getHeight()/2f;
        glBegin(GL_LINES);
        glVertex2f(cx-10,cy); glVertex2f(cx+10,cy);
        glVertex2f(cx,cy-10); glVertex2f(cx,cy+10);
        glEnd();
        glEnable(GL_DEPTH_TEST);
        float prog = cicle.progress();
        int hour = (int)(prog*24f);
        int min = (int)((prog*24f-hour)*60f);
        renderer.drawText(String.format("%02d:%02d", hour, min), 10, 10, 18, Color.WHITE);
    }
    public void postUpdate() { }
    public void cleanup() { world.cleanup(); }
}
