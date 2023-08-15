package carsharing.DBC;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.Optional;

public class DBC {
    private static final String JDBC_DRIVER = "org.h2.Driver";
    private static final String DB_URL = "jdbc:h2:.\\src\\main\\java\\carsharing\\db\\";
    private static String dbName = "Test";
    public static void setName(String[] args) {
        if (args.length != 0 && Optional.of(args[0]).get().equals("-databaseFileName")) {
            dbName = args[1];
        } else {
            dbName = "Test";
        }
    }
    public static void createDb(Statement stmt){
                String sqlCompany = "CREATE TABLE IF NOT EXISTS COMPANY " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL);";

        String sqlCar = "CREATE TABLE IF NOT EXISTS CAR " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                "COMPANY_ID INTEGER NOT NULL, " +
                "CONSTRAINT fk_company FOREIGN KEY (COMPANY_ID) " +
                "REFERENCES COMPANY (ID));";

        String sqlCustomer = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                "(ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                "RENTED_CAR_ID INTEGER DEFAULT NULL, " +
                "CONSTRAINT fk_customer FOREIGN KEY (RENTED_CAR_ID) " +
                "REFERENCES CAR (ID));";
        try {
            stmt.executeUpdate(sqlCompany);
            stmt.executeUpdate(sqlCar);
            stmt.executeUpdate(sqlCustomer);
        } catch (SQLException e) {
            System.out.println("SQL while creating");
        }
    }
    public static void dropDb(Statement stmt){
        String dropCustomer = "DROP TABLE IF EXISTS CUSTOMER;";
        String dropCar = "DROP TABLE IF EXISTS CAR;";
        String dropCompany = "DROP TABLE IF EXISTS COMPANY;";
        try {
            stmt.executeUpdate(dropCustomer);
            stmt.executeUpdate(dropCar);
            stmt.executeUpdate(dropCompany);
        } catch (SQLException e) {
            System.out.println("SQL while dropping");
        }
    }
    public static Connection getConnection() {
        Connection connection;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + dbName);
            connection.setAutoCommit(true);
        } catch (SQLException | ClassNotFoundException e ) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    public static void closeConnection(Connection connection) {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch(SQLException se){
            throw new RuntimeException(se);
        }
    }
}
