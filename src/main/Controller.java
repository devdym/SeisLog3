package main;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import main.ballasting.SeqData;
import main.ballasting.ballasting_Controller;
import main.entities.Ballasting;
import main.entities.Projects;
import main.general.PropertiesWorker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @FXML private Button removeProject;
    @FXML private Canvas spread;

    public static List<LocalDate> InsTestDates = new ArrayList<>();
    public static List<Ballasting> last_seq_data = new ArrayList<>();
    //list of last 10 seq
    public static List<Integer>seq = new ArrayList<>();
    //list of streamers
    public static List<Integer>str = new ArrayList<>();
    //list of compasses
    public static List<Integer>compass = new ArrayList<>();
    List<SeqData> tenSeq = new ArrayList<>();

    @FXML public void initialize(){
        logger.warn("Entering application.");

        Runnable readPref = () -> {
            PropertiesWorker.ReadPreferences();
            IPAddress.setText(PropertiesWorker.MySQLDB_IP);
            UserName.setText(PropertiesWorker.USERNAME);
            Password.setText(PropertiesWorker.PASSWORD);

            InsTestDates = getInsTestDates();
        };
        new Thread(readPref).start();

        tabPane.getSelectionModel().select(0);
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) -> {
            if(newValue == InsTestPane) {
                logger.warn("instest selected");
                InsTestDates = getInsTestDates();
            } else if(newValue == BatteryPane) {
                logger.warn("battery tab selected");
            } else if(newValue == BallastingPane) {
                logger.warn("ballasting tab selected");
                last_seq_data = new ArrayList<>();
                //list of last 10 seq
                seq = getLastSeq();
                //list of streamers
                str = getStrList();
                //list of compasses
                compass = getCompassList();

                for(Integer s : seq) {
                    List<Ballasting> data = getDataList(s);
                    last_seq_data.addAll(data);
                }

                for (int st : str) {
                    for (int u : compass) {
                        double s1 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(0))
                                .collect(Collectors.toList()).get(0).getMean();
                        double s2 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(1))
                                .collect(Collectors.toList()).get(0).getMean();
                        double s3 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(2))
                                .collect(Collectors.toList()).get(0).getMean();
                        double s4 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(3))
                                .collect(Collectors.toList()).get(0).getMean();
                        double s5 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(4))
                                .collect(Collectors.toList()).get(0).getMean();
                        double s6 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(5))
                                .collect(Collectors.toList()).get(0).getMean();
                        double s7 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(6))
                                .collect(Collectors.toList()).get(0).getMean();
                        double s8 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(7))
                                .collect(Collectors.toList()).get(0).getMean();
                        double s9 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(8))
                                .collect(Collectors.toList()).get(0).getMean();
                        double s10 = last_seq_data
                                .stream()
                                .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(9))
                                .collect(Collectors.toList()).get(0).getMean();

                        tenSeq.add(new SeqData(st, u, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10));
                    }
                }

                ballasting_Controller.fillTable(tenSeq);
            }
            else {
                logger.warn("other tab");
            }
        });

        GraphicsContext gc = spread.getGraphicsContext2D();
        drawStreamers(gc);
//        drawCoordinates(gc);
    }

    public static List<LocalDate> getInsTestDates(){
        logger.warn("reading InsTest dates from DB");
        List<LocalDate> res = new ArrayList<>();
        //read InsTest Dates
        Runnable readPreferences = () -> {
            logger.warn("query ins test dates");
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            org.hibernate.Transaction transaction = session.beginTransaction();

            List<LocalDate> tt = session.createQuery("SELECT DISTINCT updated FROM InsTestRes order by updated desc", LocalDate.class).getResultList();
            res.addAll(tt);
            transaction.commit();
        };
        new Thread(readPreferences).start();
        return res;
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

    public List<Integer> getLastSeq(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Integer> query = session.createQuery("SELECT DISTINCT seq FROM Ballasting order by seq desc", Integer.class);
        query.setMaxResults(10);
        List<Integer> res = query.getResultList();
        transaction.commit();
        return res;
    }

    public List<Integer> getStrList(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Integer> query = session.createQuery("SELECT DISTINCT streamer FROM Ballasting order by streamer", Integer.class);
        List<Integer> res = query.getResultList();
        transaction.commit();
        return res;
    }

    public List<Integer> getCompassList(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Integer> query = session.createQuery("SELECT DISTINCT compass FROM Ballasting order by compass", Integer.class);
        List<Integer> res = query.getResultList();
        transaction.commit();
        return res;
    }

    public List<Ballasting> getDataList(int seq){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Ballasting> query = session.createQuery("FROM Ballasting WHERE seq = :seq", Ballasting.class);
        query.setParameter("seq", seq);
        List<Ballasting> res = query.getResultList();
        transaction.commit();
        return res;
    }

}
