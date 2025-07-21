import controller.Controller;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;

import javax.swing.*;
import java.io.FileInputStream;

public class LB_calc {
    private static final Logger logger = LoggerFactory.getLogger(LB_calc.class);

    public static void main(String[] args) {
        logger.info("Start");
        View view=new View();
        Controller controller=new Controller(view);
        view.setController(controller);
        SwingUtilities.invokeLater(view::init);
        controller.init();
    }
}
