package homework02;


public class Main {
    public static void main(String[] args) {
        // Создание элементов
        Elements element1 = new Elements("Сахар", 1000, 85.0);
        Elements element2 = new Elements("Кофе", 200, 250.0);
        Elements element3 = new Elements("Арбуз", 3000, 600.0);

        // Создание контейнеров
        Containers container1 = new Containers("Коробка");
        Containers container2 = new Containers("Ящик");


        // Добавление элементов в контейнеры
        container1.addComponent(element1);
        container1.addComponent(element2);
        container1.addComponent(element3);
        container2.addComponent(element3);

        //Сумма веса в контейнере

        System.out.println(container1.sumWeight());

        // Вызов операции для контейнеров
        container2.operation();
        container1.operation();


    }


}

