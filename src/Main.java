import javax.swing.*;

public class Main {
    public static void main(String[] args) {
      /*  double i = 9;

        if (i < 9) {
            System.out.print("this is lesser");
        }
        else {
            System.out.print("this is greater");
        }

        Dog john = new Dog(5, "John");

        john.bark();
        Dog.woof();
        */

        int[] numArr = new int[500];
        int[] anotherArray = new int[] {1, 2, 3, 4, 5};


        for (int i = 0; i < numArr.length; i++) {
            numArr[i] = i*i;
        }

        for (int i = 0; i < numArr.length; i++) {
            System.out.println(numArr[i]);
        }




    }
}