package motor.sky;

public class Skays {
    private final Cicle cicle;
    public Skays(Cicle c){this.cicle=c;}
    public float[] skyColor(){ return cicle.isDay()? new float[]{0.5f,0.7f,1f}: new float[]{0.05f,0.05f,0.15f}; }
}
