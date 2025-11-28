package Visual.objects;

public class Point3d {
    private double x;
    private double y;
    private double z;

    public Point3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Point3d(int x, int y, int z) {
        this.x = (double) x;
        this.y = (double) y;
        this.z = (double) z;
    }

    public double getX() {return x;}
    public double getY() {return y;}
    public double getZ() {return z;}

    public void setX(double newX) {x = newX;}
    public void setY(double newY) {y = newY;}
    public void setZ(double newZ) {z = newZ;}

    @Override
    public String toString() {
        return "(" + x + " ," + y + " ," + z +")";
    }
}
