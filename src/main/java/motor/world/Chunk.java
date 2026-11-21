package motor.world;

public class Chunk {
    public final int cx,cz;
    public static final int SIZE=16, HEIGHT=64;
    private final Blok[][][] blocks=new Blok[SIZE][HEIGHT][SIZE];
    public Chunk(int cx,int cz){this.cx=cx;this.cz=cz; for(int x=0;x<SIZE;x++)for(int y=0;y<HEIGHT;y++)for(int z=0;z<SIZE;z++) blocks[x][y][z]=Blok.AIR;}
    public Blok get(int x,int y,int z){ if(x<0||x>=SIZE||z<0||z>=SIZE||y<0||y>=HEIGHT) return null; return blocks[x][y][z]; }
    public void set(int x,int y,int z,Blok b){ if(x>=0&&x<SIZE&&z>=0&&z<SIZE&&y>=0&&y<HEIGHT) blocks[x][y][z]=b; }
}
