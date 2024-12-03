package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.*;

public class BoardViewModel4Play {
    private final GridViewModel4Play gridViewModel4Play;
    private final Board4Play board4Play;

    public BoardViewModel4Play(Board4Design board4Design) {
        this.board4Play = new Board4Play(board4Design);
        gridViewModel4Play = new GridViewModel4Play(board4Play);
        mushroomVisibleProperty().addListener((observable, oldValue, newValue) -> {
            board4Play.manageMushroomVisibility();
        });
    }
    public int getMushroomLine() {
        return board4Play.getMushroomLine();
    }
    public int getMushroomCol() {
        return board4Play.getMushroomCol();
    }
    public BooleanProperty mushroomVisibleProperty() {
        return board4Play.mushroomVisibleProperty();
    }
    public void setMushroomVisible(boolean isVisible) {
        board4Play.setMushroomVisible(isVisible);
    }
    public GridViewModel4Play getGridViewModel() {
        return gridViewModel4Play;
    }
    public LongProperty nbMovesProperty() {
        return board4Play.nbMovesProperty();
    }
    public LongBinding nbGoalsReachedProperty() {
        return board4Play.nbGoalsReachedProperty();
    }
    public void movePlayer(String direction) {
        board4Play.move(direction);
    }
    public void undoLastCommand() {
        board4Play.undoLastCommand();
    }
    public void redoLastCommand() {
        board4Play.redoLastCommand();
    }
    public int getGoalCount() {
        return board4Play.getGoalCount();
    }
    public BooleanBinding heWonProperty() {
        return board4Play.heWonProperty();
    }
}

