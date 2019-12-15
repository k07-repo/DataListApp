package net.k07.datalist;

import javax.swing.*;
import java.awt.*;

public class ComponentUtils {
    public static JPanel componentWithLabel(Component c, String name) {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel(name));
        panel.add(c);
        return panel;
    }
}
