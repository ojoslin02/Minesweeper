package minesweeper.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;
import minesweeper.model.GameState;
import minesweeper.model.Minesweeper;
import minesweeper.model.MinesweeperException;

@Testable
public class MinesweeperTest {
    @Test
    public void tmakeselection() throws MinesweeperException {
        Minesweeper minesweeper = new Minesweeper(10, 10, 10);
        int row = 5; int col = 5;
        // Location locationB= new Location(-1, 11);

        minesweeper.makeSelection(row, col);
        int actual = minesweeper.getMoveCount();
        int expected = 1;

        assertEquals(expected, actual);
    }

    @Test
    public void tendGame() throws MinesweeperException {
        Minesweeper m = new Minesweeper(1, 1, 1);
        int row = 0; int col = 0;
        m.makeSelection(row, col);
        assertEquals(GameState.LOST, m.getGameState());
    }
}
