package view;

import als.LB;
import als.TypeLb;

import javax.swing.*;
import java.awt.*;

public class LBPanel extends JPanel{
    private JLabel nameLB=new JLabel();
    private JButton deleteLBButton = new JButton("Удалить");
    private JComboBox<String> typeLb= new JComboBox<>();
    private JLabel heightLB=new JLabel();
    private JTextField widthLB =new JTextField(5);
    private JLabel depthLB=new JLabel();
    private JTextField numCellsLB=new JTextField(5);
    private JLabel heightCellLB=new JLabel();
    private JTextField widthCellLB =new JTextField(5);
    private JLabel depthCellLB=new JLabel();
    private JPanel imageLBPanel=new JPanel();
    private DrawLB drawLB;
    public JButton getDeleteLBButton() {
        return deleteLBButton;
    }
    public JTextField getWidthLB() {
        return widthLB;
    }
    public JTextField getNumCellsLB() {
        return numCellsLB;
    }
    public JTextField getWidthCellLB() {
        return widthCellLB;
    }
    public JComboBox<String> getTypeLb() {
        return typeLb;
    }
    public LBPanel(LB lb) {
        this.nameLB.setText(lb.getName()); //"nameLB"
        this.add(this.nameLB);
        this.add(deleteLBButton);

        JLabel lbType=new JLabel("Тип модуля: ");
        TypeLb[] types=TypeLb.values();
        for(TypeLb type:types){
            typeLb.addItem(String.valueOf(type));
        }
        typeLb.setSelectedItem(String.valueOf(lb.getType()));

        JLabel l1=new JLabel("Высота, мм: ");
        // this.add(l1);
        this.heightLB.setText(String.valueOf(lb.getHeightLB()));
       // this.add(this.heightLB);

        JLabel l2=new JLabel("Ширина, мм: ");
        this.add(l2);
        this.widthLB.setText(String.valueOf(lb.getWidth()));
        this.add(this.widthLB);

        JLabel l3=new JLabel("Глубина, мм: ");
        this.add(l3);
        this.depthLB.setText(String.valueOf(lb.getDepth()));
        this.add(this.depthLB);

        JLabel l4=new JLabel("Кол-во ячеек: ");
        this.add(l4);
        this.numCellsLB.setText(String.valueOf(lb.getCountCells()));
        this.numCellsLB.setColumns(5);
        this.add(this.numCellsLB);

        JLabel l5=new JLabel("Высота ячеек, мм: ");
        this.add(l5);
        this.heightCellLB.setText(String.valueOf(lb.getHeightCell()));
        this.add(this.heightCellLB);

        JLabel l6=new JLabel("Ширина ячеек, мм: ");
        this.add(l6);
        this.widthCellLB.setText(String.valueOf(lb.getWidthCell()));
        this.add(this.widthCellLB);
        this.widthCellLB.setColumns(5);

        JLabel l7=new JLabel("Глубина ячеек, мм: ");
        this.add(l7);
        this.depthCellLB.setText(String.valueOf(lb.getDepthCell()));
        this.add(this.depthCellLB);

        this.add(imageLBPanel);
        drawLB =new DrawLB(lb);
        imageLBPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,3));

        imageLBPanel.add(drawLB,BorderLayout.CENTER);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(nameLB)
                                    .addComponent(lbType)
                                    .addComponent(l1)
                                    .addComponent(l2)
                                    .addComponent(l3)
                                    .addComponent(l4)
                                    .addComponent(l5)
                                    .addComponent(l6)
                                    .addComponent(l7)
                                )
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(deleteLBButton)
                                        .addComponent(typeLb)
                                        .addComponent(heightLB)
                                        .addComponent(widthLB)
                                        .addComponent(depthLB)
                                        .addComponent(numCellsLB)
                                        .addComponent(heightCellLB)
                                        .addComponent(widthCellLB)
                                        .addComponent(depthCellLB)
                                )
                        )
                        .addComponent(imageLBPanel)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(nameLB)
                        .addComponent(deleteLBButton))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(lbType)
                        .addComponent(typeLb))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l1)
                        .addComponent(heightLB))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l2)
                        .addComponent(widthLB))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l3)
                        .addComponent(depthLB))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l4)
                        .addComponent(numCellsLB))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l5)
                        .addComponent(heightCellLB))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l6)
                        .addComponent(widthCellLB))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(l7)
                        .addComponent(depthCellLB))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(imageLBPanel))
        );
    }
    public void refreshLBPanel(LB lb, int number){
        nameLB.setText(lb.getName());
        heightLB.setText(String.valueOf(lb.getHeightLB()));
        widthLB.setText(String.valueOf(lb.getWidth()));
        depthLB.setText(String.valueOf(lb.getDepth()));
        numCellsLB.setText(String.valueOf(lb.getCountCells()));
        heightCellLB.setText(String.valueOf(lb.getHeightCell()));
        widthCellLB.setText(String.valueOf(lb.getWidthCell()));
        depthCellLB.setText(String.valueOf(lb.getDepthCell()));
        typeLb.setSelectedItem(String.valueOf(lb.getType()));
        drawLB.refreshDrawLB(lb);
        imageLBPanel.revalidate();
        imageLBPanel.repaint();
        System.out.println("ОБНОВЛЕНА [панель модуля хранения]№"+number+": "+lb.getName());
    }

}
