package net.k07.datalist;

import javax.swing.*;
import java.awt.*;

public class TableWindow extends JFrame {

    public TableWindow(JTable table) {
        this.setLayout(new GridLayout(1, 1));
        this.setTitle("Query Output");

        JScrollPane pane = new JScrollPane(table);
        this.add(pane);
    }
}
