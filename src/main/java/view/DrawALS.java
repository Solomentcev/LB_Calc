package view;

import model.*;

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
    private PositionLC positionLC;
    private LC lc;
    private List<LB> lbList;
    private Color doorColor;
    private Color bodyColor;

    public DrawALS(ALS als) {
        this.height = als.getHeight();
        this.depth = als.getDepth();
        this.width = als.getWidth();
        this.upperFrame = als.getUpperFrame();
        this.bottomFrame = als.getBottomFrame();
        this.lc =als.getLc();
        this.positionLC=als.getPositionLC();
        this.lbList = als.getLbList();
        this.doorColor=als.getColorDoor().getColor();
        this.bodyColor=als.getColorBody().getColor();
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D)g).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        ((Graphics2D)g).setRenderingHint ( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
        int x=0;
        if (this.positionLC.equals(PositionLC.LEFT)) {
            drawLC(g,x);
            x = x + (lc.getWidth() / 10);
        }
        for (int i = 0; i < lbList.size(); i++) {
            LB lb = lbList.get(i);
            if ((positionLC.equals(PositionLC.CENTER)) && (i==lbList.size()/2)) {
                drawLC(g,x);
                x = x + (lc.getWidth() / 10);

            }
            drawLB(g,x,lb);
            x = x + (lb.getWidth() / 10);
        }
        if (positionLC.equals(PositionLC.RIGHT)) {
            drawLC(g,x);
        }
        this.setPreferredSize(new Dimension(((width+10)/10+50), ((height+10)/10)+50));
    }
    public void drawLC(Graphics g, int x){
        g.setColor(bodyColor);
        g.fillRoundRect(x,0, lc.getWidth()/10,lc.getHeight()/10, 1,1);//габариты модуля
        g.setColor(Color.BLACK);
        g.drawRoundRect(x,0, lc.getWidth()/10,lc.getHeight()/10, 1,1);//габариты модуля
        g.drawRoundRect(x,0, lc.getWidth()/10,upperFrame/10, 1,1); //верхняя рама
        g.drawRoundRect(x,(lc.getHeight()-bottomFrame)/10, lc.getWidth()/10,bottomFrame/10, 1,1);//нижняя рама
        g.fillRoundRect(x+((lc.getWidth()-lc.getDisplay().getDisplayWidth())/2)/10,(lc.getHeight()-1300-lc.getDisplay().getDisplayHeight())/10,
                lc.getDisplay().getDisplayWidth() /10,lc.getDisplay().getDisplayHeight()/10, 1,1);//дисплей
        g.drawRoundRect(x,upperFrame/10, lc.getWidth()/10,(lc.getHeight()-1100)/10, 1,1);//панель
    }
    public void drawLB(Graphics g, int x, LB lb){
        g.setColor(doorColor);
        g.fillRoundRect(x,0, lb.getWidth()/10,lb.getHeight()/10, 1,1);//габариты модуля
        g.setColor(bodyColor);
        g.fillRoundRect(x,0, lb.getWidth()/10,upperFrame/10, 1,1); //верхняя рама
        g.fillRoundRect(x,(lb.getHeight()-bottomFrame)/10, lb.getWidth()/10,bottomFrame/10, 1,1);//нижняя рама
        if (lb.getDirectionDoorOpening()== DirectionDoorOpening.LEFT){
            g.fillRoundRect(x+(lb.getWidth()-lb.getType().getServiceZoneWidth())/10,(upperFrame)/10,
                    lb.getType().getServiceZoneWidth()/10,(lb.getHeight()-upperFrame- bottomFrame)/10, 1,1);// сервисная планка
            for (int i = 2; i <=lb.getCountCells() ; i++) {
                g.fillRoundRect(x, (int) ((lb.getHeight() - upperFrame - (lb.getHeightCell() * (i - 1) + lb.getShelfThick() * i)) / 10),
                        (lb.getWidth() - lb.getType().getServiceZoneWidth()) / 10, lb.getShelfThick() / 10, 1, 1);
            }
        } else {
            g.fillRoundRect(x,(upperFrame)/10,
                    lb.getType().getServiceZoneWidth()/10,(lb.getHeight()-upperFrame- bottomFrame)/10, 1,1);// сервисная планка
            for (int i = 2; i <=lb.getCountCells(); i++) {
                g.fillRoundRect((x+lb.getType().getServiceZoneWidth()/10), (int) ((lb.getHeight() - upperFrame - (lb.getHeightCell() * (i - 1) + lb.getShelfThick() * i)) / 10),
                        (lb.getWidth() - lb.getType().getServiceZoneWidth()) / 10, lb.getShelfThick() / 10, 1, 1);
            }
        }
        g.setColor(Color.BLACK);
        g.drawRoundRect(x,0, lb.getWidth()/10,lb.getHeight()/10, 1,1);//габариты модуля
        g.drawRoundRect(x,0, lb.getWidth()/10,upperFrame/10, 1,1); //верхняя рама
        g.drawRoundRect(x,(lb.getHeight()-bottomFrame)/10, lb.getWidth()/10,bottomFrame/10, 1,1);//нижняя рама
        if (lb.getDirectionDoorOpening()== DirectionDoorOpening.LEFT){
            g.drawRoundRect(x+(lb.getWidth()-lb.getType().getServiceZoneWidth())/10,(upperFrame)/10,
                    lb.getType().getServiceZoneWidth()/10,(lb.getHeight()-upperFrame- bottomFrame)/10, 1,1);// сервисная планка
            for (int i = 2; i <=lb.getCountCells() ; i++) {
                g.drawRoundRect(x, (int) ((lb.getHeight() - upperFrame - (lb.getHeightCell() * (i - 1) + lb.getShelfThick() * i)) / 10),
                        (lb.getWidth() - lb.getType().getServiceZoneWidth()) / 10, lb.getShelfThick() / 10, 1, 1);
            }
        } else {
            g.drawRoundRect(x,(upperFrame)/10,
                    lb.getType().getServiceZoneWidth()/10,(lb.getHeight()-upperFrame- bottomFrame)/10, 1,1);// сервисная планка
            for (int i = 2; i <=lb.getCountCells(); i++) {
                g.drawRoundRect((x+lb.getType().getServiceZoneWidth()/10), (int) ((lb.getHeight() - upperFrame - (lb.getHeightCell() * (i - 1) + lb.getShelfThick() * i)) / 10),
                        (lb.getWidth() - lb.getType().getServiceZoneWidth()) / 10, lb.getShelfThick() / 10, 1, 1);
            }
        }

    }
    public void refreshDrawAls(ALS als){
        this.height = als.getHeight();
        this.depth = als.getDepth();
        this.width = als.getWidth();
        this.upperFrame = als.getUpperFrame();
        this.bottomFrame = als.getBottomFrame();
        this.lc =als.getLc();
        this.lbList = als.getLbList();
        this.positionLC=als.getPositionLC();
        this.bodyColor=als.getColorBody().getColor();
        this.doorColor=als.getColorDoor().getColor();
        this.setPreferredSize(new Dimension((width+10)/10, (height+10)/10));
        revalidate();
        repaint();
    }
}
