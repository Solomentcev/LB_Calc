package view;

import als.Project;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectTabPanel extends JTabbedPane {
    private List<ProjectPanel> projectPanelsList=new ArrayList<>();
    public List<ProjectPanel> getProjectPanelsList() {
        return projectPanelsList;
    }
    public ProjectTabPanel(){
        super();
    }
    public ProjectPanel addProjectPanel(Project project) {

        ProjectPanel projectPanel=new ProjectPanel(project);
        ButtonTabComponent btnClose=new ButtonTabComponent(this);
        this.addTab(project.getName(), projectPanel);
        this.setSelectedIndex(this.getTabCount()-1);
        this.setTabComponentAt(this.getTabCount()-1,btnClose);

        projectPanelsList.add(projectPanel);
        return projectPanel;
    }
    public ProjectPanel getSelectedProjectPanel(){
        return (ProjectPanel) this.getSelectedComponent();
    }
}
