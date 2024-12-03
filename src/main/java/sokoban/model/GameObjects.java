package sokoban.model;

import java.util.Objects;

public abstract class GameObjects {
    public void setId(int id) {
        this.id = id;
    }

    protected int id;
    private final CellValue type ;

    public int getId() {
        return id;
    }

    public GameObjects(CellValue type) {
        this.type = type;
    }
    public CellValue getType() {
        return type;
    }
    public static GameObjects getGameObject(CellValue cellValue){
        GameObjects g ;
        switch (cellValue){
            case GROUND -> g = new Ground();
            case BOX -> g = new Box();
            case GOAL -> g = new Goal();
            case WALL -> g = new Wall();
            case PLAYER -> g = new Player();
            default -> g =new Ground();
        }
        return g;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameObjects that = (GameObjects) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }









}
