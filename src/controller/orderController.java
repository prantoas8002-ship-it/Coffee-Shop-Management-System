package controller;

import database.DBconnection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class orderController {

    @FXML
    private TextField searchField;

    @FXML
    private TextField customerNameField;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button addOrderBtn;

    @FXML
    private Button updateOrderBtn;

    @FXML
    private Button deleteOrderBtn;

    @FXML
    private Button completeOrderBtn;

    @FXML
    private TableView<order> orderTable;

    @FXML
    private TableColumn<order, String> customerColumn;

    @FXML
    private TableColumn<order, String> productColumn;

    @FXML
    private TableColumn<order, Integer> quantityColumn;

    @FXML
    private TableColumn<order, Double> totalColumn;

    @FXML
    private TableColumn<order, String> statusColumn;

    @FXML
    private TableColumn<order, Number> serialColumn;

    private ObservableList<order> orderList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        productColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadOrders();

        Platform.runLater(() -> {
            dashboardBtn.requestFocus();
        });

        serialColumn.setCellFactory(col ->
                new TableCell<>() {
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

        orderTable.setOnMouseClicked(event -> {

            order selected = orderTable.getSelectionModel().getSelectedItem();

            if (selected != null) {

                customerNameField.setText(selected.getCustomerName());
                productNameField.setText(selected.getProductName());
                quantityField.setText(String.valueOf(selected.getQuantity()));
            }
        });
    }

    private void loadOrders() {

        orderList.clear();

        try {

            Connection con = DBconnection.connect();

            String sql = "SELECT * FROM orders";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                orderList.add(
                        new order(
                                rs.getInt("id"),
                                rs.getString("customer_name"),
                                rs.getString("product_name"),
                                rs.getInt("quantity"),
                                rs.getDouble("total"),
                                rs.getString("status")
                        )
                );
            }

            orderTable.setItems(orderList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addOrder(ActionEvent event) {

        try {

            Connection con = DBconnection.connect();

            String customer = customerNameField.getText().trim();
            String product = productNameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());

            double price = 0;

            String priceSql =
                    "SELECT price FROM products WHERE name=?";

            PreparedStatement pricePs =
                    con.prepareStatement(priceSql);

            pricePs.setString(1, product);

            ResultSet priceRs =
                    pricePs.executeQuery();

            if (priceRs.next()) {

                price = priceRs.getDouble("price");

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Product not found!");
                alert.show();
                return;
            }

            double total = price * quantity;

            String sql =
                    "INSERT INTO orders(customer_name,product_name,quantity,total,status) VALUES(?,?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, customer);
            ps.setString(2, product);
            ps.setInt(3, quantity);
            ps.setDouble(4, total);
            ps.setString(5, "Pending");

            ps.executeUpdate();

            loadOrders();

            customerNameField.clear();
            productNameField.clear();
            quantityField.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateOrder(ActionEvent event) {

        try {

            order selected =
                    orderTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                return;
            }

            Connection con = DBconnection.connect();

            String customer = customerNameField.getText().trim();
            String product = productNameField.getText().trim();
            int quantity = Integer.parseInt(quantityField.getText().trim());

            double price = 0;

            String priceSql =
                    "SELECT price FROM products WHERE name=?";

            PreparedStatement pricePs =
                    con.prepareStatement(priceSql);

            pricePs.setString(1, product);

            ResultSet priceRs =
                    pricePs.executeQuery();

            if (priceRs.next()) {

                price = priceRs.getDouble("price");

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Product not found!");
                alert.show();
                return;
            }

            double total = price * quantity;

            String sql =
                    "UPDATE orders SET customer_name=?, product_name=?, quantity=?, total=? WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, customer);
            ps.setString(2, product);
            ps.setInt(3, quantity);
            ps.setDouble(4, total);
            ps.setInt(5, selected.getId());

            ps.executeUpdate();

            loadOrders();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteOrder(ActionEvent event) {

        try {

            order selected =
                    orderTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                return;
            }

            Connection con = DBconnection.connect();

            String sql =
                    "DELETE FROM orders WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, selected.getId());

            ps.executeUpdate();

            loadOrders();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void completeOrder(ActionEvent event) {

        try {

            order selected =
                    orderTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                return;
            }

            Connection con = DBconnection.connect();

            String sql =
                    "UPDATE orders SET status='Completed' WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, selected.getId());

            ps.executeUpdate();

            loadOrders();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void refreshOrders(ActionEvent event) {
        loadOrders();
    }

    @FXML
    public void searchOrder(KeyEvent event) {

        String keyword =
                searchField.getText().toLowerCase();

        ObservableList<order> filtered =
                FXCollections.observableArrayList();

        for (order o : orderList) {

            if (o.getCustomerName().toLowerCase().contains(keyword)
                    || o.getProductName().toLowerCase().contains(keyword)
                    || o.getStatus().toLowerCase().contains(keyword)) {

                filtered.add(o);
            }
        }

        orderTable.setItems(filtered);
    }

    @FXML
    public void goToDashboard(ActionEvent event) {

        try {

            Parent root =
                    FXMLLoader.load(
                            getClass().getResource("/view/Dashboard.fxml")
                    );

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}