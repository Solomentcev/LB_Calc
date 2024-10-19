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
    private final View view;
    private final ProjectService projectService=new ProjectService();
    private final Map<ProjectPanel,Project> projectMap =new HashMap<>();
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
        if (file!=null) {  String a= file.getName();
            String[] b=a.split("\\.");
            String ext=b[b.length-1];
            Project project;
            switch (ext) {
                case "alx" -> project = projectService.readProject(file.getAbsolutePath());
                case "xml" -> project = projectService.readProjectFromXML(file.getAbsolutePath());
                case "json" -> project = projectService.readProjectFromJSON(file.getAbsolutePath());
                default -> throw new IllegalStateException("Unexpected value: " + ext);
            }
            
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
            switch (ext) {
                case "alx" -> projectService.writeProject(project, file.getAbsolutePath());
                case "xml" -> projectService.writeProjectToXML(project, file.getAbsolutePath());
                case "json" -> projectService.writeProjectToJSON(project, file.getAbsolutePath());
                case "docx" -> projectService.writeProjectToDOCX(project, file.getAbsolutePath());
                case "jpg" -> projectService.writeProjectToJPG(project, file.getAbsolutePath());
                case "png" -> projectService.writeProjectToPNG(project, file.getAbsolutePath());
                case "pdf" -> projectService.writeProjectToPDF(project, file.getAbsolutePath());
                case "xlsx" -> projectService.writeProjectToXLSX(project, file.getAbsolutePath());
            }
        }

    }
}
