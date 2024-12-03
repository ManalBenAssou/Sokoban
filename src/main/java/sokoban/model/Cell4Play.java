package sokoban.model;

import javafx.beans.property.*;

class Cell4Play extends Cell {

    public Cell4Play(Cell4Design cell4Design){
        //parcourir la liste et faire une copie de chaque GameObject
        for (GameObjects value : cell4Design.gameObjects) {
            switch (value.getType()) {
                case PLAYER -> gameObjects.add(new Player());
                case BOX -> gameObjects.add(new Box());
                case GOAL -> gameObjects.add(new Goal());
                case WALL -> gameObjects.add(new Wall());
                case GROUND -> gameObjects.add(new Ground());

                default -> gameObjects.add(new Ground());
            }
        }
    }

    public Box getBox() {
        for (GameObjects gameObject : gameObjects) {
            if (gameObject instanceof Box) {
                return (Box) gameObject;
            }
        }
        return null;
    }

    @Override
    void addValue(GameObjects value){
        if (value.getType() == CellValue.BOX || value.getType() == CellValue.PLAYER) {
            if (gameObjects.contains(new Goal())) {
                gameObjects.clear();
                gameObjects.add(value);
                gameObjects.add(new Goal());
            } else {
                gameObjects.clear();
                gameObjects.add(value);
            }
        }
        else if(value.getType() == CellValue.MUSHROOM) {
            gameObjects.clear();
            gameObjects.add(value);
        }
        else if(value.getType() == CellValue.GROUND) {
            gameObjects.clear();
            //gameObjects.add(new Mushroom());
            gameObjects.add(value);
        }
    }
}
