package view;

import als.Project;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class View extends JFrame implements ActionListener {
    private Controller controller;
    private ProjectTabPanel projectTabPanel =new ProjectTabPanel();

    public View() {
        try {
            UIManager.setLookAndFeel("Metal");
        } catch (Exception e) {
        }
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    public Controller getController() {
        return controller;
    }
    public ProjectTabPanel getProjectTabPanel() {
        return projectTabPanel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command=e.getActionCommand();
        switch (command){
            case "Новый": {controller.addNewProject(); break;}
            case "Открыть": controller.openProject(); break;
            case "Сохранить": controller.saveProject(); break;
            case "Сохранить как...": controller.saveProjectAs(); break;
            case "Выход": controller.exit(); break;
            case "О программе": this.showAbout();
        }
    }
    public void init() {
        initGui();
        addWindowListener(new FrameListener(this));
        setVisible(true);
    }
    public void exit() {
        controller.exit();
    }
    public void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, menuBar);
        MenuHelper.initHelpMenu(this, menuBar);
        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    public void initGui() {
        initMenuBar();
        setPreferredSize(new Dimension(1000,800));
        pack();
    }
    public void showAbout() {
        JOptionPane.showMessageDialog(this, "Проекты АКХ", "О программе", JOptionPane.INFORMATION_MESSAGE);
    }
    public ProjectPanel initProject(Project project){
       // ProjectPanel projectPanel=new ProjectPanel(project);
        projectTabPanel.addProjectPanel(project);

      //  ButtonTabComponent btnClose=new ButtonTabComponent(projectTabbedPane);
     //   projectTabbedPane.addTab(project.getName(), projectPanel);


     //   projectTabbedPane.setSelectedIndex(projectTabbedPane.getTabCount()-1);
      //  projectTabbedPane.setTabComponentAt(projectTabbedPane.getTabCount()-1,btnClose);

      //  getContentPane().add(projectTabbedPane,BorderLayout.CENTER);
        getContentPane().add(projectTabPanel,BorderLayout.CENTER);
        pack();
        return (ProjectPanel) projectTabPanel.getSelectedComponent();
    }


    public File openProject() {
        File file=null;
        JFileChooser fileChooser=new JFileChooser();

        UIManager.put(
                "FileChooser.saveButtonText", "Сохранить");
        UIManager.put(
                "FileChooser.cancelButtonText", "Отмена");
        UIManager.put(
                "FileChooser.fileNameLabelText", "Наименование файла");
        UIManager.put(
                "FileChooser.filesOfTypeLabelText", "Типы файлов");
        UIManager.put(
                "FileChooser.lookInLabelText", "Директория");
        UIManager.put(
                "FileChooser.saveInLabelText", "Сохранить в директории");
        UIManager.put(
                "FileChooser.folderNameLabelText", "Путь директории");
        String[][] FILTERS = {{"docx", "Файлы Word (*.docx)"},
                {"pdf" , "Adobe Reader(*.pdf)"}};

        fileChooser.setDialogTitle("Выберите файл");
        // Определяем фильтры типов файлов
        for (int i = 0; i < FILTERS[0].length; i++) {
            FileFilterExt eff = new FileFilterExt(FILTERS[i][0],
                    FILTERS[i][1]);
            fileChooser.addChoosableFileFilter(eff);
        }

        // Определение режима - только файл
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);
        // Если файл выбран, покажем его в сообщении
        if (result == JFileChooser.APPROVE_OPTION )
            JOptionPane.showMessageDialog(this,
                    "Выбран файл ( " +
                            fileChooser.getSelectedFile() + " )");
        file=fileChooser.getSelectedFile();
        return file;
    }

    public void saveProject() {

    }

    public File saveProjectAs() {
        File file=null;
        JFileChooser fileChooser=new JFileChooser();
        fileChooser.setDialogTitle("Сохранение файла");
        String[][] FILTERS = {{"alx", "Файлы Проект АКХ (*.alx)"},
                {"xml" , "XML(*.xml)"}};
        // Определяем фильтры типов файлов
        for (int i = 0; i < FILTERS[0].length; i++) {
            FileFilterExt eff = new FileFilterExt(FILTERS[i][0],
                    FILTERS[i][1]);
            fileChooser.addChoosableFileFilter(eff);
        }
        // Определение режима - только файл
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showSaveDialog(this);
        // Если файл выбран, то представим его в сообщении
        if (result == JFileChooser.APPROVE_OPTION ) {
            JOptionPane.showMessageDialog(this,
                    "Файл '" + fileChooser.getSelectedFile() +
                            " ) сохранен");
           file=fileChooser.getSelectedFile();

        }
        return file;
    }

    class FileFilterExt extends javax.swing.filechooser.FileFilter
    {
        String extension  ;  // расширение файла
        String description;  // описание типа файлов

        FileFilterExt(String extension, String descr)
        {
            this.extension = extension;
            this.description = descr;
        }
        @Override
        public boolean accept(java.io.File file)
        {
            if(file != null) {
                if (file.isDirectory())
                    return true;
                if( extension == null )
                    return (extension.length() == 0);
                return file.getName().endsWith(extension);
            }
            return false;
        }
        // Функция описания типов файлов
        @Override
        public String getDescription() {
            return description;
        }
    }
}

