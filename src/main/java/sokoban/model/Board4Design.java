package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board4Design {
    private final BooleanProperty boardChanged = new SimpleBooleanProperty(false);
    private final Grid4Design grid4Design;
    private final BooleanBinding isFull;
    /**contructeur pour le open et le new **/
    public Board4Design(int width, int height) {
        grid4Design = new Grid4Design(height, width);
        isFull = grid4Design.filledCellsCountProperty().isEqualTo(grid4Design.getMaxFillableCells());
    }
    public Board4Design() {
        grid4Design = new Grid4Design();
        isFull = grid4Design.filledCellsCountProperty().isEqualTo(grid4Design.getMaxFillableCells());
    }
    public int getGridWidth() {
        return grid4Design.getGridWidth();
    }
    public int getGridHeight() {
        return grid4Design.getGridHeight();
    }
    private Boolean isFull() {
        return isFull.get();
    }
    public BooleanProperty boardChangedProperty() {
        return boardChanged;
    }
    public int maxFilledCells() {
        return grid4Design.getMaxFillableCells();
    }
    public ReadOnlyListProperty<GameObjects> play(int row, int col, GameObjects objectSelected) {
        if (grid4Design.getValues(row, col).contains(new Ground()) && isFull())
            return grid4Design.getValues(row, col);

        grid4Design.play(row, col, objectSelected);
        boardChanged.setValue(true);

        return grid4Design.getValues(row, col);
    }
    public Grid4Design getGrid() {
        return this.grid4Design;
    }
    public ReadOnlyListProperty<GameObjects> getValues(int line, int col) {
        return grid4Design.getValues(line, col);
    }
    public LongBinding filledCellsCountProperty() {
        return grid4Design.filledCellsCountProperty();
    }
    /**pour surveiler les changement dans la grille et afficher les messages d'erreurs **/
    public ReadOnlyBooleanProperty changeMadeProperty() {
        return grid4Design.gridChangedProperty();
    }
    public boolean isEmpty() {
        return grid4Design.isGridEmpty();
    }
    public int getBoxCount() {
        return grid4Design.getBoxCount();
    }
    public int getGoalCount() {
        return grid4Design.getGoalCount();
    }
    public int getCountPlayer() {
        return grid4Design.getPlayerCount();
    }
    /** Mettre à jour le modèle de grille pour réinitialiser toutes les cellules à GROUND**/
    public void resetBoard() {
        for (int row = 0; row < grid4Design.getGridHeight(); row++) {
            for (int col = 0; col < grid4Design.getGridWidth(); col++) {
                grid4Design.setCellValue(row, col, new Ground());
            }
        }
    }
    public void saveGrid(File selectedFile) {
        try {
            // Créer un écrivain de fichier
            BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile));

            // Parcourir la grille pour écrire chaque objet dans le fichier
            for (int row = 0; row < grid4Design.getGridHeight(); row++) {
                for (int col = 0; col < grid4Design.getGridWidth(); col++) {
                    ReadOnlyListProperty<GameObjects> values = getValues(row, col); // recuperer la liste des objets de chaque cellule
                    char character;
                    if (values.isEmpty() || values.contains(new Ground()))
                        character = ' ';
                    else if (values.contains(new Wall()))
                        character = '#';
                    else if (values.contains(new Player()) && values.contains(new Goal()))
                        character = '+';
                    else if (values.contains(new Box()) && values.contains(new Goal()))
                        character = '*';
                    else if (values.contains(new Goal()))
                        character = '.';
                    else if (values.contains(new Box()))
                        character = '$';
                    else if (values.contains(new Player()))
                        character = '@';
                    else
                        character = '9';

                    // Écrire le caractère dans le fichier
                    writer.write(character);

                }
                // Aller à la ligne suivante dans le fichier après chaque ligne de la grille
                writer.newLine();
            }

            // Fermer l'écrivain de fichier
            writer.close();

            //enlever l'etoile de titre
            boardChanged.set(false);

            // Message de succès
            System.out.println("Fichier sauvegardé avec succès : " + selectedFile.getAbsolutePath());
        } catch (IOException e) {
            // Gérer les exceptions d'entrée/sortie (par exemple, erreur d'écriture dans le fichier)
            e.printStackTrace();
        }
    }
    public void openFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            List<String> lines = new ArrayList<>();
            int gridWidth = 0;
            int gridHeight = 0;

            // Lire le fichier et collecter les lignes
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);
                gridWidth = Math.max(gridWidth, line.length());
                ++gridHeight;

            }

            grid4Design.setGridHeight(gridHeight);
            grid4Design.setGridWidth(gridWidth);


            // Remplir la grille avec les valeurs lues du fichier
            for (int y = 0; y < gridHeight; y++) {
                String line = lines.get(y);
                for (int x = 0; x < gridWidth; x++) {
                    char character = line.charAt(x);

                    if (character == ' ')
                        play(y, x, new Ground());
                    else if (character == '#')
                        play(y, x, new Wall());
                    else if (character == '.')
                        play(y, x, new Goal());
                    else if (character == '$')
                        play(y, x, new Box());
                    else if (character == '@')
                        play(y, x, new Player());
                    else if (character == '+') {
                        play(y, x, new Player());
                        play(y, x, new Goal());
                    }
                    else if (character == '*') {
                        play(y, x, new Box());
                        play(y, x, new Goal());
                    }
                }
            }

            boardChanged.set(false); // pour suppeimer l'etoile apres l'overture d'un fichier

        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }
}