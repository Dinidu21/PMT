package com.dinidu.lk.pmt.model;

import com.dinidu.lk.pmt.db.DBConnection;
import com.dinidu.lk.pmt.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserModel {
    public boolean saveUser(UserDTO userDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        String sql = "INSERT INTO users (username, password, email, phoneNumber) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(sql);

        pst.setObject(1, userDTO.getUsername());
        pst.setObject(2, userDTO.getPassword());
        pst.setObject(3, userDTO.getEmail());
        pst.setObject(4, userDTO.getPhoneNumber());

        int result = pst.executeUpdate();
        return result > 0;
    }
}
