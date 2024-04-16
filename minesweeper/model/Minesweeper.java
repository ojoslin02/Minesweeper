package minesweeper.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Minesweeper extends Location {
    public static final char MINE = 'M';
    public static final char COVERED = '-';
    private final int mineCount;
    private GameState gameState = GameState.NOT_STARTED;
    private Set<Location> possibleSelections;
    private List<Location> userSelections;
    private Set<Location> mineLocations;
    private Location[][] board;
    private int moveCount;

    private MinesweeperObserver observer;

    public void register(MinesweeperObserver observer){
        this.observer = observer;
    }

    private void notifyObserver(Location location){
        if(observer != null){
            observer.cellUpdated(location);
        }
    }

    public char getSymbol(Location location) throws MinesweeperException {
        return location.getPiece().getSymbol();
    }

    public Set<Location> getPossibleSelections() {
        return this.possibleSelections;
    }

    public boolean isCovered(Location location) throws MinesweeperException {
        return location.isCovered();
    }

    public Minesweeper(int rows, int cols, int mineCount) {
        super(rows, cols, Artifact.BLANK);
        this.possibleSelections = new HashSet<>();
        this.gameState = GameState.IN_PROGRESS;
        this.board = new Location[rows][cols];
        this.userSelections = new ArrayList<>();
        this.mineLocations = new HashSet<>();
        this.mineCount = mineCount;
        this.moveCount = 0;
        makeBoard(rows, cols);
    }

    public Minesweeper(Minesweeper minesweeper, List<Location> playedLocations) {
        super(minesweeper.board.length, minesweeper.board[0].length, Artifact.BLANK);
        this.possibleSelections = new HashSet<>(minesweeper.possibleSelections);
        this.gameState = minesweeper.gameState;
        this.board = minesweeper.board;
        this.userSelections = playedLocations;
        this.mineLocations = new HashSet<>(minesweeper.mineLocations);
        this.mineCount = minesweeper.mineCount;
        this.moveCount = minesweeper.moveCount;
    }

    private void makeBoard(int rows, int cols){
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                this.board[i][j] = new Location(i, j, Artifact.BLANK);
            }
        }

        for (int i = 0; i < this.mineCount; i++) {
            Random RNG = new Random(); int row = RNG.nextInt(rows); int col = RNG.nextInt(cols);
            Location newLocation = new Location(row, col, Artifact.MINE);
            if(!mineLocations.contains(newLocation)){
                mineLocations.add(newLocation);
                board[row][col].setArtifact(Artifact.MINE);
            }else{
                i--;
            }
        }

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if(this.board[i][j].getPiece() != Artifact.MINE){
                    this.possibleSelections.add(this.board[i][j]);
                    int squareVal = 0;
                    for(int m = 0; m < 8; m++){
                        try{
                            if(m == 0 && this.board[i-1][j-1].getPiece() == Artifact.MINE){
                                squareVal++;
                            }if(m == 1 && this.board[i-1][j].getPiece() == Artifact.MINE){
                                squareVal++;
                            }if(m == 2 && this.board[i-1][j+1].getPiece() == Artifact.MINE){
                                squareVal++;
                            }if(m == 3 && this.board[i+1][j-1].getPiece() == Artifact.MINE){
                                squareVal++;
                            }if(m == 4 && this.board[i+1][j].getPiece() == Artifact.MINE){
                                squareVal++;
                            }if(m == 5 && this.board[i+1][j+1].getPiece() == Artifact.MINE){
                                squareVal++;
                            }if(m == 6 && this.board[i][j-1].getPiece() == Artifact.MINE){
                                squareVal++;
                            }if(m == 7 && this.board[i][j+1].getPiece() == Artifact.MINE){
                                squareVal++;
                            }
                        }catch (IndexOutOfBoundsException e){
                            continue;
                        }
                    }
                    switch(squareVal){
                        case 0:
                            this.board[i][j].setArtifact(Artifact.BLANK);
                            break;
                        case 1:
                            this.board[i][j].setArtifact(Artifact.ONE);
                            break;
                        case 2:
                            this.board[i][j].setArtifact(Artifact.TWO);
                            break;
                        case 3:
                            this.board[i][j].setArtifact(Artifact.THREE);
                            break;
                        case 4:
                            this.board[i][j].setArtifact(Artifact.FOUR);
                            break;
                        case 5:
                            this.board[i][j].setArtifact(Artifact.FIVE);
                            break;
                        case 6:
                            this.board[i][j].setArtifact(Artifact.SIX);
                            break;
                        case 7:
                            this.board[i][j].setArtifact(Artifact.SEVEN);
                            break;
                        case 8:
                            this.board[i][j].setArtifact(Artifact.EIGHT);
                            break;
                    }
                }
            }
        }
    }

    public void makeSelection(int row, int col) throws MinesweeperException {
        Location location = this.board[row][col];
        if(this.gameState != GameState.IN_PROGRESS){
            throw new MinesweeperException("Sorry! The game is already over! You " + this.gameState + "! Please 'reset' or 'quit'.");
        }else{
            if(location.isCovered()){
                if(this.board[location.getRow()][location.getCol()].getPiece() == Artifact.MINE){
                    this.gameState = GameState.LOST;
                    checkGameState();
                }else if(this.board[row][col].getPiece().equals(Artifact.BLANK)){
                    recursiveCallBoard(row, col);
                }else{
                    location.uncover();
                    this.possibleSelections.remove(location);
                    this.userSelections.add(location);
                    this.moveCount++;
                }
                if(this.possibleSelections.size() == 0){
                    this.gameState = GameState.WON;
                    checkGameState();
                }
                notifyObserver(location);
            }else if(!location.isCovered()){
                throw new MinesweeperException("Piece Already Flipped! Try again.");
            }
        }
    }

    private void recursiveCallBoard(int row, int col){
        Location location = this.board[row][col];
        if(location.isCovered()){
            location.uncover();
            notifyObserver(location);
            this.possibleSelections.remove(location);
            this.userSelections.add(location);
            for(int m = 0; m < 8; m++){
                try{
                    switch(m){
                        case 0:
                            checkAround(row-1, col-1);
                        case 1:
                            checkAround(row-1, col);
                        case 2:
                            checkAround(row-1, col+1);
                        case 3:
                            checkAround(row+1, col-1);
                        case 4:
                            checkAround(row+1, col);
                        case 5:
                            checkAround(row+1, col+1);
                        case 6:
                            checkAround(row, col-1);
                        case 7:
                            checkAround(row, col+1);
                    }
                }catch (IndexOutOfBoundsException e){
                    continue;
                }
            }
        }
    }

    private void checkAround(int row, int col){
        Location location = this.board[row][col];
        if(location.isCovered()){
            this.possibleSelections.remove(location);
            this.userSelections.add(location);
            if(this.board[row][col].getPiece() == Artifact.BLANK){
                recursiveCallBoard(row, col);
            }else{
                this.board[row][col].uncover();
            }
            notifyObserver(this.board[row][col]);
        }
    }

    public Location getHint() throws MinesweeperException{
        if(this.gameState == GameState.IN_PROGRESS){
            for(Location location : this.possibleSelections){
                return location;
            }
        }else{
            throw new MinesweeperException("Sorry! The game is already over! You " + this.gameState + "! Please 'reset' or 'quit'.");
        }
        return null;
    }

    public Minesweeper deepCopy() {
        return new Minesweeper(this, this.userSelections);
    }

    public int getMineCount(){
        return this.mineCount;
    }

    public Location[][] getBoard() {
        return this.board;
    }

    public int getMoveCount() {
        return this.moveCount;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    private void checkGameState(){
        if(this.gameState == GameState.WON){
            System.out.println("Congratulations! You won!");
            endGame();
        }else if(this.gameState == GameState.LOST){
            System.out.println("BOOM! Better luck next time!");
            endGame();
        }
    }

    private void endGame(){
        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[i].length; j++) {
                if(this.board[i][j].isCovered()){
                    this.board[i][j].uncover();
                    notifyObserver(this.board[i][j]);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.board.length; i++) {
            for (int n = 0; n < this.board[i].length; n++) {
                if(this.board[i][n].isCovered()){
                    builder.append(COVERED);
                }else{
                    builder.append(this.board[i][n].getPiece().getSymbol());
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}