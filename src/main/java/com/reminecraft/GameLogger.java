package com.reminecraft;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameLogger {
    private static final String DIR = "exe/Re_Minecraft";
    private static final String LOG = DIR + "/log.txt";
    private static final StringBuilder buf = new StringBuilder();

    public static void init(){ new File(DIR).mkdirs(); new File(DIR+"/CrasssError").mkdirs(); log("INFO","Game started"); }
    public static void log(String level, String msg){
        String line = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " ["+level+"] " + msg;
        buf.append(line).append("\n");
        try { Files.createDirectories(Paths.get(DIR)); Files.writeString(Paths.get(LOG), line+"\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND); } catch(Exception ignored){}
    }
    public static String getLogs(){ return buf.toString(); }
    public static void crash(Throwable t){
        try {
            new File(DIR+"/CrasssError").mkdirs();
            int n = new File(DIR+"/CrasssError").listFiles().length+1;
            String name = String.format("crash_%03d.txt", n);
            Files.writeString(Paths.get(DIR+"/CrasssError/"+name), LocalDateTime.now()+"\n"+t+"\n");
        } catch(Exception ignored){}
        log("ERROR", t.toString());
    }
}
