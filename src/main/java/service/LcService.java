package service;

import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static service.ConnectionToMySqlDb.getConnection;

public class LcService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);
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
    public int insertLCToDB(LC lc) throws SQLException {
        String sqlLC="insert into lc (name, height, width,depth,\n" +
                "display_Id ,bar_reader_id,payment_id,\n" +
                "printer,rfid_reader,color_body_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlLC, Statement.RETURN_GENERATED_KEYS))
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

    public LC loadLCFromDB(int lcId) throws SQLException {
        LC lc=new LC();
        String sqlGetLC ="select lc.id, name, height,width, depth, display, reader, payment, printer, rfid_reader, color\n" +
                "from lc, display, bar_reader, payment, colors\n" +
                "             where lc.id=? and\n" +
                "             lc.display_id=display.id and\n" +
                "             lc.payment_id=payment.id and\n" +
                "             lc.color_body_id=colors.id and\n" +
                "             lc.bar_reader_id=bar_reader.id;";
        try (Connection connection=getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlGetLC)){
            statement.setInt(1, lcId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String lcName = result.getString("name");
                lc.setName(lcName);
                int height = result.getInt("height");
                lc.setHeight(height);
                int width = result.getInt("width");
                lc.setWidth(width);
                int depth = result.getInt("depth");
                lc.setDepth(depth);
                String display = result.getString("display");
                lc.setDisplay(DisplayLC.valueOf(String.valueOf(display).toUpperCase()));
                String reader = result.getString("reader");
                lc.setBarReader(BarReader.valueOf(String.valueOf(reader).toUpperCase()));
                String payment= result.getString("payment");
                lc.setPayment(Payment.valueOf(String.valueOf(payment).toUpperCase()));
                boolean printer = result.getBoolean("printer");
                lc.setPrinter(printer);
                boolean rfidReader = result.getBoolean("rfid_reader");
                lc.setRfidReader(rfidReader);
                String colorBody = result.getString("color");
                lc.setColorBody(Colors.valueOf(String.valueOf(colorBody)));
                lc.updateDescription();
                lc.updateName();
                System.out.println("Load "+lc.getDescription());
            }
        } catch (SQLException e) {
            logger.error("Не удалось получить LC" + e);
            throw new SQLException(e);
        } catch (DimensionException e) {
            throw new RuntimeException(e);
        }
        return lc;
    }
}
