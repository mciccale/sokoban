package es.upm.pproject.sokoban.model;

import javax.swing.*;

public abstract class MovementCounter extends JLabel {
    private static final String SCORE_TEXT = " Score: ";
    protected int movements;

    protected MovementCounter(String label) {
        super(label + "Score: 0");
        this.movements = 0;
    }

    public int getMovementCount() {
        return this.movements;
    }

    public void setMovementCount(int movements){
        this.movements = movements;
        setText(getLabel() + SCORE_TEXT + this.movements);
    }

    public void resetMovementCount() {
        this.movements = 0;
        setText(getLabel() + SCORE_TEXT + this.movements);
    }

    public void incrementMovementCount() {
        this.movements++;
        setText(getLabel() + SCORE_TEXT + this.movements);
    }

    public void decrementMovementCount() {
        this.movements--;
        setText(getLabel() + SCORE_TEXT + this.movements);
    }
    protected abstract String getLabel();
}

