package view;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;

    public ButtonEditor() {
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(label + " : " + e.getActionCommand());
                fireEditingStopped();

            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object o, boolean isSelected, int i, int i1) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (o == null) ? "" : o.toString();
        button.setText(label);
        isPushed = true;

        return button;
    }

    @Override
    public Object getCellEditorValue() {
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return true;
    }
}
