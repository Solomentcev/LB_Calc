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
        if (file!=null) {
            Project project = projectService.readProject(file.getAbsolutePath());
            projectService.addProject(project);
            project.setFile(file);
            projectMap.put(view.initProject(project), project);
        }
    }

    public void saveProject() {
        Project project=projectMap.get(view.getProjectTabPanel().getSelectedProjectPanel());
        String fileName=project.getName();
        String saveDirectory="C:\\Users\\DENIS-SDA\\Desktop\\";
        File file=new File(saveDirectory+"\\"+fileName+"."+"alx");
        project.setFile(file);
        System.out.println(project.getFile());
        projectService.writeProject(project, file.getAbsolutePath());

    }

    public void saveProjectAs() {
        Project project=projectMap.get(view.getProjectTabPanel().getSelectedProjectPanel());
        String filename=project.getName();
        File file=view.saveProjectAs(filename);
        if (file!=null){
            String a= file.getName();
           String[] b=a.split("\\.");
           String ext=b[b.length-1];
           switch (ext){
                case "alx" : projectService.writeProject(project, file.getAbsolutePath()); break;
                case "xml" : projectService.writeProjectToXML(project, file.getAbsolutePath()); break;
                case "docx" : projectService.writeProjectToDOCX(project, file.getAbsolutePath()); break;
                case "jpg" : projectService.writeProjectToJPG(project, file.getAbsolutePath()); break;
               case  "png" : projectService.writeProjectToPNG(project, file.getAbsolutePath()); break;
                case "pdf" : projectService.writeProjectToPDF(project, file.getAbsolutePath()); break;
                case "xlsx" : projectService.writeProjectToXLSX(project, file.getAbsolutePath()); break;
        }
        }

    }
}
