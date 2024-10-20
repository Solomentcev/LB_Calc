package view;

import als.ALS;
import als.Colors;
import als.LB;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ALSInfoPanel extends JPanel {
    private final JLabel nameALS=new JLabel();
    private final JLabel countLB=new JLabel();
    private final JLabel numCellsSumALS= new JLabel();
    private final JTextField heightALS=new JTextField(5);
    private final JLabel weightALS=new JLabel();
    private final JTextField depthALS=new JTextField(5);
    private final JLabel upperFrameALS=new JLabel();
    private final JLabel bottomFrameALS=new JLabel();
    private final JLabel depthCellALS=new JLabel();
    private final JComboBox<Colors> colorBody= new JComboBox<>();
    private final JComboBox<Colors> colorDoor= new JComboBox<>();
    private final JButton addLBButton= new JButton("Добавить МХ");
    private final JButton deleteALSButton= new JButton("Удалить");
    private final JPanel infoLbPanel=new JPanel();

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

        JLabel l9=new JLabel("Цвет корпуса:");
        this.add(l9);
        Colors[] colors=Colors.values();
        for (Colors color:colors){
            colorBody.addItem(color);
        }
        colorBody.setSelectedItem(als.getColorBody());

        JLabel l10=new JLabel("Цвет дверей:");
        this.add(l10);
        for (Colors color:colors){
            colorDoor.addItem(color);
        }
        colorDoor.setSelectedItem(als.getColorDoor());

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
        infoLbPanel.setLayout(new BoxLayout(infoLbPanel, BoxLayout.Y_AXIS));

        infoLbPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (Map.Entry<LB,Integer> lb:als.getUniqueLB().entrySet()){
            JLabel lb1=new JLabel(lb.getKey().getDescription()+" - "+lb.getValue()+" шт.");
            infoLbPanel.add(lb1);
            lb1.setAlignmentX(Component.LEFT_ALIGNMENT);

        }
        this.add(infoLbPanel);
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
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
                                    .addComponent(l9)
                                    .addComponent(l10)
                                    .addComponent(deleteALSButton)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(addLBButton)
                                    .addComponent(countLB)
                                    .addComponent(numCellsSumALS)
                                    .addComponent(heightALS)
                                    .addComponent(weightALS)
                                    .addComponent(depthALS)
                                    .addComponent(upperFrameALS)
                                    .addComponent(bottomFrameALS)
                                    .addComponent(depthCellALS)
                                    .addComponent(colorBody)
                                    .addComponent(colorDoor)
                                )
                        )
                        .addComponent(infoLbPanel)
                )
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
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l9)
                        .addComponent(colorBody)
                        )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l10)
                        .addComponent(colorDoor)
                        )
                .addComponent(deleteALSButton)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(infoLbPanel))
        );

    }

    public JComboBox<Colors> getColorBody() {
        return colorBody;
    }

    public JComboBox<Colors> getColorDoor() {
        return colorDoor;
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
        colorBody.setSelectedItem(als.getColorBody());
        colorDoor.setSelectedItem(als.getColorDoor());
        infoLbPanel.removeAll();
        infoLbPanel.setLayout(new BoxLayout(infoLbPanel, BoxLayout.Y_AXIS));

        for (Map.Entry<LB,Integer> lb:als.getUniqueLB().entrySet()){
            JLabel lb1=new JLabel(lb.getKey().getDescription()+" - "+lb.getValue()+" шт.");
            lb1.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoLbPanel.add(lb1);
        }
        revalidate();
        repaint();

    }
}
