package view;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JTableButtonRenderer extends JButton implements TableCellRenderer {
    public JTableButtonRenderer() {
        System.out.println("JTableButtonRenderer constructor");
        setOpaque(true);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object o, boolean isSelected, boolean b1, int i, int i1) {
        JButton button = (JButton) o;
//        if (isSelected) {
//            setForeground(table.getSelectionForeground());
//            setBackground(table.getSelectionBackground());
//        } else {
//            setForeground(table.getForeground());
//            setBackground(UIManager.getColor("Button.background"));
//        }
        return button;
    }
}
