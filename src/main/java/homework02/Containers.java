package homework02;


import java.util.ArrayList;
import java.util.List;


public class Containers implements ContainerWithContents {

    String name;
    Integer weight;

    public Containers(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return name +
                " компоненты = " + components +
                '}';
    }


    private final List<ContainerWithContents> components = new ArrayList<>();

    public void addComponent(ContainerWithContents component) {
        components.add(component);

    }

    public void removeComponent(ContainerWithContents component) {
        components.remove(component);

    }

    @Override
    public void operation() {

        System.out.println(name + components);

        for (ContainerWithContents component : components) {
            component.operation();
        }
    }

    @Override
    public Integer getWeight() {
        return weight;
    }


    public Integer sumWeight() {
        List<Integer> sum = new ArrayList<>();

        for (ContainerWithContents con : components) {
            sum.add(con.getWeight());
        }
        int sumAll = 0;
        for (int i : sum) {
            sumAll += i;
        }
        return sumAll;

    }

}

