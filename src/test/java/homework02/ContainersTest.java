package homework02;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainersTest {

    @Test
    void creatingElement() {
        //given
        Elements element = new Elements("Арбуз",3000,600.0);

        assertEquals("Арбуз",element.name);
        assertEquals(3000,element.weight);
    }

    @Test
    void creatingContainer() {
        //given
        Containers containers = new Containers("Ящик");
        //then
        assertEquals("Ящик",containers.name);

    }

    @Test
    void sumWeight() {
        //given
        Elements element = new Elements("Арбуз",3000,600.0);
        Elements element1 = new Elements("Дыня",2000,900.0);
        Containers containers = new Containers("Ящик");
        //when
        containers.addComponent(element);
        containers.addComponent(element1);
        //then
        assertEquals(5000,containers.sumWeight());



    }
}