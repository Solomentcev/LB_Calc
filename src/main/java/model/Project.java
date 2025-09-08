package model;

import com.fasterxml.jackson.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ProjectService;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@XmlRootElement(name = "Project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Project.class);
    @XmlAttribute(name = "id")
    private int id;
    @XmlAttribute(name = "name")
    private String name;
    @JsonIgnore
    private String description;
    @XmlAttribute(name = "Company")
    private String company="Company";
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @XmlJavaTypeAdapter(ProjectService.LocalDateTimeAdapter.class)
    @XmlAttribute(name = "CreatedDate")
    private LocalDate createdDate= LocalDate.now();

    @XmlElementWrapper(name="ALSList")
    @XmlElement(name="ALS",type = ALS.class)
    private List<ALS> alsList = new ArrayList<>();
    @XmlTransient
    @JsonIgnore
    private  Map<ALS, Integer> uniqueALS=new HashMap<>();
    @XmlTransient
    @JsonIgnore
    private File file;

    public File getFile() {
        return file;
    }

    public Project() {

    }
    public Project createProject() {
         Project project = new Project();
         updateName();
         ALS als=new ALS(this);
         alsList.add(als);
         uniqueALS.put(als,1);
         file=null;
         logger.info("СОЗДАН проект:"+name);
         return project;
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
        updateName();

    }
    public String updateName(){
        return name=company+"_"+id+"_"+createdDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
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
        updateUniqueALS();
        if (uniqueALS.containsKey(als)){
            Integer i=uniqueALS.get(als);
            i=i+1;
            uniqueALS.put(als,i);
            logger.info("Добавлен еще одна "+als.getName());
        } else {uniqueALS.put(als,1);
            logger.info("Добавлена уникальная "+als.getName());}
        updateDescription();
        return als;
    }
    public void deleteALS(ALS als){
        alsList.remove(als);
        updateUniqueALS();
        for (Map.Entry<ALS,Integer> als1:uniqueALS.entrySet()){
            Integer i=als1.getValue();
            if (als.equals(als1.getKey())){
                if (als1.getValue()==1) {
                    uniqueALS.remove(als);
                    break;}
                else {
                    i=i-1;
                    uniqueALS.put(als,i);
                }
            }
        }
        updateUniqueALS();
        logger.info(" УДАЛЕНА АКХ:"+als.getName());
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    public Map<ALS, Integer> updateUniqueALS() {
        uniqueALS.clear();
        for(ALS als:alsList){
            if (uniqueALS.containsKey(als)){
                Integer i=uniqueALS.get(als);
                i=i+1;
                uniqueALS.put(als,i);
            } else uniqueALS.put(als,1);
        }
        updateDescription();
        return uniqueALS;
    }
    public Map<ALS, Integer> getUniqueALS() {
        return uniqueALS;
    }

    public void setUniqueALS(Map<ALS, Integer> uniqueALS) {
        this.uniqueALS = uniqueALS;
    }
    public String updateDescription(){
        StringBuilder builder=new StringBuilder();
        for(Map.Entry<ALS,Integer> als:uniqueALS.entrySet()){
            builder.append(als.getKey().getDescription()).append(" - ").append(als.getValue()).append(" шт. \n");
        }
        description=builder.toString();
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return getId() == project.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
