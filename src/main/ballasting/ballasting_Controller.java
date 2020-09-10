package main.ballasting;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import main.HibernateUtil;
import main.entities.Ballasting;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ballasting_Controller {
    @FXML public ToggleButton avgsButton;
    @FXML public ToggleButton sdButton;
    @FXML public ToggleButton diffButton;
    @FXML public TreeTableView BallTable;

    List<SeqData> tenSeq = new ArrayList<>();
    private TreeItem<SeqData> root = new TreeItem<>();

    private int str = 0;

    public void initialize() {
        //TODO data colorcode

        //TODO load last 10 seq data
        BallTable.setRoot(root);
        ToggleGroup toggleGroup = new ToggleGroup();

        avgsButton.setToggleGroup(toggleGroup);
        sdButton.setToggleGroup(toggleGroup);
        diffButton.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(avgsButton);

        toggleGroup.selectedToggleProperty().addListener(event -> {
            System.out.println((ToggleButton)  toggleGroup.getSelectedToggle());
        });

        List<Ballasting> last_seq_data = new ArrayList<Ballasting>();
        //list of last 10 seq
        List<Integer>seq = getLastSeq();
        //list of streamers
        List<Integer>str = getStrList();
        //list of compasses
        List<Integer>compass = getCompassList();

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

        Runnable getRes = () -> {
            fillTable();
        };
        new Thread(getRes).start();
    }

    public void fillTable(){
        str = 0;
        //clear table
        root.getChildren().clear();


        tenSeq.stream().forEach((Result)-> {
            if(Result.getStreamer() != str){
                TreeItem<SeqData> strN = new TreeItem<>(new SeqData(str+1, 0, 0, 0.0, 0.0, 0, 0, 0, 0, 0, 0, 0));
                strN.getChildren().add(new TreeItem<>(new SeqData(Result.getStreamer(), Result.getUnit(), Result.getSeq1_mean(), Result.getSeq2_mean(), Result.getSeq3_mean(), Result.getSeq4_mean(), Result.getSeq5_mean(), Result.getSeq6_mean(), Result.getSeq7_mean(), Result.getSeq8_mean(), Result.getSeq9_mean(), Result.getSeq10_mean())));
                str++;
                root.getChildren().add(strN);
            } else {
                TreeItem t = root.getChildren().get(str-1);
                t.getChildren().add(new TreeItem<>(new SeqData(Result.getStreamer(), Result.getUnit(), Result.getSeq1_mean(), Result.getSeq2_mean(), Result.getSeq3_mean(), Result.getSeq4_mean(), Result.getSeq5_mean(), Result.getSeq6_mean(), Result.getSeq7_mean(), Result.getSeq8_mean(), Result.getSeq9_mean(), Result.getSeq10_mean())));
            }
        });
        str = 0;
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
}
