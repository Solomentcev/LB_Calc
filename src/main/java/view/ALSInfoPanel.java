package view;

import als.ALS;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ALSInfoPanel extends JPanel {
    private JLabel nameALS=new JLabel();
    private JLabel countLB=new JLabel();
    private JLabel numCellsSumALS= new JLabel();
    private JTextField heightALS=new JTextField(5);
    private JLabel weightALS=new JLabel();
    private JTextField depthALS=new JTextField(5);
    private JLabel upperFrameALS=new JLabel();
    private JLabel bottomFrameALS=new JLabel();
    private JLabel depthCellALS=new JLabel();
    private JButton addLBButton= new JButton("Добавить Модуль хранения");
    private JButton deleteALSButton= new JButton("Удалить");

    public JTextField getHeightALS() {
        return heightALS;
    }

    public JTextField getDepthALS() {
        return depthALS;
    }

    public JButton getAddLBButton() {
        return addLBButton;
    }

    public JButton getDeleteALSButton() {
        return deleteALSButton;
    }

    public ALSInfoPanel(ALS als) {
        this.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLoweredBevelBorder(), "ALSInfoPanel"));
        // this.setLayout(new GridLayout(9,2));
        this.nameALS.setText(als.getName());
        this.add(this.nameALS);
        this.add(this.addLBButton);


        JLabel l1=new JLabel("Кол-во модулей: ");
        this.add(l1);
        this.countLB.setText(String.valueOf(als.getLbList().size()));
        this.add(this.countLB);

        JLabel l2=new JLabel("Общее кол-во ячеек: ");
        this.add(l2);
        this.numCellsSumALS.setText(String.valueOf(als.getCountCells()));
        this.add(this.numCellsSumALS);

        JLabel l3=new JLabel("Высота АКХ, мм: ");
        this.add(l3);
        this.heightALS.setText(String.valueOf(als.getHeight()));

        this.add(this.heightALS);


        JLabel l4=new JLabel("Ширина АКХ, мм:");
        this.add(l4);
        this.weightALS.setText(String.valueOf(als.getWidth()));
        this.add(this.weightALS);

        JLabel l5=new JLabel("Глубина АКХ, мм: ");
        this.add(l5);
        this.depthALS.setText(String.valueOf(als.getDepth()));

        this.add(this.depthALS);

        JLabel l6=new JLabel("Высота верхней рамы,мм: ");
        this.add(l6);
        this.upperFrameALS.setText(String.valueOf(als.getUpperFrame()));
        this.add(this.upperFrameALS);

        JLabel l7=new JLabel("Высота нижней рамы,мм: ");
        this.add(l7);
        this.bottomFrameALS.setText(String.valueOf(als.getBottomFrame()));
        this.add(this.bottomFrameALS);

        JLabel l8=new JLabel("Глубина ячеек АКХ, мм: ");
        this.add(l8);
        this.depthCellALS.setText(String.valueOf(als.getDepthCell()));
        this.add(this.depthCellALS);
        this.add(deleteALSButton);
        deleteALSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                als.getParentProject().deleteALS(als);
                ALSPanel alsPanel= (ALSPanel) getParent().getParent();
                getParent().getParent().getParent().remove(alsPanel);

                getParent().getParent().revalidate();
                repaint();
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameALS)
                        .addComponent(l1)
                        .addComponent(l2)
                        .addComponent(l3)
                        .addComponent(l4)
                        .addComponent(l5)
                        .addComponent(l6)
                        .addComponent(l7)
                        .addComponent(l8)
                        .addComponent(deleteALSButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(addLBButton)
                        .addComponent(countLB)
                        .addComponent(numCellsSumALS)
                        .addComponent(heightALS)
                        .addComponent(weightALS)
                        .addComponent(depthALS)
                        .addComponent(upperFrameALS)
                        .addComponent(bottomFrameALS)
                        .addComponent(depthCellALS))
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameALS)
                        .addComponent(addLBButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l1)
                        .addComponent(countLB))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l2)
                        .addComponent(numCellsSumALS))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l3)
                        .addComponent(heightALS))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l4)
                        .addComponent(weightALS))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l5)
                        .addComponent(depthALS))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l6)
                        .addComponent(upperFrameALS))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l7)
                        .addComponent(bottomFrameALS))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l8)
                        .addComponent(depthCellALS))
                .addComponent(deleteALSButton)
        );
    }
    public void refreshALSInfo(ALS als){
        nameALS.setText(als.getName());
        countLB.setText(String.valueOf(als.getLbList().size()));
        numCellsSumALS.setText(String.valueOf(als.getCountCells()));
        heightALS.setText(String.valueOf(als.getHeight()));
        weightALS.setText(String.valueOf(als.getWidth()));
        depthALS.setText(String.valueOf(als.getDepth()));
        upperFrameALS.setText(String.valueOf(als.getUpperFrame()));
        bottomFrameALS.setText(String.valueOf(als.getBottomFrame()));
        depthCellALS.setText(String.valueOf(als.getDepthCell()));

        System.out.println("ОБНОВЛЕНА [панель инфо АКХ]: "+als.getName());
        System.out.println("-----------");
    }
}
