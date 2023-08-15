package carsharing.Impl;

import carsharing.Abstractions.AbstractDAO;
import carsharing.Entity.Car;
import carsharing.Entity.Company;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import static carsharing.DBC.DBC.closeConnection;
import static carsharing.DBC.DBC.getConnection;

public class CarDaoImpl implements AbstractDAO<Car> {
    private static final String SELECT_QUERY = "SELECT * FROM CAR";
    private static final String BY_COMPANY = " WHERE COMPANY_ID =?";
    private static final String BY_NAME= " WHERE NAME =?";
    private static final String INSERT_CARS_SQL = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES (?, ?);";
    @Override
    public List<Car> getByName(String name) {
        List<Car> cars = new ArrayList<>();
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_QUERY + BY_NAME)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String receivedName = rs.getString("name");
                int company_id = rs.getInt("company_id");
                cars.add(new Car(id, receivedName, company_id));
            }
        } catch (Exception e) {
            System.out.println("Err while getting All");
        }
        closeConnection(conn);
        return cars;
    }
    @Override
    public List<Car> getAll() {
        return null;
    }
    @Override
    public void create(Car entity) {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(INSERT_CARS_SQL)) {
            ps.setString(1, entity.getName());
            ps.setInt(2, entity.getCompanyId());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while creating");
        }
        closeConnection(conn);
    }
    public List<Car> getAllByCompany(Company company) {
        List<Car> cars = new ArrayList<>();
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_QUERY + BY_COMPANY)) {
            ps.setInt(1, company.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int company_id = rs.getInt("company_id");
                cars.add(new Car(id, name, company_id));
            }
        } catch (Exception e) {
            System.out.println("Err while getting All");
        }
        closeConnection(conn);
        return cars;
    }
}