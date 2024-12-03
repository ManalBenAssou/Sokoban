package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;

import java.util.Arrays;

class Grid4Play extends Grid {
    Grid4Play(Grid4Design grid4Design) {
        gridHeight.setValue(grid4Design.getGridHeight());
        gridWidth.setValue(grid4Design.getGridWidth());

        //initialiser la position de joueur
        playerLine = grid4Design.getPlayerLine();
        playerCol = grid4Design.getPlayerCol();

        matrix = new Cell4Play[gridHeight.get()][gridWidth.get()];
        for (int i = 0; i < gridHeight.get(); i++) {
            for (int j = 0; j < gridWidth.get(); j++) {
                matrix[i][j] = new Cell4Play(grid4Design.getCell(i, j));
            }
        }

        nbGoalsReached = Bindings.createLongBinding(() ->
                Arrays.stream(matrix)
                        .flatMap(Arrays::stream)
                        .filter(cell -> cell.getValues().contains(new Goal()) && cell.getValues().contains(new Box()))
                        .count()
        );
        heWon = Bindings.createBooleanBinding(() -> getGoalCount() == nbGoalsReached.get());

    }
    Grid4Play(int gridHeight, int gridWidth) {
        this.gridHeight.set(gridHeight);
        this.gridWidth.set(gridWidth);
        matrix = new Cell4Design[gridHeight][gridWidth];
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                matrix[i][j] = new Cell4Design();
            }
        }
        nbGoalsReached = Bindings.createLongBinding(() ->
                Arrays.stream(matrix)
                        .flatMap(Arrays::stream)
                        .filter(cell -> cell.getValues().contains(new Goal()) && cell.getValues().contains(new Box()))
                        .count()
        );

        heWon = Bindings.createBooleanBinding(() -> getGoalCount() == nbGoalsReached.get());

    }
//    ReadOnlyListProperty<GameObjects> getValues(int row, int col) {
//        ListProperty<GameObjects> emptyList = new SimpleListProperty<>();
//        return isValidPos(row, col) ? matrix[row][col].getValues() : emptyList;
//    }
    public void play(int line, int col, GameObjects value) {
        matrix[line][col].play(value);

        if (value.getType() == CellValue.PLAYER) {
            matrix[playerLine][playerCol].removeValue(value);
            matrix[line][col].addValue(value);

            playerLine = line;
            playerCol = col;
        }
        nbGoalsReached.invalidate();
        heWon.invalidate();
    }
    public boolean isValidPos(int row, int col) {
        // Vérifie si la nouvelle position est à l'intérieur de la grille
        return row >= 0 && row < getGridHeight() &&
                col >= 0 && col < getGridWidth();
    }
    public boolean is2CellsContainBox(int row, int col) {
        if (isValidPos(row, col)) {
            if (getValues(row, col).contains(new Box())) {
                // Vérifier si la cellule adjacente contiennent une boite
                return (row > 0 && getValues(row - 1, col).contains(new Box())) ||
                        (row < getGridHeight() - 1 && getValues(row + 1, col).contains(new Box())) ||
                        (col > 0 && getValues(row, col - 1).contains(new Box())) ||
                        (col < getGridWidth() - 1 && getValues(row, col + 1).contains(new Box()));
            }
        }
        return false;
    }
    private final LongBinding nbGoalsReached;
    public LongBinding nbGoalsReachedProperty() {
        return nbGoalsReached;
    }
    private final BooleanBinding heWon;
    public BooleanBinding heWonProperty() {
        return heWon;
    }
    public Cell4Play getCell(int line, int col) {
        return (Cell4Play) matrix[line][col];
    }
}
