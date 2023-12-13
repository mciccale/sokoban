package es.upm.pproject.sokoban.model;

import es.upm.pproject.sokoban.interfaces.Square;

public class GoalPosition implements Square {

    private final Position position;
    
    public GoalPosition(Position position) {
        this.position = position;
    }

    public boolean move(char way, int turn) {
        return true;
    }

    public Position getPosition() {
        return position;
    }

}
