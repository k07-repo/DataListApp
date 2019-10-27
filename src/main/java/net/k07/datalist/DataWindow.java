package net.k07.datalist;

import javax.swing.*;
import java.awt.*;

public class DataWindow extends JFrame {

    private JTextArea outputArea = new JTextArea();

    private JTextField weaponField = new JTextField();
    private JTextField versionField = new JTextField();

    public DataWindow() {
        this.setTitle("Data List App");
        this.setLayout(new GridLayout(4, 1));

        JPanel inputPanel = new JPanel(new GridLayout(2, 1));
        inputPanel.add(componentWithLabel(weaponField, "Weapon Type"));
        inputPanel.add(componentWithLabel(versionField, "Version Type"));
        this.add(inputPanel);

        JPanel statisticsPanel = new JPanel();
        this.add(statisticsPanel);

        JButton startButton = new JButton();
        startButton.addActionListener(e -> {
            outputArea.setText(DatabaseOperations.queryResults());
        });
        this.add(startButton);
        JPanel outputPanel = wrapInScrollPaneAndPanel(outputArea, "Output");
        this.add(outputPanel);
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

}
