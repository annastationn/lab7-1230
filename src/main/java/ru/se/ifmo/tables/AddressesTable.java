package ru.se.ifmo.tables;

import ru.se.ifmo.DatabaseUtils;
import ru.se.ifmo.models.Address;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddressesTable extends SQLTable<Address> {

    public AddressesTable(Connection connArg, String dbms) {
        super(connArg, dbms);
    }

    @Override
    public void insert(Address entity) {
        String insertQuery = "INSERT INTO \"Addresses\" (zip_code) VALUES ('%s') RETURNING id;".formatted(entity.getZipCode());
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(insertQuery);
            while(rs.next()) {
                entity.setId((long) rs.getInt("id"));
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    public Address getAddress(Integer id) {
        String selectQuery = "SELECT * FROM \"Addresses\" WHERE id = %d".formatted(id);
        try (Statement st = connection.createStatement()){
            ResultSet rs = st.executeQuery(selectQuery);
            while (rs.next()) {
                Address house = new Address(rs.getString("zip_code"));
                house.setId((long) id);
                return house;
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return null;
    }

    public void update(Address entity) {
        Address oldAddress = getAddress((int)(long)entity.getId());

        String updateQueryPattern = "UPDATE \"Addresses\" SET %s = %s WHERE id = " + entity.getId() + ";";
        StringBuilder updateQuery = new StringBuilder();
        if (!entity.getZipCode().equals(oldAddress.getZipCode())) {
            updateQuery.append(updateQueryPattern.formatted("zip_code", "'%s'".formatted(entity.getZipCode())));
        }
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(updateQuery.toString());
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }
}
