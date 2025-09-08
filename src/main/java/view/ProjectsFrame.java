package view;

import model.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

public class ProjectsFrame extends JFrame implements ActionListener {
         private View view;
         private ArrayList<Project> projects=new ArrayList<>();
         private final JTable table;
         private JButton btnSearch;
         private final ArrayList<JButton> listEdit, listDelete;

    public ProjectsFrame() throws HeadlessException {
        addWindowListener(new WindowAdapter() {
        });
        setTitle("Проекты");
        setVisible(false);
        setMinimumSize(new Dimension(500,200));
        listEdit = new ArrayList<JButton>();
        listDelete=new ArrayList<JButton>();
        ProjectTableModel projectTableModel=new ProjectTableModel(this.projects,listEdit,listDelete);
        table = new JTable(projectTableModel);
        //  table.setModel(projectTableModel);

        table.addMouseListener(new JTableButtonMouseListener(table));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

    }
    public void displayProjects(Set<Project> projects, View view) {
        addWindowListener(new WindowAdapter() {
        });
        setTitle("Проекты");
        setVisible(true);
        setMinimumSize(new Dimension(500,200));
        this.view = view;
        this.projects = new ArrayList<>(projects);
        ProjectTableModel projectTableModel=new ProjectTableModel(this.projects,listEdit,listDelete);
        table.setModel(projectTableModel);
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        ButtonEditor buttonEditor = new ButtonEditor();
        table.getColumn("Редактировать").setCellRenderer(buttonRenderer);
        // table.getColumn("Редактировать").setCellEditor(buttonEditor);
        table.getColumn("Удалить").setCellRenderer(buttonRenderer);
        // table.getColumn("Удалить").setCellEditor(buttonEditor);
        for(int i=0; i<projects.size(); i++) {
            JButton btn = new JButton("Edit");
            btn.addActionListener(this);
            listEdit.add(btn);
            btn = new JButton("Delete");
            btn.addActionListener(this);
            listDelete.add(btn);
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);



    }
    public ProjectsFrame(Set<Project> projects, View view) throws HeadlessException {
        addWindowListener(new WindowAdapter() {
        });
        setTitle("Проекты");
        setVisible(true);
        setMinimumSize(new Dimension(500,200));
        this.view = view;
//        JPanel panel = new JPanel();
//        this.setContentPane(panel);
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.projects = new ArrayList<>(projects);
        listEdit = new ArrayList<JButton>();
        listDelete=new ArrayList<JButton>();
        for(int i=0; i<projects.size(); i++) {
            JButton btn = new JButton("Edit");
            btn.addActionListener(this);
            listEdit.add(btn);
            btn = new JButton("Delete");
            btn.addActionListener(this);
            listDelete.add(btn);
        }
        ProjectTableModel projectTableModel=new ProjectTableModel(this.projects,listEdit,listDelete);
        table = new JTable(projectTableModel);
        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        ButtonEditor buttonEditor = new ButtonEditor();
        table.getColumn("Редактировать").setCellRenderer(buttonRenderer);
       // table.getColumn("Редактировать").setCellEditor(buttonEditor);
        table.getColumn("Удалить").setCellRenderer(buttonRenderer);
       // table.getColumn("Удалить").setCellEditor(buttonEditor);

        table.addMouseListener(new JTableButtonMouseListener(table));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btnClicked = (JButton)e.getSource();
        if(btnClicked.equals(btnSearch)){
            btnSearchClick();
            return;
        }
        for(int i=0; i<listEdit.size(); i++)
            if(btnClicked.equals(listEdit.get(i))){
                btnEditClick(i);
                return;
            }
        for(int i=0; i<listDelete.size(); i++)
            if(btnClicked.equals(listDelete.get(i))){
                btnDeleteClick(i);
                return;
            }

    }

    private void btnDeleteClick(int index) {
        System.out.println("delete"+projects.get(index).getId());
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (this, "Вы уверены что хотите удалить этот проект: "+projects.get(index).getId()+" - "+projects.get(index).getName(), "Warning", dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            try {
                view.getController().removeProject(projects.get(index));
                projects.remove(index);
                listEdit.remove(index);
                listDelete.remove(index);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        ( (ProjectTableModel) table.getModel()).fireTableDataChanged();
    }

    private void btnEditClick(int i) {
        System.out.println("edit"+projects.get(i).getId());
        System.out.println(projects.get(i).getDescription());
        view.getController().getProjectMap().put(view.initProject(projects.get(i)), projects.get(i));

    }

    private void btnSearchClick() {

    }
}

