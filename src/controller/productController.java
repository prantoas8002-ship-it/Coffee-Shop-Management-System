package controller;

import database.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Product;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class productController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField stockField;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Number> slColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, Integer> stockColumn;

    private final ObservableList<Product> products =
            FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        slColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name"));

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price"));

        stockColumn.setCellValueFactory(
                new PropertyValueFactory<>("stock"));

        loadProducts();

        productTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldItem, selected) -> {

                    if (selected != null) {

                        nameField.setText(selected.getName());

                        priceField.setText(
                                String.valueOf(selected.getPrice()));

                        stockField.setText(
                                String.valueOf(selected.getStock()));
                    }
                });
    }

    private void loadProducts() {

        products.clear();

        try {

            String sql = "SELECT * FROM products";

            PreparedStatement pst =
                    DBconnection.connect().prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                products.add(
                        new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("stock")
                        )
                );
            }

            productTable.setItems(products);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addProduct() {

        try {

            String sql =
                    "INSERT INTO products(name,price,stock) VALUES(?,?,?)";

            PreparedStatement pst =
                    DBconnection.connect().prepareStatement(sql);

            pst.setString(1, nameField.getText());
            pst.setDouble(2,
                    Double.parseDouble(priceField.getText()));
            pst.setInt(3,
                    Integer.parseInt(stockField.getText()));

            pst.executeUpdate();

            loadProducts();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateProduct() {

        Product selected =
                productTable.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        try {

            String sql =
                    "UPDATE products SET name=?, price=?, stock=? WHERE id=?";

            PreparedStatement pst =
                    DBconnection.connect().prepareStatement(sql);

            pst.setString(1, nameField.getText());
            pst.setDouble(2,
                    Double.parseDouble(priceField.getText()));
            pst.setInt(3,
                    Integer.parseInt(stockField.getText()));
            pst.setInt(4, selected.getId());

            pst.executeUpdate();

            loadProducts();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteProduct() {

        Product selected =
                productTable.getSelectionModel().getSelectedItem();

        if (selected == null) return;

        try {

            String sql =
                    "DELETE FROM products WHERE id=?";

            PreparedStatement pst =
                    DBconnection.connect().prepareStatement(sql);

            pst.setInt(1, selected.getId());

            pst.executeUpdate();

            loadProducts();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void clearFields() {

        nameField.clear();
        priceField.clear();
        stockField.clear();

        productTable.getSelectionModel().clearSelection();
    }

    public void goToDashboard(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/view/Dashboard.fxml")
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}