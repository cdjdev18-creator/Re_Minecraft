package motor.world;

public class Perling {
    private final long seed;
    public Perling(long seed){this.seed=seed;}
    public double noise(double x,double y){
        int xi=(int)Math.floor(x), yi=(int)Math.floor(y);
        double xf=x-xi, yf=y-yi;
        double a=hash(xi,yi), b=hash(xi+1,yi), c=hash(xi,yi+1), d=hash(xi+1,yi+1);
        double u=xf*xf*(3-2*xf), v=yf*yf*(3-2*yf);
        return a*(1-u)*(1-v)+b*u*(1-v)+c*(1-u)*v+d*u*v;
    }
    private double hash(int x,int y){ long n=seed+x*374761393L+y*668265263L; n=(n^(n>>13))*1274126177L; return ((n^(n>>16))&0x7fffffff)/ (double)0x7fffffff; }
}
