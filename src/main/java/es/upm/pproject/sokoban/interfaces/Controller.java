package es.upm.pproject.sokoban.interfaces;

import es.upm.pproject.sokoban.model.Position;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Deque;
import java.util.Map;

public interface Controller {
    Map<Position, Square> getBoard();

    int getRows();

    int getCols();

    String getLevelName();

    int getActualLevel();

    Deque<Character> getMovements();

    Map<Position, Square> parse(int levelNumber);

    boolean moveUp(int turn);

    boolean moveDown(int turn);

    boolean moveLeft(int turn);

    boolean moveRight(int turn);

    void startNewGame();

    boolean restartLevel(boolean finished);

    boolean undoMovement(int turn);

    void doMovements(int turn);

    boolean nextLevel();

    void saveGame(File saveFile, int gameMovementCounter);

    int openSavedGame(File savedGame) throws FileNotFoundException;

    void exitGame();
}