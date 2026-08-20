package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

        try {

            if(username.equals("admin") && password.equals("1234"))
            {
                FXMLLoader loader =
                        new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));

                Scene scene = new Scene(loader.load());

                Stage stage =
                        (Stage) usernameField.getScene().getWindow();

                stage.setScene(scene);
                stage.setWidth(900);
                stage.setHeight(700);
                stage.centerOnScreen();

//                stage.setMaximized(true);
                stage.show();
            }
            else
            {
                messageLabel.setText("Invalid Username or Password");
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            messageLabel.setText("Failed to open dashboard");
        }
    }
}