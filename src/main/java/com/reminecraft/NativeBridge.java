package com.reminecraft;

public class NativeBridge {
    static {
        try {
            System.load("C:\\Users\\seven\\Guithub\\Re_Minecraft\\rust\\target\\release\\reminecraft_native.dll");
        } catch (UnsatisfiedLinkError e) {
            System.loadLibrary("reminecraft_native");
        }
    }
    public static native int rustVersion();
    public static void init(){
        int v = rustVersion();
        System.out.println("[Re_Minecraft] Rust library loaded.");
        System.out.println("[Re_Minecraft] Rust version: " + v);
        System.out.println("[Re_Minecraft] Java-Rust communication successful.");
    }
}
