package net.k07.datalist;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class DataWindow extends JFrame {

    private static final int NUM_COLUMNS = 5;
    private static final int[] COLUMN_WIDTHS = {50, 50, 50, 50, 400};

    private JTextField weaponField = new JTextField();
    private JTextField versionField = new JTextField();
    private JTextField tableNameField = new JTextField();

    public JCheckBox customOperationBox = new JCheckBox();
    public JTextArea customQueryArea = new JTextArea();

    private DatabaseOperations dbOps = null;
    private String[] takenChoices = {"Don't Filter", "Stolen", "Not Stolen"};

    public DataWindow() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        dbOps = new DatabaseOperations();
        dbOps.startConnection();

        this.setTitle("Data List App");
        this.setLayout(new GridLayout(5, 1));

        JPanel insertionPanel = new JPanel(new GridLayout(2, 1));
        insertionPanel.add(componentWithLabel(tableNameField, "Table Name"));

        JButton insertionWindowOpener = new JButton("Open Insertion Window");
        insertionWindowOpener.addActionListener(e -> {
            InsertionWindow window = new InsertionWindow(dbOps.getColumnNamesForTable(tableNameField.getText()), tableNameField.getText(),  dbOps);
            window.pack();
            window.setVisible(true);
        });
        insertionPanel.add(insertionWindowOpener);
        this.add(insertionPanel);
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 1));
        inputPanel.add(componentWithLabel(weaponField, "Weapon Type"));
        inputPanel.add(componentWithLabel(versionField, "Version Type"));


        JComboBox takenBox = new JComboBox(takenChoices);
        inputPanel.add(componentWithLabel(takenBox, "Filter Option"));
        inputPanel.add(componentWithLabel(customOperationBox, "Use Custom SQL Query"));

        customOperationBox.addItemListener(e -> {
            boolean value = customOperationBox.isSelected();

            weaponField.setEnabled(!value);
            versionField.setEnabled(!value);
            takenBox.setEnabled(!value);

            customQueryArea.setEnabled(value);

            redraw();
        });

        this.add(inputPanel);

        customQueryArea.setEnabled(false);
        this.add(wrapInScrollPaneAndPanel(customQueryArea, "Custom query"));

        JButton startButton = new JButton();
        startButton.addActionListener(e -> {

            if(customOperationBox.isSelected()) {
                String query = customQueryArea.getText();

                String lowercase = query.toLowerCase();
                if(lowercase.contains("delete") || lowercase.contains("drop")) {
                    this.showErrorMessage("For safety reasons, this program does not execute deletion queries. Do them in the dedicated control panel.");
                    return;
                }
                else if(lowercase.contains("insert")) {
                    int rowsChanged = dbOps.executeUpdateQuery(query);
                    if(rowsChanged <= 0) {
                        this.showErrorMessage("No rows were changed!");
                    }
                    else {
                        this.showSuccessMessage(rowsChanged + " rows added successfully.");
                    }
                }
                else {
                    setupTable(dbOps.executeSelectQuery(query), dbOps.getCount(query));
                }
            }
            else {
                String query = dbOps.generateQuery(weaponField.getText(), versionField.getText(), DatabaseOperations.getOption(takenBox.getSelectedIndex()));
                setupTable(dbOps.executeSelectQuery(query), dbOps.getCount(query));
            }


        });
        startButton.setText("Get Results");
        this.add(startButton);
    }

    public static JPanel wrapInScrollPaneAndPanel(Component c, String name) {
        JScrollPane pane = new JScrollPane(c);
        JPanel panel = new JPanel(new GridLayout());
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.add(pane);
        return panel;
    }

    public static JPanel componentWithLabel(Component c, String name) {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel(name));
        panel.add(c);
        return panel;
    }

    public void setupTable(DefaultTableModel model, int count) {
        String[] columns = {"Name", "Version"};
        JTable outputTable = new JTable(model);
        outputTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        /*
        TableColumnModel columnModel = outputTable.getColumnModel();
        for(int i = 0; i < NUM_COLUMNS; i++) {
            columnModel.getColumn(i).setPreferredWidth(COLUMN_WIDTHS[i]);
        }*/

        JFrame newWindow = new TableWindow(outputTable, count);
        newWindow.setSize(500, 500);
        newWindow.setVisible(true);
    }

    public void redraw() {
        this.revalidate();
        this.repaint();
    }

    private void showErrorMessage(String s) {
        JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String s) {
        JOptionPane.showMessageDialog(this, s, "Success!", JOptionPane.INFORMATION_MESSAGE);
    }
}
