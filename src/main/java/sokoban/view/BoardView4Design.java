package sokoban.view;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import sokoban.model.*;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.BoardViewModel4Play;

import java.io.File;
import java.io.IOException;
import java.util.*;
public class BoardView4Design extends BorderPane {
    private BoardViewModel4Design boardViewModel4Design;
    private GridView4Design gridView4Design;
    private final Label messageErreur = new Label();
    private final int width;
    private final int height;
    private static final int SCENE_MIN_WIDTH = 600;
    private static final int SCENE_MIN_HEIGHT = 500;
    private final Label headerLabel = new Label("");
    private final HBox buttonBox = new HBox();
    private final Button playButton = new Button("Play");
    private final Button resetButton = new Button("Reset");
    private HBox headerBox;
    private boolean isDragging = true;
    private int lastRowUpdated = -1;
    private int lastColUpdated = -1;
    private boolean isClick; // Ajoutez cette variable pour suivre si c'est un clic
    private ImageView selectedImage;
    public BoardView4Design(Stage primaryStage, BoardViewModel4Design boardViewModel4Design) {
        this.boardViewModel4Design = boardViewModel4Design;
        width = boardViewModel4Design.gridWidth();
        height = boardViewModel4Design.gridHeight();
        primaryStage.setTitle("Sokoban");
        start(primaryStage);
        setupMouseDragBehavior(); // pour le drag un drop
        boardViewModel4Design.updateMessageErreur();
        playButton.disableProperty().bind(boardViewModel4Design.errorsPresent);
    }
    private void setupMouseDragBehavior() {
        setOnMousePressed(this::handleMousePressed);
        setOnMouseDragged(this::handleMouseDragged);
        setOnMouseReleased(this::handleMouseReleased);
    }
    private void handleMousePressed(MouseEvent event) {
        isClick = false; // Considérez initialement chaque pression comme un click
        lastRowUpdated = -1;
        lastColUpdated = -1;
        isDragging = false;
    }
    private void handleMouseDragged(MouseEvent event) {
        if (isClick) {
            // Si la souris a été déplacée, ce n'est plus un clic simple
            isClick = false;
            isDragging = true;
        }
        updateCellValue(BoardViewModel4Design.getGameObjectSelected(),event);
    }
    private void handleMouseReleased(MouseEvent event) {
        isClick = false;
        isDragging = false;
    }
    private void updateCellValue(GameObjects cellValue, MouseEvent event) {
        if (BoardViewModel4Design.getGameObjectSelected().getType().equals(CellValue.WALL) || BoardViewModel4Design.getGameObjectSelected().getType().equals(CellValue.GROUND)){
            Bounds gridBounds = gridView4Design.localToScene(gridView4Design.getBoundsInLocal());
            // Convertissez les coordonnées du MouseEvent en coordonnées locales de GridView4Design
            double mouseX = event.getSceneX() - gridBounds.getMinX();
            double mouseY = event.getSceneY() - gridBounds.getMinY();
            // Calculez les indices de ligne et de colonne en fonction des coordonnées de la souris
            int col = (int) (mouseX / gridView4Design.getCellWidth());
            int row = (int) (mouseY / gridView4Design.getCellHeight());
            // Vérifiez si l'indice est dans la plage valide avant de jouer l'action
            if (col >= 0 && col < width && row >= 0 && row < height) {
                if(row != lastRowUpdated || col != lastColUpdated) {
                    lastRowUpdated = row;
                    lastColUpdated = col;
                    boardViewModel4Design.play(row, col, cellValue.getType());
                }
            }
        }
    }
    private void start(Stage stage) {
        configMainComponents(stage);
        this.gridView4Design = createGrid();
        // Mise en place de la scène et affichage de la fenêtre
        Scene scene = new Scene(this, SCENE_MIN_WIDTH, SCENE_MIN_HEIGHT);
        String cssFile = Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm();
        scene.getStylesheets().add(cssFile);
        stage.setScene(scene);

        // Mettez en place la barre de boutons en bas avec une marge pour éviter le chevauchement
        HBox bottomContainer = new HBox(); // Espace entre les composants de VBox
        bottomContainer.setAlignment(Pos.CENTER);
        bottomContainer.getChildren().add(buttonBox);
        VBox.setMargin(buttonBox, new Insets(10, 0, 10, 0));

        // Ajoutez bottomContainer au BorderPane
        setBottom(bottomContainer);

        stage.show();

        // Ajustez la hauteur minimale pour le stage en tenant compte des boutons
        stage.setMinHeight(stage.getHeight() + bottomContainer.getHeight());
        stage.setMinWidth(stage.getWidth());

        setupMouseDragBehavior();
    }
    private void configMainComponents(Stage stage) {
        boardViewModel4Design.boardChangedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
                stage.setTitle("Sokoban(*)");
            else{
                stage.setTitle("Sokoban");

            }
        });

        createHeader();
        //createGrid();
        createSideBar();
        buttonBox.getChildren().add(resetButton);
        buttonBox.getChildren().add(playButton);
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);
        setBottom(buttonBox);
        playButton.setOnAction(event ->  playButton());
        resetButton.setOnAction(event ->  boardViewModel4Design.resetBoard());
    }
    private MenuBar createMenuBar() {
        // Création des menus
        Menu fileMenu = new Menu("File");
        // Création des items de menu
        MenuItem newItem = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save As");
        MenuItem exitItem = new MenuItem("Exit");
        // Ajout d'un gestionnaire d'événements à l'élément de menu "Open"
        newItem.setOnAction(event -> newFile());
        openItem.setOnAction(event -> openFile());
        // Ajout d'un gestionnaire d'événements à l'élément de menu "Save As"
        saveItem.setOnAction(event -> saveFile());
        // Ajout d'un gestionnaire d'événements à l'élément de menu "Exit"
        exitItem.setOnAction(event -> Platform.exit());
        // Ajout des items au menu File
        fileMenu.getItems().addAll(newItem, openItem, saveItem, exitItem);
        // Ajout des menus à la barre de menu
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu);
        return menuBar;
    }
    private void displaySaveAlert() {
        if (boardViewModel4Design.boardChangedProperty().get()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save Changes");
            alert.setHeaderText("Do you want to save changes to the current grid?");
            alert.setContentText("Choose your option.");
            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonNo = new ButtonType("No");
            ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonYes, buttonNo, buttonCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonYes) {
                // Méthode pour sauvegarder
                saveFile();
            }
        }
    }
    private void newFile() {
        displaySaveAlert();
        newGridDimensions(); // Créez une nouvelle grille
    }
    private void playButton() {
        displaySaveAlert();

        Stage stage = (Stage) this.getScene().getWindow();
        new BoardView4Play(stage, new BoardViewModel4Play(boardViewModel4Design.getBoard()), boardViewModel4Design);
    }
    // Définition d'une méthode pour configurer les nouvelles dimensions d'une grille pour un jeu.
    private void newGridDimensions() {
        // Création d'une nouvelle fenêtre de dialogue où l'utilisateur peut entrer les dimensions.
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Sokoban");  // Définition du titre de la fenêtre de dialogue.
        dialog.setHeaderText("Give new game dimensions");  // Définition du texte d'en-tête.

        // Création d'un GridPane pour organiser les étiquettes et les champs de texte.
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);  // Définition de l'écart horizontal entre les colonnes.
        gridPane.setVgap(10);  // Définition de l'écart vertical entre les rangées.
        gridPane.setPadding(new Insets(20, 150, 10, 10));  // Définition des marges autour de la grille.

        // Création d'un champ de texte pour saisir la largeur, en acceptant seulement les entrées numériques.
        TextField widthField = new TextField() {
            @Override public void replaceText(int start, int end, String text) {
                // Vérification si le texte entré est numérique avant de le remplacer.
                if (text.matches("[0-9]*")) {
                    super.replaceText(start, end, text);
                }
            }
            @Override public void replaceSelection(String text) {
                // Vérification si le texte sélectionné est numérique avant de le remplacer.
                if (text.matches("[0-9]*")) {
                    super.replaceSelection(text);
                }
            }
        };
        widthField.setPromptText("Width");  // Définition du texte indicatif pour la largeur.

        // Création d'un champ de texte similaire pour la hauteur.
        TextField heightField = new TextField() {
            @Override public void replaceText(int start, int end, String text) {
                if (text.matches("[0-9]*")) {
                    super.replaceText(start, end, text);
                }
            }
            @Override public void replaceSelection(String text) {
                if (text.matches("[0-9]*")) {
                    super.replaceSelection(text);
                }
            }
        };
        heightField.setPromptText("Height");  // Définition du texte indicatif pour la hauteur.

        // Création d'étiquettes d'erreur pour les champs de largeur et de hauteur.
        Label widthError = createErrorLabel("Width must be at least 10.");
        Label heightError = createErrorLabel("Height must be at most 50.");

        // Ajout des composants dans le GridPane.
        gridPane.add(new Label("Width:"), 0, 0);  // Ajout de l'étiquette pour la largeur.
        gridPane.add(widthField, 1, 0);           // Ajout du champ de texte pour la largeur.
        gridPane.add(widthError, 1, 1);           // Ajout de l'étiquette d'erreur pour la largeur.
        gridPane.add(new Label("Height:"), 0, 2); // Ajout de l'étiquette pour la hauteur.
        gridPane.add(heightField, 1, 2);          // Ajout du champ de texte pour la hauteur.
        gridPane.add(heightError, 1, 3);          // Ajout de l'étiquette d'erreur pour la hauteur.

        // Création des boutons pour le dialogue.
        Node okButton = createDialogButtons(dialog);

        // Ajout de validateurs pour s'assurer que les entrées sont valides avant de permettre la soumission.
        widthField.textProperty().addListener((observable, oldValue, newValue) ->
                validateDimensionInput(newValue, widthError, "Width must be at least 10.", okButton, heightField)
        );
        heightField.textProperty().addListener((observable, oldValue, newValue) ->
                validateDimensionInput(newValue, heightError, "Height must be at most 50.", okButton, widthField)
        );

        // Définition du contenu du panneau de dialogue.
        dialog.getDialogPane().setContent(gridPane);
        Platform.runLater(widthField::requestFocus);  // Mise en focus du champ de la largeur à l'ouverture du dialogue.

        // Conversion du résultat du dialogue en une paire de dimensions.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));
            }
            return null;
        });

        // Affichage du dialogue et attente de l'entrée de l'utilisateur.
        Optional<Pair<Integer, Integer>> result = dialog.showAndWait();
        result.ifPresent(dimensions -> {
            int width = Integer.parseInt(widthField.getText());
            int height = Integer.parseInt(heightField.getText());

            // Mise à jour de la vue avec les nouvelles dimensions.
            Stage oldStage = (Stage) this.getScene().getWindow();
            new BoardView4Design(oldStage, new BoardViewModel4Design(height, width));
        });
    }

    private Label createErrorLabel(String errorMessage) {
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        return errorLabel;
    }
    private Node createDialogButtons(Dialog<Pair<Integer, Integer>> dialog) {
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK
        );
        okButton.setDisable(true);
        return okButton;
    }
    private void validateDimensionInput(String newValue, Label errorLabel, String errorMessage, Node okButton, TextField otherField) {
        if (!newValue.isEmpty()) {
            try {
                int value = Integer.parseInt(newValue);
                boolean isValid = value >= 10 && value <= 50;
                errorLabel.setVisible(!isValid);
                errorLabel.setText(isValid ? "" : errorMessage);
                okButton.setDisable(!isValid || otherField.getText().isEmpty());
            } catch (NumberFormatException e) {
                errorLabel.setVisible(true);
                errorLabel.setText(errorMessage);
                okButton.setDisable(true);
            }
        } else {
            errorLabel.setVisible(false);
            okButton.setDisable(otherField.getText().isEmpty());
        }
    }
    private void saveFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer sous");
        // Définir les filtres de fichiers si nécessaire
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.xsb")
        );
        fileChooser.setInitialFileName("filename.xsb"); // Nom de fichier initial avec l'extension .xsb
        // Afficher la boîte de dialogue de sélection de fichier pour enregistrer
        File selectedFile = fileChooser.showSaveDialog(getScene().getWindow());
        // Traiter le fichier sélectionné (par exemple, enregistrer le contenu dans le fichier)
        if (selectedFile != null) {
            boardViewModel4Design.saveGrid(selectedFile);
        }
    }
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Ouvrir");
        // Définir les filtres de fichiers
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.xsb")
        );
        // Afficher la boîte de dialogue de sélection de fichier
        File selectedFile = fileChooser.showOpenDialog(getScene().getWindow());
        // Traiter le fichier sélectionné
        if (selectedFile != null) {
            try (Scanner scanner = new Scanner(selectedFile)) {
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

                this.boardViewModel4Design = new BoardViewModel4Design(gridHeight, gridWidth);
                Stage oldStage = (Stage) this.getScene().getWindow();
                new BoardView4Design(oldStage, boardViewModel4Design);
                
                // Remplir la grille avec les valeurs lues du fichier
                boardViewModel4Design.resetBoard();
                boardViewModel4Design.openFile(selectedFile);

            } catch (IOException e) {
                System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
            }

        }
    }
    private VBox createHeader() {
        headerBox = new HBox();
        headerLabel.textProperty().bind(boardViewModel4Design.filledCellsCountProperty()
                //le %d c'est l'entier qui est dans filledCellsCountProperty()
                .asString("Number of filled cells: %d of " + boardViewModel4Design.maxFilledCells()));
        headerLabel.getStyleClass().add("header");
        headerBox.getChildren().add(headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        // Configuration du label de validation
        messageErreur.textProperty().bind(boardViewModel4Design.messageErreurProperty());
        messageErreur.visibleProperty().bind(boardViewModel4Design.messageErreurProperty().isNotEmpty());
        messageErreur.getStyleClass().add("errorStyle");
        VBox header = new VBox(createMenuBar(),headerBox, messageErreur);
        header.setAlignment(Pos.CENTER);
        this.setTop(header);
        return header;
    }
    private void createSideBar() {
        VBox sideBar = new VBox();
        ImageView groundImage = new ImageView(new Image("ground.png"));
        ImageView goalImage = new ImageView(new Image("goal.png"));
        ImageView wallImage = new ImageView(new Image("wall.png"));
        ImageView playerImage = new ImageView(new Image("player.png"));
        ImageView boxImage = new ImageView(new Image("box.png"));
        groundImage.setOnMouseClicked(event -> {
            setGameObject(CellValue.GROUND);
            selectImage(groundImage); // effet de selection
        });
        goalImage.setOnMouseClicked(event -> {
            setGameObject(CellValue.GOAL);
            selectImage(goalImage); // effet de selection
        });
        wallImage.setOnMouseClicked(event -> {
            setGameObject(CellValue.WALL);
            selectImage(wallImage); // effet de selection
        });
        playerImage.setOnMouseClicked(event -> {
            setGameObject(CellValue.PLAYER);
            selectImage(playerImage); // effet de selection
        });
        boxImage.setOnMouseClicked(event -> {
            setGameObject(CellValue.BOX);
            selectImage(boxImage);// effet de selection
        });
        sideBar.getChildren().addAll(groundImage, goalImage, wallImage, playerImage, boxImage);
        //sideBar.setAlignment(Pos.CENTER);
        setLeft(sideBar);
    }
    private void selectImage(ImageView image) {
        // Effacer l'effet de l'ancienne image sélectionnée
        if (selectedImage != null) {
            selectedImage.setStyle("");
        }
        // Appliquer un effet à la nouvelle image
        image.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,255,0.8), 25, 0, 0, 0);");
        // Mettre à jour l'image sélectionnée
        selectedImage = image;
    }
    private void setGameObject(CellValue gameObject) {
        BoardViewModel4Design.setGameObjectSelected(gameObject);
    }
    private GridView4Design createGrid() {
        // Calcul de la largeur et la hauteur disponible en tenant compte de l'espace occupé par les autres composants
        DoubleBinding availableWidth = widthProperty().subtract(new SimpleDoubleProperty(20)); // Marge
        DoubleBinding availableHeight = heightProperty().subtract(headerBox.heightProperty().add(buttonBox.heightProperty())); // Hauteur des autres éléments

        DoubleBinding gridWidth = Bindings.createDoubleBinding(
                () -> {
                    double size = Math.min(availableWidth.get(), availableHeight.get());
                    return Math.floor(size / width) * width;
                },
                availableWidth,
                availableHeight,
                headerBox.heightProperty(),
                buttonBox.heightProperty());

        DoubleBinding gridHeight = Bindings.createDoubleBinding(() -> {
            var size = Math.min(widthProperty().get(), heightProperty().get() - headerBox.heightProperty().get() - buttonBox.heightProperty().get());
            return Math.floor(size / height) * height;
        }, widthProperty(), heightProperty(), headerBox.heightProperty(), buttonBox.heightProperty());


        GridView4Design gridView4Design = new GridView4Design(boardViewModel4Design.getGridViewModel(), gridWidth, gridHeight);
        gridView4Design.maxHeightProperty().bind(gridHeight);
        gridView4Design.minWidthProperty().bind(gridWidth);
        gridView4Design.maxWidthProperty().bind(gridWidth);
        setCenter(gridView4Design);
        return gridView4Design;
    }

}