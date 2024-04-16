package minesweeper.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import backtracker.Configuration;

public class MinesweeperSolver implements Configuration {
    
    private Minesweeper minesweeper;
    private List<Location> playedLocations;
    private final int n;

    public MinesweeperSolver(Minesweeper minesweeper, List<Location> playedLocations) {
        this.minesweeper = minesweeper;
        this.playedLocations = playedLocations;
        this.n = (minesweeper.getBoard().length * minesweeper.getBoard()[0].length) - minesweeper.getMineCount();
    }

    public List<Location> getPlayedLocations(){
        return this.playedLocations;
    }

    public boolean isValid() {
        int length = playedLocations.size();
        return (playedLocations.get(length-1).getPiece() != Artifact.MINE) && !playedLocations.get(length-1).isCovered();
    }

    public boolean isGoal() {
        return playedLocations.size() == n && isValid();
    }

    public Collection<Configuration> getSuccessors() {
        List<Configuration> successors = new ArrayList<>();        
        for(int i = 0; i < minesweeper.getBoard().length; i++){
            for(int j = 0; j < minesweeper.getBoard()[i].length; j++){
                Minesweeper deepCopy = minesweeper.deepCopy();
                try {
                    deepCopy.makeSelection(i, j);
                } catch (MinesweeperException e) {
                    // squash
                }
                playedLocations.add(new Location(i, j, Artifact.BLANK));
                MinesweeperSolver sucessor = new MinesweeperSolver(deepCopy, playedLocations);
                successors.add(sucessor);
            }
        }
        return successors;
    }

    @Override
    public String toString() {
        return Arrays.toString(playedLocations.toArray()) + "\n" + minesweeper;
    }
}
