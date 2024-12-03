package sokoban.viewmodel;

import sokoban.model.Board4Design;

public class GridViewModel4Design {
    private final Board4Design board4Design;
    GridViewModel4Design(Board4Design board4Design) {
        this.board4Design = board4Design;
    }
    public int getGridWidth() {
        return board4Design.getGridWidth();
    }
    public int getGridHeight() {
        return board4Design.getGridHeight();
    }
    public CellViewModel4Design getCellViewModel(int line, int col) {
        return new CellViewModel4Design(line, col, board4Design);
    }
}
