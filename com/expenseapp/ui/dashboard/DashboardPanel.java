package com.expenseapp.ui.dashboard;

import com.expenseapp.config.UIConfig;
import com.expenseapp.dao.ExpenseDAO;
import com.expenseapp.dao.PassbookDAO;
import com.expenseapp.model.Passbook;
import com.expenseapp.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    private User currentUser;
    private PassbookDAO passbookDAO;
    private ExpenseDAO expenseDAO;
    private JPanel passbookListPanel;

    public DashboardPanel(CardLayout cardLayout, JPanel parentPanel, User user) {
        this.currentUser = user;
        this.passbookDAO = new PassbookDAO();
        this.expenseDAO = new ExpenseDAO();

        setLayout(new BorderLayout());
        setBackground(UIConfig.BACKGROUND_COLOR);

        JLabel title = new JLabel("Your Passbooks");
        title.setFont(UIConfig.TITLE_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        passbookListPanel = new JPanel();
        passbookListPanel.setLayout(new BoxLayout(passbookListPanel, BoxLayout.Y_AXIS));
        passbookListPanel.setBackground(UIConfig.BACKGROUND_COLOR);

        JScrollPane scrollPane = new JScrollPane(passbookListPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton createPassbookBtn = new JButton("Create New Passbook");
        createPassbookBtn.setBackground(UIConfig.BUTTON_COLOR);
        createPassbookBtn.setForeground(UIConfig.BUTTON_TEXT_COLOR);
        createPassbookBtn.setFont(UIConfig.BUTTON_FONT);
        createPassbookBtn.addActionListener(e -> showCreatePassbookDialog());
        add(createPassbookBtn, BorderLayout.SOUTH);

        refreshPassbookList();
    }

    private void showCreatePassbookDialog() {
        String name = JOptionPane.showInputDialog(this, "Enter passbook name:");
        if (name != null && !name.trim().isEmpty()) {
            Passbook newPassbook = new Passbook();
            newPassbook.setUserId(currentUser.getId());
            newPassbook.setName(name);
            boolean created = passbookDAO.createPassbook(newPassbook);
            if (created) {
                JOptionPane.showMessageDialog(this, "Passbook created!");
                refreshPassbookList();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create passbook.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshPassbookList() {
    passbookListPanel.removeAll();
    List<Passbook> passbooks = passbookDAO.getPassbooksByUser(currentUser.getId());
    if (passbooks.isEmpty()) {
        JLabel noData = new JLabel("No passbooks found. Create one to start tracking expenses.");
        noData.setFont(UIConfig.LABEL_FONT);
        passbookListPanel.add(noData);
    } else {
        for (Passbook pb : passbooks) {
            double balance = expenseDAO.getPassbookBalance(pb.getId());

            JPanel pbPanel = new JPanel(new BorderLayout());
            pbPanel.setBackground(UIConfig.BUTTON_COLOR);
            pbPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            pbPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel nameLabel = new JLabel(pb.getName());
            nameLabel.setFont(UIConfig.PASSBOOK_FONT);
            nameLabel.setForeground(UIConfig.BUTTON_TEXT_COLOR);
            nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

            JLabel balanceLabel = new JLabel("₹" + String.format("%.2f", balance));
            balanceLabel.setFont(UIConfig.PASSBOOK_FONT);
            balanceLabel.setForeground(UIConfig.BUTTON_TEXT_COLOR);
            balanceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
            balanceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            pbPanel.add(nameLabel, BorderLayout.WEST);
            pbPanel.add(balanceLabel, BorderLayout.EAST);

            pbPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(passbookListPanel);
                    if (topFrame instanceof com.expenseapp.MainFrame mainFrame) {
                        mainFrame.openPassbookDetailPanel(pb);
                    }
                }
            });

            passbookListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            passbookListPanel.add(pbPanel);
        }
    }
    passbookListPanel.revalidate();
    passbookListPanel.repaint();
}

}
