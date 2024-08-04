package view;

import als.LB;
import als.LC;

import javax.swing.*;
import java.awt.*;

public class DrawLC extends JPanel {
    private int height;
    private int width;
    private int depth;
    private int upperFrame;
    private int bottomFrame;
    public DrawLC(LC lc){
        width =lc.getWidth();
        height=lc.getHeight();
        bottomFrame=lc.getParentALS().getBottomFrame();
        upperFrame=lc.getParentALS().getUpperFrame();

        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0,0, width/10,height/10, 1,1);//габариты модуля
        g.drawRoundRect(0,0, width/10,upperFrame/10, 1,1); //верхняя рама
        g.drawRoundRect(0,(height-bottomFrame)/10, width/10,bottomFrame/10, 1,1);//нижняя рама
        g.drawRoundRect(((width-255)/2)/10,(625)/10, 255/10,175/10, 1,1);//дисплей
        g.drawRoundRect(0,upperFrame/10, width/10,(height-1100)/10, 1,1);//панель
    }
    public void refreshDrawLC(LC lc){
        width =lc.getWidth();
        height=lc.getHeight();
        bottomFrame=lc.getParentALS().getBottomFrame();
        upperFrame=lc.getParentALS().getUpperFrame();
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
        revalidate();
        repaint();
    }
}
