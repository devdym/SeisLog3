package main.insTest;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Callback;
import main.HibernateUtil;
import main.entities.InsTestLimits;
import main.entities.InsTestRes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class insTest_Controller {

    @FXML public DatePicker InsTestDate;
    @FXML public TextArea calcInfoArea;
    @FXML public Spinner dS2;
    @FXML public Spinner dS3;
    @FXML public Spinner dS4;
    @FXML public Spinner dS5;
    @FXML public Spinner dS6;
    @FXML public Spinner tS2;
    @FXML public Spinner tS3;
    @FXML public Spinner tS4;
    @FXML public Spinner tS5;
    @FXML public Spinner tS6;
    @FXML public TextField lkgTF;
    @FXML public TextField noiseTF;
    @FXML public TextField capTolTF;
    @FXML public TextField cutTolTF;
    @FXML public LineChart SpreadLC;
    @FXML public NumberAxis xAxisIns;
    @FXML public NumberAxis yAxisIns;
    @FXML public Spinner StrSP;
    @FXML public TreeTableView InsTable;
    @FXML public ToggleButton CapButton;
    @FXML public ToggleButton CutButton;
    @FXML public ToggleButton LeakageButton;
    @FXML public ToggleButton NoiseButton;

    private TreeItem<InsTestRes> root = new TreeItem<>();
    private List<LocalDate> InsTestDates = new ArrayList<>();
    private int str = 0;
    DecimalFormat DecFormatOneOne = new DecimalFormat("#.#");
    List<InsTestLimits> CalculatedLimits = new ArrayList<>();

    public void initialize() {
        //TODO data colorcode

        InsTable.setRoot(root);

        //TODO Select last date

        //read preferences
        Runnable readPreferences = () -> {
            System.out.println("read ins test dates");
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            org.hibernate.Transaction transaction = session.beginTransaction();

            List<LocalDate> tt = session.createQuery("SELECT DISTINCT updated FROM InsTestRes", LocalDate.class).getResultList();
            InsTestDates.addAll(tt);
            transaction.commit();
        };
        new Thread(readPreferences).start();

        //InsDatePicker
        final Callback<DatePicker, DateCell> InsDateFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(true);
                        InsTestDates.forEach(date -> {
                            if (item.equals(date)) {
                                setStyle("-fx-background-color: #3c3fee;");
                                setDisable(false);
                            }
                        });
                    }
                };
            }
        };
        InsTestDate.setDayCellFactory(InsDateFactory);

        InsTestDate.setOnAction(event -> {
            //clear table
            root.getChildren().clear();

            //populate tree table
            LocalDate date = InsTestDate.getValue();
            Runnable getRes = () -> {
                SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
                Session session = sessionFactory.getCurrentSession();
                Transaction transaction = session.beginTransaction();

                Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd", InsTestRes.class);
                query.setParameter("dateupd", date);
                List<InsTestRes> tt = query.getResultList();
                transaction.commit();

                str = 0;
                tt.stream().forEach((Result)-> {
                    if(Result.getStreamer() != str){
                        TreeItem<InsTestRes> strN = new TreeItem<>(new InsTestRes(str+1,0,0,0,0,0,0,0));
                        strN.getChildren().add(new TreeItem<>(new InsTestRes(Result.getStreamer(), Result.getTrace(), Result.getAss_sn(), Result.getType(), Result.getCap(), Result.getCutoff(), Result.getNoise(), Result.getLeakage())));
                        str++;
                        root.getChildren().add(strN);
                    } else {
                        TreeItem t = root.getChildren().get(str-1);
                        t.getChildren().add(new TreeItem<>(new InsTestRes(Result.getStreamer(), Result.getTrace(), Result.getAss_sn(), Result.getType(), Result.getCap(), Result.getCutoff(), Result.getNoise(), Result.getLeakage())));
                    }
                });
                str = 0;
            };
            new Thread(getRes).start();
        });

        tS2.valueProperty().addListener((observable) -> limitsFunc());
        tS3.valueProperty().addListener((observable) -> limitsFunc());
        tS4.valueProperty().addListener((observable) -> limitsFunc());
        tS5.valueProperty().addListener((observable) -> limitsFunc());
        tS6.valueProperty().addListener((observable) -> limitsFunc());
        dS2.valueProperty().addListener((observable) -> limitsFunc());
        dS3.valueProperty().addListener((observable) -> limitsFunc());
        dS4.valueProperty().addListener((observable) -> limitsFunc());
        dS5.valueProperty().addListener((observable) -> limitsFunc());
        dS6.valueProperty().addListener((observable) -> limitsFunc());
    }

    private void limitsFunc() {
        CalculatedLimits.clear();
        int s = 2;
        double cap, cutoff, minCap, maxCap, minCutoff, maxCutoff, capTol, CutoffTol;
        capTol = Double.valueOf(capTolTF.getText());
        CutoffTol = Double.valueOf(cutTolTF.getText());
        List<Integer> te = new ArrayList<>();
        List<Integer> pr = new ArrayList<>();

        te.add((Integer) tS2.getValue());
        te.add((Integer) tS3.getValue());
        te.add((Integer) tS4.getValue());
        te.add((Integer) tS5.getValue());
        te.add((Integer) tS6.getValue());

        pr.add((Integer) dS2.getValue());
        pr.add((Integer) dS3.getValue());
        pr.add((Integer) dS4.getValue());
        pr.add((Integer) dS5.getValue());
        pr.add((Integer) dS6.getValue());

        calcInfoArea.setText("");
        calcInfoArea.appendText("-- Calculated limits capacitance " + capTol + "% and cut off " + CutoffTol + "% --\n");
        for (int i : te) {
            double temp = i;
            double pressure = pr.get(te.indexOf(i));
            pressure = pressure / 10;
            cap = 260.0 + 1.742 * (temp - 20) - 9.230 * pressure;
            minCap = cap - (cap * capTol / 100);
            maxCap = cap + (cap * capTol / 100);
            calcInfoArea.appendText("Capasitance S" + s + "\t" + DecFormatOneOne.format(minCap) + "\t" + DecFormatOneOne.format(maxCap) + "\n");
            s++;
        }
        s = 2;
        for (int i : te) {
            double temp = i;
            double pressure = pr.get(te.indexOf(i));
            pressure = pressure / 10;
            cutoff = 1 / (0.4741 + 0.00277 * (temp - 20) - 0.0147 * pressure);
            minCutoff = cutoff - (cutoff * CutoffTol / 100);
            maxCutoff = cutoff + (cutoff * CutoffTol / 100);
            calcInfoArea.appendText("Cut off S" + s + "\t\t" + DecFormatOneOne.format(minCutoff) + "\t" + DecFormatOneOne.format(maxCutoff) + "\n");
            s++;
        }
        for (int i = 2; i <= 6; i++) {
            calcInfoArea.appendText("Leakage S" + i + "\t" + lkgTF.getText() + "\n");
        }
        for (int i = 2; i <= 6; i++) {
            calcInfoArea.appendText("Noise S" + i + "\t" + noiseTF.getText() + "\n");
        }

        //--------
        s = 2;
        for (Integer i : te) {

            double temp = i;
            double pressure = pr.get(te.indexOf(i));
            capTol = Double.valueOf(capTolTF.getText());
            CutoffTol = Double.valueOf(cutTolTF.getText());
            pressure = pressure / 10;
            cap = 260.0 + 1.742 * (temp - 20) - 9.230 * pressure;
            cutoff = 1 / (0.4741 + 0.00277 * (temp - 20) - 0.0147 * pressure);

            minCap = cap - (cap * capTol / 100);
            maxCap = cap + (cap * capTol / 100);

            minCutoff = cutoff - (cutoff * CutoffTol / 100);
            maxCutoff = cutoff + (cutoff * CutoffTol / 100);

            //   CalculatedLimits.add(new Limits(selectedDate, Double.valueOf(noiseTF.getText()), minCap, maxCap, Double.valueOf(lkgTF.getText()), minCutoff, maxCutoff, s));
            s++;
        }
    }

    @FXML public void ShowCap(ActionEvent actionEvent) {
        System.out.println("cap was selected");

        Platform.runLater(() -> {
            int SelStr = (Integer)StrSP.getValue();

            LocalDate date = InsTestDate.getValue();

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();

            Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd and streamer = :str", InsTestRes.class);
            query.setParameter("dateupd", date);
            query.setParameter("str", SelStr);
            List<InsTestRes> sp = query.getResultList();
            transaction.commit();

            XYChart.Series aSeries = new XYChart.Series();

            for(int i=0; i<sp.size(); i++) {
                aSeries.getData().add(new XYChart.Data(sp.get(i).getTrace(), sp.get(i).getCap()));
            }
            aSeries.setName("Cap");

            SpreadLC.getData().retainAll();
            SpreadLC.getData().add(aSeries);
        });
    }

    @FXML public void ShowCut(ActionEvent actionEvent) {
        System.out.println("cut was selected");

        Platform.runLater(() -> {
            int SelStr = (Integer)StrSP.getValue();

            LocalDate date = InsTestDate.getValue();

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();

            Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd and streamer = :str", InsTestRes.class);
            query.setParameter("dateupd", date);
            query.setParameter("str", SelStr);
            List<InsTestRes> sp = query.getResultList();
            transaction.commit();

            XYChart.Series aSeries = new XYChart.Series();

            for(int i=0; i<sp.size(); i++) {
                aSeries.getData().add(new XYChart.Data(sp.get(i).getTrace(), sp.get(i).getCutoff()));
            }
            aSeries.setName("cutoff");

            SpreadLC.getData().retainAll();
            SpreadLC.getData().add(aSeries);
        });

    }

    @FXML public void ShowLeakage(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            int SelStr = (Integer)StrSP.getValue();

            LocalDate date = InsTestDate.getValue();

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();

            Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd and streamer = :str", InsTestRes.class);
            query.setParameter("dateupd", date);
            query.setParameter("str", SelStr);
            List<InsTestRes> sp = query.getResultList();
            transaction.commit();

            XYChart.Series aSeries = new XYChart.Series();

            for(int i=0; i<sp.size(); i++) {
                aSeries.getData().add(new XYChart.Data(sp.get(i).getTrace(), sp.get(i).getLeakage()));
            }
            aSeries.setName("leakage");

            SpreadLC.getData().retainAll();
            SpreadLC.getData().add(aSeries);
        });
    }

    @FXML public void ShowNoise(ActionEvent actionEvent) {
        Platform.runLater(() -> {
            int SelStr = (Integer)StrSP.getValue();

            LocalDate date = InsTestDate.getValue();

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();

            Query<InsTestRes> query = session.createQuery("FROM InsTestRes where updated like :dateupd and streamer = :str", InsTestRes.class);
            query.setParameter("dateupd", date);
            query.setParameter("str", SelStr);
            List<InsTestRes> sp = query.getResultList();
            transaction.commit();

            XYChart.Series aSeries = new XYChart.Series();

            for(int i=0; i<sp.size(); i++) {
                aSeries.getData().add(new XYChart.Data(sp.get(i).getTrace(), sp.get(i).getNoise()));
            }
            aSeries.setName("noise");

            SpreadLC.getData().retainAll();
            SpreadLC.getData().add(aSeries);
        });
    }
}
