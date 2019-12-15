package net.k07.datalist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

public class DatabaseOperations {

    enum FilterOptions {
        NO_FILTER, TAKEN, NOT_TAKEN
    }

    private Connection conn = null;

    public void executeInsertQuery(String tableName, ArrayList<String> args) {
        String query = "INSERT INTO " + tableName + " VALUES (";

        for(int k = 0; k < args.size(); k++) {

            if(k != 2) {
                query += "\"" + args.get(k) + "\"";
            }
            else {
                query += args.get(k);
            }

            if(k < args.size() - 1) {
                query += ", ";
            }
        }
        query += ")";
        System.out.println(query);
        executeUpdateQuery(query);
    }

    public String generateQuery(String weaponType, String version, FilterOptions filterOption) {
        String query = "SELECT * FROM spriteData";

        //there's almost certainly a better way to do this, will fix it eventually
        if (!weaponType.equals("")) {
            query += " WHERE type = \"" + weaponType + "\"";
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

    public ArrayList<String> getColumnNamesForTable(String tableName) {
        String query = "SELECT * FROM " + tableName;
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();

            ArrayList<String> columnData = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++ ) {
                columnData.add(meta.getColumnName(i));
            }
            return columnData;

        } catch (SQLException e) {
            showErrorDialog(e);
            return null;
        }
    }

    public DefaultTableModel executeSelectQuery(String query) {
        System.out.println(query); //debug

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            return createModel(rs);

        } catch (SQLException e) {
            return null;
        }
    }

    public int executeUpdateQuery(String query) {
        try {
            Statement st = conn.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException e) {
            showErrorDialog(e);
            return -1;
        }
    }

    public int getCount(String query) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);

            rs.last();
            return rs.getRow();

        } catch (SQLException e) {
            showErrorDialog(e);
            return -1;
        }
    }

    public static DefaultTableModel createModel(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();

        Vector<Object> columnNames = new Vector<>();
        int columnCount = meta.getColumnCount();
        for(int column = 1; column <= columnCount; column++) {
            columnNames.add(meta.getColumnName(column));
        }

        Vector<Vector<Object>> data = new Vector<>();
        while(rs.next()) {
            Vector<Object> list = new Vector<>();
            for(int column = 1; column <= columnCount; column++) {
                Object object = rs.getObject(column);
                if(object == null) {
                    list.add("");
                }
                else {
                    list.add(object.toString());
                }
            }
            data.add(list);
        }

        return new DefaultTableModel(data, columnNames);
    }

    public boolean startConnection(String url, String username, String password) {
        if (conn != null) {
            return false;
        }

        // auto close connection
        try {
            conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
                System.out.println("Connected to the database!");
                return true;
            } else {
                System.out.println("Failed to make connection!");
                return false;
            }

        } catch (SQLException e) {
            showErrorDialog(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

    private void showErrorDialog(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
