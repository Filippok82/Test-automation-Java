package home01;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private List<Door> doors;

    @BeforeEach
    void testCreatingDoor(){
        doors = new ArrayList<>();

        doors.add(new Door(false));
        doors.add(new Door(true));
        doors.add(new Door(false));
    }


    @Test
    void testRoundDoorTrue() {
        //given
        Player player = new Player("Игрок", false);
        Game game = new Game(player,doors);
        //when
        Door door = game.round(1);
        //then
        assertTrue(door.isPrize());

    }

    @Test
    void testRoundDoorFalse() {
        //given
        Player player = new Player("Игрок", false);
        Game game = new Game(player,doors);
        //when
        Door door = game.round(0);
        //then
        assertFalse(door.isPrize());

    }

    @Test
    void testRiskTrue() {
        //given
        Player player = new Player("Игрок", true);
        Game game = new Game(player,doors);
        //when
        Door door = game.round(2);
        //then
        assertTrue(door.isPrize());

    }

    @Test
    void testRiskFalse() {
        //given
        Player player = new Player("Игрок", false);
        Game game = new Game(player,doors);
        //when
        Door door = game.round(1);
        //then
        assertTrue(door.isPrize());

    }

}