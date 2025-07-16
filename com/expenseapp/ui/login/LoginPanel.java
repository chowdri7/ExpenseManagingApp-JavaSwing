package com.expenseapp.ui.login;

import com.expenseapp.config.UIConfig;
import com.expenseapp.dao.UserDAO;
import com.expenseapp.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;


public class LoginPanel extends JPanel {
    public LoginPanel(CardLayout cardLayout, JPanel parentPanel, Consumer<User> onLoginSuccess, UserDAO userDAO){
        setLayout(null);
        setBackground(UIConfig.BACKGROUND_COLOR);

        JLabel title = new JLabel("Login");
        title.setFont(UIConfig.TITLE_FONT);
        title.setBounds(200, 30, 200, 30);
        add(title);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(80, 100, 150, 25);
        add(emailLabel);
        JTextField emailField = new JTextField();
        emailField.setBounds(180, 100, 200, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(80, 150, 150, 25);
        add(passwordLabel);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(180, 150, 200, 25);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(UIConfig.BUTTON_COLOR);
        loginButton.setForeground(UIConfig.BUTTON_TEXT_COLOR);
        loginButton.setFont(UIConfig.BUTTON_FONT);
        loginButton.setBounds(180, 200, UIConfig.BUTTON_SIZE.width, UIConfig.BUTTON_SIZE.height);
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(180, 260, UIConfig.BUTTON_SIZE.width, UIConfig.BUTTON_SIZE.height);
        add(registerButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            User user = userDAO.loginUser(email, password);

            if (user != null) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                onLoginSuccess.accept(user);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> cardLayout.show(parentPanel, "register"));
    }
}
