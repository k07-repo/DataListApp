package net.k07.datalist;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InsertionWindow extends JFrame{
    private ArrayList<JTextField> textFields = new ArrayList<JTextField>();
    private JTextField tableNameField = new JTextField();
    private DatabaseOperations dbOps;
    public String tableName;

    public InsertionWindow(ArrayList<String> columnNames, String tableName, DatabaseOperations dbOps) {
        this.dbOps = dbOps;
        this.tableName = tableName;

        this.setTitle("Insertion Window");
        this.setLayout(new GridLayout(2, 1));
        this.setSize(400, 50);

        JPanel fieldPanel = new JPanel(new GridLayout(1, 5));

        for(int k = 0; k < columnNames.size(); k++) {
            JTextField field = new JTextField();
            fieldPanel.add(ComponentUtils.componentWithLabel(field, columnNames.get(k)));
            textFields.add(field);
        }
        this.add(fieldPanel);

        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(e -> {
            ArrayList<String> inputs = new ArrayList<String>();
            for(int k = 0; k < textFields.size(); k++) {
                JTextField currentField = textFields.get(k);
                inputs.add(currentField.getText());
                currentField.setText("");
            }
            dbOps.executeInsertQuery(this.tableName, inputs);
        });
        this.add(insertButton);
    }
}
