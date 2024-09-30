package view;

import als.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Component.*;

public class ALSPanel extends JPanel {
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
   // private LBPanel lbPanel;

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
                    als.setDepth(Integer.parseInt(alsInfoPanel.getDepthALS().getText()));
                    als.updateALS();
                } catch (NumberFormatException ex) {
                    System.out.println("Введите целое число");
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
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
                    als.setHeight(Integer.parseInt(alsInfoPanel.getHeightALS().getText()));
                    als.updateALS();
                } catch (NumberFormatException ex) {
                    System.out.println("Введите целое число");
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
                    lb.setCountCells(Integer.parseInt(lbPanel.getNumCellsLB().getText()));
                    als.updateALS();
                } catch (NumberFormatException ex) {
                    System.out.println("Введите целое число");
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
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
                    System.out.println("Введите целое число");
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
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
                    System.out.println("Введите целое число");
                } catch (DimensionException ex) {
                    throw new RuntimeException(ex);
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
                    lb.setType((String) lbPanel.getTypeLb().getSelectedItem());
                    als.updateALS();
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
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
        System.out.println("ОБНОВЛЕНА [панель АКХ] " + als.getName());
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
