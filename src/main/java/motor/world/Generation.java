package motor.world;

public class Generation {
    private final long seed; private final Perling perlin;
    public Generation(long seed){this.seed=seed;this.perlin=new Perling(seed);}
    public Chunk generate(int cx,int cz){
        Chunk c=new Chunk(cx,cz);
        for(int x=0;x<Chunk.SIZE;x++) for(int z=0;z<Chunk.SIZE;z++){
            double h=perlin.noise((cx*Chunk.SIZE+x)*0.05,(cz*Chunk.SIZE+z)*0.05);
            int height= (int)((h+1)*16)+4;
            for(int y=0;y<=height && y<Chunk.HEIGHT;y++){
                c.set(x,y,z, y==height?Blok.GRASS:Blok.STONE);
            }
        }
        return c;
    }
}
