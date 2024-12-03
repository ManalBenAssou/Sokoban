package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.Board4Play;
import sokoban.model.GameObjects;

public class CellViewModel4Play {
    private final int line, col;
    private final Board4Play board;
    CellViewModel4Play(int line, int col, Board4Play board) {
        this.line = line;
        this.col = col;
        this.board = board;
    }
    public ReadOnlyListProperty<GameObjects> getValues() {
        return board.getValues(line, col);
    }

    public void moveMushroom() {
        board.moveMushroom();
    }
}
