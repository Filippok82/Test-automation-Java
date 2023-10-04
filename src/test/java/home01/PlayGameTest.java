package home01;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayGameTest {


    @Test
    void testPlayGameName(){
        //given
       Player player = new Player("Иван", true);
       // then
        assertEquals("Иван",player.getName());

    }
    @Test
    void testPlayGameRisk(){
        //given
        Player player = new Player("Иван", true);
        //then
        assertTrue(player.getRisk());











    }
}