package sokoban.viewmodel;

import javafx.beans.property.ReadOnlyListProperty;
import sokoban.model.Board4Design;
import sokoban.model.CellValue;
import sokoban.model.GameObjects;

public class CellViewModel4Design {
    private final int line, col;
    private final Board4Design board4Design;
    CellViewModel4Design(int line, int col, Board4Design board4Design) {
        this.line = line;
        this.col = col;
        this.board4Design = board4Design;
    }
    public void play() {
        board4Design.play(line, col, BoardViewModel4Design.getGameObjectSelected());
    }
    public void removeValue(){
        BoardViewModel4Design.setGameObjectSelected(CellValue.GROUND);
    }
    public ReadOnlyListProperty<GameObjects> getValues() {
        return board4Design.getValues(line, col);
    }
}
