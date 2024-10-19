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
        ((Graphics2D)this.getGraphics()).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        ((Graphics2D)this.getGraphics()).setRenderingHint ( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        ((Graphics2D)this.getGraphics()).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        ((Graphics2D)this.getGraphics()).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


        for (ALS als:project.getAlsList()){
            JLabel description=new JLabel(als.getDescription());
            imagePanel.add(description);

            DrawALS a=new DrawALS(als);
            a.setAlignmentX(Component.CENTER_ALIGNMENT);
            imagePanel.add(a);

        }

        pack();
    }

}
