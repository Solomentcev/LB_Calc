package view;

import als.LB;
import als.TypeLb;

import javax.swing.*;
import java.awt.*;

public class DrawLB extends JPanel {
    private int height;
    private int width;
    private int depth;
    private int upperFrame;
    private int bottomFrame;
    private int shelfThick;
    private int countCells;
    private double heightCell;
    private TypeLb typeLb;
    private int widthCell;
    private int depthCell;
    public DrawLB(LB lb){
        typeLb=lb.getType();
        width =lb.getWidth();
        height=lb.getHeight();
        bottomFrame=lb.getBottomFrame();
        upperFrame=lb.getUpperFrame();
        shelfThick=lb.getShelfThick();
        countCells=lb.getCountCells();
        heightCell=lb.getHeightCell();
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0,0, width/10,height/10, 1,1);//габариты модуля

        g.drawRoundRect(0,0, width/10,upperFrame/10, 1,1); //верхняя рама
        g.drawRoundRect(0,(height-bottomFrame)/10, width/10,bottomFrame/10, 1,1);//нижняя рама

         g.drawRoundRect((width-typeLb.getServiceZoneWidth())/10,(upperFrame)/10, typeLb.getServiceZoneWidth()/10,(height-upperFrame- bottomFrame)/10, 1,1);// сервисная планка

        for (int i = 2; i <=countCells ; i++) {
            g.drawRoundRect(0, (int) ((height-upperFrame-(heightCell*(i-1)+shelfThick*i))/10), (width-typeLb.getServiceZoneWidth())/10,shelfThick/10, 1,1);

        }
    }
    public void refreshDrawLB(LB lb){
        typeLb=lb.getType();
        width =lb.getWidth();
        height=lb.getHeight();
        bottomFrame=lb.getBottomFrame();
        upperFrame=lb.getUpperFrame();
        shelfThick=lb.getShelfThick();
        countCells=lb.getCountCells();
        heightCell=lb.getHeightCell();
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
    }
}
