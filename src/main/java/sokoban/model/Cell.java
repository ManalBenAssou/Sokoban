package sokoban.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;


abstract class Cell {
    protected final ListProperty<GameObjects> gameObjects = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected ReadOnlyListProperty<GameObjects> getValues() {
        return gameObjects;
    }
    protected void removeValue(GameObjects value){
        gameObjects.remove(value);
    }
    abstract void addValue(GameObjects value);
    void play(GameObjects value) {
        if (gameObjects.contains(value)) {
            removeValue(value);
        }
        else {
            addValue(value);
        }
    }
    boolean isEmpty () {
        return gameObjects.isEmpty() || (gameObjects.contains(new Ground()) && gameObjects.size() == 1);
    }
}
