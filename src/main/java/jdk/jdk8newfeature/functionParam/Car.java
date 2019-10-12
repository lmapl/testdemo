package jdk.jdk8newfeature.functionParam;

public class Car {
    private String name;
    //Supplier是jdk1.8的接口，这里和lamda一起使用了
    public static Car create(final Supplier<Car> supplier) {
        return supplier.get();
    }
 
    public static void collide(final Car car) {
        System.out.println("Collided " + car.getName());
    }
 
    public void follow(final Car another) {
        System.out.println(this.getName() + " Following the " + another.getName());
    }
 
    public void repair() {
        System.out.println("Repaired " + this.getName()+"aaaa");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}