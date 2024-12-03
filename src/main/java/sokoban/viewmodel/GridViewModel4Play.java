package sokoban.viewmodel;

import sokoban.model.Board4Play;

public class GridViewModel4Play {
    private final Board4Play board;
    GridViewModel4Play(Board4Play board) {
        this.board = board;
    }
    public int getGridWidth() {
        return board.getGridWidth();
    }
    public int getGridHeight() {
        return board.getGridHeight();
    }
    public CellViewModel4Play getCellViewModel(int line, int col) {
        return new CellViewModel4Play(line, col, board);
    }
    public boolean isContainsBox(int i , int j){
        return board.isContainsBox(i,j);
    }
}
