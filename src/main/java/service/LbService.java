package service;

import model.ALS;
import model.LB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

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
}
