package sokoban.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sokoban.model.*;
import sokoban.viewmodel.*;

import java.util.*;

public class BoardView4Play extends BorderPane {
    private final BoardViewModel4Play boardViewModel4Play;
    private final BoardViewModel4Design boardViewModel4Design;
    private GridView4Play gridView4Play;
    private final int width;
    private final int height;
    private static final int SCENE_MIN_WIDTH = 600;
    private static final int SCENE_MIN_HEIGHT = 500;
    private final Label nbMovesLabel = new Label("");
    private final Label goalsReachedLabel = new Label("");
    private final HBox buttomBox = new HBox();
    private final Button finishButton = new Button("Finish");
    private final Button restartButton = new Button("Restart");
    private final Button mushroomBtn = new Button();
    private VBox headerBox;

    public BoardView4Play(Stage primaryStage, BoardViewModel4Play boardViewModel4Play , BoardViewModel4Design boardViewModel4Design) {
        this.boardViewModel4Play = boardViewModel4Play;
        this.boardViewModel4Design = boardViewModel4Design;
        width = 15;
        height = 10;
        primaryStage.setTitle("Sokoban");
        start(primaryStage);
    }

    private void start(Stage stage) {
        // Mise en place des composants principaux
        configMainComponents(stage);
        manageMushroomButton();
        this.gridView4Play = createGrid();
        // Mise en place de la scène et affichage de la fenêtre
        Scene scene = new Scene(this, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        String cssFile = Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssFile);
        stage.setScene(scene);
        stage.show();
        stage.setMinHeight(stage.getHeight());
        stage.setMinWidth(stage.getWidth());
        setupKeyboardBehavior(scene);
    }
    private void manageMushroomButton() {
        mushroomBtn.setFocusTraversable(false);
        mushroomBtn.textProperty().bindBidirectional(boardViewModel4Play.mushroomVisibleProperty(), new StringConverter<Boolean>() {
            @Override
            public String toString(Boolean bool) {
                return bool ? "Hide mushroom" : "Show mushroom";
            }

            @Override
            public Boolean fromString(String s) {
                return null;
            }
        });
        mushroomBtn.setOnAction(event ->{
            if (mushroomBtn.textProperty().get().equals("Show mushroom")) {
                boardViewModel4Play.setMushroomVisible(true);
            }
            else if (mushroomBtn.textProperty().get().equals("Hide mushroom")) {
                boardViewModel4Play.setMushroomVisible(false);
            }
        });

        buttomBox.getChildren().add(mushroomBtn);
    }

    private void setupKeyboardBehavior(Scene scene) {
        scene.setOnKeyPressed(event -> {
            // si le joueur n'as pas encore gagne si il gane le move se desactive
            if (!boardViewModel4Play.heWonProperty().get() && !boardViewModel4Play.mushroomVisibleProperty().get()) {
                KeyCode keyCode = event.getCode();
                if (event.isControlDown()) {
                    switch (keyCode) {
                        case Z -> boardViewModel4Play.undoLastCommand(); // Exécute l'annulation si CTRL est enfoncé
                        case Y -> boardViewModel4Play.redoLastCommand();
                    }
                }
                else {
                    switch (keyCode) {
                        case UP, Z -> boardViewModel4Play.movePlayer("UP");

                        case DOWN, S -> boardViewModel4Play.movePlayer("DOWN");

                        case LEFT, Q -> boardViewModel4Play.movePlayer("LEFT");

                        case RIGHT, D -> boardViewModel4Play.movePlayer("RIGHT");

                    }
                }
            }
        });
    }
    private void configMainComponents(Stage stage) {
        stage.setTitle("Sokoban");
        createHeader();
        //createGrid();
        buttomBox.getChildren().add(finishButton);
        buttomBox.getChildren().add(restartButton);
        buttomBox.setAlignment(Pos.CENTER);
        setBottom(buttomBox);
        finishButton.setFocusTraversable(false);
        restartButton.setFocusTraversable(false);
        finishButton.focusTraversableProperty().bind(boardViewModel4Play.heWonProperty());
        finishButton.setOnAction(event -> finishGame());
        restartButton.setOnAction(event -> restartGame());
    }

    private void restartGame() {
        Stage stage = (Stage) this.getScene().getWindow();
        new BoardView4Play(stage, new BoardViewModel4Play(boardViewModel4Design.getBoard()), boardViewModel4Design);


    }

    private void finishGame() {
        Stage stage = (Stage) this.getScene().getWindow();
        new BoardView4Design(stage, boardViewModel4Design);
    }
    private void createHeader() {
        headerBox = new VBox();

        nbMovesLabel.textProperty().bind(boardViewModel4Play.nbMovesProperty()
                //le %d c'est l'entier qui est dans nbMovesProperty()
                .asString("Number of moves played: %d "));

        goalsReachedLabel.textProperty().bind(boardViewModel4Play.nbGoalsReachedProperty()
                .asString("Number of goals reached: %d of "+boardViewModel4Play.getGoalCount()));

        Label winnerLabel = new Label();
        winnerLabel.textProperty().bind(boardViewModel4Play.nbMovesProperty()
                .asString("You won in %d moves, congratulations !!"));

        winnerLabel.visibleProperty().bind(boardViewModel4Play.heWonProperty());

        Label score = new Label("Score");

        score.getStyleClass().add("header");
        winnerLabel.getStyleClass().add("header");
        headerBox.setAlignment(Pos.CENTER);

        headerBox.getChildren().add(score);
        headerBox.getChildren().add(nbMovesLabel);
        headerBox.getChildren().add(goalsReachedLabel);
        headerBox.getChildren().add(winnerLabel);

        VBox header = new VBox(headerBox);
        header.setAlignment(Pos.CENTER);

        this.setTop(header);
    }
    private GridView4Play createGrid() {
        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get(), heightProperty().get() - headerBox.heightProperty().get());
                    return Math.floor(size / width) * width;
                },
                widthProperty(),
                heightProperty(),
                headerBox.heightProperty());

        DoubleBinding gridHeight = Bindings.createDoubleBinding(
                () -> {
                    var size = Math.min(widthProperty().get(), heightProperty().get() - headerBox.heightProperty().get());
                    return Math.floor(size / height) * height;
                },
                widthProperty(),
                heightProperty(),
                headerBox.heightProperty());

        GridView4Play gridView = new GridView4Play(boardViewModel4Play.getGridViewModel(), gridWidth, gridHeight);

        // Grille carrée
        //gridView.minHeightProperty().bind(gridHeight);
        gridView.maxHeightProperty().bind(gridHeight);
        gridView.minWidthProperty().bind(gridWidth);
        gridView.maxWidthProperty().bind(gridWidth);

        setCenter(gridView);

        return gridView;//c'etait void j'ai change
    }

}
