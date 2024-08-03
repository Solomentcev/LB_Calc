package als;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectService {
    private  Map<Integer,Project> projects;

    public ProjectService() {
         projects=new HashMap<>();
    }

    public Project getProjectById(int id){
        return  projects.get(id);
    }
    public Project createProject(){
        int id=projects.size()+1;
        Project project=new Project(id);
        projects.put(id,project);
        return project;
    }
    public void addProject(Project project){
        projects.put(projects.size()+1,project);
    }
    public ALS addALS(int id){
        return projects.get(id).addALS();
    }
    public void deleteALS(int idProject, int idALS){
        projects.get(idProject).deleteALS(idALS);
    }
    public void deleteProject(int id){
        projects.remove(id);
    }
    public Map<String,String> getInfoLB(int idProject, int idAls, int idLB){
        return getProjectById(idProject).getAlsList().get(idAls).getLbList().get(idLB).getInfoLB();
    }
    public List<Map<String,String>> getInfoLbAls(int idProject, int idAls){
        return projects.get(idProject).getAlsList().get(idAls).getInfoLbAls();
    }
    public Map<String,String> getInfoAls(int idProject,int idAls){
        return getProjectById(idProject).getAlsList().get(idAls).getInfoAls();
    }
    public Map<String,String> getInfoLc(int idProject,int idAls){
        return getProjectById(idProject).getAlsList().get(idAls).getLc().getInfoLC();
    }
    public void writeProject(Project project, String fileName){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeProjectToXML(Project project, String fileName){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeProjectToPDF(Project project, String fileName){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeProjectToJPG(Project project, String fileName){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeProjectToDOCX(Project project, String fileName){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeProjectToXLSX(Project project, String fileName){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Project readProject(String fileName){
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Project project=(Project) objectInputStream.readObject();
            int id= projects.size()+1;
            project.setId(id);
            projects.put(id,project);

            return project;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
