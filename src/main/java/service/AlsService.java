package service;

import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static service.ConnectionToMySqlDb.getConnection;

public class AlsService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
    private LbService lbService=new LbService();
    private LcService lcService=new LcService();
    public int insertAlsToDB(ALS als) throws SQLException {
        String sqlALS="insert into als(name, height, width, depth, upper_frame, bottom_frame, " +
                "depth_cell,count_cells," +
                "lc_id, position_LC_id, color_door_id,color_body_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlALS, Statement.RETURN_GENERATED_KEYS)) {
            if (lcService.getLCId(als.getLc())==0){
                int lcId=lcService.insertLCToDB(als.getLc());
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
            lbService.deleteLBFromALS(als);
            Map<LB, Integer> uniqueLB=als.getUniqueLB();
            for(Map.Entry<LB, Integer> lb:uniqueLB.entrySet()){
                if (lbService.getLBId(lb.getKey())==0){
                    int lbId=lbService.insertLBToDB(lb.getKey());
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
        try (
             PreparedStatement statement = getConnection().prepareStatement(sqlALS))
        {
            statement.setInt(1, als.getHeight());
            statement.setInt(2, als.getWidth());
            statement.setInt(3, als.getDepth());
            statement.setInt(4, als.getUpperFrame());
            statement.setInt(5, als.getBottomFrame());
            statement.setInt(6, als.getDepthCell());
            statement.setInt(7, als.getCountCells());
            statement.setInt(8, lcService.getLCId(als.getLc()));
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

    public Map<ALS,Integer> loadALSListFromDB(int projectId) throws SQLException {
        Map<ALS,Integer> uniqueALS = new HashMap<>();
        String sqlGetALSFromProject ="select als_id,quantity from project_als where project_id=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlGetALSFromProject)){
            statement.setInt(1, projectId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int alsId = result.getInt(1);
                int quantity=result.getInt(2);
                ALS als =loadALSFromDB(alsId);
                uniqueALS.put(als,quantity);
                System.out.println("LOAD list ALS... "+als.getDescription());
            }

        } catch (SQLException e) {
            logger.error("Не удалось получить список ALS " +String.valueOf(e));
            throw new SQLException(e);
        }
        return uniqueALS;
    }

    private ALS loadALSFromDB(int alsId) throws SQLException {
        ALS als = new ALS();
        String sqlGetALS ="select als.id, name, height,width, depth, upper_frame,bottom_frame,depth_cell,count_cells,\n" +
                "       lc_id, position_lc.position,\n" +
                "        color_body.color as color_body,\n" +
                "        color_door.color as color_door\n" +
                "from als\n" +
                "\n" +
                "inner join colors color_body on als.color_body_id=color_body.id\n" +
                "inner join colors color_door on als.color_door_id=color_door.id\n" +
                "inner join position_lc on als.position_LC_id = position_lc.id\n" +
                "where als.id=?\n";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlGetALS)){
            statement.setInt(1, alsId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
               // int alsId = result.getInt(1);
                String alsName = result.getString(2);
                als.setId(alsId);
                als.setName(alsName);
                int height = result.getInt(3);
                als.setHeight(height);
                int width = result.getInt(4);
                als.setWidth(width);
                int depth = result.getInt(5);
                als.setDepth(depth);
                int upperFrame = result.getInt(6);
                als.setUpperFrame(upperFrame);
                int bottomFrame = result.getInt(7);
                als.setBottomFrame(bottomFrame);
                int depthCell = result.getInt(8);
                als.setDepthCell(depthCell);
                int countCells = result.getInt(9);
                als.setCountCells(countCells);
                int lcId = result.getInt(10);
                LC lc =lcService.loadLCFromDB(lcId);
                als.setLc(lc);
                lc.setParentALS(als);
                String positionLCId = result.getString(11);
                als.setPositionLC(PositionLC.valueOf(positionLCId));
                String colorDoor = result.getString("color_door");
                als.setColorDoor(Colors.valueOf(colorDoor));
                String colorBody = result.getString("color_body");
                als.setColorBody(Colors.valueOf(colorBody));
                als.setUniqueLB(lbService.loadLBListfromDB(alsId));
                List<LB> lbList=new ArrayList<>();
                for (Map.Entry<LB,Integer> lb:als.getUniqueLB().entrySet()) {
                    for (int i = 0; i < lb.getValue(); i++) {
                        lbList.add(lb.getKey());
                    }
                }
                als.setLbList(lbList);
                als.updateALS();
                System.out.println("LOAD ALS..."+als.getDescription());
            }
        } catch (SQLException e) {
            logger.error("Не удалось получить ALS" + e);
            throw new SQLException(e);
        }
        return als;

    }
}
