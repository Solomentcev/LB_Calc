package view;

import als.Project;
import controller.Controller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class View extends JFrame implements ActionListener {
    private Controller controller;
    private final ProjectTabPanel projectTabPanel =new ProjectTabPanel();

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
            case "Открыть из БД": controller.openProjectFromDB(); break;
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
        setMinimumSize(new Dimension(500,200));
        pack();
    }
    public void showAbout() {
        JOptionPane.showMessageDialog(this, "Проекты АКХ", "О программе", JOptionPane.INFORMATION_MESSAGE);
    }
    public ProjectPanel initProject(Project project){
        projectTabPanel.addProjectPanel(project);
        getContentPane().add(projectTabPanel,BorderLayout.CENTER);
        setMinimumSize(new Dimension(500,200));
        pack();
        return (ProjectPanel) projectTabPanel.getSelectedComponent();
    }


    public File openProject() {
        File file=null;
        String homeDirectory=controller.getProps().getProperty("homeDirectory");

        JFileChooser fileChooser=new JFileChooser(homeDirectory);

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
        String[][] FILTERS = {
                {"JSON(*.json)","json" },
                {"XML(*.xml)","xml" },
                {"Файлы Проект АКХ (*.alx)","alx"},
        };

        fileChooser.setDialogTitle("Выберите файл");
        // Определяем фильтры типов файлов
        for (int i = 0; i < FILTERS.length; i++) {
            FileNameExtensionFilter ff= new FileNameExtensionFilter (FILTERS[i][0],
                    FILTERS[i][1]);
            fileChooser.setFileFilter(ff);

        }
        fileChooser.setAcceptAllFileFilterUsed(false);

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION )
            JOptionPane.showMessageDialog(this,
                    "Выбран файл  " +
                            fileChooser.getSelectedFile());
        file=fileChooser.getSelectedFile();
        return file;
    }

    public void saveProject() {

    }

    public File saveProjectAs(String fileName) {
        String saveDirectory=controller.getProps().getProperty("homeDirectory");
        File file=new File(saveDirectory+"\\"+fileName+"."+"alx");
        JFileChooser fileChooser=new JFileChooser(saveDirectory);
        fileChooser.setCurrentDirectory(file);
        fileChooser.setDialogTitle("Сохранение файла");
        fileChooser.setSelectedFile(file);
        String[][] FILTERS = {
                {"JSON(*.json)","json" },
                {"XML(*.xml)","xml" },
                {"Файлы Word (*.docx)","docx" },
                {"Adobe Reader(*.pdf)","pdf" },
                {"Image(*.jpg)","jpg"  },
                {"Image(*.png)","png"  },
                {"Файлы Проект АКХ (*.alx)","alx" }
        };
        // Определяем фильтры типов файлов
        for (int i = 0; i < FILTERS.length; i++) {
            FileNameExtensionFilter ff= new FileNameExtensionFilter (FILTERS[i][0],
                    FILTERS[i][1]);
            fileChooser.setFileFilter(ff);
        }
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY, new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                String currentDir = ((BasicFileChooserUI)fileChooser.getUI()).getDirectoryName();
                String currentName = ((BasicFileChooserUI)fileChooser.getUI()).getFileName();
                String currentPath=currentDir+currentName;
                FileNameExtensionFilter ff= (FileNameExtensionFilter) e.getNewValue();
                if (!currentName.contains("."))
                 currentName=currentName+"."+ff.getExtensions()[0];
                else {
                    currentName=currentName.substring(0,currentName.lastIndexOf("."))+"."+ff.getExtensions()[0];}
                fileChooser.setSelectedFile(new File(currentDir+"\\"+currentName));
            }
        });
        // Определение режима - только файл
         fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
         int result = fileChooser.showSaveDialog(this);
        FileNameExtensionFilter ff= (FileNameExtensionFilter) fileChooser.getFileFilter();
        file=fileChooser.getSelectedFile();
        if (result == JFileChooser.APPROVE_OPTION ) {
            ff=(FileNameExtensionFilter) fileChooser.getFileFilter();
            if (!file.getName().contains("."))
                file=new File(file.getPath()+"."+ff.getExtensions()[0]);
            else {
                file=new File(file.getPath().substring(0,file.getPath().lastIndexOf("."))+"."+ff.getExtensions()[0]);}
            fileChooser.setSelectedFile(file);

            while (file.exists()) {
                int res=JOptionPane.showConfirmDialog(fileChooser, "Файл " + fileChooser.getSelectedFile()+ " уже существует. Перезаписать?", "", JOptionPane.YES_NO_OPTION);
                if (res==JOptionPane.YES_OPTION) break;
                if (res==JOptionPane.NO_OPTION){
                    fileChooser.showSaveDialog(this);
                    file=fileChooser.getSelectedFile();
                    System.out.println("Выбран файл "+file);
                    ff=(FileNameExtensionFilter) fileChooser.getFileFilter();
                    if (!file.getName().contains("."))
                        file=new File(file.getPath()+"."+ff.getExtensions()[0]);
                    else {
                        file=new File(file.getPath().substring(0,file.getPath().lastIndexOf("."))+"."+ff.getExtensions()[0]);}
                    fileChooser.setSelectedFile(file);
                }
            }
        }
        else file=null;

        return file;
    }

    public void openProjectsFromDB() {
        ProjectsFrame projectsFrame=new ProjectsFrame();
    }
}

