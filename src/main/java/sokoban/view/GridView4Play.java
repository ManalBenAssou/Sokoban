package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.GridViewModel4Play;

public class GridView4Play extends GridPane {
    private static final int PADDING = 20;
    public int width;
    public int height;
    GridView4Play(GridViewModel4Play gridViewModel, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        width = gridViewModel.getGridWidth();
        height = gridViewModel.getGridHeight();

        setGridLinesVisible(true);
        setPadding(new Insets(PADDING));
        //setStyle(" -fx-background-color:green; ");

        DoubleBinding cellWidth = gridWidth
                .subtract(PADDING * 2)
                .divide(width);
        DoubleBinding cellHeight = gridWidth
                .subtract(PADDING * 2)
                .divide(width);

        // Remplissage de la grille
        int id = 0;
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                if (gridViewModel.isContainsBox(i,j)){
                    gridViewModel.getCellViewModel(i, j).getValues().get(0).setId(++id);
                }
                CellView4Play cellView = new CellView4Play(gridViewModel.getCellViewModel(i, j), cellWidth, cellHeight);
                add(cellView, j, i); // lignes/colonnes inversées dans gridpane
            }
        }


    }
}

