package com.expenseapp;

import com.expenseapp.dao.UserDAO;
import com.expenseapp.model.Passbook;
import com.expenseapp.model.User;
import com.expenseapp.ui.dashboard.DashboardPanel;
import com.expenseapp.ui.expense.ExpenseEntryPanel;
import com.expenseapp.ui.login.LoginPanel;
import com.expenseapp.ui.login.RegisterPanel;
import com.expenseapp.ui.passbook.PassbookDetailPanel;
import com.expenseapp.ui.report.ReportsPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private User currentUser;

    private UserDAO userDAO;

    public MainFrame() {
        setTitle("Expense Tracker Application");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        userDAO = new UserDAO();

        // Login panel
        LoginPanel loginPanel = new LoginPanel(cardLayout, mainPanel, this::onLoginSuccess, userDAO);
        mainPanel.add(loginPanel, "login");

        // Register panel
        RegisterPanel registerPanel = new RegisterPanel(cardLayout, mainPanel);
        mainPanel.add(registerPanel, "register");

        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }

    private void onLoginSuccess(User user) {
        this.currentUser = user;

        DashboardPanel dashboardPanel = new DashboardPanel(cardLayout, mainPanel, currentUser);
        mainPanel.add(dashboardPanel, "dashboard");
        cardLayout.show(mainPanel, "dashboard");
    }


    public void openExpensePanel(Passbook passbook) {
        ExpenseEntryPanel expensePanel = new ExpenseEntryPanel(cardLayout, mainPanel, passbook);
        JScrollPane scrollPane = new JScrollPane(expensePanel);
        mainPanel.add(scrollPane, "expense");
        cardLayout.show(mainPanel, "expense");
    }
    public void openExpensePanel(Passbook passbook, String type) {
        ExpenseEntryPanel expensePanel = new ExpenseEntryPanel(cardLayout, mainPanel, passbook, type);
        JScrollPane scrollPane = new JScrollPane(expensePanel);
        mainPanel.add(scrollPane, "expense");
        cardLayout.show(mainPanel, "expense");
    }

    public void openReportsPanel(Passbook passbook) {
        ReportsPanel reportsPanel = new ReportsPanel(cardLayout, mainPanel, passbook);
        mainPanel.add(reportsPanel, "reports");
        cardLayout.show(mainPanel, "reports");
    }

    public void openPassbookDetailPanel(Passbook passbook) {
        PassbookDetailPanel detailPanel = new PassbookDetailPanel(cardLayout, mainPanel, passbook);
        mainPanel.add(detailPanel, "passbookDetail");
        cardLayout.show(mainPanel, "passbookDetail");
    }
    public void refreshPassbookDetail(Passbook passbook) {
        PassbookDetailPanel detailPanel = new PassbookDetailPanel(cardLayout, mainPanel, passbook);
        mainPanel.add(detailPanel, "passbookDetail");
    }

    public void refreshDashboard() {
        DashboardPanel newDashboard = new DashboardPanel(cardLayout, mainPanel, currentUser);
        mainPanel.add(newDashboard, "dashboard");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame app = new MainFrame();
            app.setVisible(true);
            app.setSize(825, 550);
            app.setResizable(false);
        });
    }
}
