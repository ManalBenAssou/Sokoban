package sokoban.model;

interface Command {
    void execute();
    void undo();

}
