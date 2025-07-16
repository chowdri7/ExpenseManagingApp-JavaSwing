package com.expenseapp.ui.report;

import com.expenseapp.config.UIConfig;
import com.expenseapp.dao.ExpenseDAO;
import com.expenseapp.model.ExpenseEntry;
import com.expenseapp.model.Passbook;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsPanel extends JPanel {
    private ExpenseDAO expenseDAO;
    private Passbook passbook;
    private JTextArea reportArea;

    public ReportsPanel(CardLayout cardLayout, JPanel parentPanel, Passbook passbook) {
        this.passbook = passbook;
        this.expenseDAO = new ExpenseDAO();
        setLayout(null);
        setBackground(UIConfig.BACKGROUND_COLOR);

        JLabel title = new JLabel("Reports - " + passbook.getName());
        title.setFont(UIConfig.TITLE_FONT);
        title.setBounds(150, 10, 400, 30);
        add(title);

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setBounds(50, 60, 100, 25);
        add(fromLabel);
        JTextField fromField = new JTextField("2023-01-01");
        fromField.setBounds(120, 60, 100, 25);
        add(fromField);

        JLabel toLabel = new JLabel("To:");
        toLabel.setBounds(250, 60, 50, 25);
        add(toLabel);
        JTextField toField = new JTextField("2023-12-31");
        toField.setBounds(300, 60, 100, 25);
        add(toField);

        JButton dateRangeBtn = new JButton("Date Range Report");
        dateRangeBtn.setBounds(420, 60, 150, 25);
        add(dateRangeBtn);

        JButton monthlyBtn = new JButton("Monthly");
        monthlyBtn.setBounds(50, 100, 100, 25);
        add(monthlyBtn);

        JButton quarterlyBtn = new JButton("Quarterly");
        quarterlyBtn.setBounds(160, 100, 100, 25);
        add(quarterlyBtn);

        JButton halfYearlyBtn = new JButton("Half-Yearly");
        halfYearlyBtn.setBounds(270, 100, 120, 25);
        add(halfYearlyBtn);

        JButton yearlyBtn = new JButton("Yearly");
        yearlyBtn.setBounds(400, 100, 100, 25);
        add(yearlyBtn);

        reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBounds(50, 140, 520, 300);
        add(scrollPane);

        JButton backButton = new JButton("Back");
        backButton.setBounds(250, 460, UIConfig.BUTTON_SIZE.width, UIConfig.BUTTON_SIZE.height);
        add(backButton);

        dateRangeBtn.addActionListener(e -> {
            try {
                Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse(fromField.getText());
                Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(toField.getText());
                showReport(fromDate, toDate);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        monthlyBtn.addActionListener(e -> generatePeriodReport("month"));
        quarterlyBtn.addActionListener(e -> generatePeriodReport("quarter"));
        halfYearlyBtn.addActionListener(e -> generatePeriodReport("half"));
        yearlyBtn.addActionListener(e -> generatePeriodReport("year"));

        backButton.addActionListener(e -> cardLayout.show(parentPanel, "dashboard"));
    }

    private void showReport(Date fromDate, Date toDate) {
        List<ExpenseEntry> allEntries = expenseDAO.getExpensesByPassbook(passbook.getId());

        List<ExpenseEntry> filtered = allEntries.stream()
                .filter(entry -> !entry.getDate().before(fromDate) && !entry.getDate().after(toDate))
                .collect(Collectors.toList());

        displayReport(filtered);
    }
    
    @SuppressWarnings("deprecation")
    private void generatePeriodReport(String periodType) {
        Date now = new Date();
        Date fromDate = null;

        switch (periodType) {
            case "month":
                fromDate = new Date(now.getYear(), now.getMonth(), 1);
                break;
            case "quarter":
                int month = now.getMonth();
                int startMonth = month / 3 * 3;
                fromDate = new Date(now.getYear(), startMonth, 1);
                break;
            case "half":
                fromDate = new Date(now.getYear(), (now.getMonth() < 6) ? 0 : 6, 1);
                break;
            case "year":
                fromDate = new Date(now.getYear(), 0, 1);
                break;
        }

        showReport(fromDate, now);
    }

    private void displayReport(List<ExpenseEntry> entries) {
        double totalIn = 0, totalOut = 0;
        StringBuilder sb = new StringBuilder();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        for (ExpenseEntry entry : entries) {
            sb.append(sdf.format(entry.getDate()))
                    .append(" | ").append(entry.getType())
                    .append(" | ₹").append(String.format("%.2f", entry.getAmount()))
                    .append(" | ").append(entry.getCategory())
                    .append(" | ").append(entry.getRemarks()).append("\n");

            if ("Cash In".equals(entry.getType())) {
                totalIn += entry.getAmount();
            } else if ("Cash Out".equals(entry.getType())) {
                totalOut += entry.getAmount();
            }
        }

        double balance = totalIn - totalOut;
        sb.append("\n--- Summary ---\n");
        sb.append("Total In: ₹").append(String.format("%.2f", totalIn)).append("\n");
        sb.append("Total Out: ₹").append(String.format("%.2f", totalOut)).append("\n");
        sb.append("Balance: ₹").append(String.format("%.2f", balance)).append("\n");

        reportArea.setText(sb.toString());
    }
}
