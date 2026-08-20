package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    public void login() {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.equals("admin") && password.equals("1234"))
        {
            messageLabel.setText("Login Successful");
        }
        else
        {
            messageLabel.setText("Invalid Username or Password");
        }
    }
}