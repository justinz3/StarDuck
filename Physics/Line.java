package Physics;

public class Line {

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

    public Line(Vector start, Vector end) {
        this.start = start;
        this.end = end;
    }

    private Vector start, end;


}
