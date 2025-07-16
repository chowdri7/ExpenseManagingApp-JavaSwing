package com.expenseapp.dao;

import com.expenseapp.config.DBConfig;
import com.expenseapp.model.Passbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassbookDAO {
    public boolean createPassbook(Passbook passbook) {
        String sql = "INSERT INTO passbooks (user_id, name) VALUES (?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passbook.getUserId());
            stmt.setString(2, passbook.getName());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Passbook> getPassbooksByUser(int userId) {
        List<Passbook> list = new ArrayList<>();
        String sql = "SELECT * FROM passbooks WHERE user_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Passbook p = new Passbook(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getTimestamp("created_at")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
