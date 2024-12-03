package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;

import java.util.Random;

public class Board4Play {
    private final Grid4Play grid;
    private final CommandInvoker commandInvoker = new CommandInvoker();
    private int mushroomLine;
    private int mushroomCol;
    public Board4Play(Board4Design board4Design) {
        grid = new Grid4Play(board4Design.getGrid());
        this.mushroomLine = mushroomLine(grid.getGridHeight());
        this.mushroomCol = mushroomCol(grid.getGridWidth());
    }
    public void manageMushroomVisibility() {
        System.out.println("mushroomLine "+mushroomLine);
        System.out.println("mushroomCol "+mushroomCol);
        if (mushroomVisible.get()) {
            play(mushroomLine, mushroomCol, new Mushroom());
            nbMoves.set(nbMoves.get()+10);
        }
        else {
            play(mushroomLine, mushroomCol, new Ground());
        }
    }
    private int mushroomLine(int height) {
        Random random = new Random();
        return random.nextInt(height);
    }
    private int mushroomCol(int width) {
        Random random = new Random();
        return random.nextInt(width);
    }
    public int getMushroomLine() {
        return mushroomLine;
    }

    public int getMushroomCol() {
        return mushroomCol;
    }
    private final BooleanProperty mushroomVisible = new SimpleBooleanProperty(false);
    public BooleanProperty mushroomVisibleProperty() {
        return mushroomVisible;
    }
    public void setMushroomVisible(boolean isVisible) {
        mushroomVisible.set(isVisible);
    }
    public void play(int row, int col, GameObjects objectSelected) {
        grid.play(row, col, objectSelected);

        if (objectSelected.getType() == CellValue.PLAYER)
            nbMoves.setValue(nbMoves.get()+1);
    }
    public boolean isValidMove(int row, int col) {
        // Vérifier si la nouvelle position est à l'intérieur de la grille et si elle est libre
        return grid.isValidPos(row, col) &&
                !grid.getValues(row, col).contains(new Wall());
    }
    private boolean is2CellsContainBox(int row, int col) {
        return grid.is2CellsContainBox(row, col);
    }
    public ReadOnlyListProperty<GameObjects> getValues(int line, int col) {
        return grid.getValues(line, col);
    }
    public int getGoalCount() {
        return grid.getGoalCount();
    }
    public int getPlayerLine() {
        return grid.getPlayerLine();
    }
    public int getPlayerCol() {
        return grid.getPlayerCol();
    }
    private final LongProperty nbMoves = new SimpleLongProperty();
    public LongProperty nbMovesProperty() {
        return nbMoves;
    }
    public void setNbMoves(long nbMoves) {
        this.nbMoves.set(nbMoves);
    }
    public long getNbMoves() {
        return nbMoves.getValue();
    }
    public LongBinding nbGoalsReachedProperty() {
        return grid.nbGoalsReachedProperty();
    }
    public Box moveUp() {
        Box b = null;
        if (isValidMove(getPlayerLine()-1, getPlayerCol())) { // verif de  move player
            if (isContainsBox(getPlayerLine()-1, getPlayerCol())) { // verification de move Box
                b = grid.getCell(getPlayerLine()-1, getPlayerCol()).getBox();
                if (isValidMove(getPlayerLine()-2, getPlayerCol())) {
                    if (!is2CellsContainBox(getPlayerLine()-2, getPlayerCol())) {
                        play(getPlayerLine() - 2, getPlayerCol(), b);
                        play(getPlayerLine() - 1, getPlayerCol(), new Player());
                    }
                }
            } else {
                play(getPlayerLine()-1, getPlayerCol(), new Player());
            }
        }
        return b;
    }
    public Box moveDown() {
        Box b = null;
        if (isValidMove(getPlayerLine()+1, getPlayerCol())) {
            if (isContainsBox(getPlayerLine()+1, getPlayerCol())) {
                b = grid.getCell(getPlayerLine()+1, getPlayerCol()).getBox();
                if (isValidMove(getPlayerLine()+2, getPlayerCol())) {;
                    if (!is2CellsContainBox(getPlayerLine()+2, getPlayerCol())) {
                        play(getPlayerLine() + 2, getPlayerCol(), b);
                        play(getPlayerLine() + 1, getPlayerCol(), new Player());
                    }
                }
            }
            else {
                play(getPlayerLine()+1, getPlayerCol(), new Player());
            }
        }
        return b;
    }
    public Box moveRight() {
        Box b = null;
        if (isValidMove(getPlayerLine(), getPlayerCol()+1)) {
            if (isContainsBox(getPlayerLine(), getPlayerCol()+1)) {
                b = grid.getCell(getPlayerLine(), getPlayerCol()+1).getBox();
                if (isValidMove(getPlayerLine(), getPlayerCol()+2)) {
                    if (!is2CellsContainBox(getPlayerLine(), getPlayerCol()+2)) {
                        play(getPlayerLine(), getPlayerCol() + 2, b);
                        play(getPlayerLine(), getPlayerCol() + 1, new Player());
                    }
                }
            }
            else {
                play(getPlayerLine(), getPlayerCol()+1, new Player());
            }
        }
        return b;
    }
    public Box moveLeft() {
        Box b = null;
        if (isValidMove(getPlayerLine(), getPlayerCol()-1)) {
            if (isContainsBox(getPlayerLine(), getPlayerCol()-1)) {
                b = grid.getCell(getPlayerLine(), getPlayerCol()-1).getBox();
                if (isValidMove(getPlayerLine(), getPlayerCol()-2)) {
                    if (!is2CellsContainBox(getPlayerLine(), getPlayerCol()-2)) {
                        play(getPlayerLine(), getPlayerCol() - 2, b);
                        play(getPlayerLine(), getPlayerCol() - 1, new Player());
                    }
                }
            }
            else {
                play(getPlayerLine(), getPlayerCol()-1, new Player());
            }
        }
        return b;
    }
    public int getGridWidth() {
        return grid.getGridWidth();
    }
    public int getGridHeight() {
        return grid.getGridHeight();
    }
    public BooleanBinding heWonProperty() {
        return grid.heWonProperty();
    }
    public boolean isContainsBox(int i , int j){
        return getValues(i,j).contains(new Box());
    }
    public void move(String direction) {

        if (direction.compareTo("UP") == 0) {
            if (isValidMove(getPlayerLine()-1, getPlayerCol())) {
                if (!isContainsBox(getPlayerLine()-1, getPlayerCol()))
                    commandInvoker.executeCommand(new MoveCommands.MoveUpCommand(this)); // player

                else if (isValidMove(getPlayerLine()-2, getPlayerCol()) && !is2CellsContainBox(getPlayerLine()-2, getPlayerCol()))
                    commandInvoker.executeCommand(new MoveCommands.MoveUpCommand(this)); //box
            }
        }
        else if (direction.compareTo("DOWN") == 0) {
            if (isValidMove(getPlayerLine()+1, getPlayerCol())) {
                if (!isContainsBox(getPlayerLine()+1, getPlayerCol()))
                    commandInvoker.executeCommand(new MoveCommands.MoveDownCommand(this));

                else if (isValidMove(getPlayerLine()+2, getPlayerCol()) && !is2CellsContainBox(getPlayerLine()+2, getPlayerCol()))
                    commandInvoker.executeCommand(new MoveCommands.MoveDownCommand(this));
            }
        }
        else if (direction.compareTo("RIGHT") == 0) {
            if (isValidMove(getPlayerLine(), getPlayerCol()+1)) {
                if (!isContainsBox(getPlayerLine(), getPlayerCol()+1))
                    commandInvoker.executeCommand(new MoveCommands.MoveRightCommand(this));

                else if (isValidMove(getPlayerLine(), getPlayerCol()+2) && !is2CellsContainBox(getPlayerLine(), getPlayerCol()+2))
                    commandInvoker.executeCommand(new MoveCommands.MoveRightCommand(this));
            }
        }
        else if (direction.compareTo("LEFT") == 0) {
            if (isValidMove(getPlayerLine(), getPlayerCol()-1)) {
                if (!isContainsBox(getPlayerLine(), getPlayerCol()-1))
                    commandInvoker.executeCommand(new MoveCommands.MoveLeftCommand(this));

                else if (isValidMove(getPlayerLine(), getPlayerCol()-2) && !is2CellsContainBox(getPlayerLine(), getPlayerCol()-2))
                    commandInvoker.executeCommand(new MoveCommands.MoveLeftCommand(this));
            }
        }
    }
    public void undoLastCommand() {
        commandInvoker.undoLastCommand();
    }
    public void redoLastCommand() {
        commandInvoker.redoLastCommand();
    }

    public void moveMushroom() {
        commandInvoker.executeCommand(new MoveMushroom());
        nbMoves.set(nbMoves.get()+20);
        play(mushroomLine, mushroomCol, new Mushroom());
        mushroomVisible.set(false);
        this.mushroomLine = mushroomLine(grid.getGridHeight());
        this.mushroomCol = mushroomCol(grid.getGridWidth());
    }
}