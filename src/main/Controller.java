package main;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.ballasting.ballasting_Controller;
import main.entities.Projects;
import main.general.DeleteData;
import main.general.PropertiesWorker;
import main.general.ReadData;
import main.general.StoreData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//TODO Dashboard tab with stats
//TODO WEB to GunLink
//TODO add more logging
//TODO unit test
//TODO Streamer Tension
//TODO TapeLog

public class Controller {

    static final Logger logger = LogManager.getLogger(Controller.class.getName());

    public Controller() {

    }

    @FXML private TabPane tabPane;
    @FXML private Tab InsTestPane;
    @FXML private Tab BatteryPane;
    @FXML private Tab BallastingPane;
    @FXML private TextField IPAddress;
    @FXML private TextField UserName;
    @FXML private TextField Password;
    @FXML private TextField Projectname;
    @FXML private TextField Area;
    @FXML private TextField Client;
    @FXML private TextField Status;
    @FXML private TextField Type;
    @FXML private TextField Vesselname;
    @FXML private TextField JobNumber;
    @FXML private Button addProject;
    @FXML private Canvas spread;
    @FXML public TableView<Projects> ProjectsTable;
    @FXML private AnchorPane projectInfo;
    @FXML private AnchorPane spreadView;
    @FXML WebView webView;
    private WebEngine webEngine;

    public static List<LocalDate> InsTestDates = new ArrayList<>();
    public static List<Projects> projects = new ArrayList<>();
    long selectedProject = 0;

    @FXML public void initialize(){
        logger.warn("Entering application.");

        Runnable readPref = () -> {
            PropertiesWorker.ReadPreferences();
            IPAddress.setText(PropertiesWorker.MySQLDB_IP);
            UserName.setText(PropertiesWorker.USERNAME);
            Password.setText(PropertiesWorker.PASSWORD);

            InsTestDates = ReadData.getInsTestDates();
        };
        new Thread(readPref).start();

        tabPane.getSelectionModel().select(0);
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
            if(newValue == InsTestPane) {
                logger.warn("instest selected");
                InsTestDates = ReadData.getInsTestDates();
            } else if(newValue == BatteryPane) {
                logger.warn("battery tab selected");
            } else if(newValue == BallastingPane) {
                logger.warn("ballasting tab selected");
                ballasting_Controller.doBallasting();
            }
            else {
                logger.warn("other tab");
            }
        });

        ProjectsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedProject = newValue.getId();
            }
        });


        GraphicsContext gc = spread.getGraphicsContext2D();
        drawStreamers(gc);
//        drawCoordinates(gc);
//
//        webEngine = webView.getEngine();
//        try {
//            webEngine.load("10.103.1.30");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        update();
    }

    public void update(){
        Platform.runLater(() -> {
            projects = ReadData.getAllProjects();
            //fill limits table
            final ObservableList<Projects> data = FXCollections.observableArrayList(projects);
            ProjectsTable.setItems(data);
        });
    }

    private void drawStreamers(GraphicsContext gc) {
        gc.setStroke(Color.YELLOW);
        gc.setLineWidth(5);

        int str = 6;
        int length = 3000;

        int startx = 10;

        int starty = 10;
        int endx = 10;
        int endy = length;
        int separation = 30;

        for(int i = 1; i<=str; i++){
            gc.strokeLine(startx, starty, endx, endy);
            startx = startx + separation;
            endx = endx + separation;
        }

//        gc.strokeLine(40, 10, 10, 40);
//
//        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
//        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);

    }

    private void drawCoordinates(GraphicsContext gc) {
        gc.setStroke(Color.GRAY);
        gc.setLineWidth(1);

        int width = 2;
        int length = 300;

        int startx = 10;

        int starty = 0;
        int endx = 10;
        int endy = length;
        int separation = 100;

        for(int i = 1; i<=width; i++){
            gc.strokeLine(startx, starty, endx, endy);
            startx = startx + separation;
            endx = endx + separation;
        }

        for(int i = 1; i<=length; i=i+separation){
            gc.strokeLine(starty, startx, endy, endx);
            startx = startx + separation;
            endx = endx + separation;
        }
    }

    @FXML public void storeDBProperty(){
        Runnable store = () -> PropertiesWorker.storeDBPreferences(
                IPAddress.getText(),
                UserName.getText(),
                Password.getText());
        new Thread(store).start();
    }

    @FXML public void addProjectButton(){
        logger.warn("New Project");
        // collect information
        Projects pr = new Projects(
                Projectname.getText(),
                Area.getText(),
                Client.getText(),
                JobNumber.getText(),
                Type.getText(),
                Status.getText(),
                Vesselname.getText());

        // store information
        StoreData.storeProject(pr);

        // clear text fields UI
        Projectname.clear();
        Area.clear();
        Client.clear();
        JobNumber.clear();
        Type.clear();
        Status.clear();
        Vesselname.clear();
        update();
    }

    @FXML public void showProjectInfo(){
        projectInfo.setPrefWidth(100);
    }
    @FXML public void showSpreadView(){
        spreadView.setPrefWidth(100);
    }

    @FXML public void deleteProjectButton(){
        logger.warn("delete " + selectedProject);
        DeleteData.deleteProject(selectedProject);
        update();
    }

    @FXML public void checkProjectFields(){
        if(Status.getText().isBlank() |
                Type.getText().isBlank() |
                Projectname.getText().isBlank() |
                Client.getText().isBlank() |
                Area.getText().isBlank() |
                Vesselname.getText().isBlank() |
                JobNumber.getText().isBlank()){
            addProject.setDisable(true);
        } else {
            addProject.setDisable(false);
        }
    }

}
