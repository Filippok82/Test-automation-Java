package lesson01_hw;


import org.junit.jupiter.api.Test;


import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.*;

class TriangleTest {
    @Test
    void testCheckingSquare() {
        //given
        Triangle triangle = new Triangle(3, 4, 5);//6.0
        //then
        assertEquals(6.0, triangle.square());
    }

    @Test
    void testNotTriangle() {
        //given
        Triangle triangle = new Triangle(4, 1, 7);
        //then
        assertEquals(NaN, triangle.square());
    }


}