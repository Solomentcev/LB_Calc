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

public class LbService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

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
    public int insertLBToDB(LB lb) throws SQLException {
        String sqlLB="insert into lb (name,type_LB_id, height, width, depth, " +
                "upper_frame, bottom_frame,shelf_thick, count_cells, " +
                "height_cell, width_cell, depth_cell, " +
                "direction_door_opening_id, color_door_id, color_body_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlLB, Statement.RETURN_GENERATED_KEYS))
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

    public Map<LB, Integer> loadLBListfromDB(int alsId) throws SQLException {
        Map<LB, Integer> uniqueLB=new HashMap<>();
        String sqlGetLBFromALS ="select lb_id,quantity from als_lb where als_id=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlGetLBFromALS)){
            statement.setInt(1, alsId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                int lbId = result.getInt(1);
                int quantity=result.getInt(2);
                LB lb =loadLBFromDB(lbId);
                uniqueLB.put(lb,quantity);
                System.out.println("LOAD list LB... "+lb.getDescription());
            }

        } catch (SQLException e) {
            logger.error("Не удалось получить список ALS " +String.valueOf(e));
            throw new SQLException(e);
        }
        return uniqueLB;
    }

    private LB loadLBFromDB(int lbId) throws SQLException {
        LB lb=new LB();
        String sqlGetLB ="select lb.id, name,\n" +
                "       height,width, depth,\n" +
                "       upper_frame,bottom_frame,\n" +
                "       shelf_thick,\n" +
                "       height_cell,width_cell, depth_cell,count_cells,\n" +
                "       type_lb.type,\n" +
                "       direction_door_opening,\n" +
                "       color_body.color as color_body,\n" +
                "       color_door.color as color_door\n" +
                "from lb\n" +
                "         inner join colors color_body on lb.color_body_id=color_body.id\n" +
                "         inner join colors color_door on lb.color_door_id=color_door.id\n" +
                "         inner join direction_door_opening on lb.direction_door_opening_id =  direction_door_opening.id\n" +
                "         inner join type_lb on lb.type_LB_id = type_lb.id\n" +
                "where lb.id=?;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlGetLB)){
            statement.setInt(1, lbId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                // int alsId = result.getInt(1);
                String lbName = result.getString("name");
                lb.setName(lbName);
                lb.setHeight(result.getInt("height"));
                String type = result.getString("type");
                lb.setType(type);
                lb.setBottomFrame(result.getInt("bottom_frame"));
                lb.setUpperFrame(result.getInt("upper_frame"));
                lb.setDepth(result.getInt("depth"));
                lb.setWidth(result.getInt("width"));
                lb.setHeightCell(result.getInt("height_cell"));
                lb.setWidthCell(result.getInt("width_cell"));
                lb.setDepthCell(result.getInt("depth_cell"));
                lb.setCountCells(result.getInt("count_cells"));
                lb.setDirectionDoorOpening(DirectionDoorOpening.valueOf(result.getString("direction_door_opening")));
                lb.setColorBody(Colors.valueOf(result.getString("color_body")));
                lb.setColorDoor(Colors.valueOf(result.getString("color_door")));
                lb.updateName();
                lb.updateDescription();
                System.out.println("LOAD LB... "+lb.getDescription());
            }
        } catch (SQLException e) {
            logger.error("Не удалось получить LB" + e);
            throw new SQLException(e);
        } catch (DimensionException e) {
            throw new RuntimeException(e);
        }
        return lb;
    }
}
