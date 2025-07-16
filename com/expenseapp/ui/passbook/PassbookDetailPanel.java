package com.expenseapp.ui.passbook;

import com.expenseapp.config.UIConfig;
import com.expenseapp.dao.ExpenseDAO;
import com.expenseapp.model.ExpenseEntry;
import com.expenseapp.model.Passbook;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

public class PassbookDetailPanel extends JPanel {
    private Passbook passbook;
    private ExpenseDAO expenseDAO;
    private JPanel entriesPanel;
    @SuppressWarnings("unused")
    private CardLayout cardLayout;
    @SuppressWarnings("unused")
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
    Collections.reverse(entries); // Latest first

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");

    if (entries.isEmpty()) {
        JLabel noEntry = new JLabel("No transactions yet.");
        noEntry.setFont(UIConfig.LABEL_FONT);
        entriesPanel.add(noEntry);
    } else {
        for (ExpenseEntry entry : entries) {
            JPanel rowPanel = new JPanel(new BorderLayout());
            rowPanel.setBackground(UIConfig.ENTRY_BG_COLOR);
            rowPanel.setBorder(BorderFactory.createLineBorder(UIConfig.PANEL_BORDER_COLOR, 1));
            rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

            // Details panel (left side)
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setOpaque(false);

            JLabel dateLabel = new JLabel(sdf.format(entry.getDate()));
            dateLabel.setFont(UIConfig.LABEL_FONT);

            JLabel catRemarksLabel = new JLabel(entry.getCategory() + " | " + entry.getRemarks());
            catRemarksLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));

            detailsPanel.add(dateLabel);
            detailsPanel.add(catRemarksLabel);

            // Amount panel (right side)
            JPanel amountPanel = new JPanel(new BorderLayout());
            amountPanel.setOpaque(false);

            JLabel amountLabel = new JLabel("₹" + String.format("%.2f", entry.getAmount()));
            amountLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            amountLabel.setForeground(entry.getType().equals("Cash In") ? new Color(40, 167, 69) : new Color(220, 53, 69));
            amountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            // Arrow panel
            JPanel arrowPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setStroke(new BasicStroke(3));

                    if ("Cash In".equals(entry.getType())) {
                        g2.setColor(new Color(40, 167, 69)); // Green
                        g2.drawLine(10, 30, 30, 10);
                        g2.drawLine(10, 30, 30, 30);
                        g2.drawLine(10, 10, 10, 30);
                    } else {
                        g2.setColor(new Color(220, 53, 69)); // Red
                        g2.drawLine(10, 30, 30, 10);
                        g2.drawLine(10, 10, 30, 10);
                        g2.drawLine(30, 10, 30, 30);
                    }
                }
            };
            arrowPanel.setPreferredSize(new Dimension(40, 40));
            arrowPanel.setOpaque(false);

            amountPanel.add(amountLabel, BorderLayout.CENTER);
            amountPanel.add(arrowPanel, BorderLayout.EAST);

            rowPanel.add(detailsPanel, BorderLayout.CENTER);
            rowPanel.add(amountPanel, BorderLayout.EAST);

            entriesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            entriesPanel.add(rowPanel);
        }
    }

    entriesPanel.revalidate();
    entriesPanel.repaint();
}

}
