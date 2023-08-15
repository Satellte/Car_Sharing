package carsharing.Impl;

import carsharing.Abstractions.AbstractDAO;
import carsharing.Entity.Company;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import static carsharing.DBC.DBC.closeConnection;
import static carsharing.DBC.DBC.getConnection;


public class CompanyDaoImpl implements AbstractDAO<Company> {
    private static final String SELECT_QUERY = "SELECT * FROM COMPANY";
    private static final String INSERT_COMPANIES_SQL = "INSERT INTO COMPANY (NAME) VALUES (?);";
    @Override
    public List<Company> getByName(String name) {
        return null;
    }
    @Override
    public List<Company> getAll() {
        List<Company> companies = new ArrayList<>();
        Connection conn = getConnection();
        try {
            ResultSet rs = conn.createStatement().executeQuery(SELECT_QUERY);
            while (rs.next()) {
                int id = rs.getInt("id");
                String receivedName = rs.getString("name");
                companies.add(new Company(id, receivedName));
            }
        } catch (Exception e) {
            System.out.println("Err while getting All");
        }
        closeConnection(conn);
        return companies;
    }
    @Override
    public void create(Company entity) {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(INSERT_COMPANIES_SQL)) {
            ps.setString(1, entity.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Err while creating");
        }
        closeConnection(conn);
    }
}
