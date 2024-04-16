package minesweeper.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import minesweeper.model.Location;

public class MinesweeperMoveMaker implements EventHandler<ActionEvent> {

    private final MinesweeperGUI minesweepergui;
    private final Location location;

    public MinesweeperMoveMaker(MinesweeperGUI minesweepergui, Location location){
        this.minesweepergui = minesweepergui;
        this.location = location;
    }

    @Override
    public void handle(ActionEvent event) {
        minesweepergui.makeMove(location);        
    }    
}
