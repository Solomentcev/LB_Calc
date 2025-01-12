package view;

import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ALSPanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(ALSPanel.class);
    private final ALSInfoPanel alsInfoPanel;
    private final LCPanel lcpanel;
    private final DrawALS imageALSPanel;

    public List<LBPanel> getLBPanelList() {
        return LBPanelList;
    }
    JPanel p1=new JPanel();
    JPanel p2=new JPanel();
    private final List<LBPanel> LBPanelList;
    private final ALS als;

    public ALSPanel(ALS als) {
        this.als=als;
        this.setLayout(new BorderLayout());
        p1.setLayout (new FlowLayout(FlowLayout.LEFT));
        this.add(p1, BorderLayout.NORTH);
        alsInfoPanel = new ALSInfoPanel(this.als);
        p1.add(alsInfoPanel);

        lcpanel = new LCPanel(als.getLc());
        p1.add(lcpanel);

        LBPanelList=new ArrayList<>();
        for (LB lb : als.getLbList()) {
            addLBPanel(lb);
        }
        this.add(p1, BorderLayout.NORTH);

        imageALSPanel=new DrawALS(als);
        p2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED ));
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        p2.add(imageALSPanel);
        this.add(p2,BorderLayout.SOUTH);


        alsInfoPanel.getDepthALS().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    als.changeDepth((Integer.parseInt(alsInfoPanel.getDepthALS().getText())));

                } catch (NumberFormatException ex) {
                    logger.error("Введите целое число");
                } catch (DimensionException ex) {
                     logger.error(ex.getMessage());

                }
                alsInfoPanel.refreshALSInfo(als);
                refreshALSPanel(als);
                imageALSPanel.refreshDrawAls(als);
                revalidate();
                repaint();
            }

        });
        alsInfoPanel.getHeightALS().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    als.changeHeight(Integer.parseInt(alsInfoPanel.getHeightALS().getText()));
                } catch (NumberFormatException ex) {
                    logger.error("Введите целое число");
                } catch (DimensionException ex) {
                    logger.error(ex.getMessage());
                }
                alsInfoPanel.refreshALSInfo(als);
                refreshALSPanel(als);
                imageALSPanel.refreshDrawAls(als);
                revalidate();
                repaint();
            }
        });
        alsInfoPanel.getColorDoor().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                als.setColorDoor(Colors.valueOf(String.valueOf(alsInfoPanel.getColorDoor().getSelectedItem())));
                alsInfoPanel.refreshALSInfo(als);
                refreshALSPanel(als);
                imageALSPanel.refreshDrawAls(als);
                revalidate();
                repaint();
            }
        });
        alsInfoPanel.getColorBody().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                als.setColorBody(Colors.valueOf(String.valueOf(alsInfoPanel.getColorBody().getSelectedItem())));
                alsInfoPanel.refreshALSInfo(als);
                refreshALSPanel(als);
                imageALSPanel.refreshDrawAls(als);
                revalidate();
                repaint();
            }
        });
        alsInfoPanel.getAddLBButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLBPanel(als.addLb());
                als.updateALS();
                alsInfoPanel.refreshALSInfo(als);
                imageALSPanel.refreshDrawAls(als);
                revalidate();
                repaint();
            }
        });

    }

    public LBPanel addLBPanel(LB lb){
        LBPanel lbPanel = new LBPanel(lb);
        LBPanelList.add(lbPanel);
        lbPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLoweredBevelBorder(), "LBPanel"+LBPanelList.size()));
        lbPanel.getNumCellsLB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lb.changeCountCells(Integer.parseInt(lbPanel.getNumCellsLB().getText()));
                    als.updateALS();
                } catch (NumberFormatException ex) {
                   logger.error("Введите целое число");
                } catch (DimensionException ex) {
                    logger.error(ex.getMessage());

                }
                lbPanel.refreshLBPanel(lb,LBPanelList.indexOf(lbPanel)+1);
                alsInfoPanel.refreshALSInfo(als);
                imageALSPanel.refreshDrawAls(als);
                revalidate();
                repaint();
            }
        });

        lbPanel.getWidthCellLB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lb.setWidthCell(Integer.parseInt(lbPanel.getWidthCellLB().getText()));
                    als.updateALS();
                } catch (NumberFormatException ex) {
                    logger.error("Введите целое число");
                } catch (DimensionException ex) {
                    logger.error(ex.getMessage());
                }
                lbPanel.refreshLBPanel(lb,LBPanelList.indexOf(lbPanel)+1);
                alsInfoPanel.refreshALSInfo(als);
                imageALSPanel.refreshDrawAls(als);
                revalidate();
                repaint();
            }
        });

        lbPanel.getWidthLB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lb.setWidth(Integer.parseInt(lbPanel.getWidthLB().getText()));
                    als.updateALS();
                } catch (NumberFormatException ex) {
                    logger.error("Введите целое число");
                } catch (DimensionException ex) {
                    logger.error(ex.getMessage());
                }
                lbPanel.refreshLBPanel(lb,LBPanelList.indexOf(lbPanel)+1);
                alsInfoPanel.refreshALSInfo(lb.getParentALS());
                imageALSPanel.refreshDrawAls(lb.getParentALS());
                revalidate();
                repaint();
            }
        });

        lbPanel.getDeleteLBButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                als.deleteLB(lb);
                deleteLBPanel(lbPanel);
                refreshALSPanel(als);
                imageALSPanel.refreshDrawAls(als);
                revalidate();
                repaint();
            }
        });
        lbPanel.getTypeLb().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lb.changeType((String) lbPanel.getTypeLb().getSelectedItem());
                    als.updateALS();
                } catch (DimensionException ex) {
                    logger.error(ex.getMessage());
                }
                lbPanel.refreshLBPanel(lb,LBPanelList.indexOf(lbPanel)+1);
                alsInfoPanel.refreshALSInfo(als);
                imageALSPanel.refreshDrawAls(als);
                revalidate();
                repaint();
            }
        });
        p1.add(lbPanel);
        alsInfoPanel.refreshALSInfo(als);
        revalidate();
        repaint();
        return lbPanel;

    }
    public void refreshALSPanel(ALS als){
        alsInfoPanel.refreshALSInfo(als);
        lcpanel.refreshLCPanel(als.getLc());
        for (LBPanel lbPanel : LBPanelList) {
            lbPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLoweredBevelBorder(), "LBPanel"+(LBPanelList.indexOf(lbPanel)+1)));
            lbPanel.refreshLBPanel(als.getLbList().get(LBPanelList.indexOf(lbPanel)),LBPanelList.indexOf(lbPanel)+1);
        }
        logger.debug("Обновление ALSPanel");
        revalidate();
        repaint();
    }
    public void deleteLBPanel(LBPanel lbpanel){
        LBPanelList.remove(lbpanel);
        p1.remove(lbpanel);
        revalidate();
        repaint();
    }

}
