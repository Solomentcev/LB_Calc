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
import java.util.*;
import java.util.List;

public class ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private static final Properties dbProps = new Properties();

    private final List<Project> projects=new ArrayList<>();

    public ProjectService() {
    }

    public Project createProject() {
        Project project = new Project();
        projects.add(project);
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

    public void loadProjects() {
        try {
            dbProps.load(new FileInputStream("src/main/resources/db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection=getConnection();
             Statement statement = connection.createStatement()) {
            String sql="select * from project ";
            ResultSet result=statement.executeQuery(sql);
            result.next();
            String company = result.getString("company");
            System.out.println(company);

        } catch (SQLException e) {
            logger.error(String.valueOf(e));
        }
    }

    public void saveProjectToDB(Project project) {
        try {
            dbProps.load(new FileInputStream("src/main/resources/db.properties"));
        } catch (IOException e) {
            logger.error("Не удалось загрузить настройки БД " +String.valueOf(e));
            throw new RuntimeException(e);
        }
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
                if (getALSId(als.getKey())==0){
                    int alsId=insertAlsToDB(als.getKey());
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
                if (getALSId(als.getKey())==0){
                    int alsId=insertAlsToDB(als.getKey());
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

    public int insertAlsToDB(ALS als) throws SQLException {
        String sqlALS="insert into als(name, height, width, depth, upper_frame, bottom_frame, " +
                "depth_cell,count_cells," +
                "lc_id, position_LC_id, color_door_id,color_body_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlALS,Statement.RETURN_GENERATED_KEYS)) {
            if (getLCId(als.getLc())==0){
                int lcId=insertLCToDB(als.getLc());
                als.getLc().setId(lcId);
            }
            statement.setString(1, als.getName());
            statement.setInt(2, als.getHeight());
            statement.setInt(3, als.getWidth());
            statement.setInt(4, als.getDepth());
            statement.setInt(5, als.getUpperFrame());
            statement.setInt(6, als.getBottomFrame());
            statement.setInt(7, als.getDepthCell());
            statement.setInt(8, als.getCountCells());
            statement.setInt(9, als.getLc().getId());
            statement.setInt(10, getPositionLCID(als));
            statement.setInt(11, getColorDoorID(als));
            statement.setInt(12, getColorBodyID(als));
            statement.executeUpdate();
            int alsId=als.getId();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    alsId=generatedKeys.getInt(1);
                    als.setId(alsId);
                }
            }
            deleteLBFromALS(als);
            Map<LB, Integer> uniqueLB=als.getUniqueLB();
            for(Map.Entry<LB, Integer> lb:uniqueLB.entrySet()){
                if (getLBId(lb.getKey())==0){
                    int lbId=insertLBToDB(lb.getKey());
                    lb.getKey().setId(lbId);
                }
                insertLbToALS(lb.getKey(),als,lb.getValue());
            }
            logger.info(als+" добавлен в БД");
            return alsId;

        } catch (SQLException e) {
            logger.error("Не удалось добавить АКХ в БД "+ String.valueOf(e));
            throw new SQLException(e);
        }

    }
    public void insertLbToALS(LB lb, ALS als, int quantity ) throws SQLException {
        String sqLbToALS="insert into als_lb values(?, ?, ?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqLbToALS))
        {
            statement.setInt(1, als.getId());
            statement.setInt(2, lb.getId());
            statement.setInt(3, quantity);
            statement.executeUpdate();
            logger.info(lb+" добавлен в "+als);
        } catch (SQLException e) {
            logger.error("Не удалось добавить МХ в АКХ "+ String.valueOf(e));
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
    public int getALSId(ALS als) throws SQLException {
        String sqlALS="select als.id from als " +
                "where (als.height=? " +
                "and als.width=? " +
                "and als.depth=? " +
                "and als.upper_frame=? " +
                "and als.bottom_frame=? " +
                "and als.depth_cell=? " +
                "and als.count_cells=? " +
                "and als.lc_id=? " +
                "and als.position_LC_id=? " +
                "and als.color_door_id=? " +
                "and als.color_body_id=?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlALS))
        {
            statement.setInt(1, als.getHeight());
            statement.setInt(2, als.getWidth());
            statement.setInt(3, als.getDepth());
            statement.setInt(4, als.getUpperFrame());
            statement.setInt(5, als.getBottomFrame());
            statement.setInt(6, als.getDepthCell());
            statement.setInt(7, als.getCountCells());
            statement.setInt(8, getLCId(als.getLc()));
            statement.setInt(9, getPositionLCID(als));
            statement.setInt(10, getColorDoorID(als));
            statement.setInt(11, getColorBodyID(als));
            ResultSet result = statement.executeQuery();
            int ALSId;
            if (!result.next()) {
                return 0;
            }
            ALSId=result.getInt(1);
            als.setId(ALSId);
            return ALSId;
        } catch (SQLException e) {
            logger.error("Не удалось получить id АКХ " +String.valueOf(e));
            throw new SQLException(e);
        }
    }

    public int insertLCToDB(LC lc) throws SQLException {
        String sqlLC="insert into lc (name, height, width,depth,\n" +
                "display_Id ,bar_reader_id,payment_id,\n" +
                "printer,rfid_reader,color_body_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlLC,Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, lc.getName());
            statement.setInt(2, lc.getHeight());
            statement.setInt(3, lc.getWidth());
            statement.setInt(4, lc.getDepth());
            statement.setInt(5, getDisplayID(lc)); //display_id
            statement.setInt(6, getBarReaderID(lc)); //bar_reader_id
            statement.setInt(7, getPaymentID(lc));//payment_id
            statement.setBoolean(8, lc.isPrinter()); //printer
            statement.setBoolean(9, lc.isRfidReader()); //rfid_reader
            statement.setInt(10, getColorBodyID(lc));
            statement.executeUpdate();
            int lcId = 0;
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) lcId=generatedKeys.getInt(1);
            }
            logger.info(lc+" добавлен в БД");
            return lcId;
        } catch (SQLException e) {
            logger.error("Не удалось добавить МУ в БД "+ String.valueOf(e));
            throw new SQLException(e);
        }
    }

    public int insertLBToDB(LB lb) throws SQLException {
        String sqlLB="insert into lb (name,type_LB_id, height, width, depth, " +
                "upper_frame, bottom_frame,shelf_thick, count_cells, " +
                "height_cell, width_cell, depth_cell, " +
                "direction_door_opening_id, color_door_id, color_body_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlLB,Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, lb.getName());
            statement.setInt(2, getTypeLbID(lb));
            statement.setInt(3, lb.getHeight());
            statement.setInt(4, lb.getWidth());
            statement.setInt(5, lb.getDepth());
            statement.setInt(6, lb.getUpperFrame());
            statement.setInt(7, lb.getBottomFrame());
            statement.setInt(8, lb.getShelfThick());
            statement.setInt(9, lb.getCountCells());
            statement.setDouble(10, lb.getHeightCell());
            statement.setInt(11, lb.getWidthCell());
            statement.setInt(12, lb.getDepthCell());
            statement.setInt(13, getDirectionDoorOpeningID(lb));
            statement.setInt(14, getColorDoorID(lb));
            statement.setInt(15, getColorBodyID(lb));

            statement.executeUpdate();
            int lbId = 0;
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) lbId=generatedKeys.getInt(1);
            }
            logger.info(lb+" добавлен в БД");
            return lbId;

        } catch (SQLException e) {
            logger.error("Не удалось добавить МХ в БД "+ String.valueOf(e));
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
    public void deleteLBFromALS(ALS als) throws SQLException {
        String deleteALS="delete from als_lb where als_id=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteALS)) {
            statement.setInt(1, als.getId());
            statement.executeUpdate();
            logger.info("МХ в АКХ удалены ");
        } catch (SQLException e) {
            logger.error("Не удалось удалить МУ в АКХ "+ String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getLCId(LC lc) throws SQLException {
        String sqlLC="select lc.id from lc " +
                "where (lc.height=? " +
                "and lc.width=? " +
                "and lc.depth=? " +
                "and lc.display_id=? " +
                "and lc.bar_reader_id=? " +
                "and lc.payment_id =? " +
                "and lc.printer=? " +
                "and lc.rfid_reader=? " +
                "and lc.color_body_id=?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlLC))
        {
            statement.setInt(1, lc.getHeight());
            statement.setInt(2, lc.getWidth());
            statement.setInt(3, lc.getDepth());
            statement.setInt(4, getDisplayID(lc)); //display_id
            statement.setInt(5, getBarReaderID(lc)); //bar_reader_id
            statement.setInt(6, getPaymentID(lc));//payment_id
            statement.setBoolean(7, lc.isPrinter()); //printer
            statement.setBoolean(8, lc.isRfidReader()); //rfid_reader
            statement.setInt(9, getColorBodyID(lc));
            ResultSet result = statement.executeQuery();
            int LCId;
            if (!result.next()) return 0;
            LCId=result.getInt(1);
            lc.setId(LCId);
            return LCId;
        } catch (SQLException e) {
            logger.error("Не удалось получить id МУ " +String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getLBId(LB lb) throws SQLException {
        String sqlLB="select lb.id from lb " +
                "where (lb.type_LB_id=? " +
                "and lb.height=? and lb.width=? and lb.depth=? " +
                "and lb.upper_frame=? and lb.bottom_frame=? " +
                "and lb.shelf_thick=? " +
                "and lb.count_cells=? " +
                "and lb.height_cell=? and lb.width_cell=? and lb.depth_cell=? " +
                "and lb.direction_door_opening_id=? " +
                "and lb.color_door_id=? " +
                "and lb.color_body_id=?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlLB))
        {
            statement.setInt(1, getTypeLbID(lb));
            statement.setInt(2, lb.getHeight());
            statement.setInt(3, lb.getWidth());
            statement.setInt(4, lb.getDepth());
            statement.setInt(5, lb.getUpperFrame());
            statement.setInt(6, lb.getBottomFrame());
            statement.setInt(7, lb.getShelfThick());
            statement.setInt(8, lb.getCountCells());
            statement.setDouble(9, lb.getHeightCell());
            statement.setInt(10, lb.getWidthCell());
            statement.setInt(11, lb.getDepthCell());
            statement.setInt(12, getDirectionDoorOpeningID(lb));
            statement.setInt(13, getColorDoorID(lb));
            statement.setInt(14, getColorBodyID(lb));
            ResultSet result = statement.executeQuery();
            int LBId;
            if (!result.next()) return 0;
            LBId=result.getInt(1);
            lb.setId(LBId);
            return LBId;
        } catch (SQLException e) {
            logger.error("Не удалось получить id МХ "+String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getPositionLCID(ALS als) throws SQLException {
        String sqlPositionLCId="select id from position_lc where position=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlPositionLCId)){
            statement.setString(1, String.valueOf(als.getPositionLC())); //position_lc_id
            ResultSet result = statement.executeQuery();
            if (!result.next()) return 0;
            return result.getInt(1);
        } catch (SQLException e) {
            logger.error("Не удалось получить id позиции МУ " +String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getColorDoorID(ALS als) throws SQLException {
        String sqlColorDoorId="select id from colors where color=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlColorDoorId)){
            statement.setString(1, String.valueOf(als.getColorDoor())); //color_door_id
            ResultSet result = statement.executeQuery();
            int colorDoorId;
            if (!result.next()) return 0;
            colorDoorId=result.getInt(1);
            return colorDoorId;
        } catch (SQLException e) {
            logger.error("Не удалось получить id colordoor АКХ " +String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getColorBodyID(ALS als) throws SQLException {
        String sqlColorBodyId="select id from colors where color=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlColorBodyId)){
            statement.setString(1, String.valueOf(als.getColorBody())); //color_body_id
            ResultSet result = statement.executeQuery();
            int colorBodyId;
            if (!result.next()) return 0;
            colorBodyId=result.getInt(1);
            return colorBodyId;
        } catch (SQLException e) {
            logger.error("Не удалось получить id colorbody АКХ " +String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getDisplayID(LC lc) throws SQLException {
        String sqlDisplayId="select id from display where display=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlDisplayId)){
            statement.setString(1, String.valueOf(lc.getDisplay())); //display_id
            ResultSet result = statement.executeQuery();
            if (!result.next()) return 0;
            return result.getInt(1);
        } catch (SQLException e) {
            logger.error("Не удалось получить id display "+String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getBarReaderID(LC lc) throws SQLException {
        String sqlBarReaderId="select id from bar_reader where reader=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlBarReaderId)){
            statement.setString(1, String.valueOf(lc.getBarReader())); //bar_reader_id
            ResultSet result = statement.executeQuery();
            if (!result.next()) return 0;
            return result.getInt(1);
        } catch (SQLException e) {
            logger.error("Не удалось получить id barreader "+String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getPaymentID(LC lc) throws SQLException {
        String sqlPaymentId = "select id from payment where payment=?;" ;
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlPaymentId)) {
            statement.setString(1, String.valueOf(lc.getPayment()));//payment_id
            ResultSet result = statement.executeQuery();
            if (!result.next()) return 0;
            return result.getInt(1);
        } catch (SQLException e) {
            logger.error("Не удалось получить id payment "+String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getTypeLbID(LB lb) throws SQLException {
        String sqlPaymentId = "select id from type_lb where type_lb.type=?;" ;
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlPaymentId)) {
            statement.setString(1, String.valueOf(lb.getType()));
            ResultSet result = statement.executeQuery();
            if (!result.next()) return 0;
            return result.getInt(1);
        } catch (SQLException e) {
            logger.error("Не удалось получить id TypeLB "+String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getColorBodyID (LB lb) throws SQLException {
        String sqlColorBodyId = "select id from colors where color=?;" ;
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlColorBodyId)) {
            statement.setString(1, String.valueOf(lb.getColorBody()));
            ResultSet result = statement.executeQuery();
            if (!result.next()) return 0;
            return result.getInt(1);
        } catch (SQLException e) {
            logger.error("Не удалось получить id colorbody МХ "+String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getColorDoorID (LB lb) throws SQLException {
        String sqlColorDoorID = "select id from colors where color=?;" ;
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlColorDoorID)) {
            statement.setString(1, String.valueOf(lb.getColorDoor()));
            ResultSet result = statement.executeQuery();
            if (!result.next()) return 0;
            return result.getInt(1);
        } catch (SQLException e) {
            logger.error("Не удалось получить id colordoor МХ "+String.valueOf(e));
            throw new SQLException(e);
        }
    }
    public int getDirectionDoorOpeningID (LB lb) throws SQLException {
        String sqlDirectionDoorOpeningID = "select id from direction_door_opening where direction_door_opening.direction_door_opening=?;" ;
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlDirectionDoorOpeningID)) {
            statement.setString(1, String.valueOf(lb.getDirectionDoorOpening()));
            ResultSet result = statement.executeQuery();
            if (!result.next()) return 0;
            return result.getInt(1);
        } catch (SQLException e) {
            logger.error("Не удалось получить id DirectionDoorOpening МХ "+String.valueOf(e));
            throw new SQLException(e);
        }
    }
        public int getColorBodyID (LC lc) throws SQLException {
            String sqlColorBodyId = "select id from colors where color=?;" ;
            try (Connection connection=getConnection();
                 PreparedStatement statement = connection.prepareStatement(sqlColorBodyId)) {
                statement.setString(1, String.valueOf(lc.getColorBody())); //colorBody
                ResultSet result = statement.executeQuery();
                if (!result.next()) return 0;
                return result.getInt(1);
            } catch (SQLException e) {
                logger.error("Не удалось получить id colorbody МУ "+String.valueOf(e));
                throw new SQLException(e);
            }
        }
        public static Connection getConnection() throws SQLException {
            Connection connection;
            try {
                connection = DriverManager.getConnection(
                        dbProps.getProperty("url"),
                        dbProps.getProperty("user"),
                        dbProps.getProperty("password"));
            } catch (SQLException e) {
                logger.error("Не удалось подключиться к БД "+String.valueOf(e));
                throw new SQLException(e);
            }
            return connection;

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
