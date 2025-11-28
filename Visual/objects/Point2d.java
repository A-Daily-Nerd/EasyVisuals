package Visual.objects;

public class Point2d {
    public double x, y;

    public Point2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Point2d)) return false;
        Point2d p = (Point2d) o;
        return p.x == x && p.y == y;
    }

    @Override
    public int hashCode() {
        return (int) (x * 31 + y);
    }

    @Override
    public String toString() {
        return "(" + x + " ," + y + ")";
    }
}