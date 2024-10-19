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
    public boolean saveUser(UserDTO userDTO) {
        try {
            String hashedPassword = BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(12));
            return CrudUtil.execute("INSERT INTO users (username, password, email, phoneNumber) VALUES (?, ?, ?, ?)",
                    userDTO.getUsername(),
                    hashedPassword,
                    userDTO.getEmail(),
                    userDTO.getPhoneNumber());
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            CustomErrorAlert.showAlert("ERROR","Error saving user: " + e.getMessage());
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
            CustomErrorAlert.showAlert("ERROR","Error verifying user: " + e.getMessage());
        } finally {
            // closeResources(connection, pst, rs);
        }
        return "ERROR";
    }
    public boolean isEmailRegistered(String email) {
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            connection = DBConnection.getInstance().getConnection();
            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, email);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if email is found
            }
        } catch (SQLException e) {
            System.out.println("Error checking email registration: " + e.getMessage());
            CustomErrorAlert.showAlert("ERROR", "Error checking email registration: " + e.getMessage());
        } finally {
            // closeResources(connection, pst, rs);
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
