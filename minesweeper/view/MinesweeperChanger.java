package minesweeper.view;

import minesweeper.model.Location;
import minesweeper.model.MinesweeperObserver;

public class MinesweeperChanger implements MinesweeperObserver{
    
    private final MinesweeperGUI minesweepergui;

    public MinesweeperChanger(MinesweeperGUI minesweepergui){
        this.minesweepergui = minesweepergui;
    }

    @Override
    public void cellUpdated(Location location) {
        minesweepergui.updateCell(location);
    }
}
