package motor.player;

public class Player {
    public float x,y,z, vx,vy,vz;
    public float yaw, pitch;
    public boolean onGround;
    public float sensitivity = 0.5f;
    public float width=0.6f, depth=0.6f, height=1.8f;
    public float walkSpeed=4f, runSpeed=8f;
    public void update(float dt){
        vy -= 24f*dt;
        y += vy*dt;
        if(y<=0){ y=0; vy=0; onGround=true; } else onGround=false;
        x+=vx*dt; z+=vz*dt; vx*=0.85f; vz*=0.85f;
    }
    public void move(float f,float s){
        float sin=(float)Math.sin(Math.toRadians(yaw)), cos=(float)Math.cos(Math.toRadians(yaw));
        vx += (sin*f + cos*s);
        vz += (cos*f - sin*s);
    }
    public void jump(){ if(onGround){vy=8;onGround=false;} }
}
