package net.k07.datalist;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Vector;

public class DatabaseOperations {

    enum FilterOptions {
        NO_FILTER, TAKEN, NOT_TAKEN
    }

    private Connection conn = null;

    public String generateQuery(String weaponType, String version, FilterOptions filterOption) {
        String query = "SELECT * FROM spriteData";

        //there's almost certainly a better way to do this, will fix it eventually
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
        if (!(filterOption == FilterOptions.NO_FILTER)) {
            if (!weaponType.equals("") || !version.equals("")) {
                query += " AND";
            }
            else {
                query += " WHERE";
            }

            if (filterOption == FilterOptions.TAKEN) {
                query += " stolen = true";
            }
            else {
                query += " stolen = false";
            }
        }

        return query;
    }

    public DefaultTableModel executeQuery(String query) {
        System.out.println(query);

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            return createModel(rs);

        } catch (SQLException e) {
            return null;
        }
    }

    public int getCount(String query) {
        String newQuery = query.replace("SELECT *", "SELECT COUNT(*)");
        System.out.println(newQuery);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(newQuery);

            rs.next();
            return rs.getInt(1);

        } catch (SQLException e) {
            return -1;
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

    public static FilterOptions getOption(int index) {
        switch(index) {
            case 0:
                return FilterOptions.NO_FILTER;
            case 1:
                return FilterOptions.TAKEN;
            case 2:
                return FilterOptions.NOT_TAKEN;
        }
        return FilterOptions.NO_FILTER;

    }
}
