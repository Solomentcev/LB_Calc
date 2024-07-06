package view;

import als.ALS;
import als.DimensionException;
import als.LB;
import als.TypeLb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ALSPanel extends JPanel {
    private ALSInfoPanel alsInfoPanel;
    private LCPanel lcpanel;

    public List<LBPanel> getLBPanelList() {
        return LBPanelList;
    }

    private List<LBPanel> LBPanelList;
   // private LBPanel lbPanel;

    public ALSPanel(ALS als) {

        //this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        this.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.alsInfoPanel = new ALSInfoPanel(als);
        this.add(this.alsInfoPanel);

        this.lcpanel = new LCPanel(als.getLc());
        this.add(this.lcpanel);

        LBPanelList=new ArrayList<>();
        for (LB lb : als.getLbList()) {
            addLBPanel(lb);
        }
        alsInfoPanel.getDepthALS().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    als.setDepth(Integer.parseInt(alsInfoPanel.getDepthALS().getText()));
                } catch (NumberFormatException ex) {
                    System.out.println("Введите целое число");
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
                }
                alsInfoPanel.refreshALSInfo(als);
                refreshALSPanel(als);
                revalidate();
                repaint();
            }

        });
        alsInfoPanel.getHeightALS().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    als.setHeight(Integer.parseInt(alsInfoPanel.getHeightALS().getText()));
                } catch (NumberFormatException ex) {
                    System.out.println("Введите целое число");
                }
                alsInfoPanel.refreshALSInfo(als);
                refreshALSPanel(als);
                revalidate();
                repaint();
            }
        });
        alsInfoPanel.getAddLBButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addLBPanel(als.addLb());
                alsInfoPanel.refreshALSInfo(als);
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
                } catch (NumberFormatException ex) {
                    System.out.println("Введите целое число");
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
                }
                lbPanel.refreshLBPanel(lb,LBPanelList.indexOf(lbPanel)+1);
                alsInfoPanel.refreshALSInfo(lb.getParentALS());
                revalidate();
                repaint();
            }
        });

        lbPanel.getWidthCellLB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lb.setWidthCell(Integer.parseInt(lbPanel.getWidthCellLB().getText()));
                } catch (NumberFormatException ex) {
                    System.out.println("Введите целое число");
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
                }
                lbPanel.refreshLBPanel(lb,LBPanelList.indexOf(lbPanel)+1);
                alsInfoPanel.refreshALSInfo(lb.getParentALS());
                revalidate();
                repaint();
            }
        });

        lbPanel.getWidthLB().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lb.setWidth(Integer.parseInt(lbPanel.getWidthLB().getText()));
                } catch (NumberFormatException ex) {
                    System.out.println("Введите целое число");
                } catch (DimensionException ex) {
                    throw new RuntimeException(ex);
                }
                lbPanel.refreshLBPanel(lb,LBPanelList.indexOf(lbPanel)+1);
                alsInfoPanel.refreshALSInfo(lb.getParentALS());

                revalidate();
                repaint();
            }
        });

        lbPanel.getDeleteLBButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ALS als= lb.getParentALS();
                als.deleteLB(lb);
                deleteLBPanel(lbPanel);
                refreshALSPanel(als);
                revalidate();
                repaint();
            }
        });
        lbPanel.getTypeLb().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    lb.setType((String) lbPanel.getTypeLb().getSelectedItem());
                } catch (DimensionException ex) {
                    System.out.println(ex.getMessage());
                }
                lbPanel.refreshLBPanel(lb,LBPanelList.indexOf(lbPanel)+1);
                alsInfoPanel.refreshALSInfo(lb.getParentALS());
                revalidate();
                repaint();
            }
        });
        this.add(lbPanel);
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
        remove(lbpanel);
        revalidate();
        repaint();
    }

}
