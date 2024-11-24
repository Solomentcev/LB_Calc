package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;

public class ProjectsFrame extends JFrame {
    public ProjectsFrame() throws HeadlessException {
        addWindowListener(new WindowAdapter() {
        });
        setTitle("Проекты");
        setVisible(true);
        setMinimumSize(new Dimension(500,200));
    }
}

