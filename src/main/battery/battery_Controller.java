package main.battery;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Callback;
import main.HibernateUtil;
import main.entities.Batteries;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class battery_Controller {
    @FXML public ToggleButton errorsButton;
    @FXML public DatePicker BatteriesDate;
    @FXML public Spinner minBankA;
    @FXML public Spinner minBankB;
    @FXML public TreeTableView BattTable;
    @FXML public TreeTableColumn<Batteries, Number> bankAColumn;
    @FXML public TreeTableColumn<Batteries, Number> bankBColumn;
    @FXML public LineChart SpreadLC;
    @FXML public CategoryAxis xAxisDate;
    @FXML public NumberAxis yAxisVolts;

    private final TreeItem<Batteries> rootB = new TreeItem<>();
    private final List<LocalDate> BattDates = new ArrayList<>();
    private int str = 0;

    public void initialize() {
        BattTable.setRoot(rootB);

        errorsButton.setSelected(true);
        // Value factory.
        SpinnerValueFactory<Double> valueAFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(4.2, 6.2, 3, 0.1);
        minBankA.setValueFactory(valueAFactory);

        // Value factory.
        SpinnerValueFactory<Double> valueBFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(4.2, 6.2, 3, 0.1);
        minBankB.setValueFactory(valueBFactory);

        //query dates from DB for DatePicker
        Runnable readBatteriesDates = () -> {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            org.hibernate.Transaction transaction = session.beginTransaction();

            List<LocalDate> batteryDate =
                    session.createQuery("SELECT DISTINCT date_ FROM Batteries order by date_ desc", LocalDate.class)
                            .getResultList();
            BattDates.addAll(batteryDate);
            transaction.commit();

            System.out.println(BattDates.get(0));
            BatteriesDate.setValue(BattDates.get(0));
        };
        new Thread(readBatteriesDates).start();

        //Batt Date Picker factory
        final Callback<DatePicker, DateCell> BatteriesDateFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(true);
                        BattDates.forEach(d -> {
                            if (item.equals(d)) {
                                setStyle("-fx-background-color: #41ee82;");
                                setDisable(false);
                            }
                        });
                    }
                };
            }
        };
        BatteriesDate.setDayCellFactory(BatteriesDateFactory);

        minBankB.valueProperty().addListener(event -> update());
        minBankA.valueProperty().addListener(event -> update());
        errorsButton.setOnAction(event -> update());
        BatteriesDate.setOnAction(event -> update());

        // table select listener
        BattTable.getSelectionModel().selectedItemProperty().addListener((ChangeListener<TreeItem<Batteries>>)
                (observable, oldValue, newValue) -> drawBattGraph(newValue.getValue()));

        // color code for bankA column
        bankAColumn.setCellFactory((TreeTableColumn<Batteries, Number> param) -> {
            TreeTableCell cell = new TreeTableCell<Batteries, Number>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<Batteries> ttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        ttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() > 4.2
                                ? ""
                                : "-fx-background-color:red");
                    }
                }
            };
            return cell;
        });
        // color code for bankB column
        bankBColumn.setCellFactory((TreeTableColumn<Batteries, Number> param) -> {
            TreeTableCell cell = new TreeTableCell<Batteries, Number>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<Batteries> ttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        ttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() > 4.2
                                ? ""
                                : "-fx-background-color:red");
                    }
                }
            };
            return cell;
        });
    }

    private void update(){
        LocalDate date = BatteriesDate.getValue();
        Runnable getRes = () -> {
            List<Batteries> bt;
            if (errorsButton.isSelected()) {
                System.out.println("selected");
                minBankA.setDisable(true);
                minBankB.setDisable(true);
                bt = getAllBatt(date);
                fillTable(bt);
            }else {
                System.out.println("NOT selected");
                bt = getErrBatt(date, (Double) minBankA.getValue(), (Double) minBankB.getValue());
                fillTable(bt);
                minBankA.setDisable(false);
                minBankB.setDisable(false);
            }
        };
        new Thread(getRes).start();
    }

    private static List<Batteries> getAllBatt(LocalDate date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Batteries> query =
                session.createQuery("FROM Batteries where date_ like :dateupd",
                        Batteries.class);
        query.setParameter("dateupd", date);
        List<Batteries> bt = query.getResultList();
        transaction.commit();
        return bt;
    }

    private static List<Batteries> getErrBatt(LocalDate date, Double min_a_v, Double min_b_v){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Batteries> query =
                session.createQuery("FROM Batteries where date_ like :dateupd and bankA < :minA or bankB < :minB",
                        Batteries.class);
        query.setParameter("dateupd", date);
        query.setParameter("minA", min_a_v);
        query.setParameter("minB", min_b_v);
        List<Batteries> bt = query.getResultList();
        transaction.commit();
        return bt;
    }

    private void fillTable(List<Batteries> bt){
        //clear table
        rootB.getChildren().clear();

        bt.stream().forEach((Result)-> {
            if(Result.getStreamerNumber() != str){
                TreeItem<Batteries> strN =
                        new TreeItem<>(new Batteries(
                                str+1,
                                "C",
                                0,
                                0.0,
                                0.0,
                                "a"));
                strN.setExpanded(true);
                strN.getChildren().add(
                        new TreeItem<>(
                            new Batteries(
                                    Result.getStreamerNumber(),
                                    Result.getUnit(),
                                    Result.getUnitNumber(),
                                    Result.getBankA(),
                                    Result.getBankB(),
                                    Result.getActiveBank())));
                str++;
                rootB.getChildren().add(strN);
            } else {
                TreeItem t = rootB.getChildren().get(str-1);
                t.getChildren().add(
                        new TreeItem<>(
                                new Batteries(
                                        Result.getStreamerNumber(),
                                        Result.getUnit(),
                                        Result.getUnitNumber(),
                                        Result.getBankA(),
                                        Result.getBankB(),
                                        Result.getActiveBank())));
            }
        });
        str = 0;
    }

    public void drawBattGraph(Batteries unit) {
        System.out.println("ShowBattGraph selected");

        Platform.runLater(() -> {
            String selected = unit.getUnitName();
            System.out.println("selected unit " + selected);

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            Transaction transaction = session.beginTransaction();

            Query<Batteries> query = session.createQuery("FROM Batteries where unit like :u order by date_", Batteries.class);
            query.setParameter("u", selected);
            List<Batteries> sp = query.getResultList();
            transaction.commit();

            ObservableList<XYChart.Series<String, Double>> series = observableArrayList();
            series.retainAll();
            XYChart.Series aSeries = new XYChart.Series();
            XYChart.Series bSeries = new XYChart.Series();

            for (Batteries batteries : sp) {
                aSeries.getData().add(new XYChart.Data(batteries.getDate_().toString(), batteries.getBankA()));
                bSeries.getData().add(new XYChart.Data(batteries.getDate_().toString(), batteries.getBankB()));
            }
            aSeries.setName("Bank A");
            bSeries.setName("Bank B");
            series.addAll(aSeries, bSeries);

            SpreadLC.getData().retainAll();
            SpreadLC.getData().addAll(series);
            aSeries.getNode().setStyle("-fx-stroke: green;");
            bSeries.getNode().setStyle("-fx-stroke: orange;");
        });
    }

}

