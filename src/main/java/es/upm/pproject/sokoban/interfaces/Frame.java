package es.upm.pproject.sokoban.interfaces;

public interface Frame {
    void initialize(Controller gameController);
    void setController(Controller gameController);
    void setTitle(String title);
    int getWidth();
    int getHeight();

    void undoMovement();
}
