package application;
import database.DBconnection ;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.Connection;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Connection con = DBconnection.connect();

        if(con != null){
            System.out.println("Connected");
        }

        DBconnection.getProducts();

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/view/login.fxml"));

        Scene scene = new Scene(loader.load());

        stage.setTitle("Coffee Shop Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}