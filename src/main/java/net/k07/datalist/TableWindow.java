package net.k07.datalist;

import javax.swing.*;
import java.awt.*;

public class TableWindow extends JFrame {

    public TableWindow(JTable table, int count) {
        this.setLayout(new BorderLayout());
        this.setTitle("Query Output");

        JScrollPane pane = new JScrollPane(table);
        this.add(pane, BorderLayout.CENTER);

        JLabel label = new JLabel("Number of records returned: " + count);
        this.add(label, BorderLayout.SOUTH);

    }
}
