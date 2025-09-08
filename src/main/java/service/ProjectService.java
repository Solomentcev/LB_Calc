package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.ALS;
import model.LB;
import model.LC;
import model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.PreviewImageProject;

import javax.imageio.ImageIO;
import javax.xml.bind.*;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.transform.stream.StreamSource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static service.ConnectionToMySqlDb.getConnection;

public class ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private AlsService alsService=new AlsService();
    private final Set<Project> projects=new HashSet<>();

    public ProjectService() {
    }

    public Project  createProject() {
        Project project = new Project();
        project.updateName();
        ALS als=new ALS(project);
        project.getAlsList().add(als);
        project.getUniqueALS().put(als,1);
        logger.info("СОЗДАН проект:"+project.getName());
      //  projects.add(project);
        return project;
    }

    public void addProject(Project project) {
        projects.add( project);
    }

    public void writeProject(Project project, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
            logger.info("Файл " + fileName + " сохранен.");
        } catch (IOException e) {
            logger.info("Файл " + fileName + " НЕ сохранен.");
            throw new RuntimeException(e);
        }
    }

    public void writeProjectToXML(Project project, String fileName) {
        try {
            File outFile = new File(fileName);
            BufferedOutputStream objectOutputStream = new BufferedOutputStream(new FileOutputStream(outFile));

            JAXBContext context = JAXBContext.newInstance(Project.class, ALS.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(project, outFile);
            logger.info("Файл "+fileName+" сохранен.");

            /*
            ObjectMapper mapperXML = new XmlMapper();
            mapperXML.enable(SerializationFeature.INDENT_OUTPUT);
            mapperXML.registerModule(new JavaTimeModule());
            mapperXML.writeValue(objectOutputStream, project);

            String projectString = mapperXML.writeValueAsString(project);
            System.out.println(projectString); */

        } catch (IOException | JAXBException e) {
            logger.info("Файл " + fileName + " НЕ сохранен.");
            throw new RuntimeException(e);
        }
    }

    public void writeProjectToPDF(Project project, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeProjectToJPG(Project project, String fileName) {
        try {
            PreviewImageProject p = new PreviewImageProject(project);
            BufferedImage im = new BufferedImage(p.getImagePanel().getWidth(), p.getImagePanel().getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g=im.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            p.getImagePanel().paint(g);
            ImageIO.write(im, "jpg", new File(fileName));
            logger.info("Файл " + fileName + " сохранен.");

        } catch (IOException e) {
            logger.error("Файл " + fileName + "НЕ сохранен.");
            throw new RuntimeException(e);

        }
    }

    public void writeProjectToPNG(Project project, String fileName) {
        try {
            PreviewImageProject p = new PreviewImageProject(project);
            BufferedImage im = new BufferedImage(p.getContentPane().getWidth(), p.getContentPane().getHeight(), BufferedImage.TYPE_INT_RGB);
            p.getContentPane().paint(im.getGraphics());
            ImageIO.write(im, "png", new File(fileName));
            logger.info("Файл " + fileName + " сохранен.");

        } catch (IOException e) {
            logger.error("Файл " + fileName + " НЕ сохранен.");
            throw new RuntimeException(e);

        }
    }

    public void writeProjectToDOCX(Project project, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeProjectToXLSX(Project project, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(project);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Project readProject(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Project project = (Project) objectInputStream.readObject();
            projects.add(project);
            logger.info("Проект " + fileName + " открыт.");
            return project;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Проект " + fileName + " НЕ удалось открыть.");
            throw new RuntimeException(e);
        }
    }

    public Project readProjectFromXML(String fileName) {
        try {
            File outFile = new File(fileName);
         /*
            BufferedReader objectInputStream=new BufferedReader(new FileReader(outFile));
            ObjectMapper mapperXML = new XmlMapper();
            mapperXML.enable(SerializationFeature.INDENT_OUTPUT);
            mapperXML.registerModule(new JavaTimeModule());
            Project project =mapperXML.readValue(objectInputStream, Project.class);
         */
            JAXBContext context = JAXBContext.newInstance(LC.class, LB.class,ALS.class,Project.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            JAXBElement<Project> jaxbElement = unmarshaller
                    .unmarshal(new StreamSource(outFile), Project.class);
            Project project = jaxbElement.getValue();
            List<ALS> alsList = project.getAlsList();
            for (ALS als:alsList) {
                als.getLc().setParentALS(als);
                for (LB lb:als.getLbList()){
                    lb.setParentALS(als);
                }
                als.updateALS();
            }
            projects.add(project);
            project.updateUniqueALS();
            logger.info("Проект " + fileName + " открыт.");
            return project;
        } catch (JAXBException e) {
            logger.error("Проект " + fileName + " НЕ удалось открыть.");
            throw new RuntimeException(e);
        }
    }
    public Project readProjectFromJSON(String fileName) {
        try {
            File outFile = new File(fileName);

            BufferedReader objectInputStream=new BufferedReader(new FileReader(outFile));
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.registerModule(new JavaTimeModule());
            Project project =mapper.readValue(objectInputStream, Project.class);

            List<ALS> alsList = project.getAlsList();
            for (ALS als:alsList) {
                als.getLc().setParentALS(als);
                for (LB lb:als.getLbList()){
                    lb.setParentALS(als);
                }
                als.updateALS();
            }
            projects.add(project);
            project.updateUniqueALS();
            logger.info("Проект " + fileName + " открыт.");
            return project;
        } catch (IOException e) {
            logger.error("Проект " + fileName + " НЕ удалось открыть.");
            throw new RuntimeException(e);
        }
    }
    public void writeProjectToJSON(Project project, String fileName) {
        try {
            File outFile = new File(fileName);
            BufferedOutputStream objectOutputStream = new BufferedOutputStream(new FileOutputStream(outFile));

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
         //   mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
            mapper.registerModule(new JavaTimeModule());
            mapper.writeValue(objectOutputStream, project);
            logger.info("Файл " + fileName + " сохранен.");
        } catch (IOException e) {
            logger.error("Файл " + fileName + " НЕ сохранен.");
            throw new RuntimeException(e);
        }
    }

    public Set<Project> loadProjects() {
        try (Connection connection=getConnection();
             Statement statement = connection.createStatement()) {
            String sql="select * from project ";
            ResultSet result=statement.executeQuery(sql);
            while (result.next()) {
                int projectId = result.getInt("id");
                String name = result.getString("name");
                String company = result.getString("company");
                LocalDate createdDate = result.getDate("created_date").toLocalDate();
                Project project=new Project();
                project.setId(projectId);
                project.setName(name);
                project.setCompany(company);
                project.setCreatedDate(createdDate);
                Map<ALS,Integer> uniqueALS=alsService.loadALSListFromDB(projectId);
                List<ALS> alsList=new ArrayList<>();
                for (Map.Entry<ALS,Integer> als:uniqueALS.entrySet()) {
                    for (int i = 0; i < als.getValue(); i++) {
                        alsList.add(als.getKey());
                        als.getKey().setParentProject(project);
                    }
                }

                project.setUniqueALS(uniqueALS);
                project.setAlsList(alsList);
                project.updateDescription();
                projects.add(project);
                System.out.println("LOAD project... "+project.getName()+" "+project.getDescription());

            }
            System.out.println(projects);

        } catch (SQLException e) {
            logger.error(String.valueOf(e));
        }
        return projects;
    }

    public void saveProjectToDB(Project project) {

        try {
            if (getProjectId(project)==0) {
                insertProjectToDB(project);
            } else {
                updateProjectToDB(project);
            }
        } catch (SQLException e) {
            logger.error("Не удалось сохранить проект " +String.valueOf(e));
        }
    }
    private void updateProjectToDB(Project project) throws SQLException {
        String sqlProject="update project set name=?, company=?, created_date=? where id=?;";
        try ( Connection connection=getConnection();
              PreparedStatement statement = connection.prepareStatement(sqlProject,Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, project.getName());
            statement.setString(2, project.getCompany());
            statement.setString(3, String.valueOf(project.getCreatedDate()));
            statement.setInt(4, project.getId());
            statement.executeUpdate();
            Map<ALS, Integer> uniqueALS=project.getUniqueALS();
            deleteALSFromProject(project);
            for(Map.Entry<ALS, Integer> als:uniqueALS.entrySet()){
                if (alsService.getALSId(als.getKey())==0){
                    int alsId=alsService.insertAlsToDB(als.getKey());
                    als.getKey().setId(alsId);
                }
                insertAlsToProject(als.getKey(),project,als.getValue());

            }
            logger.info("Проект "+project+" обновлен в БД");
        } catch (SQLException e) {
            logger.error("Не удалось обновить проект " +String.valueOf(e));
            throw new SQLException(e);
        }
    }

    private void insertProjectToDB(Project project) throws SQLException {
        String sqlProject="insert into project (name, company, created_date) values(?,?,?);";
        try ( Connection connection=getConnection();
              PreparedStatement statement = connection.prepareStatement(sqlProject,Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, project.getName());
            statement.setString(2, project.getCompany());
            statement.setString(3, String.valueOf(project.getCreatedDate()));
            statement.executeUpdate();
            int projectId;
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    projectId=generatedKeys.getInt(1);
                    project.setId(projectId);
                }
            }
            Map<ALS, Integer> uniqueALS=project.getUniqueALS();
            deleteALSFromProject(project);
            for(Map.Entry<ALS, Integer> als:uniqueALS.entrySet()){
                if (alsService.getALSId(als.getKey())==0){
                    int alsId=alsService.insertAlsToDB(als.getKey());
                    als.getKey().setId(alsId);
                }
                insertAlsToProject(als.getKey(),project,als.getValue());

            }
            logger.info("Проект "+project+" добавлен в БД");
        } catch (SQLException e) {
            logger.error("Не удалось сохранить проект " +String.valueOf(e));
            throw new SQLException(e);
        }
    }

    private void insertAlsToProject(ALS als, Project project, Integer quantity) throws SQLException {
        String sqAlsToProject="insert into project_als values(?, ?, ?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqAlsToProject))
        {
            statement.setInt(1, project.getId());
            statement.setInt(2, als.getId());
            statement.setInt(3, quantity);
            statement.executeUpdate();
            logger.info(als+" добавлен в проект "+project);
        } catch (SQLException e) {
            logger.error("Не удалось добавить АКХ в проект " +String.valueOf(e));
            throw new SQLException(e);
        }
    }

    public int getProjectId(Project project) throws SQLException {
        String sqlProject="select project.id from project " +
                "where (project.name=? " +
                "and project.company=? " +
                "and project.created_date=?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlProject))
        {
            statement.setString(1, project.getName());
            statement.setString(2, project.getCompany());
            statement.setString(3, String.valueOf(project.getCreatedDate()));
            ResultSet result = statement.executeQuery();
            int projectId;
            if (!result.next()) {
                return 0;
            }
            projectId=result.getInt(1);
            project.setId(projectId);
            return projectId;
        } catch (SQLException e) {
            logger.error("Не удалось получить id Проекта " +String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public void deleteALSFromProject(Project project) throws SQLException {
        String deleteALS="delete from project_als where project_id=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteALS))
        {
            statement.setInt(1, project.getId());
            statement.executeUpdate();
            logger.info("АКХ в проекте удалены ");
        } catch (SQLException e) {
            logger.error("Не удалось удалить АКХ проекта "+ String.valueOf(e));
            throw new SQLException(e);
        }
    }

    public void removeProject(Project project){
        String deleteProject="delete from project where id=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteProject))
        {
            deleteALSFromProject(project);
            statement.setInt(1, project.getId());
            statement.executeUpdate();
            projects.remove(project);
            logger.info("Проект удален ");
        } catch (SQLException e) {
            logger.error("Не удалось удалить проект "+ e);

        }
    }

    public static class LocalDateTimeAdapter extends XmlAdapter<String, LocalDate> {
        @Override
        public LocalDate unmarshal(String value) {
            return value == null ? null : LocalDate.parse(value);
        }
        @Override
        public String marshal(LocalDate localDate) {
            return localDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        }
    }
}
