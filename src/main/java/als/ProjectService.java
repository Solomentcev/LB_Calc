package als;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private static final Properties dbProps = new Properties();
    private final Map<Integer, Project> projects;

    public ProjectService() {
        projects = new HashMap<>();
    }

    public Project getProjectById(int id) {
        return projects.get(id);
    }

    public Project createProject() {
        int id = projects.size() + 1;
        Project project = new Project(id);
        projects.put(id, project);
        return project;
    }

    public void addProject(Project project) {
        projects.put(projects.size() + 1, project);
    }

    public ALS addALS(int id) {
        return projects.get(id).addALS();
    }

    public void deleteALS(int idProject, int idALS) {
        projects.get(idProject).deleteALS(idALS);
    }

    public void deleteProject(int id) {
        projects.remove(id);
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

            JAXBContext context = JAXBContext.newInstance(Project.class,ALS.class);
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
            int id = projects.size() + 1;
            project.setId(id);
            projects.put(id, project);
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
            JAXBContext context = JAXBContext.newInstance(LC.class,LB.class,ALS.class,Project.class);
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
            int id = projects.size() + 1;
            project.setId(id);
            projects.put(id, project);
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
            int id = projects.size() + 1;
            project.setId(id);
            projects.put(id, project);
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

    public void loadProjects() {
        try {
            dbProps.load(new FileInputStream("src/main/resources/db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(
                dbProps.getProperty("url"),
                dbProps.getProperty("user"),
                dbProps.getProperty("password"))) {
            // Дальнейшая работа с БД через connection
            Statement statement = connection.createStatement();
            String sql="select * from project ";
            ResultSet result=statement.executeQuery(sql);
            result.next();
            String company = result.getString("company");
            System.out.println(company);

        } catch (SQLException e) {
            logger.error(String.valueOf(e));
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
