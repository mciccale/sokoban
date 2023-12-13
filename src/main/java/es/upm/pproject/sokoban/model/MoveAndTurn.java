package es.upm.pproject.sokoban.model;

public class MoveAndTurn {
    private final Character movement;
    private final int turn;
    public MoveAndTurn(Character movement, int turn) {
        this.movement = movement;
        this.turn = turn;
    }

    public Character getMovement() {
        return movement;
    }

    public int getTurn() {
        return turn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o.getClass() != this.getClass()) {
            return false;
        }
        MoveAndTurn p = (MoveAndTurn) o;

        return p.getMovement().equals(this.movement) && p.getTurn() == this.turn;
    }
    @Override
    public int hashCode() {
        return ("_" + this.movement + "_" + this.turn + "_").hashCode();
    }
}
