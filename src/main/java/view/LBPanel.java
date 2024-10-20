package view;

import als.LB;
import als.TypeLb;

import javax.swing.*;
import java.awt.*;

public class LBPanel extends JPanel{
    private final JLabel nameLB=new JLabel();
    private final JButton deleteLBButton = new JButton("Удалить");
    private final JComboBox<String> typeLb= new JComboBox<>();
    private final JLabel heightLB=new JLabel();
    private final JTextField widthLB =new JTextField(5);
    private final JLabel depthLB=new JLabel();
    private final JTextField numCellsLB=new JTextField(5);
    private final JLabel heightCellLB=new JLabel();
    private final JTextField widthCellLB =new JTextField(5);
    private final JLabel depthCellLB=new JLabel();
    private final JPanel imageLBPanel=new JPanel();
    private final DrawLB drawLB;
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
        nameLB.setText(lb.getName());

        JLabel lbType=new JLabel("Тип модуля: ");
        TypeLb[] types=TypeLb.values();
        for(TypeLb type:types){
            typeLb.addItem(String.valueOf(type));
        }
        typeLb.setSelectedItem(String.valueOf(lb.getType()));
        JLabel l1=new JLabel("Высота, мм: ");
        heightLB.setText(String.valueOf(lb.getHeightLB()));
        JLabel l2=new JLabel("Ширина, мм: ");
        widthLB.setText(String.valueOf(lb.getWidth()));
        JLabel l3=new JLabel("Глубина, мм: ");
        depthLB.setText(String.valueOf(lb.getDepth()));
        JLabel l4=new JLabel("Кол-во ячеек: ");
        numCellsLB.setText(String.valueOf(lb.getCountCells()));
        numCellsLB.setColumns(5);
        JLabel l5=new JLabel("Высота ячеек, мм: ");
        heightCellLB.setText(String.valueOf(lb.getHeightCell()));
        JLabel l6=new JLabel("Ширина ячеек, мм: ");
        widthCellLB.setText(String.valueOf(lb.getWidthCell()));
        widthCellLB.setColumns(5);
        JLabel l7=new JLabel("Глубина ячеек, мм: ");
        depthCellLB.setText(String.valueOf(lb.getDepthCell()));
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

    }

}
