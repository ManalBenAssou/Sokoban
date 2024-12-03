package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import sokoban.viewmodel.GridViewModel4Design;

public class GridView4Design extends GridPane {
    private static final int PADDING = 20;
    public int width;
    public int height;
    GridView4Design(GridViewModel4Design gridViewModel4Design, DoubleBinding gridWidth, DoubleBinding gridHeight) {
        width = gridViewModel4Design.getGridWidth();
        height = gridViewModel4Design.getGridHeight();

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
        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                CellView4Design cellView4Design = new CellView4Design(gridViewModel4Design.getCellViewModel(i, j), cellWidth, cellHeight);
                add(cellView4Design, j, i); // lignes/colonnes inversées dans gridpane
            }
        }
    }
    public double getCellWidth() {
        // Supposons que la largeur de la cellule est uniforme pour toute la grille.
        return getWidth() / width - PADDING  / (double) width;
    }
    public double getCellHeight() {
        // Supposons que la hauteur de la cellule est uniforme pour toute la grille.
        return getHeight() / height - PADDING / (double) height;
    }
}

