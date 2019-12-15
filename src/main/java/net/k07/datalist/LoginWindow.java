package net.k07.datalist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class LoginWindow extends JFrame {

    public LoginWindow() {

        this.setTitle("Login");

        JTextField urlField = new JTextField();
        JTextField userField = new JTextField();
        JTextField passField = new JPasswordField();

        //local database only
        urlField.setText("jdbc:mysql://127.0.0.1:3306/myDB");
        urlField.setEditable(false);

        this.setLayout(new GridLayout(4, 1));
        this.add(ComponentUtils.componentWithLabel(urlField, "Database URL"));
        this.add(ComponentUtils.componentWithLabel(userField, "Username"));
        this.add(ComponentUtils.componentWithLabel(passField, "Password"));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            DatabaseOperations dbOps = new DatabaseOperations();
            if(dbOps.startConnection(urlField.getText(), userField.getText(), passField.getText())) {
                DataWindow dataWindow = new DataWindow(dbOps);
                dataWindow.setVisible(true);
            }
            else {
                JOptionPane.showMessageDialog(null, "Login failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            //close this window
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        this.add(loginButton);
    }
}
