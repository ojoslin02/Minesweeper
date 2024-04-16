package minesweeper.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.platform.commons.annotation.Testable;

import minesweeper.model.Artifact;
import minesweeper.model.Location;

@Testable
public class LocationTest {
    @Test
    public void tequals() {
        int row= 1;
        int col= 2;
        Location locationA = new Location(row, col, Artifact.BLANK);
        Location locationB = new Location(row, col, Artifact.BLANK);

        Boolean expected = locationA.equals(locationB);
        Boolean actual = true;

        assertEquals(expected, actual);
    }

    @Test
    public void thashcode() {
        int row= 1;
        int col= 2;
        Location locationA= new Location(row, col, Artifact.BLANK);

        int expected= locationA.hashCode();
        int actual= 1191761716;     //"(1, 2).hashcode()"

        assertEquals(expected, actual);
    }
}
