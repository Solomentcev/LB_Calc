package view;

import als.ALS;
import als.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

public class PreviewImageProject extends JFrame  {
    JPanel imagePanel;

    public JPanel getImagePanel() {
        return imagePanel;
    }

    public PreviewImageProject(Project project) {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        addWindowListener(new WindowAdapter() {
        });
        setVisible(true);
        imagePanel=new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane=new JScrollPane(imagePanel);
        this.add(scrollPane);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        for (ALS als:project.getAlsList()){
            JLabel description=new JLabel(als.getDescription());
            imagePanel.add(description);

            DrawALS a=new DrawALS(als);
            a.setAlignmentX(Component.CENTER_ALIGNMENT);
            imagePanel.add(a);

        }
        setMinimumSize(new Dimension(500,200));
        pack();
    }

}
