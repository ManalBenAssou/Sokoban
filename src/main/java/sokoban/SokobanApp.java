package sokoban;

import javafx.application.Application;
import javafx.stage.Stage;
import sokoban.model.Board4Design;
import sokoban.view.BoardView4Design;
import sokoban.viewmodel.BoardViewModel4Design;

public class SokobanApp extends Application  {

    @Override
    public void start(Stage primaryStage) {
        // TODO: basez vous sur l'exercice de la grille comme point de départ pour votre projet
        Board4Design board4Design = new Board4Design();
        BoardViewModel4Design vm = new BoardViewModel4Design(board4Design);
        new BoardView4Design(primaryStage, vm);
    }

    public static void main(String[] args) {
        launch();
    }

}
