package sokoban.model;

import javafx.beans.property.*;

abstract class Grid {
    protected final IntegerProperty gridHeight = new SimpleIntegerProperty(10);
    protected final IntegerProperty gridWidth = new SimpleIntegerProperty(15);
    protected int playerLine;
    protected int playerCol;
    protected Cell[][] matrix;
    abstract void play(int line, int col, GameObjects value);
    ReadOnlyListProperty<GameObjects> getValues(int row, int col) {
        return matrix[row][col].getValues();
    }
    public int getPlayerLine() {
        return playerLine;
    }
    public int getPlayerCol() {
        return playerCol;
    }
    public int getGridWidth() {
        return gridWidth.get();
    }
    public int getGridHeight() {
        return gridHeight.get();
    }
    public void setGridHeight(int gridHeight) {
        this.gridHeight.setValue(gridHeight);
    }
    public void setGridWidth(int gridWidth) {
        this.gridWidth.setValue(gridWidth);
    }
    public IntegerProperty gridWidthProperty() {
        return gridWidth;
    }
    public IntegerProperty gridHeightProperty() {
        return gridHeight;
    }
    protected int getGoalCount() {
        int count = 0;
        for (int i = 0; i < getGridHeight(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {
                if (matrix[i][j].getValues().contains(new Goal())) {
                    count++;
                }
            }
        }
        return count;
    }
    protected int getBoxCount() {
        int count = 0;
        for (int i = 0; i < getGridHeight(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {
                if (matrix[i][j].getValues().contains(new Box())){
                    count++;
                }
            }
        }
        return count;
    }
    protected int getPlayerCount() {
        int count = 0;
        for (int i = 0; i < getGridHeight(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {
                if (matrix[i][j].getValues().contains(new Player())){
                    count++;
                }
            }
        }
        return count;
    }
}
