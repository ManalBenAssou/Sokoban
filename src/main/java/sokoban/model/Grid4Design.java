package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import java.util.Arrays;

class Grid4Design extends Grid {
    private final BooleanProperty gridChanged = new SimpleBooleanProperty(false);
    private  int MAX_FILLABLE_CELLS;
    private final LongBinding filledCellsCount;

    Grid4Design() {
        this(10, 15);
        //this(gridHeight.getValue(), gridWidth.getValue());
    }
    Grid4Design(int gridHeight, int gridWidth) {
        this.gridHeight.set(gridHeight);
        this.gridWidth.set(gridWidth);
        matrix = new Cell4Design[gridHeight][gridWidth];
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                matrix[i][j] = new Cell4Design();
            }
        }

        filledCellsCount = Bindings.createLongBinding(() ->
                        Arrays.stream(matrix)
                                .flatMap(Arrays::stream)
                                .filter(cell4Design -> !(cell4Design.isEmpty()))
                                .count()
        );

        filledCellsCount.addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() > MAX_FILLABLE_CELLS) {
                throw new IllegalStateException("Filled cells count exceeds the maximum limit");
            }
        });

        MAX_FILLABLE_CELLS = (this.gridWidth.get() * this.gridHeight.get()) / 2;
    }
    public void setCellValue(int row, int col, GameObjects value) {
        // Check if we can set the cell content to a non-Ground value
        matrix[row][col].play(value);
        filledCellsCount.invalidate();
    }
    public ReadOnlyBooleanProperty gridChangedProperty() {
        return gridChanged;
    }
    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }
    public int getMaxFillableCells() {
        return MAX_FILLABLE_CELLS;
    }

    public void play(int line, int col, GameObjects value) {
        /** si on a pas encore de player et la valeur selectionner n'est pas un player**/
        if (getPlayerCount() == 0 || value.getType() != CellValue.PLAYER) {
            /** on peut placer les objets dans la grille si :
             * objet selectionner est une groound (value.getType()==CellValue.GROUND)
             * filledCellsCount < MAX_FILLABLE_CELLS si la grille est pas encore remplie !(filledCellsCount.get() >= MAX_FILLABLE_CELLS)
             * si la grille est deja Full mais la cellule selectione est remplie on peut modifier les objets de cette cellule (filledCellsCount.get() >= MAX_FILLABLE_CELLS && !getValues(line, col).isEmpty())**/
            if ((value.getType()==CellValue.GROUND) || !(filledCellsCount.get() >= MAX_FILLABLE_CELLS) || (filledCellsCount.get() >= MAX_FILLABLE_CELLS && !getValues(line, col).isEmpty())) {
                matrix[line][col].play(value);
                if (value.getType() == CellValue.PLAYER) {
                    playerLine = line; // stocker la position de player
                    playerCol = col;
                }
            }
        }
        else {
            if (matrix[playerLine][playerCol].getValues().contains(new Goal()))
                matrix[playerLine][playerCol].play(new Player());
            else
                matrix[playerLine][playerCol].play(new Ground());

            matrix[line][col].play(value); // placer le player et changer ca position
            playerLine = line;
            playerCol = col;
        }
        filledCellsCount.invalidate();
        gridChanged.setValue(true);
        // on met a false pour refresh le binding
        gridChanged.setValue(false);
    }
    public boolean isGridEmpty(){
        boolean result = true;
        for (int i = 0; i < getGridHeight(); i++) {
            for (int j = 0; j < getGridWidth(); j++) {
                if (!matrix[i][j].getValues().contains(new Ground())){
                   result = false;

                }
            }
        }
        return result;

    }
    public Cell4Design getCell(int i, int j) {
        return (Cell4Design) matrix[i][j];
    }
}
