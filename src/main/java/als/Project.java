package als;

import com.fasterxml.jackson.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private LocalDateTime createdDate= LocalDateTime.now();

    @XmlElementWrapper(name="ALSList")
    @XmlElement(name="ALS",type = ALS.class)
    private List<ALS> alsList = new ArrayList<>();
    @XmlTransient
    @JsonIgnore
    private File file;

    public File getFile() {
        return file;
    }

    public Project(int id) {
        this.id=id;
        updateName();
        alsList.add(new ALS(this));
        file=null;
        logger.info("СОЗДАН проект:"+name);
    }
    public Project(){}

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
        return als;
    }
    public void deleteALS(int id){
        alsList.remove(id);
    }
    public void deleteALS(ALS als){
        alsList.remove(als);
        logger.info(" УДАЛЕНА АКХ:"+als.getName());
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
