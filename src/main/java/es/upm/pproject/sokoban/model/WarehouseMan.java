package es.upm.pproject.sokoban.model;

import es.upm.pproject.sokoban.interfaces.Square;
import es.upm.pproject.sokoban.interfaces.Unmove;

import java.security.InvalidParameterException;
import java.util.Map;

public class WarehouseMan implements Square, Unmove {
    private Position position;
    private final Map<Position, Square> board;
    private GoalPosition goalPosition = null;

    public WarehouseMan(Position position, Map<Position, Square> board) {
        this.position = position;
        this.board = board;
    }

    private boolean updateMap(Position newPosition) {
        if (goalPosition != null) {
            board.put(position, goalPosition);
            goalPosition = null;
        } else {
            board.remove(position);
        }
        this.position = newPosition;
        board.put(position, this);
        return true;
    }

    private boolean checkPosition(Position newPosition, char way, int turn) {
        Square square;
        if (newPosition == null) return false;

        if ((square = this.board.get(newPosition)) != null) {
            if (!square.move(way, turn)) {
                return false;
            }
            square = this.board.get(newPosition);
            if (square instanceof GoalPosition) {
                if (goalPosition != null) {
                    board.put(position, goalPosition);
                } else {
                    board.remove(position);
                }
                goalPosition = (GoalPosition) square;
                this.position = newPosition;
                board.put(position, this);
                return true;
            }
        }
        return updateMap(newPosition);
    }

    public boolean move(char way, int turn) {
        Position newPosition;
        switch (way) {
            case 'N':
                newPosition = new Position(position.getX(), position.getY() - 1);
                break;
            case 'S':
                newPosition = new Position(position.getX(), position.getY() + 1);
                break;
            case 'E':
                newPosition = new Position(position.getX() + 1, position.getY());
                break;
            case 'W':
                newPosition = new Position(position.getX() - 1, position.getY());
                break;
            default:
                throw new InvalidParameterException("The warehouse man just can move N (north), S (south), E (east), W (west)");
        }
        return checkPosition(newPosition, way, turn);
    }

    public void unmove(char way, int turn) {
        Position oldPosition;
        Position nextPosition;
        Square square;
        switch (way) {
            case 'N':
                oldPosition = new Position(position.getX(), position.getY() + 1);
                nextPosition = new Position(position.getX(), position.getY() - 1);
                break;
            case 'S':
                oldPosition = new Position(position.getX(), position.getY() - 1);
                nextPosition = new Position(position.getX(), position.getY() + 1);
                break;
            case 'E':
                oldPosition = new Position(position.getX() - 1, position.getY());
                nextPosition = new Position(position.getX() + 1, position.getY());
                break;
            case 'W':
                oldPosition = new Position(position.getX() + 1, position.getY());
                nextPosition = new Position(position.getX() - 1, position.getY());
                break;
            default:
                throw new InvalidParameterException("Unable to undo movement");
        }
        if (goalPosition != null) {
            board.put(position, goalPosition);
            goalPosition = null;
        } else {
            board.remove(position);
        }
        square = this.board.get(oldPosition);
        if (square instanceof GoalPosition) {
            goalPosition = (GoalPosition) square;
            board.remove(oldPosition);
        }
        this.position = oldPosition;
        board.put(position, this);
        square = this.board.get(nextPosition);
        if (square instanceof Box) {
            ((Box) square).unmove(way, turn);
        }
    }

    public GoalPosition getGoalPosition() {
        return goalPosition;
    }

    public Position getPosition() {
        return position;
    }
}
