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

            String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt());
            pst.setObject(1, userDTO.getUsername());
            pst.setObject(2, hashedPassword);
            pst.setObject(3, userDTO.getEmail());
            pst.setObject(4, userDTO.getPhoneNumber());

            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
        } finally {
            //closeResources(connection, pst, null);
        }
        return false;
    }

    public boolean verifyUser(String username, String password) {
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT password FROM users WHERE username = ?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, username);

            rs = pst.executeQuery();
            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                return BCrypt.checkpw(password, storedHashedPassword); // Verify the password
            }
        } catch (SQLException e) {
            System.out.println("Error verifying user: " + e.getMessage());
        } finally {
            //closeResources(connection, pst, rs);
        }
        return false;
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
