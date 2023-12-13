package es.upm.pproject.sokoban.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.Clip;
import javax.swing.*;

import es.upm.pproject.sokoban.interfaces.Controller;
import es.upm.pproject.sokoban.interfaces.Frame;
import es.upm.pproject.sokoban.interfaces.Square;
import es.upm.pproject.sokoban.model.*;
import es.upm.pproject.sokoban.model.Box;

import javax.imageio.ImageIO;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GamePanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(GamePanel.class);
    private transient GameMovementCounter gameMovementCounter;
    private transient LevelMovementCounter levelMovementCounter;
    private transient Map<Position, Square> board;
    private transient Image wall;
    private transient Image box;
    private transient Image boxWin;
    private transient Image goalPosition;
    private transient Image warehouseMan;
    private transient Image floor;
    private transient Frame gameFrame;
    private transient Controller gameController;
    private transient Action moveUp;
    private transient Action moveDown;
    private transient Action moveLeft;
    private transient Action moveRight;
    private transient Action undoCtrlZ;
    private int numGoals = 0;
    private int rows;
    private int cols;
    private Clip levelClip;
    private Clip congratulationsClip;
    private boolean finished = false;

    public GamePanel(Map<Position, Square> board, int rows, int cols, Frame gameFrame, Controller gameController,
                     GameMovementCounter gameMovementCounter, LevelMovementCounter levelMovementCounter, Clip levelClip, Clip congratulationsClip) {
        this.board = board;
        this.rows = rows;
        this.cols = cols;
        this.gameFrame = gameFrame;
        this.gameController = gameController;
        this.wall = loadImage("./sprites/wall.png");
        this.box = loadImage("./sprites/box.png");
        this.boxWin = loadImage("./sprites/box_win.png");
        this.goalPosition = loadImage("./sprites/goal-position.png");
        this.floor = loadImage("./sprites/floor.png");
        this.warehouseMan = loadImage("./sprites/warehouse-man.png");
        this.gameMovementCounter = gameMovementCounter;
        this.levelMovementCounter = levelMovementCounter;
        this.levelClip=levelClip;
        this.congratulationsClip=congratulationsClip;

        setActions();
        setKeys();
    }

    private Image loadImage(String imagePath) {
        Image img = null;
        try {
            File imageFile = new File(imagePath);
            img = ImageIO.read(imageFile);
        } catch (IOException e) {
            logger.error("Failed to load the image: {}", e.getMessage());
        }
        return img;
    }

    private Image classToImage(Object o) {
        if (o instanceof Wall) {
            return this.wall;
        } else if (o instanceof Box) {
            if (((Box) o).getGoalPosition() != null) {
                return this.boxWin;
            }
            return this.box;
        } else if (o instanceof GoalPosition) {
            numGoals++;
            return this.goalPosition;
        } else if (o instanceof WarehouseMan) {
            if (((WarehouseMan) o).getGoalPosition() != null)
                numGoals++;
            return this.warehouseMan;
        }
        return null;
    }

    private void setKeys() {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUpArrow");
        getActionMap().put("moveUpArrow", moveUp);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "moveUpW");
        getActionMap().put("moveUpW", moveUp);

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDownArrow");
        getActionMap().put("moveDownArrow", moveDown);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "moveDownS");
        getActionMap().put("moveDownS", moveDown);

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeftArrow");
        getActionMap().put("moveLeftArrow", moveLeft);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "moveLeftA");
        getActionMap().put("moveLeftA", moveLeft);

        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRightArrow");
        getActionMap().put("moveRightArrow", moveRight);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0), "moveRightD");
        getActionMap().put("moveRightD", moveRight);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "ctrlZ");
        getActionMap().put("ctrlZ", undoCtrlZ);
    }

    private void updateScoreAndPaint() {
        gameMovementCounter.incrementMovementCount();
        levelMovementCounter.incrementMovementCount();
        repaint();
    }

    private void setActions() {
        this.moveUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameController.moveUp(levelMovementCounter.getMovementCount())) {
                    updateScoreAndPaint();
                }
            }
        };

        this.moveDown = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameController.moveDown(levelMovementCounter.getMovementCount())) {
                    updateScoreAndPaint();
                }

            }
        };

        this.moveLeft = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameController.moveLeft(levelMovementCounter.getMovementCount())) {
                    updateScoreAndPaint();
                }

            }
        };

        this.moveRight = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameController.moveRight(levelMovementCounter.getMovementCount())) {
                    updateScoreAndPaint();
                }
            }
        };
        this.undoCtrlZ = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameFrame.undoMovement();
            }
        };

    }

    public void startNewGame() {
        setActions();
        setKeys();
        finished = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        numGoals = 0;
        int cellHeight = (gameFrame.getHeight() / rows) - (rows / 2);
        int cellWidth = gameFrame.getWidth() / cols;
        super.paintComponent(g);
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j)
                g.drawImage(floor, j * cellWidth, i * cellHeight, cellWidth, cellHeight, null);

        board.forEach((pos, square) -> g.drawImage(classToImage(square), pos.getX() * cellWidth,
                pos.getY() * cellHeight, cellWidth, cellHeight, null));

        if (numGoals == 0 && !finished) {
            this.levelMovementCounter.resetMovementCount();
            if (!gameController.nextLevel()) {
                gameFrame.setTitle("Sokoban - Congratulations!");
                finished = true;
                getInputMap().clear();
                getActionMap().clear();
                ImageIcon backgroundImage = new ImageIcon("./sprites/final.png");
                ImageIcon congratulations = new ImageIcon("./sprites/congratulations.png");
                g.drawImage(backgroundImage.getImage(), 0, 0, gameFrame.getWidth(), gameFrame.getHeight(), null);
                g.drawImage(congratulations.getImage(), 0, 0, gameFrame.getWidth() / 2, gameFrame.getHeight() / 2, null);
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Arial", Font.BOLD, 36));
                FontMetrics fm = g.getFontMetrics();
                String message = String.format("Score: %d", gameMovementCounter.getMovementCount());
                int messageWidth = fm.stringWidth(message);
                int messageHeight = fm.getHeight();
                int x = (gameFrame.getWidth() - messageWidth) / 2;
                int y = (gameFrame.getHeight() / 2 - messageHeight) / 2;
                g.drawString(message, x, y);
                levelClip.stop();
                congratulationsClip.start();
                logger.info("Game finished.");
            } else {
                gameFrame.setTitle("Sokoban - " + gameController.getLevelName());
                repaint();
            }
        }
    }

    boolean getFinished() {
        return this.finished;
    }
}
