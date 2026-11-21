package motor.sky;

public class Cicle {
    private float time=0; private float dayLength=120f;
    public void update(float dt){ time=(time+dt)%dayLength; }
    public float progress(){ return time/dayLength; }
    public boolean isDay(){ float p=progress(); return p>0.2 && p<0.8; }
}
