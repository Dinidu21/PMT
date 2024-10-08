package com.dinidu.lk.pmt.model;

import com.dinidu.lk.pmt.db.DBConnection;
import com.dinidu.lk.pmt.dto.UserDTO;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class UserModel {
    public boolean saveUser(UserDTO userDTO) {
        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO users (username, password, email, phoneNumber) VALUES (?, ?, ?, ?)";
            pst = connection.prepareStatement(sql);

            String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(12));
            pst.setObject(1, userDTO.getUsername());
            pst.setObject(2, hashedPassword);
            pst.setObject(3, userDTO.getEmail());
            pst.setObject(4, userDTO.getPhoneNumber());

            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
        } finally {
            // closeResources(connection, pst, null);
        }
        return false;
    }

    public String verifyUser(String username, String password) {
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = DBConnection.getInstance().getConnection();

            String checkUsernameSQL = "SELECT password FROM users WHERE username = ?";
            pst = connection.prepareStatement(checkUsernameSQL);
            pst.setString(1, username);

            rs = pst.executeQuery();
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");

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
        } finally {
            // closeResources(connection, pst, rs);
        }
        return "ERROR";
    }

    private void closeResources(Connection connection, PreparedStatement pst, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
}
