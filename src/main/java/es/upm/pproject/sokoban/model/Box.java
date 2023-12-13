package es.upm.pproject.sokoban.model;

import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import es.upm.pproject.sokoban.interfaces.Square;
import es.upm.pproject.sokoban.interfaces.Unmove;

public class Box implements Square, Unmove {

    private Position position;
    private Deque<MoveAndTurn> movements;
    private Map<Position, Square> board;
    private GoalPosition goalPosition = null;

    public Box(Position position, Map<Position, Square> board) {
        this.position = position;
        this.board = board;
        movements = new ArrayDeque<>();
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

    private boolean checkPosition(Position newPosition) {
        Square square;
        if (newPosition == null) return false;
        if ((square = this.board.get(newPosition)) != null) {
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
            } else {
                return false;
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
        if (checkPosition(newPosition)) {
            movements.push(new MoveAndTurn(way, turn));
            return true;
        }
        return false;
    }

    public void unmove(char way, int turn) {
        Position oldPosition;
        if (!movements.isEmpty() && movements.peek().equals(new MoveAndTurn(way, turn))) {
            movements.pop();
            switch (way) {
                case 'N':
                    oldPosition = new Position(position.getX(), position.getY() + 1);
                    break;
                case 'S':
                    oldPosition = new Position(position.getX(), position.getY() - 1);
                    break;
                case 'E':
                    oldPosition = new Position(position.getX() - 1, position.getY());
                    break;
                case 'W':
                    oldPosition = new Position(position.getX() + 1, position.getY());
                    break;
                default:
                    throw new InvalidParameterException("Unable to undo movement");
            }
            checkPosition(oldPosition);
        }
    }

    public Deque<MoveAndTurn> getMovements() {
        return movements;
    }

    public Position getPosition() {
        return position;
    }

    public GoalPosition getGoalPosition() {
        return goalPosition;
    }

    public Map<Position, Square> getBoard() {
        return board;
    }
}
