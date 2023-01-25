public class Dog {
    public int age;
    public String name;

    public Dog(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public void bark() {
        System.out.println(this.name + " barks");
    }

    public static void woof() {
        System.out.print("barks statically");
    }
}
