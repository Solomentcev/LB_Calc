package view;

import als.LB;
import als.LC;

import javax.swing.*;
import java.awt.*;

public class DrawLC extends JPanel {
    private int height;
    private int width;
    private int depth;
    private int displayWidth;
    private int displayHeight;
    private int upperFrame;
    private int bottomFrame;
    private Color colorBody;
    public DrawLC(LC lc){
        width =lc.getWidth();
        height=lc.getHeight();
        bottomFrame=lc.getParentALS().getBottomFrame();
        upperFrame=lc.getParentALS().getUpperFrame();
        displayHeight=lc.getDisplay().getDisplayHeight();
        displayWidth=lc.getDisplay().getDisplayWidth();
        colorBody=lc.getColorBody().getColor();
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g.setColor(Color.BLACK);
        drawLC(g,0);
    }
    public void drawLC(Graphics g, int x){
        g.setColor(colorBody);
        g.fillRoundRect(x,0, width/10,height/10, 1,1);//габариты модуля

        g.setColor(Color.BLACK);
        g.drawRoundRect(x,0, width/10,height/10, 1,1);//габариты модуля
        g.drawRoundRect(x,0, width/10,upperFrame/10, 1,1); //верхняя рама
        g.drawRoundRect(x,(height-bottomFrame)/10, width/10,bottomFrame/10, 1,1);//нижняя рама
        g.fillRoundRect(((width-displayWidth)/2)/10,(height-1300-displayHeight)/10, displayWidth/10,displayHeight/10, 1,1);//дисплей
        g.drawRoundRect(x,upperFrame/10, width/10,(height-1100)/10, 1,1);//панель
    }
    public void refreshDrawLC(LC lc){
        width =lc.getWidth();
        height=lc.getHeight();
        bottomFrame=lc.getParentALS().getBottomFrame();
        upperFrame=lc.getParentALS().getUpperFrame();
        displayHeight=lc.getDisplay().getDisplayHeight();
        displayWidth=lc.getDisplay().getDisplayWidth();
        colorBody=lc.getParentALS().getColorBody().getColor();
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
        revalidate();
        repaint();
    }
}
