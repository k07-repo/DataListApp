package net.k07.datalist;

import java.sql.*;

public class Main {

    public static void main(String[] args) throws SQLException {

        DataWindow dataWindow = new DataWindow();
        dataWindow.setSize(500, 500);
        dataWindow.setVisible(true);
        // https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html#package.description
        // auto java.sql.Driver discovery -- no longer need to load a java.sql.Driver class via Class.forName

        // register JDBC driver, optional since java 1.6
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection conn = null;
        // auto close connection
        try {
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/myDB", "root", "MyNewPass");
            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String query = "SELECT * FROM tableName";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next())
        {
            String version = rs.getString("version");
            String name = rs.getString("name");

            System.out.println(version + " " + name);
        }
        st.close();

    }
}
