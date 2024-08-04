package view;

import als.ALS;
import als.Project;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectPanel extends JPanel{
    private JPanel panel;
    private JLabel projectName;
    private JTextField companyLabel;
    private JButton addALSButton;

    private JPanel jpanel;
    private ALSPanel alsPanel;
    private List<ALSPanel> alsPanelList;

    public ProjectPanel(Project project) {
        projectName=new JLabel("Заказчик:");
        companyLabel=new JTextField( project.getCompany(),20);
        companyLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                project.setCompany(companyLabel.getText());
                JTabbedPane projectTabbedPane= (JTabbedPane) getParent();
                projectTabbedPane.setTitleAt(projectTabbedPane.getSelectedIndex(), project.getName() );
                revalidate();
                repaint();
            }
        });
        addALSButton=new JButton("Добавить АКХ");

        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLoweredBevelBorder(), "ProjectPanel"));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        panel=new JPanel();
        this.add(panel);
        panel.add(projectName);
        panel.add(companyLabel);
        panel.add(addALSButton);
        addALSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPanel(project.addALS());
                revalidate();
                repaint();
            }
        });

        jpanel=new JPanel();
        jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));

        alsPanelList=new ArrayList<>();
        for (ALS als:project.getAlsList()){

            alsPanel = new ALSPanel(als);
            alsPanelList.add(alsPanel);
            alsPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLoweredBevelBorder(), "ALSPanel"+(alsPanelList.indexOf(alsPanel)+1)));

            jpanel.add(alsPanel);
        }
        JScrollPane scrollPane=new JScrollPane(jpanel);
        this.add(scrollPane);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
    }
    public ALSPanel addPanel(ALS als){

        ALSPanel panel=new ALSPanel(als);
        alsPanelList.add(panel);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLoweredBevelBorder(), "ALSPanel"+(alsPanelList.indexOf(panel)+1)));
        jpanel.add(panel);
        return panel;
    }
}
