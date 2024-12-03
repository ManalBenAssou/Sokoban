package sokoban.view;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sokoban.model.CellValue;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import sokoban.model.GameObjects;
import sokoban.model.Mushroom;
import sokoban.viewmodel.CellViewModel4Play;

class CellView4Play extends StackPane {
    private static final Image playerImage = new Image("player.png");
    private static final Image boxImage = new Image("box.png");
    private static final Image goalImage = new Image("goal.png");
    private static final Image groundImage = new Image("ground.png");
    private static final Image wallImage = new Image("wall.png");
    private static final Image mushroomImage = new Image("mushroom.png");
    private final CellViewModel4Play viewModel;
    private final DoubleBinding cellWidth;
    private final DoubleBinding cellHeight;
    private final ImageView backgroundImageView = new ImageView(groundImage); // Pour l'image de fond
    private final ImageView imageView = new ImageView();
    // Effet pour assombrir l'image lorsque la souris survole la cellule
    private final ColorAdjust darkenEffect = new ColorAdjust();

    CellView4Play(CellViewModel4Play cellViewModel, DoubleBinding cellWidth, DoubleBinding cellHeight) {
        this.viewModel = cellViewModel;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;

        setAlignment(Pos.CENTER);

        layoutControls();
        configureBindings();
    }
    private void layoutControls() {
        backgroundImageView.setPreserveRatio(false);
        backgroundImageView.setSmooth(true);

        // Assume that the ground image is the default background.
        backgroundImageView.setImage(groundImage);

        imageView.setPreserveRatio(true);

        // Ajouter le backgroundImageView en tant qu'enfant de la StackPane
        getChildren().add(backgroundImageView);

        // Écouter les changements sur la valeur de la cellule pour mettre à jour la vue
        viewModel.getValues().addListener((obs, oldVal, newVal) -> updateView(newVal));

        //remplir la cellule avec les valeurs de cellDesign
        updateView(viewModel.getValues());
    }
    private void addImageView(Image image) {
        ImageView imageView = new ImageView(image);
        configureImageView(imageView);
        getChildren().add(imageView);
    }

    public void addBoxView(int boxId) { // new one
        ImageView boxImageView = new ImageView(boxImage);
        configureImageView(boxImageView); // Configurez les propriétés de taille de l'image.
        //boxNumber = getBoxNumber(boxNumber);

        // Créez un label avec le numéro de la boîte.
        Label numberLabel = new Label(String.valueOf(boxId));
        numberLabel.setAlignment(Pos.CENTER);

        // Appliquez le style CSS pour la bordure et le numéro à l'intérieur de la boîte.
        numberLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");

        // Créez un rectangle pour la bordure blanche.
        Rectangle borderRectangle = new Rectangle();
        borderRectangle.setFill(Color.TRANSPARENT);
        borderRectangle.setStroke(Color.WHITE);
        borderRectangle.setStrokeWidth(10);
        borderRectangle.setHeight(10);// Épaisseur de la bordure.

        // Créez une StackPane pour superposer le rectangle et l'image de la boîte avec le numéro.
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(boxImageView, borderRectangle, numberLabel);
        configureImageView(stackPane);

        getChildren().add(stackPane);
    }


    private void configureImageView(ImageView imageView) { // pour les autres objets
        imageView.fitWidthProperty().bind(cellWidth);
        imageView.fitHeightProperty().bind(cellHeight);
        imageView.setPreserveRatio(true);
    }

    private void configureImageView(StackPane stackPane) { //pour les boxs
        stackPane.maxWidthProperty().bind(cellWidth);
        stackPane.maxHeightProperty().bind(cellHeight);
        stackPane.setAlignment(Pos.CENTER);
    }
    public void updateView(ObservableList<GameObjects> list) {
        getChildren().clear();
        getChildren().add(backgroundImageView); // Ajoute toujours l'arrière-plan.
        for (GameObjects value : list) {

            if (value.getType() == CellValue.BOX) {
                addBoxView(value.getId());
            } else {
                addImageViewForCellValue(value);
            }
        }
    }

    private void addImageViewForCellValue(GameObjects cellValue) { //new one
        Image image = switch (cellValue.getType()) {
            case PLAYER -> playerImage;
            case WALL -> wallImage;
            case GOAL -> goalImage;
            case MUSHROOM -> mushroomImage;
            default -> null; // Les boîtes sont gérées séparément, donc retourne null ici.
        };

        if (image != null) {
            addImageView(image);
        }
    }
    private void configureBindings() {
        backgroundImageView.fitWidthProperty().bind(cellWidth);
        backgroundImageView.fitHeightProperty().bind(cellHeight);
        minWidthProperty().bind(cellWidth);
        minHeightProperty().bind(cellHeight);

        this.setOnMouseClicked(e -> {
            if (viewModel.getValues().contains(new Mushroom())) {
                viewModel.moveMushroom();
            }
        });


        // Lier l'effet d'assombrissement à la propriété hover de la cellule
        hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                darkenEffect.setBrightness(-0.1); // Assombrir l'image
                backgroundImageView.setEffect(darkenEffect);
                imageView.setEffect(darkenEffect);
            } else {
                darkenEffect.setBrightness(0); // Retour à la normale
                backgroundImageView.setEffect(null);
                imageView.setEffect(null);
            }
        });
    }


}
