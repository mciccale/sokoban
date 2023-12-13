package es.upm.pproject.sokoban.model;

public class Position {
    private int x;
    private int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        Position p = (Position) o;
        
        return p.getX() == this.x && p.getY() == this.y;
    }
    @Override
    public int hashCode() {
        return ("_" + this.x + "_" + this.y + "_").hashCode();
    }
}
