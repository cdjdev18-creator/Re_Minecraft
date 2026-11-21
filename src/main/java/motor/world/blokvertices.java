package motor.world;

public class blokvertices {
    public static class FaceUV { public int x, y; public FaceUV(int x,int y){this.x=x;this.y=y;} }
    public static class BlockUV { public FaceUV TOP,BOTTOM,NORTH,SOUTH,EAST,WEST;
        public BlockUV(FaceUV t,FaceUV b,FaceUV n,FaceUV s,FaceUV e,FaceUV w){TOP=t;BOTTOM=b;NORTH=n;SOUTH=s;EAST=e;WEST=w;}
    }
    public static final BlockUV GRASS = new BlockUV(new FaceUV(0,0),new FaceUV(0,2),new FaceUV(0,3),new FaceUV(0,3),new FaceUV(0,3),new FaceUV(0,3));
    public static final BlockUV STONE = new BlockUV(new FaceUV(0,1),new FaceUV(0,1),new FaceUV(0,1),new FaceUV(0,1),new FaceUV(0,1),new FaceUV(0,1));
    public static final BlockUV DIRT = new BlockUV(new FaceUV(0,2),new FaceUV(0,2),new FaceUV(0,2),new FaceUV(0,2),new FaceUV(0,2),new FaceUV(0,2));
    public static final BlockUV BEDROCK = new BlockUV(new FaceUV(1,1),new FaceUV(1,1),new FaceUV(1,1),new FaceUV(1,1),new FaceUV(1,1),new FaceUV(1,1));

    public static float[] uv(int cellX, int cellY, int atlasW, int atlasH, int cellSize) {
        float u0 = (cellX*cellSize)/(float)atlasW;
        float v0 = (cellY*cellSize)/(float)atlasH;
        float u1 = ((cellX+1)*cellSize)/(float)atlasW;
        float v1 = ((cellY+1)*cellSize)/(float)atlasH;
        return new float[]{u0,v0,u1,v1};
    }
}

