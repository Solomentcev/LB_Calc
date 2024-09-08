package view;

import als.*;

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
    private JComboBox<String> display= new JComboBox<>();
    private JComboBox<String> barReader= new JComboBox<>();
    private JComboBox<String> payment= new JComboBox<>();
    private JCheckBox printer= new JCheckBox();
    private JCheckBox rfidReader= new JCheckBox();
    private JComboBox<String> positionLC= new JComboBox<>();
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
                ALSPanel alsPanel= (ALSPanel) getParent().getParent();
                alsPanel.refreshALSPanel(lc.getParentALS());
                getParent().repaint();
            }
        });

        JLabel l3=new JLabel("Глубина, мм: ");
        this.add(l3);
        this.depthLC.setText(String.valueOf(lc.getDepth()));
        this.add(this.depthLC);
        JLabel l4=new JLabel("Дисплей: ");
        this.add(l4);
        DisplayLC[] displays=DisplayLC.values();
        for(DisplayLC displayLC:displays){
            display.addItem(String.valueOf(displayLC));
        }
        display.setSelectedItem(String.valueOf(lc.getDisplay()));
        display.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lc.setDisplay(DisplayLC.valueOf(String.valueOf(display.getSelectedItem())));
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
                }
                refreshLCPanel(lc);
                ALSPanel alsPanel= (ALSPanel) getParent().getParent();
                alsPanel.refreshALSPanel(lc.getParentALS());
                getParent().repaint();
            }
        });

        JLabel l5=new JLabel("Считыватель: ");
        this.add(l5);
        BarReader[] barReaders=BarReader.values();
        for (BarReader barReader1:barReaders){
            barReader.addItem(String.valueOf(barReader1));
        }
        barReader.setSelectedItem(String.valueOf(lc.getBarReader()));
        barReader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lc.setBarReader(BarReader.valueOf(String.valueOf(barReader.getSelectedItem())));
                refreshLCPanel(lc);
                ALSPanel alsPanel= (ALSPanel) getParent().getParent();
                alsPanel.refreshALSPanel(lc.getParentALS());
                getParent().repaint();
            }
        });
        JLabel l6=new JLabel("Сканер: ");
        this.add(l6);
        rfidReader.setSelected(lc.isRfidReader());
        rfidReader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lc.setRfidReader(rfidReader.isSelected());
                refreshLCPanel(lc);
                ALSPanel alsPanel= (ALSPanel) getParent().getParent();
                alsPanel.refreshALSPanel(lc.getParentALS());
                getParent().repaint();
            }
        });

        JLabel l7=new JLabel("Принтер: ");
        this.add(l7);
        printer.setSelected(lc.isPrinter());
        printer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lc.setPrinter(printer.isSelected());
                refreshLCPanel(lc);
                ALSPanel alsPanel= (ALSPanel) getParent().getParent();
                alsPanel.refreshALSPanel(lc.getParentALS());
                getParent().repaint();
            }
        });
        JLabel l8=new JLabel("Оплата: ");
        this.add(l8);
        Payment[] payments=Payment.values();
        for (Payment payment1:payments){
            payment.addItem(String.valueOf(payment1));
        }
        payment.setSelectedItem(String.valueOf(lc.getPayment()));
        payment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lc.setPayment(Payment.valueOf(String.valueOf(payment.getSelectedItem())));
                refreshLCPanel(lc);
                ALSPanel alsPanel= (ALSPanel) getParent().getParent();
                alsPanel.refreshALSPanel(lc.getParentALS());
                getParent().repaint();
            }
        });
        JLabel l9=new JLabel("Расположение МУ: ");
        this.add(l9);
        PositionLC[] positionLC1=PositionLC.values();
        for (PositionLC positionLC2:positionLC1){
            positionLC.addItem(String.valueOf(positionLC2));
        }
        positionLC.setSelectedItem((String.valueOf(lc.getParentALS().getPositionLC())));
        positionLC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lc.getParentALS().setPositionLC(PositionLC.valueOf(String.valueOf(positionLC.getSelectedItem())));
                refreshLCPanel(lc);
                ALSPanel alsPanel= (ALSPanel) getParent().getParent();
                alsPanel.refreshALSPanel(lc.getParentALS());
                getParent().repaint();
            }
        });

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
                                .addComponent(l4)
                                .addComponent(l5)
                                .addComponent(l6)
                                .addComponent(l7)
                                .addComponent(l8)
                                .addComponent(l9)
                        )
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(heightLC)
                                .addComponent(widthLC)
                                .addComponent(depthLC)
                                .addComponent(display)
                                .addComponent(rfidReader)
                                .addComponent(barReader)
                                .addComponent(printer)
                                .addComponent(payment)
                                .addComponent(positionLC)

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
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l4)
                        .addComponent(display))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l5)
                        .addComponent(rfidReader))

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l6)
                        .addComponent(barReader))

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l7)
                        .addComponent(printer))

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l8)
                        .addComponent(payment))

                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l9)
                        .addComponent(positionLC)
                        )
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
