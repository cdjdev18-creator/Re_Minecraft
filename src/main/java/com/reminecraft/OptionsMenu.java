package com.reminecraft;

import java.awt.Color;

public class OptionsMenu {
    private final ConfigManager cfg;
    private int tab = 0;
    private final String[] tabs = {"GENERAL","RENDERIZADO","IDIOMA","PANTALLA","AUDIO","CONTROLES"};
    private String returnState;
    private boolean dragging=false;

    public OptionsMenu(ConfigManager cfg){ this.cfg = cfg; if(cfg.get("showFps").isEmpty()) cfg.set("showFps","false"); if(cfg.get("debug").isEmpty()) cfg.set("debug","false"); if(cfg.get("quality").isEmpty()) cfg.set("quality","Media"); if(cfg.get("language").isEmpty()) cfg.set("language","Español"); if(cfg.get("windowMode").isEmpty()) cfg.set("windowMode","Windowed"); if(cfg.get("musicVolume").isEmpty()) cfg.set("musicVolume","1.0"); if(cfg.get("effectsVolume").isEmpty()) cfg.set("effectsVolume","1.0"); }

    public void render(Game g, Renderer r, Window w, InputManager in){
        r.clear(0.12f,0.12f,0.12f);
        r.drawText("OPCIONES", w.getWidth()/2f-60, 20, 32, Color.WHITE);
        float x=40, y=70;
        for(int i=0;i<tabs.length;i++){
            float bx=x+i*150;
            boolean sel=i==tab;
            boolean hover = in.getMouseX()>=bx && in.getMouseX()<=bx+140 && in.getMouseY()>=y && in.getMouseY()<=y+30;
            r.drawRect(bx,y,140,30, hover?0.35f:(sel?0.5f:0.2f), hover?0.35f:(sel?0.5f:0.2f), 0.7f);
            r.drawText(tabs[i], bx+10, y+6, 14, Color.WHITE);
            if(hover && in.isMouseButtonClicked(0)){ tab=i; }
        }
        float cy=120;
        switch(tab){
            case 0:
                toggle(r,x,cy,"Auto-guardado","autoSave","true",in);
                toggle(r,x,cy+40,"Mostrar FPS","showFps","true",in);
                toggle(r,x,cy+80,"Debug info","debug","true",in);
                break;
            case 1:
                slider(r,x,cy,"Distancia (chunks)","renderDistance",2,16,in);
                dropdown(r,x,cy+40,"Calidad","quality",new String[]{"Baja","Media","Alta"},in);
                toggle(r,x,cy+80,"VSync","vsync","true",in);
                break;
            case 2:
                dropdown(r,x,cy,"Idioma","language",new String[]{"Español","English"},in);
                break;
            case 3:
                dropdown(r,x,cy,"Modo","windowMode",new String[]{"Windowed","Fullscreen"},in);
                r.drawText("Resolucion: "+cfg.get("resolution"), x, cy+40, 18, Color.WHITE);
                break;
            case 4:
                slider(r,x,cy,"Volumen","masterVolume",0,1,in);
                slider(r,x,cy+40,"Musica","musicVolume",0,1,in);
                slider(r,x,cy+80,"Efectos","effectsVolume",0,1,in);
                break;
            case 5:
                r.drawText("WASD mover, SPACE saltar, SHIFT correr, ESC pausa", x, cy, 18, Color.WHITE);
                break;
        }
        float by=w.getHeight()-60;
        button(r,x,by,120,40,"APLICAR",in,()->{ cfg.save(); });
        button(r,x+140,by,140,40,"RESTABLECER",in,()->{ cfg.set("showFps","false"); cfg.set("debug","false"); cfg.set("quality","Media"); cfg.set("language","Español"); cfg.set("windowMode","Windowed"); cfg.set("masterVolume","1.0"); cfg.set("musicVolume","1.0"); cfg.set("effectsVolume","1.0"); cfg.set("renderDistance","8"); cfg.set("vsync","true"); cfg.save(); });
        button(r,x+300,by,120,40,"VOLVER",in,()->{ g.setStateFromOptions(); });
    }

    private void toggle(Renderer r,float x,float y,String label,String key,String onVal,InputManager in){
        boolean on = cfg.get(key).equalsIgnoreCase(onVal);
        r.drawText(label+": "+(on?"ON":"OFF"), x, y, 18, Color.WHITE);
        boolean hover = in.getMouseX()>=x+260 && in.getMouseX()<=x+320 && in.getMouseY()>=y && in.getMouseY()<=y+24;
        r.drawRect(x+260,y,60,24, hover?0.3f:(on?0.2f:0.4f), on?0.6f:0.2f, 0.2f);
        r.drawText(on?"ON":"OFF", x+275, y+3, 14, Color.WHITE);
        if(hover && in.isMouseButtonClicked(0)){ cfg.set(key, on?"false":"true"); }
    }

    private void dropdown(Renderer r,float x,float y,String label,String key,String[] opts,InputManager in){
        String cur=cfg.get(key);
        r.drawText(label+": "+cur, x, y, 18, Color.WHITE);
        boolean hover = in.getMouseX()>=x+260 && in.getMouseX()<=x+380 && in.getMouseY()>=y && in.getMouseY()<=y+24;
        r.drawRect(x+260,y,120,24, hover?0.35f:0.25f, hover?0.35f:0.25f, 0.6f);
        r.drawText(cur, x+265, y+3, 14, Color.WHITE);
        if(hover && in.isMouseButtonClicked(0)){
            int idx=0; for(int i=0;i<opts.length;i++) if(opts[i].equals(cur)) idx=i;
            cfg.set(key, opts[(idx+1)%opts.length]);
        }
    }

    private void slider(Renderer r,float x,float y,String label,String key,float min,float max,InputManager in){
        float val=cfg.getFloat(key);
        r.drawText(label+": "+String.format("%.2f",val), x, y, 18, Color.WHITE);
        boolean trackHover = in.getMouseX()>=x+260 && in.getMouseX()<=x+410 && in.getMouseY()>=y-4 && in.getMouseY()<=y+20;
        r.drawRect(x+260,y,150,12,0.2f,0.2f,0.2f);
        float pos=x+260+(val-min)/(max-min)*150;
        r.drawRect(pos-5,y-4,10,20,0.6f,0.6f,0.8f);
        if(in.isMouseButtonPressed(0) && (dragging || trackHover)){
            dragging=true;
            if(in.getMouseX()>=x+260 && in.getMouseX()<=x+410){
                float nv=min+(float)((in.getMouseX()-(x+260))/150.0)*(max-min);
                nv=Math.max(min,Math.min(max,nv));
                cfg.set(key, String.valueOf(nv));
            }
        } else {
            dragging=false;
        }
    }

    private void button(Renderer r,float x,float y,float w,float h,String label,InputManager in,Runnable action){
        boolean hover = in.getMouseX()>=x && in.getMouseX()<=x+w && in.getMouseY()>=y && in.getMouseY()<=y+h;
        r.drawRect(x,y,w,h, hover?0.35f:0.25f, hover?0.35f:0.25f, hover?0.7f:0.6f);
        r.drawText(label, x+10, y+10, 16, Color.WHITE);
        if(hover && in.isMouseButtonClicked(0)){ action.run(); }
    }

    public void setReturn(String s){ returnState=s; }
    public String getReturn(){ return returnState; }
}
