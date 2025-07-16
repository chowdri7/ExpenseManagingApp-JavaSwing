package com.expenseapp.dao;

import com.expenseapp.config.DBConfig;
import com.expenseapp.model.ExpenseEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    public boolean addExpense(ExpenseEntry expense) {
        String sql = "INSERT INTO expenses (passbook_id, type, category, amount, remarks, person_name, contact_details, mode_of_transaction, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, expense.getPassbookId());
            stmt.setString(2, expense.getType());
            stmt.setString(3, expense.getCategory());
            stmt.setDouble(4, expense.getAmount());
            stmt.setString(5, expense.getRemarks());
            stmt.setString(6, expense.getPersonName());
            stmt.setString(7, expense.getContactDetails());
            stmt.setString(8, expense.getModeOfTransaction());
            stmt.setTimestamp(9, new Timestamp(expense.getDate().getTime()));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ExpenseEntry> getExpensesByPassbook(int passbookId) {
        List<ExpenseEntry> list = new ArrayList<>();
        String sql = "SELECT * FROM expenses WHERE passbook_id = ? ORDER BY date ASC";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passbookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ExpenseEntry e = new ExpenseEntry(
                        rs.getInt("id"),
                        rs.getInt("passbook_id"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("remarks"),
                        rs.getString("person_name"),
                        rs.getString("contact_details"),
                        rs.getString("mode_of_transaction"),
                        rs.getTimestamp("date")
                );
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getPassbookBalance(int passbookId) {
        double balance = 0.0;
        String sql = "SELECT type, amount FROM expenses WHERE passbook_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passbookId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                if ("Cash In".equals(type)) {
                    balance += amount;
                } else if ("Cash Out".equals(type)) {
                    balance -= amount;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
}
