package main;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import main.ballasting.ballasting_Controller;
import main.battery.battery_Controller;
import main.entities.Projects;
import main.general.PropertiesWorker;
import main.insTest.insTest_Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

//TODO init git
//TODO add dags to project
//TODO add SiesLogAPI
//TODO Dashboard tab with stats
//TODO Streamer tension
//TODO TapeLog
//TODO WEB to GunLink
//TODO add more logging
//TODO unit test

public class Controller {

    static final Logger logger = LogManager.getLogger(Controller.class.getName());

    public Controller() {

    }

    @FXML private TabPane tabPane;
    @FXML private insTest_Controller insTest_Tab_Controller;
    @FXML private battery_Controller battery_Tab_Controller;
    @FXML private ballasting_Controller ballasting_Tab_Controller;

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
    @FXML private Button removeProject;
    @FXML private Canvas spread;

    @FXML public void initialize(){
        Runnable readPref = () -> {
            PropertiesWorker.ReadPreferences();
            IPAddress.setText(PropertiesWorker.MySQLDB_IP);
            UserName.setText(PropertiesWorker.USERNAME);
            Password.setText(PropertiesWorker.PASSWORD);
        };
        new Thread(readPref).start();

        //TODO check with tab is open and load a date or data

        logger.trace("Entering application.");

        GraphicsContext gc = spread.getGraphicsContext2D();
        drawStreamers(gc);
//        drawCoordinates(gc);
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
        //TODO add logger ip address
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
        SessionFactory sessFact = HibernateUtil.getSessionFactory();
        Session session = sessFact.getCurrentSession();
        org.hibernate.Transaction tr = session.beginTransaction();
        session.save(pr);
        tr.commit();
        logger.warn("New Project Successfully inserted");
        sessFact.close();

        // clear text fields UI
        Projectname.clear();
        Area.clear();
        Client.clear();
        JobNumber.clear();
        Type.clear();
        Status.clear();
        Vesselname.clear();
    }

    @FXML public void deleteProjectButton(){
        logger.warn("delete");
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