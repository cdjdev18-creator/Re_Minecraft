package motor.world;

public class Limite {
    public static final int MAX_Y=256, MIN_Y=-64;
    public static boolean valid(int y){ return y>=MIN_Y && y<=MAX_Y; }
}
