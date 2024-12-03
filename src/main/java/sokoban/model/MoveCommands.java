package sokoban.model;


abstract class MoveCommands {
    static class MoveDownCommand implements Command {
        private final Board4Play board4Play;
        private int previousRow;// Garder la position précédente de player pour l'annulation
        private Box movedBox ;

        MoveDownCommand(Board4Play board4Play) {
            this.board4Play = board4Play;
        }

        @Override
        public void execute() {
            previousRow = board4Play.getPlayerLine();
            movedBox = board4Play.moveDown();
        }

        @Override
        public void undo() {
            if(previousRow != board4Play.getPlayerLine()){
                if (movedBox != null){
                    // placer le box dans la cellule qui convient
                    board4Play.play(previousRow+1, board4Play.getPlayerCol(), movedBox);
                    // enlever le box de l'ancienne cellule
                    board4Play.play(previousRow+2, board4Play.getPlayerCol(), movedBox);
                }
                // pour placer le player dans l'ancienne cellule
                    board4Play.moveUp();// 1 mouvement
                // annulation coute 5 mouvements
                board4Play.setNbMoves(board4Play.getNbMoves() + 4);// 4 mouvements
            }
        }
    }

    static class MoveLeftCommand implements Command {
        private final Board4Play board4Play;
        private int previousCol;
        private Box movedBox ;

        MoveLeftCommand(Board4Play board4Play) {
            this.board4Play = board4Play;
        }
        @Override
        public void execute() {
            previousCol = board4Play.getPlayerCol();
            movedBox = board4Play.moveLeft();
        }
        @Override
        public void undo() {
            if(previousCol != board4Play.getPlayerCol()){
                if (movedBox != null){
                    board4Play.play(board4Play.getPlayerLine(), previousCol-1, movedBox);
                    board4Play.play(board4Play.getPlayerLine(), previousCol-2, movedBox);
                }
                board4Play.moveRight();
                board4Play.setNbMoves(board4Play.getNbMoves() + 4);
            }
        }
    }

    static class MoveRightCommand implements Command {
        private final Board4Play board4Play;
        private int previousCol;
        private Box movedBox ;

        MoveRightCommand(Board4Play board4Play) {
            this.board4Play = board4Play;
        }
        @Override
        public void execute() {
            previousCol = board4Play.getPlayerCol();
            movedBox = board4Play.moveRight();
        }
        @Override
        public void undo() {
            if(previousCol != board4Play.getPlayerCol()) {
                if (movedBox != null) {
                    // placer le box dans la cellule qui convient
                    board4Play.play(board4Play.getPlayerLine(), previousCol + 1, movedBox);
                    // enlever le box de l'ancienne cellule
                    board4Play.play(board4Play.getPlayerLine(), previousCol + 2, movedBox);
                }
                board4Play.moveLeft();
                board4Play.setNbMoves(board4Play.getNbMoves() + 4);
            }
        }
    }

    static class MoveUpCommand implements Command {
        private final Board4Play board4Play;
        private int previousRow; // Garder la position précédente pour l'annulation
        private Box movedBox ;
        MoveUpCommand(Board4Play board4Play) {
            this.board4Play = board4Play;
        }
        @Override
        public void execute() {
            // Exécute le mouvement vers le haut
            previousRow = board4Play.getPlayerLine();
            movedBox = board4Play.moveUp();
        }
        @Override
        public void undo() {
            if(previousRow != board4Play.getPlayerLine()) {
                if (movedBox != null) {
                    board4Play.play(previousRow - 1, board4Play.getPlayerCol(), movedBox);
                    board4Play.play(previousRow - 2, board4Play.getPlayerCol(), movedBox);
                }
                board4Play.moveDown();
                //une annulation coute 5 mouvements
                board4Play.setNbMoves(board4Play.getNbMoves() + 4);     // 4 mouvements
            }
        }
    }
}
