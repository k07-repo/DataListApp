package net.k07.datalist;

import javax.swing.*;
import java.awt.*;

public class DataWindow extends JFrame {

    private JTextField weaponField = new JTextField();
    private JTextField versionField = new JTextField();

    private DatabaseOperations dbOps = null;

    public DataWindow() {

        dbOps = new DatabaseOperations();
        dbOps.startConnection();

        this.setTitle("Data List App");
        this.setLayout(new GridLayout(3, 1));

        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        inputPanel.add(componentWithLabel(weaponField, "Weapon Type"));
        inputPanel.add(componentWithLabel(versionField, "Version Type"));
        this.add(inputPanel);

        JPanel statisticsPanel = new JPanel();
        this.add(statisticsPanel);

        JButton startButton = new JButton();
        startButton.addActionListener(e -> {
            setupTable(dbOps.queryResults(weaponField.getText(), versionField.getText()));
        });
        this.add(startButton);
    }

    public JPanel wrapInScrollPaneAndPanel(Component c, String name) {
        JScrollPane pane = new JScrollPane(c);
        JPanel panel = new JPanel(new GridLayout());
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.add(pane);
        return panel;
    }

    public JPanel componentWithLabel(Component c, String name) {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel(name));
        panel.add(c);
        return panel;
    }

    public void setupTable(String[][] data) {
        String[] columns = {"Name", "Version"};
        JTable outputTable = new JTable(data, columns);
        JFrame newWindow = new TableWindow(outputTable);
        newWindow.setSize(500, 500);
        newWindow.setVisible(true);
    }

}
