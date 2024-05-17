package ru.se.ifmo.tables;

import ru.se.ifmo.DatabaseUtils;
import ru.se.ifmo.models.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrganizationsTable extends SQLTable<Organization> {
    private User user;

    public OrganizationsTable(Connection connection, String dbms, User user) {
        super(connection, dbms);
        this.user = user;
    }

    @Override
    public void insert(Organization entity) {
        new CoordinatesTable(connection, dbms).insert(entity.getCoordinates());
        new AddressesTable(connection, dbms).insert(entity.getOfficialAddress());
        entity.setCreationDate(ZonedDateTime.now());
        String insertQuery = "INSERT INTO \"Organizations\" (name, coordinates_id, creation_date, annual_turnover, type_id, address_id, owner_id) " +
                             "VALUES ('%s', %d, '%s', %f, %d, %d, %d) RETURNING id;".formatted(entity.getName(), entity.getCoordinates().getId(),
                                     entity.getCreationDate(), entity.getAnnualTurnover(), entity.getType().ordinal()+1, entity.getOfficialAddress().getId(), user.getId());

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(insertQuery);
            while(rs.next()) {
                entity.setId((long) rs.getInt("id"));
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    public void delete(Organization entity) {
        if (entity.getUser().equals(user)) {
            String deleteStatement = ("DELETE FROM \"Organizations\" WHERE id = %d;" +
                                      "DELETE FROM \"Coordinates\" WHERE id = %d;" +
                                      "DELETE FROM \"Addresses\" WHERE id = %d;").formatted(entity.getId(),
                    entity.getCoordinates().getId(), entity.getOfficialAddress().getId());
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(deleteStatement);
            } catch (SQLException e) {
                DatabaseUtils.printSQLException(e);
            }
        }
    }

    public void deleteAll() {
        String deleteStatementPattern = ("DELETE FROM \"Organizations\" WHERE id = %d;"+
                                  "DELETE FROM \"Coordinates\" WHERE id = %d;" +
                                  "DELETE FROM \"Addresses\" WHERE id = %d;");
        StringBuilder deleteQuery = new StringBuilder();
        for (Organization organization : getOrganizations()) {
            if (organization.getUser().equals(user)) {
                deleteQuery.append(deleteStatementPattern.formatted(organization.getId(), organization.getCoordinates().getId(), organization.getOfficialAddress().getId()));
            }
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteQuery.toString());
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    public void update(Organization entity) {
        if (entity.getUser().equals(user)) {
            CoordinatesTable coordinatesTable = new CoordinatesTable(connection, dbms);
            AddressesTable addressesTable = new AddressesTable(connection, dbms);
            Organization oldOrganization = getOrganization((int)(long) entity.getId());
            Coordinates oldCoordinates = coordinatesTable.getCoordinates((int)(long)oldOrganization.getCoordinates().getId());
            Address oldAddress = addressesTable.getAddress((int)(long) oldOrganization.getOfficialAddress().getId());
            String updateQueryPattern = "UPDATE \"Organizations\" SET %s = %s WHERE id = " + entity.getId() + ";";
            StringBuilder updateQuery = new StringBuilder();
            if (!entity.getName().equals(oldOrganization.getName())) {
                updateQuery.append(updateQueryPattern.formatted("name", "'%s'".formatted(entity.getName())));
            } if (entity.getAnnualTurnover() != oldOrganization.getAnnualTurnover()) {
                updateQuery.append(updateQueryPattern.formatted("annual_turnover", entity.getAnnualTurnover()));
            } if (entity.getType() != oldOrganization.getType()) {
                updateQuery.append(updateQueryPattern.formatted("type_id", entity.getType().ordinal()+1));
            }

            if (!entity.getOfficialAddress().equals(oldAddress)) {
                entity.getOfficialAddress().setId(oldAddress.getId());
                addressesTable.update(entity.getOfficialAddress());
            } if (!entity.getCoordinates().equals(oldCoordinates)) {
                entity.getCoordinates().setId(oldCoordinates.getId());
                coordinatesTable.update(entity.getCoordinates());
            }
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(updateQuery.toString());
            } catch (SQLException e) {
                DatabaseUtils.printSQLException(e);
            }
        }
    }

    public List<Organization> getOrganizations() {
        CoordinatesTable coordinatesTable = new CoordinatesTable(connection, dbms);
        AddressesTable addressesTable = new AddressesTable(connection, dbms);
        UsersTable usersTable = new UsersTable(connection, dbms);
        String selectQuery = "SELECT * FROM \"Organizations\"";
        List<Organization> flats = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(selectQuery);
            while (rs.next()) {
                Organization flat = parseOrganization(coordinatesTable, addressesTable, usersTable, rs);
                flats.add(flat);
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return flats;
    }

    public Organization getOrganization(Integer id) {
        CoordinatesTable coordinatesTable = new CoordinatesTable(connection, dbms);
        AddressesTable addressesTable = new AddressesTable(connection, dbms);
        UsersTable usersTable = new UsersTable(connection, dbms);
        String selectQuery = "SELECT * FROM \"Organizations\" WHERE id = %d".formatted(id);
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(selectQuery);
            while (rs.next()) {
                return parseOrganization(coordinatesTable, addressesTable, usersTable, rs);
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return null;
    }

    private Organization parseOrganization(CoordinatesTable coordinatesTable, AddressesTable addressesTable, UsersTable usersTable, ResultSet rs) throws SQLException {
        return new Organization((long) rs.getInt("id"),
                rs.getString("name"),
                coordinatesTable.getCoordinates(rs.getInt("coordinates_id")),
                ZonedDateTime.parse(rs.getString("creation_date")),
                rs.getFloat("annual_turnover"),
                OrganizationType.values()[rs.getInt("type_id") - 1],
                addressesTable.getAddress(rs.getInt("address_id")), usersTable.getUser(rs.getInt("owner_id")));
    }
}
