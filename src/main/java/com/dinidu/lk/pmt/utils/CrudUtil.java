package com.dinidu.lk.pmt.utils;

import com.dinidu.lk.pmt.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    // This class contains utility methods for executing CRUD operations (Create, Read, Update, Delete) with the database.
    public static <T> T execute(String sql, Object... obj) {
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;

        try {
            // Obtain the connection from the DBConnection singleton
            connection = DBConnection.getInstance().getConnection();

            if (connection == null || connection.isClosed()) {
                System.out.println("Database connection is closed.");
                return null;
            }

            // Prepare the statement
            pst = connection.prepareStatement(sql);

            // Set parameters in the prepared statement.
            for (int i = 0; i < obj.length; i++) {
                pst.setObject((i + 1), obj[i]);
            }

            if (sql.toLowerCase().startsWith("select")) {
                // If the SQL query is a SELECT statement
                resultSet = pst.executeQuery();
                return (T) resultSet; // Return the result set cast to the generic type `T`.
            } else {
                // For non-SELECT queries (INSERT, UPDATE, DELETE)
                int affectedRows = pst.executeUpdate();
                return (T) ((Boolean) (affectedRows > 0)); // Return true if at least one row was affected.
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            // You might want to throw a custom exception or handle it as needed.
            return null; // Returning null or an appropriate value can be considered.
        } finally {
            // Ensure the PreparedStatement is closed, but leave the connection open
            if (resultSet != null) try { resultSet.close(); } catch (SQLException ignored) {}
            if (pst != null) try { pst.close(); } catch (SQLException ignored) {}
        }
    }
}