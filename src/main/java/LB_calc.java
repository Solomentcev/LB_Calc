
import controller.Controller;
import view.View;

import javax.swing.*;

public class LB_calc {
    public static void main(String[] args) {
        View view=new View();
        Controller controller=new Controller(view);
        view.setController(controller);
        SwingUtilities.invokeLater(view::init);

        controller.init();
    }
}
