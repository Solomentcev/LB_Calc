package controller;

import als.Project;
import als.ProjectService;
import view.ProjectPanel;
import view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private View view;
    private ProjectService projectService=new ProjectService();
    private Map<ProjectPanel,Project> projectMap =new HashMap<>();
    public Map<ProjectPanel,Project> getProjectMap() {
        return projectMap;
    }

    public Controller(View view) {
        this.view = view;
    }
    public void init(){
        view.setTitle("Проекты АКХ");

    }
    public void exit(){
        System.exit(0);
    }


    public void addNewProject() {
        Project project=projectService.createProject();
        projectMap.put(view.initProject(project),project);
    }
    public void openProject() {
           File file=view.openProject();
           Project project=projectService.readProject(file.getAbsolutePath());
           projectService.addProject(project);
           project.setFile(file);
           projectMap.put(view.initProject(project),project);
    }

    public void saveProject() {

    }

    public void saveProjectAs() {
        File file=view.saveProjectAs();
        projectService.writeProject(projectMap.get(view.getProjectTabPanel().getSelectedProjectPanel()), file.getAbsolutePath());
    }
}
