package sokoban.viewmodel;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import sokoban.model.*;

import java.io.File;

public class BoardViewModel4Design {
    private final GridViewModel4Design gridViewModel4Design;
    private final Board4Design board4Design;
    private final StringProperty messageErreur = new SimpleStringProperty();
    public BoardViewModel4Design(Board4Design board4Design) {
        this.board4Design = board4Design;
        gridViewModel4Design = new GridViewModel4Design(board4Design);
        board4Design.changeMadeProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                updateMessageErreur();
        });
    }
    public BoardViewModel4Design(int gridHeight, int gridWidth) { // pour open et new
        this.board4Design = new Board4Design(gridWidth, gridHeight);
        gridViewModel4Design = new GridViewModel4Design(board4Design);

        board4Design.changeMadeProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                updateMessageErreur();
        });
    }
    public StringProperty messageErreurProperty() {
        return messageErreur;
    }
    public ReadOnlyBooleanProperty boardChangedProperty(){
        return board4Design.boardChangedProperty();
    }
    public int gridWidth() {
        return board4Design.getGridWidth();
    }
    public int gridHeight() {
        return board4Design.getGridHeight();
    }
    public GridViewModel4Design getGridViewModel() {
        return gridViewModel4Design;
    }
    public LongBinding filledCellsCountProperty() {
        return board4Design.filledCellsCountProperty();
    }
    public int maxFilledCells() {
        return board4Design.maxFilledCells();
    }
    public ReadOnlyListProperty<GameObjects> play(int row, int col, CellValue selectedObject) {
        return board4Design.play(row, col, GameObjects.getGameObject(selectedObject) );
    }
    private static final ObjectProperty<CellValue> gameObjectSelected = new SimpleObjectProperty<>(CellValue.GROUND);
    public static void setGameObjectSelected(CellValue gameObject){
        gameObjectSelected.setValue(gameObject);
    }
    public static GameObjects getGameObjectSelected() {
        return GameObjects.getGameObject(gameObjectSelected.getValue());
    }
    private int getCountBox(){
        return board4Design.getBoxCount();
    }
    private int getCountGoal(){
        return board4Design.getGoalCount();
    }
    private int getPlayerCount(){
        return board4Design.getCountPlayer();
    }
    public void updateMessageErreur(){
        StringBuilder message = new StringBuilder();
        if (getPlayerCount() == 0 || getCountBox() == 0 || getCountGoal() == 0 || getCountBox() != getCountGoal()) {
            message.append("\n Please correct the following error(s): ");
            if (getPlayerCount() == 0) {
                message.append("\n• A player is required");
            }
            if (getCountGoal() == 0) {
                message.append("\n• At least one target is required");
            }
            if (getCountBox() == 0) {
                message.append("\n• At least one box is required");
            }
            if (getCountGoal() != getCountBox()) {
                message.append("\n• The number of targets and boxes must be equal");
            }
        }
        messageErreur.set(message.toString().trim());
    }
    public void resetBoard(){
        board4Design.resetBoard();
        updateMessageErreur();

    }
    public void saveGrid(File file) {
        board4Design.saveGrid(file);
    }
    public void openFile(File file) {
        board4Design.openFile(file);
    }
    public BooleanBinding errorsPresent = messageErreurProperty().isNotEmpty();
    public Board4Design getBoard() {
        return board4Design;
    }
}