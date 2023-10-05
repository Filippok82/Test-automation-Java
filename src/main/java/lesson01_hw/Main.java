package lesson01_hw;

import javax.naming.NameNotFoundException;

public class Main {
    public static void main(String[] args) throws Exception {
        Triangle triangle = new Triangle(3, 4, 5);
        System.out.println(triangle.square());

    }
}
