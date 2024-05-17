package ru.se.ifmo.tables;


import ru.se.ifmo.DatabaseUtils;
import ru.se.ifmo.models.Coordinates;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class CoordinatesTable extends SQLTable<Coordinates> {
    public CoordinatesTable(Connection connArg, String dbmsArg) {
        super(connArg, dbmsArg);
    }

    @Override
    public void insert(Coordinates entity) {
        String insertQuery = "INSERT INTO \"Coordinates\" (x, y) VALUES (%d, %d) RETURNING id;".formatted(entity.getX(), entity.getY());
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(insertQuery);
            while(rs.next()) {
                entity.setId((long) rs.getInt("id"));
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }

    public Coordinates getCoordinates(Integer id) {
        String selectQuery = "SELECT * FROM \"Coordinates\" WHERE id = %d".formatted(id);
        try (Statement st = connection.createStatement()){
            ResultSet rs = st.executeQuery(selectQuery);
            while (rs.next()) {
                Coordinates coordinates = new Coordinates(
                        (long) rs.getInt("x"),
                        rs.getInt("y")
                );
                coordinates.setId((long) id);
                return coordinates;
            }
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
        return null;
    }

    public void update(Coordinates entity) {
        Coordinates oldCoordinates = getCoordinates((int)(long)entity.getId());

        String updateQueryPattern = "UPDATE \"Coordinates\" SET %s = %s WHERE id = " + entity.getId() + ";";
        StringBuilder updateQuery = new StringBuilder();
        if (!Objects.equals(entity.getX(), oldCoordinates.getX())) {
            updateQuery.append(updateQueryPattern.formatted("x", entity.getX()));
        } if (entity.getY() != oldCoordinates.getY()) {
            updateQuery.append(updateQueryPattern.formatted("y", entity.getX()));
        }

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(updateQuery.toString());
        } catch (SQLException e) {
            DatabaseUtils.printSQLException(e);
        }
    }
}
