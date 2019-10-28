package net.k07.datalist;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class DatabaseOperations {

    private Connection conn = null;

    public DefaultTableModel queryResults(String weaponType, String version) {
        String query = "SELECT * FROM spriteData";

        if (!weaponType.equals("")) {
            query += " WHERE type = " + weaponType;
        }
        if (!version.equals("")) {
            if (!weaponType.equals("")) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " version = " + version;
        }
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            return createModel(rs);

        } catch (SQLException e) {
            return null;
        }
    }

    public static DefaultTableModel createModel(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();

        Vector<Object> columnNames = new Vector<>();
        int columnCount = meta.getColumnCount();
        for(int column = 1; column < columnCount; column++) {
            columnNames.add(meta.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while(rs.next()) {
            Vector<Object> list = new Vector<>();
            for(int column = 1; column < columnCount; column++) {
                list.add(rs.getObject(column).toString());
            }
            data.add(list);
        }

        return new DefaultTableModel(data, columnNames);
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
