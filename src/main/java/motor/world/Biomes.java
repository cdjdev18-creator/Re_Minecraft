package motor.world;

public class Biomes {
    public enum Biome { PLAINS, FOREST, DESERT, MOUNTAINS, SNOW }
    public static Biome get(double t,double h,double alt){
        if(alt>0.7) return Biome.MOUNTAINS;
        if(t<0.2) return Biome.SNOW;
        if(t>0.7 && h<0.3) return Biome.DESERT;
        if(h>0.6) return Biome.FOREST;
        return Biome.PLAINS;
    }
}
