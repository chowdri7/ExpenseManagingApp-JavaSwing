package com.expenseapp.ui.passbook;

import com.expenseapp.config.UIConfig;
import com.expenseapp.dao.ExpenseDAO;
import com.expenseapp.model.ExpenseEntry;
import com.expenseapp.model.Passbook;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PassbookDetailPanel extends JPanel {
    private Passbook passbook;
    private ExpenseDAO expenseDAO;
    private JPanel entriesPanel;
    private CardLayout cardLayout;
    private JPanel parentPanel;

    public PassbookDetailPanel(CardLayout cardLayout, JPanel parentPanel, Passbook passbook) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        this.passbook = passbook;
        this.expenseDAO = new ExpenseDAO();

        setLayout(new BorderLayout());
        setBackground(UIConfig.BACKGROUND_COLOR);

        // Top buttons
        JPanel topButtonsPanel = new JPanel(new FlowLayout());
        JButton cashInBtn = new JButton("Cash In");
        cashInBtn.setBackground(new Color(40, 167, 69));
        cashInBtn.setForeground(UIConfig.BUTTON_TEXT_COLOR);

        JButton cashOutBtn = new JButton("Cash Out");
        cashOutBtn.setBackground(new Color(220, 53, 69));
        cashOutBtn.setForeground(UIConfig.BUTTON_TEXT_COLOR);

        JButton reportBtn = new JButton("Reports");
        reportBtn.setBackground(UIConfig.BUTTON_COLOR);
        reportBtn.setForeground(UIConfig.BUTTON_TEXT_COLOR);

        topButtonsPanel.add(cashInBtn);
        topButtonsPanel.add(cashOutBtn);
        topButtonsPanel.add(reportBtn);

        add(topButtonsPanel, BorderLayout.NORTH);

        // Passbook entries
        entriesPanel = new JPanel();
        entriesPanel.setLayout(new BoxLayout(entriesPanel, BoxLayout.Y_AXIS));
        entriesPanel.setBackground(UIConfig.BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(entriesPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setBackground(UIConfig.BUTTON_COLOR);
        backButton.setForeground(UIConfig.BUTTON_TEXT_COLOR);
        backButton.addActionListener(e -> {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame instanceof com.expenseapp.MainFrame mainFrame) {
                mainFrame.refreshDashboard();
            }
            cardLayout.show(parentPanel, "dashboard");
        });

        add(backButton, BorderLayout.SOUTH);

        // Button actions
        cashInBtn.addActionListener(e -> openExpenseEntry("Cash In"));
        cashOutBtn.addActionListener(e -> openExpenseEntry("Cash Out"));
        reportBtn.addActionListener(e -> openReports());

        refreshEntries();
    }

    private void openExpenseEntry(String type) {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof com.expenseapp.MainFrame mainFrame) {
            mainFrame.openExpensePanel(passbook, type);
        }
    }

    private void openReports() {
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (topFrame instanceof com.expenseapp.MainFrame mainFrame) {
            mainFrame.openReportsPanel(passbook);
        }
    }

    public void refreshEntries() {
        entriesPanel.removeAll();
        List<ExpenseEntry> entries = expenseDAO.getExpensesByPassbook(passbook.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");

        if (entries.isEmpty()) {
            JLabel noEntry = new JLabel("No transactions yet.");
            noEntry.setFont(UIConfig.LABEL_FONT);
            entriesPanel.add(noEntry);
        } else {
            for (ExpenseEntry entry : entries) {
                String text = sdf.format(entry.getDate())
                        + " | " + entry.getType()
                        + " | ₹" + String.format("%.2f", entry.getAmount())
                        + " | " + entry.getCategory()
                        + " | " + entry.getRemarks();

                JLabel label = new JLabel(text);
                label.setOpaque(true);
                label.setBackground(UIConfig.ENTRY_BG_COLOR);
                label.setBorder(BorderFactory.createLineBorder(UIConfig.PANEL_BORDER_COLOR));
                label.setFont(UIConfig.LABEL_FONT);
                label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                entriesPanel.add(label);
                entriesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
        entriesPanel.revalidate();
        entriesPanel.repaint();
    }
}
