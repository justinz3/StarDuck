/*
 * Author: Justin Zhu
 *
 * Description: Classwork for Java Programming during 5-31-18, edited 6-18-18, and reused for StarDuck
 *
 * Created: 5-31-18
 */


package Physics;

public class Vector {

    private double x, y;


    // constructors -----------------------------------------------------------------------------------------------------

    public Vector()
    {
        x = y = 0;
    }

    public Vector(double x, double y)
    {
        this();
        setX(x);
        setY(y);
    }

    public Vector(double magnitude, Vector direction) {
        this(scalarMult(unitVector(direction.getAngle()), magnitude));
    }

    public Vector(Vector other)
    {
        this(other.getX(), other.getY());
    }


    // accessors -----------------------------------------------------------------------------------------------------

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public String toString()
    {
        return String.format("<%.2f, %.2f>", x, y);
    }

    /*public static Vector add(Vector a, Vector b)
    {
        return new Vector(a.getX() + b.getX(), a.getY() + b.getY());
    }*/

    public void add(Vector other) {
        this.setX(getX() + other.getX());
        this.setY(getY() + other.getY());
    }

    public double getMagnitude()
    {
        return Math.hypot(x, y);
    }

    public boolean isMoving()
    {
        return !(x == 0 && y == 0);
    }

    public double abs() {
        return getMagnitude();
    }

    public void invertX() {
        x = -x;
    }

    public void invertY() {
        y = -y;
    }

    public static double dot(Vector a, Vector b)
    {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    public static Vector scalarMult(Vector a, double scalar)
    {
        return new Vector(a.getX() * scalar, a.getY() * scalar);
    }

    public static Vector add(Vector a, Vector b) {
        return new Vector(a.getX() + b.getX(), a.getY() + b.getY());
    }

    public double getAngle() {
        // returns angle in radians
        return Math.atan2(y, x);
    }

    public static Vector unitVector(double radians) {
        return new Vector(Math.cos(radians), Math.sin(radians));
    }


    // mutators -----------------------------------------------------------------------------------------------------

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public void multiplyScalar(double scalar)
    {
        x *= scalar;
        y *= scalar;
    }

}
