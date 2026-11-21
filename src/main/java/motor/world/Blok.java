package motor.world;

public class Blok {
    public int id; public String name; public String type; public boolean solid;
    // texture indices in atlas: [top,bottom,north,south,east,west] or single
    public int texTop, texBottom, texSide;
    public Blok(int id,String name,String type,boolean solid,int t,int b,int s){this.id=id;this.name=name;this.type=type;this.solid=solid;this.texTop=t;this.texBottom=b;this.texSide=s;}
    public static final Blok AIR=new Blok(0,"Air","air",false,0,0,0);
    public static final Blok GRASS=new Blok(1,"Grass","solid",true,0,2,3);
    public static final Blok STONE=new Blok(2,"Stone","solid",true,1,1,1);
    public static final Blok DIRT=new Blok(3,"Dirt","solid",true,2,2,2);
    public static final Blok BEDROCK=new Blok(4,"Bedrock","solid",true,5,5,5);
}
