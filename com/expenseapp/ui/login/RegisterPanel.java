package com.expenseapp.ui.login;

import com.expenseapp.config.UIConfig;
import com.expenseapp.dao.UserDAO;
import com.expenseapp.model.User;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    public RegisterPanel(CardLayout cardLayout, JPanel parentPanel) {
        setLayout(null);
        setBackground(UIConfig.BACKGROUND_COLOR);

        JLabel title = new JLabel("User Registration");
        title.setFont(UIConfig.TITLE_FONT);
        title.setBounds(150, 10, 300, 30);
        add(title);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 60, 150, 25);
        add(nameLabel);
        JTextField nameField = new JTextField();
        nameField.setBounds(200, 60, 200, 25);
        add(nameField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(50, 100, 150, 25);
        add(addressLabel);
        JTextField addressField = new JTextField();
        addressField.setBounds(200, 100, 200, 25);
        add(addressField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 140, 150, 25);
        add(emailLabel);
        JTextField emailField = new JTextField();
        emailField.setBounds(200, 140, 200, 25);
        add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 180, 150, 25);
        add(phoneLabel);
        JTextField phoneField = new JTextField();
        phoneField.setBounds(200, 180, 200, 25);
        add(phoneField);

        JLabel other1Label = new JLabel("Other Field 1:");
        other1Label.setBounds(50, 220, 150, 25);
        add(other1Label);
        JTextField other1Field = new JTextField();
        other1Field.setBounds(200, 220, 200, 25);
        add(other1Field);

        JLabel other2Label = new JLabel("Other Field 2:");
        other2Label.setBounds(50, 260, 150, 25);
        add(other2Label);
        JTextField other2Field = new JTextField();
        other2Field.setBounds(200, 260, 200, 25);
        add(other2Field);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 300, 150, 25);
        add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(200, 300, 200, 25);
        add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(UIConfig.BUTTON_COLOR);
        registerButton.setForeground(UIConfig.BUTTON_TEXT_COLOR);
        registerButton.setFont(UIConfig.BUTTON_FONT);
        registerButton.setBounds(200, 350, UIConfig.BUTTON_SIZE.width, UIConfig.BUTTON_SIZE.height);
        add(registerButton);

        JButton backButton = new JButton("Back to Login");
        backButton.setBounds(200, 410, UIConfig.BUTTON_SIZE.width, UIConfig.BUTTON_SIZE.height);
        add(backButton);

        registerButton.addActionListener(e -> {
            User user = new User();
            user.setName(nameField.getText());
            user.setAddress(addressField.getText());
            user.setEmail(emailField.getText());
            user.setPhone(phoneField.getText());
            user.setOtherField1(other1Field.getText());
            user.setOtherField2(other2Field.getText());
            user.setPassword(new String(passwordField.getPassword()));

            UserDAO dao = new UserDAO();
            boolean success = dao.registerUser(user);

            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                cardLayout.show(parentPanel, "login");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Check details or email may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> cardLayout.show(parentPanel, "login"));
    }
}
