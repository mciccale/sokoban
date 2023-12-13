package es.upm.pproject.sokoban.model;

import es.upm.pproject.sokoban.controller.GameController;
import es.upm.pproject.sokoban.interfaces.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameModelTest {
    private Map<Position, Square> board;

    private GameController gameController;

    @BeforeEach
    void initMap() {
        gameController = new GameController();
        board = gameController.parse(2);
    }
    @Nested
    class WarehouseManMoves {
        private WarehouseMan warehouseMan;
        private Wall wall;
        private GoalPosition goalPosition;
        @BeforeEach
        void initWarehouseManAndWall() {
            warehouseMan = (WarehouseMan) board.get(new Position(2, 4));
            wall = (Wall) board.get(new Position(1, 4));
            goalPosition =  (GoalPosition) board.get(new Position(5,2));
        }

        @Test
        void WarehouseManMoveToEmptyPosition() {
            Position PreviousPosition = warehouseMan.getPosition();
            assertTrue(warehouseMan.move('N',7));
            assertNotNull(board.get(new Position(2, 3)));
            WarehouseMan warehouseManMoved = (WarehouseMan) board.get(new Position(2, 3));
            Position NewPosition = warehouseManMoved.getPosition();
            assertEquals(PreviousPosition.getX(), NewPosition.getX());
            assertNotEquals(PreviousPosition.getY(), NewPosition.getY());
        }

        @Test
        void WarehouseManMoveToWall() {
            Position PreviousPositionWarehouseMan = warehouseMan.getPosition();
            Position PreviousPositionWall = wall.getPosition();
            assertFalse(warehouseMan.move('W',2));
            assertNotNull(board.get(new Position(2, 4)));
            assertNotNull(board.get(new Position(1, 4)));
            WarehouseMan warehouseManNotMoved = (WarehouseMan) board.get(new Position(2, 4));
            Wall wallNotMoved = (Wall) board.get(new Position(1, 4));
            Position NewPositionWarehouseMan = warehouseManNotMoved.getPosition();
            assertEquals(PreviousPositionWarehouseMan.getX(), NewPositionWarehouseMan.getX());
            assertEquals(PreviousPositionWarehouseMan.getY(), NewPositionWarehouseMan.getY());
            Position NewPositionWall = wallNotMoved.getPosition();
            assertEquals(PreviousPositionWall.getX(), NewPositionWall.getX());
            assertEquals(PreviousPositionWall.getY(), NewPositionWall.getY());
        }
        @Test
        void WarehouseManToGoalPosition(){
            Position PreviousPositionWarehouseMan = warehouseMan.getPosition();
            Position PreviousPositionGoal = goalPosition.getPosition();
            assertTrue(warehouseMan.move('N',0));
            assertTrue(warehouseMan.move('E',1));
            assertTrue(warehouseMan.move('E',2));
            assertTrue(warehouseMan.move('E',3));
            assertTrue(warehouseMan.move('S',4));
            assertTrue(warehouseMan.move('N',5));
        }
        @Test
        void WarehouseManUnMove(){
            Position PreviousPositionWarehouseMan = warehouseMan.getPosition();
            gameController.moveUp(0);
            gameController.undoMovement(0);
            assertEquals(PreviousPositionWarehouseMan, warehouseMan.getPosition());
        }
        @Test
        void nullAndInvalidPositionCheck(){
            assertThrows(InvalidParameterException.class, () ->{
               warehouseMan.move('j',3);
            });
        }
    }
    @Nested
    class BoxTests {
        private WarehouseMan warehouseMan;
        private Box upBox;
        private Box rightBox;

        @BeforeEach
        void initWarehouseManAndWall() {
            warehouseMan = (WarehouseMan) board.get(new Position(2, 4));
            upBox = (Box) board.get(new Position(2, 3));
            rightBox = (Box) board.get(new Position(3, 4));
        }

        @Test
        void BoxCanMoveToEmptySpace() {
            Position PreviousManPos = warehouseMan.getPosition();
            Position PreviousBoxPos = upBox.getPosition();
            assertTrue(warehouseMan.move('N',0));
            assertNotNull(board.get(new Position(2, 3)));
            assertNotNull(board.get(new Position(2, 2)));
            WarehouseMan warehouseManMoved = (WarehouseMan) board.get(new Position(2, 3));
            Box upBoxMoved = (Box) board.get(new Position(2, 2));
            Position NewManPos = warehouseManMoved.getPosition();
            Position NewBoxPos = upBoxMoved.getPosition();
            assertEquals(NewManPos.getX(), PreviousManPos.getX());
            assertEquals(NewManPos.getY(), PreviousManPos.getY() - 1);
            assertEquals(NewBoxPos.getX(), PreviousBoxPos.getX());
            assertEquals(NewBoxPos.getY(), PreviousBoxPos.getY() - 1);
            /* El mismo test
            assertTrue(warehouseMan.move('N',0));
            assertEquals(new Position(2,2), upBox.getPosition());
             */
        }
        @Test
        void BoxAgainstWallDontMove() {
            assertFalse(warehouseMan.move('E',0));
        }
        @Test
        void BoxAgainstBoxDontMove() {
            assertTrue(warehouseMan.move('N',0));
            assertTrue(warehouseMan.move('W',1));
            assertTrue(warehouseMan.move('N',2));
            assertTrue(warehouseMan.move('E',3));
            assertTrue(warehouseMan.move('N',4));
            assertTrue(warehouseMan.move('E',5));
            assertTrue(warehouseMan.move('S',6));
            assertFalse(warehouseMan.move('S',7));
            assertEquals(new Position(rightBox.getPosition().getX(), rightBox.getPosition().getY()-2), warehouseMan.getPosition());
            assertEquals(new Position(rightBox.getPosition().getX(), rightBox.getPosition().getY()-1), upBox.getPosition());
            assertNotNull(board.get(rightBox.getPosition()));
        }
        @Test
        void BoxGetsGoal() {
            assertTrue(warehouseMan.move('N',0));
            assertTrue(warehouseMan.move('W',1));
            assertTrue(warehouseMan.move('N',2));
            assertTrue(warehouseMan.move('E',3));
            assertTrue(warehouseMan.move('E',4));
            assertTrue(warehouseMan.move('E',5));
            assertNotNull(upBox.getGoalPosition());
        }

        @Test
        void BoxMovesInAllDirection() {
            assertTrue(warehouseMan.move('N',0));
            assertTrue(warehouseMan.move('W',1));
            assertTrue(warehouseMan.move('N',2));
            assertTrue(warehouseMan.move('E',3));
            assertTrue(warehouseMan.move('N',4));
            assertTrue(warehouseMan.move('E',5));
            assertTrue(warehouseMan.move('E',6));
            assertTrue(warehouseMan.move('S',7));
            assertTrue(warehouseMan.move('W',8));
            assertTrue(warehouseMan.move('N',9));
            assertTrue(warehouseMan.move('W',10));
            assertTrue(warehouseMan.move('S',11));
        }
        @Test
        void BoxUnMovesInAllDirection() {
            gameController.moveUp(0);
            gameController.moveLeft(1);
            gameController.moveUp(2);
            gameController.moveRight(3);
            gameController.moveUp(4);
            gameController.moveRight(5);
            gameController.moveRight(6);
            gameController.moveDown(7);
            gameController.moveLeft(8);
            gameController.moveUp(9);
            gameController.moveLeft(10);
            gameController.moveDown(11);
            assertTrue(gameController.undoMovement(11));
            assertTrue(gameController.undoMovement(10));
            assertTrue(gameController.undoMovement(9));
            assertTrue(gameController.undoMovement(8));
            assertTrue(gameController.undoMovement(7));
            assertTrue(gameController.undoMovement(6));
            assertTrue(gameController.undoMovement(5));
            assertTrue(gameController.undoMovement(4));
            assertTrue(gameController.undoMovement(3));
            assertTrue(gameController.undoMovement(2));
            assertTrue(gameController.undoMovement(1));
            assertTrue(gameController.undoMovement(0));
        }

    }

    @Nested
    class gameMovementCounterTest{
        private GameMovementCounter gameMovementCounter;

        @BeforeEach
        void initMovementCounter() {
            gameMovementCounter = new GameMovementCounter();
        }
        @Test
        void movementStart(){
            assertEquals(0, gameMovementCounter.getMovementCount());
        }
        @Test
        void movementCounterIncrease(){
            assertEquals(0, gameMovementCounter.getMovementCount());
            gameMovementCounter.incrementMovementCount();
            gameMovementCounter.incrementMovementCount();
            assertEquals(2, gameMovementCounter.getMovementCount());
        }
        @Test
        void movementCounterDecrease(){
            assertEquals(0, gameMovementCounter.getMovementCount());
            gameMovementCounter.incrementMovementCount();
            gameMovementCounter.incrementMovementCount();
            gameMovementCounter.decrementMovementCount();
            assertEquals(1, gameMovementCounter.getMovementCount());
        }
        @Test
        void resetmovementCounter(){
            assertEquals(0, gameMovementCounter.getMovementCount());
            gameMovementCounter.incrementMovementCount();
            gameMovementCounter.incrementMovementCount();
            gameMovementCounter.resetMovementCount();
            assertEquals(0, gameMovementCounter.getMovementCount());
        }

        @Test
        void setmovementCounter(){
            assertEquals(0, gameMovementCounter.getMovementCount());
            gameMovementCounter.incrementMovementCount();
            gameMovementCounter.incrementMovementCount();
            gameMovementCounter.resetMovementCount();
            gameMovementCounter.setMovementCount(10);
            assertEquals(10, gameMovementCounter.getMovementCount());
        }
    }

    @Nested
    class levelCounterTest{
        private LevelMovementCounter levelMovementCounter;

        @BeforeEach
        void initLevelMovementCounter() {
            levelMovementCounter = new LevelMovementCounter();
        }
        @Test
        void levelMovementStart(){
            assertEquals(0, levelMovementCounter.getMovementCount());
        }
        @Test
        void levelMovementCounterIncrease(){
            assertEquals(0, levelMovementCounter.getMovementCount());
            levelMovementCounter.incrementMovementCount();
            levelMovementCounter.incrementMovementCount();
            assertEquals(2, levelMovementCounter.getMovementCount());
        }
        @Test
        void levelMovementCounterDecrease(){
            assertEquals(0, levelMovementCounter.getMovementCount());
            levelMovementCounter.incrementMovementCount();
            levelMovementCounter.incrementMovementCount();
            levelMovementCounter.decrementMovementCount();
            assertEquals(1, levelMovementCounter.getMovementCount());
        }
        @Test
        void resetLevelMovementCounter(){
            assertEquals(0, levelMovementCounter.getMovementCount());
            levelMovementCounter.incrementMovementCount();
            levelMovementCounter.incrementMovementCount();
            levelMovementCounter.resetMovementCount();
            assertEquals(0, levelMovementCounter.getMovementCount());
        }

        @Test
        void setLevelMovementCounter(){
            assertEquals(0, levelMovementCounter.getMovementCount());
            levelMovementCounter.incrementMovementCount();
            levelMovementCounter.incrementMovementCount();
            levelMovementCounter.resetMovementCount();
            levelMovementCounter.setMovementCount(10);
            assertEquals(10, levelMovementCounter.getMovementCount());
        }
    }

    @Nested
    class moveAndTurnTest{
        private WarehouseMan warehouseMan;
        private Box boxUp;

        private Deque<MoveAndTurn> boxMovements;

        @BeforeEach
        void initWarehouseManAndWall() {
            warehouseMan = (WarehouseMan) board.get(new Position(2, 4));
            boxUp = (Box) board.get(new Position(2,3));
            boxMovements = boxUp.getMovements();
        }
        @Test
        void moveAndTurnGetters(){
            gameController.moveUp(0);
            assertEquals('N', boxMovements.peek().getMovement());
            assertEquals(0, boxMovements.peek().getTurn());
            assertEquals(new MoveAndTurn('N', 0), boxMovements.peek());
        }
    }

}
