package carsharing.Impl;

import carsharing.Entity.Car;
import carsharing.Entity.Company;
import carsharing.Entity.Customer;
import carsharing.Abstractions.AbstractDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import static carsharing.DBC.DBC.closeConnection;
import static carsharing.DBC.DBC.getConnection;

public class CustomerDaoImpl implements AbstractDAO<Customer> {
    private static final String INSERT_CUSTOMER_SQL = "INSERT INTO CUSTOMER (NAME) VALUES (?);";
    private static final String SELECT_QUERY = "SELECT * FROM CUSTOMER";
    private static final String UPDATE_CUSTOMER_SQL = "UPDATE CUSTOMER SET RENTED_CAR_ID = ? WHERE NAME = ?;";
    private static final String BY_ID= " WHERE ID =?";
    private static final String GET_CARID = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE NAME = ?";
    private static final String GET_AVAILABLE_CARS = "SELECT * FROM CAR " +
                "WHERE ID NOT IN (SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT NULL) " +
                "AND COMPANY_ID = ?;";
    @Override
    public List<Customer> getByName(String name) {
        return null;
    }
    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        Connection conn = getConnection();
        try {
            ResultSet rs = conn.createStatement().executeQuery(SELECT_QUERY);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int rentedCarId = rs.getInt("RENTED_CAR_ID");
                customers.add(new Customer(id, name));
            }
        } catch (Exception e) {
            System.out.println("Err while getting All");
        }
        closeConnection(conn);
        return customers;
    }
    @Override
    public void create(Customer entity) {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(INSERT_CUSTOMER_SQL)) {
            ps.setString(1, entity.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while creating customer");
        }
        closeConnection(conn);
    }
    public void update(Customer customer, int chosenCarId) {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_CUSTOMER_SQL)) {
            ps.setString(1, String.valueOf(chosenCarId));
            ps.setString(2, customer.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while getting");
        }
        closeConnection(conn);
   }
    public List<Car> checkCarInCustomerTable(Company company){
        List<Car> cars = new ArrayList<>();
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(GET_AVAILABLE_CARS)) {
            ps.setInt(1, company.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int company_id = rs.getInt("company_id");
                cars.add(new Car(id, name, company_id));
            }
        } catch (Exception e) {
            System.out.println("Err while getting cars");
        }
        closeConnection(conn);
        return cars;
    }
    public static void returnCar(Customer customer) {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement("UPDATE CUSTOMER SET RENTED_CAR_ID = null " +
                "WHERE ID = " + customer.getId())) {
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while getting");
        }
    }
    public static int getCarID(Customer customer){
        int carID = -1;
        try (Connection conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(GET_CARID)) {
            preparedStatement.setString(1, customer.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    carID = resultSet.getInt("RENTED_CAR_ID");
                    if (carID == 0){
                        carID = -1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carID; // Return a default value indicating failure
    }
    public static void printRentedCar(int carID) throws SQLException {
        String sqlQuery = "SELECT\n" +
                "(SELECT NAME FROM CAR WHERE ID = ?) AS CarName,\n" +
                "(SELECT NAME FROM COMPANY WHERE ID = ca.COMPANY_ID) AS CustomerName\n" +
                "FROM\n" +
                "Customer c, CAR ca\n" +
                "WHERE\n" +
                "c.RENTED_CAR_ID  = ca.ID;";
        try (Connection conn = getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery)) {
            preparedStatement.setInt(1, carID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String carName = resultSet.getString("CarName");
                String customerName = resultSet.getString("CustomerName");
                System.out.println("Your rented car:\n" +
                        carName + "\n" +
                        "Company:\n" +
                        customerName + "\n");
                closeConnection(conn);
            } else {
            System.out.println("You didn't rent a car!\n");
            }
        }
    }

}
