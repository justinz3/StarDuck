package Physics;

import java.awt.*;

public class Line implements Drawable {

    private Vector start, end;

    public Line(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    public Vector getStart() {
        return start;
    }

    public void setStart(Vector start) {
        this.start = start;
    }

    public Vector getEnd() {
        return end;
    }

    public void setEnd(Vector end) {
        this.end = end;
    }

    public double getSlope() {
        return (start.getY() - end.getY()) / (start.getX() - end.getX());
    }

    public double getYIntercept() {
        return start.getY() - start.getX() * getSlope();
    }

    public void draw(Graphics g) {
        g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
    }

    public boolean inDomain(Vector point) {
        return point.getX() >= Math.min(start.getX(), end.getX()) &&
                point.getX() <= Math.max(start.getX(), end.getX());
    }

    public boolean inRange(Vector point) {
        return point.getY() >= Math.min(start.getY(), end.getY()) &&
                point.getY() <= Math.max(start.getY(), end.getY());
    }

    public boolean belowLine(Vector point) {
        return point.getX() * getSlope() + getYIntercept() > point.getY();
    }

    public boolean belowLineSegment(Vector point) {
        return inDomain(point) && belowLine(point);
    }

    public boolean onLine(Vector point) {
        double diff = point.getX() * getSlope() + getYIntercept() - point.getY();
        return Math.abs(diff) < 0.001;
    }

    public boolean onLineSegment(Vector point) {
        return inDomain(point) && onLine(point);
    }

    public boolean aboveLine(Vector point) {
        return point.getX() * getSlope() + getYIntercept() < point.getY();
    }

    public boolean aboveLineSegment(Vector point) {
        return inDomain(point) && aboveLine(point);
    }

}
