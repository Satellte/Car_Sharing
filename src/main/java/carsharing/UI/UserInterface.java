package carsharing.UI;

public class UserInterface {
    public static void printMainMenu(){
        System.out.println("""
                    1. Log in as a manager
                    2. Log in as a customer
                    3. Create a customer
                    0. Exit""");
    }
    public static void printManagerMenu(){
        System.out.println();
        System.out.println("""
                    1. Company list
                    2. Create a company
                    0. Back""");
    }
    public static void printCarMenu(){
        System.out.println();
        System.out.println("""
                    1. Car list
                    2. Create a car
                    0. Back""");
    }
    public static void printCustomerMenu(){
        System.out.println();
        System.out.println("""
                    1. Rent a car
                    2. Return a rented car
                    3. My rented car
                    0. Back""");
    }
    public static void isEmpty(String text){
        System.out.println("\nThe " + text + " list is empty!");
    }
    public static void enterName(String text){
        System.out.println("\nEnter the " + text + " name:");
    }
    public static void wasAdded(String text){System.out.println("\nThe " + text + " was created!\n");}
}
