package ru.se.ifmo.table;

import ru.se.ifmo.DatabaseUtils;
import ru.se.ifmo.model.*;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrganizationsTable extends SqlTable {
    private final UsersTable usersTable;

    public OrganizationsTable(Connection connection, UsersTable usersTable) {
        super(connection);
        this.usersTable = usersTable;
    }

    public void insertOrganization(Organization organization) {
        String sql = "insert into organizations(id, name, x, y, creation_date, annual_turnover, type, address_zip_code, owner) " +
                     "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, (int) (long) organization.getId());
            preparedStatement.setString(2, organization.getName());
            preparedStatement.setInt(3, (int) (long) organization.getCoordinates().getX());
            preparedStatement.setInt(4, organization.getCoordinates().getY());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(organization.getCreationDate().toLocalDateTime()));
            preparedStatement.setFloat(6, organization.getAnnualTurnover());
            String type = null;
            if (organization.getType() != null) {
                type = organization.getType().name();
            }
            preparedStatement.setString(7, type);
            preparedStatement.setString(8, organization.getOfficialAddress().getZipCode());
            preparedStatement.setString(9, organization.getUser().getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    public void cleanOrganizations() {
        String sql = "delete from organizations";
        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    public Collection<Organization> getOrganizations() {
        List<Organization> organizations = new ArrayList<>();
        String sql = "select * from organizations";
        try (Statement stmt = getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Long id = (long) rs.getInt("id");
                String name = rs.getString("name");
                Long x = (long) rs.getInt("x");
                int y = rs.getInt("y");
                Coordinates coordinates = new Coordinates(x, y);
                ZonedDateTime creationDate = rs.getTimestamp("creation_date").toLocalDateTime().atZone(ZoneId.systemDefault());
                float annualTurnover = rs.getFloat("annual_turnover");
                OrganizationType type = OrganizationType.valueOf(rs.getString("type"));
                Address address = new Address(rs.getString("address_zip_code"));
                User user = usersTable.getUser(rs.getString("owner"));
                Organization organization = new Organization(id, name, coordinates, creationDate, annualTurnover, type, address, user);
                organizations.add(organization);
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return organizations;
    }
}
