package view;

import model.Project;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class ProjectTableModel extends AbstractTableModel {
    private final ArrayList<Project> projects;
    private ArrayList<JButton> listEdit, listDelete;

    public ProjectTableModel(ArrayList<Project> projects, ArrayList<JButton> listEdit, ArrayList<JButton> listDelete) {
        this.projects = projects;
        this.listEdit = listEdit;
        this.listDelete = listDelete;

    }

    @Override
    public int getRowCount() {
        return projects.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        switch (i1){
            case 0: return projects.get(i).getId();
            case 1: return projects.get(i).getCompany();
            case 2: return projects.get(i).getDescription();
            case 3: return projects.get(i).getCreatedDate().toString();
            case 4: return listEdit.get(i);
            case 5: return listDelete.get(i);

            default: return " ";
        }
    }
    @Override
    public String getColumnName(int c) {
        switch (c) {
            case 0: return "id";
            case 1: return "Компания";
            case 2: return "Описание";
            case 3: return "Дата создания";
            case 4: return "Редактировать";
            case 5: return "Удалить";
            default:
                return "Error";
        }
    }
}
