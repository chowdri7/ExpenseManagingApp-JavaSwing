package com.expenseapp.dao;

import com.expenseapp.config.DBConfig;
import com.expenseapp.model.Passbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassbookDAO {
    public static List<String> getUniquePersonNamesByPassbook(int passbookId) {
        List<String> personNames = new ArrayList<>();
        String sql = "SELECT DISTINCT person_name FROM expenses WHERE passbook_id = ? AND person_name IS NOT NULL AND person_name != ''";
        try (Connection conn = DBConfig.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passbookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                personNames.add(rs.getString("person_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personNames;
    }

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

    public Passbook getPassbookByName(int userId, String name) {
    String sql = "SELECT * FROM passbooks WHERE user_id = ? AND name = ?";
    try (Connection conn = DBConfig.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, userId);
        stmt.setString(2, name);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Passbook(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getTimestamp("created_at")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
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
