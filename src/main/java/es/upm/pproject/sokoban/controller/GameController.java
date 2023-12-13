package es.upm.pproject.sokoban.controller;

import java.io.*;
import java.util.*;

import es.upm.pproject.sokoban.interfaces.Controller;
import es.upm.pproject.sokoban.interfaces.Square;
import es.upm.pproject.sokoban.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);
    private static final int MAX_LEVELS = 4;
    private final HashMap<Position, Square> board = new HashMap<>();
    private final ArrayDeque<Character> movements = new ArrayDeque<>();
    private int rows;
    private int cols;
    private int actualLevel;
    private String levelName;
    private WarehouseMan warehouseMan;

    public GameController() {
        this.actualLevel = 1;
    }

    public Map<Position, Square> getBoard() {
        return board;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getActualLevel() {
        return actualLevel;
    }

    public Deque<Character> getMovements() {
        return movements;
    }

    public Map<Position, Square> parse(int levelNumber) {
        logger.info("Parsing level...");
        int nBoxes = 0;
        int nGoalPositions = 0;
        int nWarehouseMan = 0;
        String path = "./levels/level" + levelNumber + ".txt";
        File fd = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(fd))) {
            // Getting the level name
            this.levelName = br.readLine();

            // Getting the dimensions of the board
            String[] dimensions = br.readLine().split(" ");
            this.rows = Integer.parseInt(dimensions[0]);
            this.cols = Integer.parseInt(dimensions[1]);

            // Getting the board itself
            String line;
            Position position;

            for (int i = 0; i < rows; ++i) {
                line = br.readLine();
                for (int j = 0; j < line.length(); ++j) {
                    switch (line.charAt(j)) {
                        case ' ':
                            // Don't add to the map
                            break;
                        case '+':
                            position = new Position(j, i);
                            this.board.put(position, new Wall(position));
                            break;
                        case '*':
                            position = new Position(j, i);
                            this.board.put(position, new Box(position, this.board));
                            nBoxes++;
                            break;
                        case 'W':
                            position = new Position(j, i);
                            this.warehouseMan = new WarehouseMan(position, this.board);
                            this.board.put(position, warehouseMan);
                            nWarehouseMan++;
                            break;
                        case '#':
                            position = new Position(j, i);
                            this.board.put(position, new GoalPosition(position));
                            nGoalPositions++;
                            break;
                        default:
                            break;
                    }
                }
            }
            if (levelOk(nBoxes, nGoalPositions, nWarehouseMan))
                return this.board;
            else
                return new HashMap<>();
        } catch (IOException e) {
            return new HashMap<>();
        }
    }

    // Moving character through board
    public boolean moveUp(int turn) {
        if (this.warehouseMan.move('N', turn)) {
            movements.push('N');
            return true;
        }
        return false;
    }

    public boolean moveDown(int turn) {
        if (this.warehouseMan.move('S', turn)) {
            movements.push('S');
            return true;
        }
        return false;
    }

    public boolean moveLeft(int turn) {
        if (this.warehouseMan.move('W', turn)) {
            movements.push('W');
            return true;
        }
        return false;
    }

    public boolean moveRight(int turn) {
        if (this.warehouseMan.move('E', turn)) {
            movements.push('E');
            return true;
        }
        return false;
    }

    public void startNewGame() {
        logger.info("Starting game...");
        this.board.clear();
        this.movements.clear();
        this.actualLevel = 1;
        parse(this.actualLevel);
    }

    public boolean restartLevel(boolean finished) {
        if (!finished) {
            logger.info("Restarting level...");
            this.board.clear();
            this.movements.clear();
            parse(this.actualLevel);
            return true;
        }
        return false;
    }

    public boolean undoMovement(int turn) {
        logger.info("Undoing movement...");
        if (movements.isEmpty()) return false;
        Character movement = movements.pop();
        this.warehouseMan.unmove(movement, turn);
        return true;
    }

    public void doMovements(int turn) {
        Iterator<Character> it = movements.descendingIterator();
        while (it.hasNext()) {
            this.warehouseMan.move(it.next(), turn);
            turn++;
        }
    }

    public boolean nextLevel() {
        logger.info("Next Level...");
        this.board.clear();
        movements.clear();
        if (this.actualLevel == MAX_LEVELS) {
            return false;
        } else {
            this.actualLevel++;
            parse(this.actualLevel);
            return true;
        }
    }

    public void saveGame(File saveFile, int gameMovementCounter) {
        logger.info("Saving game...");
        /*
         * Format of the file
         * 1- Number of level being played
         * 2- The current game punctuation
         * 3- All the movements made from the beginning
         * */
        try (FileWriter fileWriter = new FileWriter(saveFile)){
            fileWriter.write(String.valueOf(actualLevel));
            fileWriter.write('\n');
            fileWriter.write(String.valueOf(gameMovementCounter));
            fileWriter.write('\n');
            if (movements.isEmpty()) {
                fileWriter.write('-');
            }
            Iterator<Character> it = movements.descendingIterator();
            while (it.hasNext()) {
                fileWriter.write(it.next());
            }
        } catch (IOException e) {
            logger.error("Failed not found: {}", e.getMessage());
        }
    }

    public int openSavedGame(File savedGame) throws FileNotFoundException {
        logger.info("Opening saved game...");
        int gamePunctuation;
        movements.clear();
        try (Scanner scanner = new Scanner(savedGame)) {
            if (scanner.hasNextInt())
                actualLevel = scanner.nextInt();
            else
                return -1;

            if (scanner.hasNextInt())
                gamePunctuation = scanner.nextInt();
            else
                return -1;

            if (scanner.hasNext()) {
                String movementsStr = scanner.next();
                for (char c : movementsStr.toCharArray())
                    movements.push(c);
            }
            else
                return -1;
        }
        this.board.clear();
        return gamePunctuation;
    }

    public void exitGame() {
        System.exit(1);
    }

    private boolean levelOk(int nBoxes, int nGoalPositions, int nWarehouseMan) {
        return nBoxes == nGoalPositions && nBoxes != 0 && nWarehouseMan == 1;
    }
}
