package com.expenseapp.ui.expense;

import com.expenseapp.config.AppConstants;
import com.expenseapp.config.UIConfig;
import com.expenseapp.dao.ExpenseDAO;
import com.expenseapp.model.ExpenseEntry;
import com.expenseapp.model.Passbook;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ExpenseEntryPanel extends JPanel {
    @SuppressWarnings("unused")
    private Passbook passbook;
    private ExpenseDAO expenseDAO;

    public ExpenseEntryPanel(CardLayout cardLayout, JPanel parentPanel, Passbook passbook) {
        this.passbook = passbook;
        this.expenseDAO = new ExpenseDAO();
        setLayout(null);
        setBackground(UIConfig.BACKGROUND_COLOR);

        JLabel title = new JLabel("Add Expense - " + passbook.getName());
        title.setFont(UIConfig.TITLE_FONT);
        title.setBounds(120, 10, 400, 30);
        add(title);

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(50, 60, 150, 25);
        add(typeLabel);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{AppConstants.TRANSACTION_IN, AppConstants.TRANSACTION_OUT});
        typeCombo.setBounds(200, 60, 200, 25);
        add(typeCombo);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(50, 100, 150, 25);
        add(categoryLabel);
        JComboBox<String> categoryCombo = new JComboBox<>(AppConstants.CATEGORIES.toArray(new String[0]));
        categoryCombo.setBounds(200, 100, 200, 25);
        add(categoryCombo);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(50, 140, 150, 25);
        add(amountLabel);
        JTextField amountField = new JTextField();
        amountField.setBounds(200, 140, 200, 25);
        add(amountField);

        JLabel remarksLabel = new JLabel("Remarks:");
        remarksLabel.setBounds(50, 180, 150, 25);
        add(remarksLabel);
        JTextField remarksField = new JTextField();
        remarksField.setBounds(200, 180, 200, 25);
        add(remarksField);

        JLabel personLabel = new JLabel("Person Name:");
        personLabel.setBounds(50, 220, 150, 25);
        add(personLabel);
        JTextField personField = new JTextField();
        personField.setBounds(200, 220, 200, 25);
        add(personField);

        JLabel contactLabel = new JLabel("Contact Details:");
        contactLabel.setBounds(50, 260, 150, 25);
        add(contactLabel);
        JTextField contactField = new JTextField();
        contactField.setBounds(200, 260, 200, 25);
        add(contactField);

        JLabel modeLabel = new JLabel("Mode of Transaction:");
        modeLabel.setBounds(50, 300, 150, 25);
        add(modeLabel);
        JComboBox<String> modeCombo = new JComboBox<>(AppConstants.MODES_OF_TRANSACTION.toArray(new String[0]));
        modeCombo.setBounds(200, 300, 200, 25);
        add(modeCombo);

        JButton saveButton = new JButton("Save Entry");
        saveButton.setBackground(UIConfig.BUTTON_COLOR);
        saveButton.setForeground(UIConfig.BUTTON_TEXT_COLOR);
        saveButton.setFont(UIConfig.BUTTON_FONT);
        saveButton.setBounds(200, 350, UIConfig.BUTTON_SIZE.width, UIConfig.BUTTON_SIZE.height);
        add(saveButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(200, 410, UIConfig.BUTTON_SIZE.width, UIConfig.BUTTON_SIZE.height);
        add(backButton);

        saveButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                ExpenseEntry entry = new ExpenseEntry();
                entry.setPassbookId(passbook.getId());
                entry.setType((String) typeCombo.getSelectedItem());
                entry.setCategory((String) categoryCombo.getSelectedItem());
                entry.setAmount(amount);
                entry.setRemarks(remarksField.getText());
                entry.setPersonName(personField.getText());
                entry.setContactDetails(contactField.getText());
                entry.setModeOfTransaction((String) modeCombo.getSelectedItem());
                entry.setDate(new Date());

                boolean success = expenseDAO.addExpense(entry);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Expense entry added!");
                    amountField.setText("");
                    remarksField.setText("");
                    personField.setText("");
                    contactField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add expense entry.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> cardLayout.show(parentPanel, "dashboard"));
    }
}
