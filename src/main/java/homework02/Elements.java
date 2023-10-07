package homework02;

public class Elements implements ContainerWithContents {
    final String name;
    Integer weight;
    final Double price;



    public Integer getWeight() {
        return weight;
    }



    public Elements(String name, Integer weight, Double price) {
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Элемент{" +
                "название='" + name + '\'' +
                ", вес=" + weight +
                ", цена=" + price +
                '}';
    }

    @Override
    public void operation() {
        System.out.println("Элемент: " +  name );
    }


}
