package com.udacity;

import java.util.Arrays;

/**
 * Created by udacity 2016
 * The Main class containing game logic and backend 2D array
 */
public class Game {

    private char turn; // who's turn is it, 'x' or 'o' ? x always starts A qui appartient le tour, à "x" ou à "o" ? x commence toujours.
    private boolean twoPlayer; // true s'il s'agit d'un jeu à 2 joueurs, false s'il s'agit d'un jeu avec l'IA
    private char [][] grid; // un tableau 2D de caractères représentant la grille de jeu
    private int freeSpots; // compte le nombre d'emplacements vides restants sur le tableau (à partir de 9 et en comptant à rebours)
    private static GameUI gui;

    /**
     * Create a new single player game
     *
     */
    public Game() {
        newGame(false);
    }

    /**
     * Create a new game by clearing the 2D grid and restarting the freeSpots counter and setting the turn to x
     * @param twoPlayer: true if this is a 2 player game, false if playing against the computer
     */
    public void newGame(boolean twoPlayer){
        //définit un jeu à un ou deux joueurs
        this.twoPlayer = twoPlayer;

        // Initialiser tous les caractères de la grille de jeu 3x3 à '-'.
        grid = new char[3][3];
        //remplir tous les emplacements vides avec -
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                grid[i][j] = '-';
            }
        }
        //commence avec 9 places libres et décrémente d'une place chaque fois qu'une place est prise
        freeSpots = 9;
        //x commence toujours
        turn = 'x';
    }


    /**
     * Gets the char value at that particular position in the grid array
     * @param i the x index of the 2D array grid
     * @param j the y index of the 2D array grid
     * @return the char value at the position (i,j):
     *          'x' if x has played here
     *          'o' if o has played here
     *          '-' if no one has played here
     *          '!' if i or j is out of bounds
     */
    public char gridAt(int i, int j){
        if(i>=3||j>=3||i<0||j<0)
            return '!';
        return grid[i][j];
    }

    /**
     * Places current player's char at position (i,j)
     * Uses the variable turn to decide what char to use
     * @param i the x index of the 2D array grid
     * @param j the y index of the 2D array grid
     * @return boolean: true if play was successful, false if invalid play
     */
    public boolean playAt(int i, int j){
        //check for index boundries vérification des limites de l'index
        if(i>=3||j>=3||i<0||j<0)
            return false;
        //vérifier si ce poste est disponible
        if(grid[i][j] != '-'){
            return false; //renflouement en cas d'indisponibilité
        }
        //mettre à jour la grille avec les nouveaux jeux en fonction du tour de chacun
        grid[i][j] = turn;
        //mettre à jour les emplacements libres
        freeSpots--;
        return true;
    }


    /**
     * Override
     * @return string format for 2D array values
     */
    public String toString(){
        return Arrays.deepToString(this.grid);

    }

    /**
     * Performs the winner chack and displayes a message if game is over
     * @return true if game is over to start a new game
     */
    public boolean doChecks() {
        //check if there's a winner or tie ?
        String winnerMessage = checkGameWinner(grid);
        if (!winnerMessage.equals("None")) {
            gui.gameOver(winnerMessage);
            newGame(false);
            return true;
        }
        return false;
    }

    /**
     * Permet à l'ordinateur de jouer dans une partie à un joueur ou de changer de tour pour une partie à deux joueurs
     */
    public void nextTurn(){
        //vérifier si le jeu se déroule à un seul joueur, puis laisser l'ordinateur jouer le tour
        if(!twoPlayer){
            if(freeSpots == 0){
                return ; //renflouement s'il n'y a plus de places libres
            }
            int ai_i, ai_j;
            do {
                //randomly pick a position (ai_i,ai_j)
                ai_i = (int) (Math.random() * 3);
                ai_j = (int) (Math.random() * 3);
            }while(grid[ai_i][ai_j] != '-'); //continuer à essayer si cet endroit a été pris
            //mettre à jour la grille avec les nouveaux jeux, l'ordinateur est toujours en marche.
            grid[ai_i][ai_j] = 'o';
            //mettre à jour les emplacements libres
            freeSpots--;
        }
        else{
            //changer de direction
            if(turn == 'x'){
                turn = 'o';
            }
            else{
                turn = 'x';
            }
        }
        return;
    }


    /**
     * Checks if the game has ended either because a player has won, or if the game has ended as a tie.
     * If game hasn't ended the return string has to be "None",
     * If the game ends as tie, the return string has to be "Tie",
     * If the game ends because there's a winner, it should return "X wins" or "O wins" accordingly
     * @param grid 2D array of characters representing the game board
     * @return String indicating the outcome of the game: "X wins" or "O wins" or "Tie" or "None"
     */
    public String checkGameWinner(char [][]grid){
        String result = "None";

        // Check for horizontal wins
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2]) {
                if (grid[i][0] == 'X') {
                    return "X wins";
                } else if (grid[i][0] == 'O') {
                    return "O wins";
                }
            }
        }

        // Check for vertical wins
        for (int j = 0; j < 3; j++) {
            if (grid[0][j] == grid[1][j] && grid[1][j] == grid[2][j]) {
                if (grid[0][j] == 'X') {
                    return "X wins";
                } else if (grid[0][j] == 'O') {
                    return "O wins";
                }
            }
        }

        // Check for diagonal wins
        if ((grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) ||
                (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0])) {
            if (grid[1][1] == 'X') {
                return "X wins";
            } else if (grid[1][1] == 'O') {
                return "O wins";
            }
        }

        // Check for tie
        boolean emptyCellsExist = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == ' ') {
                    emptyCellsExist = true;
                    break;
                }
            }
        }


        return result;
    }

    /**
     * Main function
     * @param args command line arguments
     */
    public static void main(String args[]){
        Game game = new Game();
        gui = new GameUI(game);
    }

}
