package view;

import als.DimensionException;
import als.LC;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LCPanel extends JPanel {
    private JLabel nameLC;
    private JLabel heightLC;
    private JTextField widthLC;
    private JLabel depthLC;
    private JPanel imageLCPanel;
    private DrawLC drawLC;

    public LCPanel(LC lc) {
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLoweredBevelBorder(), "LCPanel"));
        // this.setLayout(new GridLayout(5,2));
        nameLC=new JLabel(lc.getName());
        heightLC=new JLabel();
        widthLC =new JTextField(5);
        depthLC=new JLabel();
        imageLCPanel=new JPanel();
        this.add(this.nameLC);
        JLabel l1=new JLabel("Высота, мм: ");
        this.add(l1);
        this.heightLC.setText(String.valueOf(lc.getHeight()));
        this.add(this.heightLC);

        JLabel l2=new JLabel("Ширина, мм: ");
        this.add(l2);
        this.widthLC.setText(String.valueOf(lc.getWidth()));
        this.add(this.widthLC);
        widthLC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lc.setWidth(Integer.parseInt(widthLC.getText()));
                } catch (NumberFormatException ex) {
                    System.out.println("Введите целое число");
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
                }
                refreshLCPanel(lc);
                ALSPanel alsPanel= (ALSPanel) getParent();
                alsPanel.refreshALSPanel(lc.getParentALS());
                getParent().repaint();
            }
        });

        JLabel l3=new JLabel("Глубина, мм: ");
        this.add(l3);
        this.depthLC.setText(String.valueOf(lc.getDepth()));
        this.add(this.depthLC);

        this.add(imageLCPanel);
        imageLCPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));
        drawLC =new DrawLC(lc);
        imageLCPanel.add(drawLC,BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(nameLC)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(l1)
                                .addComponent(l2)
                                .addComponent(l3)
                            )
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(heightLC)
                                .addComponent(widthLC)
                                .addComponent(depthLC)
                            )
                        )
                        .addComponent(imageLCPanel)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(nameLC)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l1)
                        .addComponent(heightLC))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l2)
                        .addComponent(widthLC))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l3)
                        .addComponent(depthLC))
                .addComponent(imageLCPanel)
        );

    }
    public void refreshLCPanel(LC lc){
        nameLC.setText(lc.getName());
        heightLC.setText(String.valueOf(lc.getHeight()));
        widthLC.setText(String.valueOf(lc.getWidth()));
        depthLC.setText(String.valueOf(lc.getDepth()));
        drawLC.refreshDrawLC(lc);
        imageLCPanel.revalidate();
        imageLCPanel.repaint();
        System.out.println("ОБНОВЛЕНА [панель модуля управления] "+lc.getName());
    }
}
