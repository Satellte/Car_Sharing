package carsharing.Service;

import carsharing.DBC.DBC;
import carsharing.Entity.Car;
import carsharing.Entity.Company;
import carsharing.Entity.Customer;
import carsharing.Impl.CarDaoImpl;
import carsharing.Impl.CompanyDaoImpl;
import carsharing.Impl.CustomerDaoImpl;
import carsharing.UI.UserInterface;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.IntStream;
import java.util.List;
import java.util.Scanner;

import static carsharing.Impl.CustomerDaoImpl.getCarID;
import static carsharing.Impl.CustomerDaoImpl.returnCar;
import static carsharing.Impl.CustomerDaoImpl.printRentedCar;
import static carsharing.UI.UserInterface.enterName;
import static carsharing.UI.UserInterface.wasAdded;
import static carsharing.UI.UserInterface.isEmpty;
import static carsharing.UI.UserInterface.printCarMenu;
import static carsharing.UI.UserInterface.printCustomerMenu;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private static final CompanyDaoImpl companies = new CompanyDaoImpl();
    private static final CarDaoImpl cars = new CarDaoImpl();
    private static final CustomerDaoImpl customers = new CustomerDaoImpl();
    public static void start(String[] args) throws SQLException {
        DBC.setName(args);
        try (Connection conn = DBC.getConnection();
             Statement stmt = conn.createStatement()) {
//            DBC.dropDb(stmt);
            DBC.createDb(stmt);
            DBC.closeConnection(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        startMainMenu();
    }
    public static void startMainMenu() throws SQLException {
        boolean mainMenu = true;
        while (mainMenu) {
            UserInterface.printMainMenu();
            int option = sc.nextInt();
            switch (option) {
                case 1:
                    gotoManagerMenu();
                    break;
                case 2:
                    gotoCustomersMenu();
                    break;
                case 3:
                    createCustomer();
                    break;
                case 0:
                    mainMenu = false;
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid op, try again.");
                    break;
            }
        }
    }
    public static void gotoManagerMenu(){
        boolean managerMenu = true;
        while (managerMenu) {
            UserInterface.printManagerMenu();
            int option = sc.nextInt();
            switch (option) {
                case 1:
                    gotoCompaniesMenu();
                    break;
                case 2:
                    createCompany();
                    break;
                case 0:
                    managerMenu = false;
                    break;
                default:
                    System.out.println("Invalid op, try again.");
                    break;
            }

        }
    }
    public static void gotoCompaniesMenu(){
        List<Company> list = companies.getAll();
        if (list.isEmpty()) {
            isEmpty("company");
        } else {
            boolean companyMenu = true;
            while (companyMenu) {
                System.out.println("\nChoose the company:");
                list.forEach(comp -> System.out.println(comp.toString()));
                System.out.println("0. Back");
                int option = sc.nextInt();
                int param = option > list.size() ? -1 : option == 0 ? 0 : 1;
                switch (param) {
                    case 1:
                        startCarsMenu(list.get(option - 1));
                        companyMenu = false;
                        break;
                    case 0:
                        companyMenu = false;
                        break;
                    default:
                        System.out.println("Invalid op, try again.");
                        break;
                }
            }
        }
    }
    public static void createCompany(){
        enterName("company");
        sc.nextLine();
        String name = sc.nextLine();
        companies.create(new Company(0, name));
        wasAdded("company");
        System.out.println("\nThe company was created!");
    }
    public static void startCarsMenu(Company company){
        boolean carMenu = true;
        System.out.printf("'%s' company%n", company.getName());
        while (carMenu) {
            printCarMenu();
            int option = sc.nextInt();
            switch (option) {
                case 1:
                    showCars(company);
                    break;
                case 2:
                    createCar(company);
                    break;
                case 0:
                    carMenu = false;
                    break;
                default:
                    System.out.println("Invalid op, try again.");
                    break;
            }

        }
    }
    public static void showCars(Company company){
        List<Car> list = cars.getAllByCompany(company);
        if (list.isEmpty()) {
            isEmpty("car");
        } else {
            System.out.println("\nCar list:");
            IntStream.iterate(1, i -> i <= list.size(), i -> i + 1)
                    .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1).getName()));
            System.out.println();
        }
    }
    public static void createCar(Company company){
        enterName("car");
        sc.nextLine();
        String name = sc.nextLine();
        cars.create(new Car(0, name, company.getId()));
        wasAdded("car");
        System.out.println("\nThe car was added!\n");
    }
    public static void gotoCustomersMenu() throws SQLException {
        List<Customer> list = customers.getAll();
        if (list.isEmpty()) {
            isEmpty("customer");
        } else {
            boolean showCustomers = true;
            while (showCustomers) {
                System.out.println("\nChoose a customer:");
                IntStream.iterate(1, i -> i <= list.size(), i -> i + 1)
                        .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1).getName()));
                System.out.println("0. Back\n");
                int option = sc.nextInt();
                int param = option > list.size() ? -1 : option == 0 ? 0 : 1;
                switch (param) {
                    case 1:
                        showCustomerMenu(list.get(option - 1));
                        showCustomers = false;
                        break;
                    case 0:
                        showCustomers = false;
                        break;
                    default:
                        System.out.println("Invalid op, try again.");
                        break;
                }
            }
        }
    }
    public static void showCustomerMenu(Customer customer) throws SQLException {
        boolean customerMenu = true;
            while (customerMenu) {
                printCustomerMenu();
                int option = sc.nextInt();
                switch (option) {
                    case 1:
                        showCompanies(customer);
                        break;
                    case 2:
                        checkRentedCar(customer);
                        break;
                    case 3:
                        showRentedCar(customer);
                        break;
                    case 0:
                        customerMenu = false;
                        break;
                    default:
                        System.out.println("Invalid op, try again.");
                        break;
                }
            }
    }
    public static void createCustomer(){
        enterName("customer");
        sc.nextLine();
        String name = sc.nextLine();
        customers.create(new Customer(name));
        wasAdded("customer");
    }
    public static void showCompanies(Customer customer) {
        int rentedCarID = customers.getCarID(customer);
            if (rentedCarID == -1) {
                List<Company> list = companies.getAll();
                if (list.isEmpty()) {
                    isEmpty("company");
                } else {
                    boolean companyMenu = true;
                    while (companyMenu) {
                        System.out.println("\nChoose the company:");
                        IntStream.iterate(1, i -> i <= list.size(), i -> i + 1)
                                .forEach(i -> System.out.printf("%d. %s%n", i, list.get(i - 1).getName()));
                        System.out.println("0. Back\n");
                        int option = sc.nextInt();
                        int param = option > list.size() ? -1 : option == 0 ? 0 : 1;
                        switch (param) {
                            case 1:
                                printAvailableCar(list.get(option - 1), customer);
                                companyMenu = false;
                                break;
                            case 0:
                                companyMenu = false;
                                break;
                            default:
                                System.out.println("Invalid op, try again.");
                                break;
                        }
                    }
                }

            } else {
                System.out.println("You've already rented a car!");
            }
    }
    public static void printAvailableCar(Company company, Customer customer) {
        List<Car> listCompanyCars = customers.checkCarInCustomerTable(company);
        if (listCompanyCars.isEmpty()) {
            isEmpty("car");
        } else {
            System.out.println("\nChoose a car:");
            IntStream.iterate(1, i -> i <= listCompanyCars.size(), i -> i + 1)
                    .forEach(i -> System.out.printf("%d. %s%n", i, listCompanyCars.get(i - 1).getName()));
            System.out.println("0. Back");
        }
        int chosenCarId = sc.nextInt();
        if (chosenCarId != 0) {
            String chosenCarName = listCompanyCars.get(chosenCarId - 1).getName();
            List<Car> selectedCar = cars.getByName(chosenCarName);
            int carID = selectedCar.get(0).getId();
            customers.update(customer, carID);
            System.out.println("You rented '" + chosenCarName + "'\n");
        }
    }
    public static void checkRentedCar(Customer customer){
        int rentedCarID = customers.getCarID(customer);
        if (rentedCarID == -1) {
            System.out.println("You didn't rent a car!\n");
            } else {
            returnCar(customer);
            System.out.println("You've returned a rented car!\n");
            }
    }
    public static void showRentedCar(Customer customer){
        int carID = getCarID(customer);
        try {
            printRentedCar(carID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
