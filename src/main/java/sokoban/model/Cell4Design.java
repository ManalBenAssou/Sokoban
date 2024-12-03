package sokoban.model;

class Cell4Design extends Cell {
    public Cell4Design(){
    }
    protected void addValue(GameObjects value){

        if (value.getType() == CellValue.GROUND || value.getType() == CellValue.WALL) {
            gameObjects.clear();
            gameObjects.add(value);
        } else if (value.getType() == CellValue.BOX || value.getType() == CellValue.PLAYER) {
            if (gameObjects.contains(new Goal())) {
                gameObjects.clear();
                gameObjects.add(value);
                gameObjects.add(new Goal());
            } else {
                gameObjects.clear();
                gameObjects.add(value);
            }
        } else if (value.getType() == CellValue.GOAL) {
            if (gameObjects.contains(new Box()) || gameObjects.contains(new Player()) || gameObjects.isEmpty()) {
                gameObjects.add(value);
            } else {
                gameObjects.clear();
                gameObjects.add(value);
            }
        }
    }
}
