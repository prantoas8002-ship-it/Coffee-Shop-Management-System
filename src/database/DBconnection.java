package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBconnection {
    public static Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/coffee_shop",
                    "root",
                    "#booooooring1"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getProducts() {
        try {
            Connection con = DBconnection.connect();

            String sql = "SELECT * FROM products";

            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {
                System.out.println(
                        rs.getInt("id") + " " +
                                rs.getString("name") + " " +
                                rs.getDouble("price")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

