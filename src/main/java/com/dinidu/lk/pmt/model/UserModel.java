package com.dinidu.lk.pmt.model;

import com.dinidu.lk.pmt.db.DBConnection;
import com.dinidu.lk.pmt.dto.UserDTO;
import com.dinidu.lk.pmt.utils.CrudUtil;
import com.dinidu.lk.pmt.utils.CustomErrorAlert;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class UserModel {
    public static boolean saveUser(UserDTO userDTO) throws SQLException {
        String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(12));
        return Boolean.TRUE.equals(CrudUtil.execute("INSERT INTO users (username, password, email, phoneNumber) VALUES (?, ?, ?, ?)",
                userDTO.getUsername(),
                hashedPassword,
                userDTO.getEmail(),
                userDTO.getPhoneNumber()));
    }

    public static String verifyUser(String username, String password) {
        String query = "SELECT password FROM users WHERE username = ?";
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Use the singleton connection instance
            connection = DBConnection.getInstance().getConnection();

            if (connection == null || connection.isClosed()) {
                System.out.println("Database connection is closed.");
                return "ERROR";
            }

            pst = connection.prepareStatement(query);
            pst.setString(1, username);

            rs = pst.executeQuery();
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

                // Verifying the password
                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    return "SUCCESS";
                } else {
                    return "INVALID_PASSWORD";
                }
            } else {
                return "INVALID_USERNAME";
            }
        } catch (SQLException e) {
            System.out.println("Error verifying user: " + e.getMessage());
            CustomErrorAlert.showAlert("ERROR", "Error verifying user: " + e.getMessage());
            return "ERROR";
        } finally {
            // Do not close the connection, just close statement and result set
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (pst != null) try { pst.close(); } catch (SQLException ignored) {}
        }
    }

    public static boolean isEmailRegistered(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Use the singleton connection instance
            connection = DBConnection.getInstance().getConnection();

            if (connection == null || connection.isClosed()) {
                System.out.println("Database connection is closed.");
                return false;
            }

            pst = connection.prepareStatement(query);
            pst.setString(1, email);

            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if the email is found
            }
        } catch (SQLException e) {
            System.out.println("Error checking email registration: " + e.getMessage());
            CustomErrorAlert.showAlert("ERROR", "Error checking email registration: " + e.getMessage());
        } finally {
            // Close statement and result set, but leave the connection open
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (pst != null) try { pst.close(); } catch (SQLException ignored) {}
        }

        return false;
    }

    public static boolean updatePassword(String email, String password) throws SQLException {
        String query = "UPDATE users SET password = ? WHERE email = ?";
        Connection connection = null;
        PreparedStatement pstm = null;

        try {
            // Use the singleton connection instance
            connection = DBConnection.getInstance().getConnection();

            if (connection == null || connection.isClosed()) {
                System.out.println("Database connection is closed.");
                return false;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
            pstm = connection.prepareStatement(query);
            pstm.setString(1, hashedPassword);
            pstm.setString(2, email);

            int affectedRows = pstm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Password updated successfully.");
                return true;
            } else {
                System.out.println("Password update failed. No rows affected.");
                CustomErrorAlert.showAlert("ERROR", "Password update failed. No rows affected.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
            CustomErrorAlert.showAlert("ERROR", "Error updating password: " + e.getMessage());
            return false;
        } finally {
            // Close the PreparedStatement, but leave the connection open
            if (pstm != null) try { pstm.close(); } catch (SQLException ignored) {}
        }
    }
}
