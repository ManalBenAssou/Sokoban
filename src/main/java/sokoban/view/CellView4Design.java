package sokoban.view;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseButton;
import sokoban.model.CellValue;
import sokoban.model.GameObjects;
import sokoban.viewmodel.BoardViewModel4Design;
import sokoban.viewmodel.CellViewModel4Design;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import static sokoban.model.CellValue.PLAYER;

class CellView4Design extends StackPane {

    private static final Image playerImage = new Image("player.png");
    private static final Image boxImage = new Image("box.png");
    private static final Image goalImage = new Image("goal.png");
    private static final Image groundImage = new Image("ground.png");
    private static final Image wallImage = new Image("wall.png");
    private final CellViewModel4Design viewModel;
    private final DoubleBinding cellWidth;
    private final DoubleBinding cellHeight;
    private final ImageView backgroundImageView = new ImageView(groundImage); // Pour l'image de fond
    private final ImageView imageView = new ImageView();
    // Effet pour assombrir l'image lorsque la souris survole la cellule
    private final ColorAdjust darkenEffect = new ColorAdjust();

    CellView4Design(CellViewModel4Design cellViewModel4Design, DoubleBinding cellWidth, DoubleBinding cellHeight) {
        this.viewModel = cellViewModel4Design;
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

        //remplir la cellule avec les valeurs si y en a
        updateView(viewModel.getValues());

    }
    private void updateView(ObservableList<GameObjects> list) {
        getChildren().clear();
        getChildren().add(backgroundImageView); // Toujours ajouter le fond.
        for(GameObjects value :list){
            addImageViewForCellValue(value); // Gérer l'affichage basé sur l'état de la cellule.
        }
    }
    private void addImageViewForCellValue(GameObjects cellValue) {
        Image image = switch (cellValue.getType()) {
            case PLAYER -> playerImage;
            case BOX -> boxImage;
            case WALL -> wallImage;
            case GOAL -> goalImage;
            default -> null;
        };
        if (image != null) {
            addImageView(image);
        }
    }
    private void addImageView(Image image) {
        ImageView imageView = new ImageView(image);
        configureImageView(imageView);
        getChildren().add(imageView);
    }
    private void configureImageView(ImageView imageView) {
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.fitHeightProperty().bind(this.heightProperty());
        imageView.setPreserveRatio(true);
    }
    private void configureBindings() {
        backgroundImageView.fitWidthProperty().bind(cellWidth);
        backgroundImageView.fitHeightProperty().bind(cellHeight);
        minWidthProperty().bind(cellWidth);
        minHeightProperty().bind(cellHeight);

        // un clic sur la cellule permet de jouer celle-ci
        //this.setOnMouseClicked(e -> viewModel.play());
        this.setOnMouseClicked(event -> {
            //supprimer avec un clic droit
            if (event.getButton() == MouseButton.SECONDARY) { // clic droit de la sourie
                viewModel.removeValue();
            }
            viewModel.play();
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
