package sokoban.model;

import java.util.Stack;

class CommandInvoker {
    private final Stack<Command> commandStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>(); // Nouvelle pile pour stocker les commandes annulées

    void executeCommand(Command command) {
        command.execute();
        commandStack.push(command);
        redoStack.clear(); // Lorsqu'une nouvelle commande est exécutée, la pile de commandes annulées est vidée
    }

    void undoLastCommand() {
        if (!commandStack.isEmpty()) {
            Command command = commandStack.pop();
            command.undo();
            redoStack.push(command); // Ajoutez la commande annulée à la pile de commandes pour répéter
        }
    }

    void redoLastCommand() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute(); // Exécute à nouveau la commande annulée
            commandStack.push(command); // Ajoute la commande à la pile de commandes
        }
    }
}

