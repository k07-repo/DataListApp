package net.k07.datalist;

import java.sql.*;

public class DatabaseOperations {

    private Connection conn = null;

    public String[][] queryResults(String weaponType, String version) {

        String query = "SELECT * FROM spriteData";

        if (!weaponType.equals("")) {
            query += "WHERE type = " + weaponType;
        }
        if (!version.equals("")) {
            if (!weaponType.equals("")) {
                query += "AND ";
            } else {
                query += "WHERE ";
            }
            query += "version = " + version;
        }

        String[][] result = new String[256][2];

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            int pos = 0;
            while (rs.next()) {

                String nameResult = rs.getString("name");
                String versionResult = rs.getString("version");
                result[pos][0] = nameResult;
                result[pos][1] = versionResult;
                pos++;
            }

            st.close();
        } catch (SQLException e) {
            return null;
        }

        return result;
    }

    public void startConnection() {
        if (conn != null) {
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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

    }
}
