package service;

import model.ALS;
import model.LB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.LCPanel;

import java.sql.*;
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
}
