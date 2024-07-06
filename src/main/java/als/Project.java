package als;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project implements Serializable {

    private int id;
    private String name;
    private String description;
    private String company;
    private LocalDateTime createdDate;
    private List<ALS> alsList;
    private File file;

    public File getFile() {
        return file;
    }

    public Project(int id) {
        this.id=id;
        createdDate= LocalDateTime.now();

        company="company";
        name=company+"_"+id+"_"+createdDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        alsList = new ArrayList<>();
        alsList.add(new ALS(this));
      //  alsList.add(new ALS(this));
        file=null;
        System.out.println("СОЗДАН проект:"+name);

    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
        name=company+"_"+id+"_"+createdDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
    }

    public List<ALS> getAlsList() {
        return alsList;
    }

    public void setAlsList(List<ALS> alsList) {
        this.alsList = alsList;
    }

    public ALS addALS(){
        ALS als=new ALS(this);
        alsList.add(als);
        return als;
    }
    public void deleteALS(int id){
        alsList.remove(id);
    }
    public void deleteALS(ALS als){
        alsList.remove(als);
        System.out.println(" УДАЛЕНА АКХ:"+als.getName());
        for(ALS alss:alsList){
            System.out.println(alss.getName());
        }
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Map<String,String> getInfoProject(){
        Map<String, String> infoProject=new HashMap<>();
        infoProject.put("id",String.valueOf(id));
        infoProject.put("company",company);
        infoProject.put("created_date",String.valueOf(createdDate));
        return infoProject;
    }
}
