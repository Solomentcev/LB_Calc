package view;

import als.ALS;
import als.LB;
import als.LC;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DrawALS extends JPanel {
    private int height;
    private int depth;
    private int width;
    private int upperFrame;
    private int bottomFrame;
    private int depthCell;
    private int countCells;
    private LC lc;
    private List<LB> lbList;

    public DrawALS(ALS als) {
        this.height = als.getHeight();
        this.depth = als.getDepth();
        this.width = als.getWidth();
        this.upperFrame = als.getUpperFrame();
        this.bottomFrame = als.getBottomFrame();
        this.lc =als.getLc();
        this.lbList = als.getLbList();
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRoundRect(0,0, lc.getWidth()/10,lc.getHeight()/10, 1,1);//габариты модуля
        g.drawRoundRect(0,0, lc.getWidth()/10,upperFrame/10, 1,1); //верхняя рама
        g.drawRoundRect(0,(lc.getHeight()-bottomFrame)/10, lc.getWidth()/10,bottomFrame/10, 1,1);//нижняя рама
        g.drawRoundRect(((lc.getWidth()-255)/2)/10,(625)/10, 255/10,175/10, 1,1);//дисплей
        g.drawRoundRect(0,upperFrame/10, lc.getWidth()/10,(lc.getHeight()-1100)/10, 1,1);//панель
        int x=lc.getWidth()/10;
        for (LB lb : lbList) {
            g.drawRoundRect(x, 0, lb.getWidth() / 10, lb.getHeight() / 10, 1, 1);//габариты модуля

            g.drawRoundRect(x, 0, lb.getWidth() / 10, upperFrame / 10, 1, 1); //верхняя рама
            g.drawRoundRect(x, (lb.getHeight() - bottomFrame) / 10, lb.getWidth() / 10, bottomFrame / 10, 1, 1);//нижняя рама

            g.drawRoundRect(x + (lb.getWidth() - lb.getType().getServiceZoneWidth()) / 10, (upperFrame) / 10, lb.getType().getServiceZoneWidth() / 10, (lb.getHeight() - upperFrame - bottomFrame) / 10, 1, 1);// сервисная планка

            for (int j = 2; j <= lb.getCountCells(); j++) {
                g.drawRoundRect(x, (int) ((lb.getHeight() - upperFrame - (lb.getHeightCell() * (j - 1) + lb.getShelfThick() * j)) / 10), (lb.getWidth() - lb.getType().getServiceZoneWidth()) / 10, lb.getShelfThick() / 10, 1, 1);
            }
            x = x + lb.getWidth() / 10;
        }
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
    }
    public void refreshDrawAls(ALS als){
        this.height = als.getHeight();
        this.depth = als.getDepth();
        this.width = als.getWidth();
        this.upperFrame = als.getUpperFrame();
        this.bottomFrame = als.getBottomFrame();
        this.lc =als.getLc();
        this.lbList = als.getLbList();
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
        revalidate();
        repaint();
    }
}
