package main.battery;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.util.Callback;
import main.HibernateUtil;
import main.entities.Batteries;
import main.entities.InsTestRes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class battery_Controller {
    //TODO data colorcode
    @FXML public ToggleButton errorsButton;
    @FXML public DatePicker BatteriesDate;
    @FXML public Spinner minBankA;
    @FXML public Spinner minBankB;
    @FXML public TreeTableView BattTable;
    @FXML public TreeTableColumn<Batteries, Number> bankAColumn;
    @FXML public TreeTableColumn<Batteries, Number> bankBColumn;
    @FXML public LineChart SpreadLC;
    @FXML public CategoryAxis xAxisDate;
    @FXML public NumberAxis yAxisIns;

    private TreeItem<Batteries> rootB = new TreeItem<>();
    private List<LocalDate> BattDates = new ArrayList<>();
    private int str = 0;

    public void initialize() {
        BattTable.setRoot(rootB);

        //TODO Select last date

        // Value factory.
        SpinnerValueFactory<Double> valueAFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(4.2, 6.2, 3, 0.1);
        minBankA.setValueFactory(valueAFactory);
        // Value factory.
        SpinnerValueFactory<Double> valueBFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(4.2, 6.2, 3, 0.1);
        minBankB.setValueFactory(valueBFactory);

        //read preferences
        Runnable readBatteriesDates = () -> {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.getCurrentSession();
            org.hibernate.Transaction transaction = session.beginTransaction();

            List<LocalDate> batteryDate = session.createQuery("SELECT DISTINCT date_ FROM Batteries", LocalDate.class).getResultList();
            BattDates.addAll(batteryDate);
            transaction.commit();
        };
        new Thread(readBatteriesDates).start();

        //BattDatePicker
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

        minBankB.valueProperty().addListener(event -> {
            LocalDate date = BatteriesDate.getValue();
            Runnable getRes = () -> {
                List<Batteries> bt;
                if (errorsButton.isSelected()) {
                    System.out.println("selected");
                    bt = getAllBatt(date);
                    fillTable(bt);
                }else {
                    System.out.println("NOT selected");
                    bt = getErrBatt(date, (Double) minBankA.getValue(), (Double) minBankB.getValue());
                    fillTable(bt);
                }
            };
            new Thread(getRes).start();
        });
        minBankA.valueProperty().addListener(event -> {
            LocalDate date = BatteriesDate.getValue();
            Runnable getRes = () -> {
                List<Batteries> bt;
                if (errorsButton.isSelected()) {
                    System.out.println("selected");
                    bt = getAllBatt(date);
                    fillTable(bt);
                }else {
                    System.out.println("NOT selected");
                    bt = getErrBatt(date, (Double) minBankA.getValue(), (Double) minBankB.getValue());
                    fillTable(bt);
                }
            };
            new Thread(getRes).start();
        });

        errorsButton.setOnAction(event -> {
            LocalDate date = BatteriesDate.getValue();
            Runnable getRes = () -> {
                List<Batteries> bt;
                if (errorsButton.isSelected()) {
                    System.out.println("selected");
                    bt = getAllBatt(date);
                    fillTable(bt);
                }else {
                    System.out.println("NOT selected");
                    bt = getErrBatt(date, (Double) minBankA.getValue(), (Double) minBankB.getValue());
                    fillTable(bt);
                }
            };
            new Thread(getRes).start();
        });

        BatteriesDate.setOnAction(event -> {
            LocalDate date = BatteriesDate.getValue();
            Runnable getRes = () -> {
                List<Batteries> bt;
                if (errorsButton.isSelected()) {
                    System.out.println("selected");
                    bt = getAllBatt(date);
                    fillTable(bt);
                }else {
                    System.out.println("NOT selected");
                    bt = getErrBatt(date, (Double) minBankA.getValue(), (Double) minBankB.getValue());
                    fillTable(bt);
                }
            };
            new Thread(getRes).start();
        });

        BattTable.getSelectionModel().selectedItemProperty().addListener((ChangeListener<TreeItem<Batteries>>)
                (observable, oldValue, newValue) -> ShowBattGraph(newValue.getValue()));

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

    public void ShowBattGraph(Batteries unit) {
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

            ObservableList<XYChart.Series<Integer, Integer>> series = observableArrayList();
            series.retainAll();
            XYChart.Series aSeries = new XYChart.Series();
            XYChart.Series bSeries = new XYChart.Series();

            for(int i=0; i<sp.size(); i++) {
                aSeries.getData().add(new XYChart.Data(sp.get(i).getDate_().toString(), sp.get(i).getBankA()));
                bSeries.getData().add(new XYChart.Data(sp.get(i).getDate_().toString(), sp.get(i).getBankB()));
            }
            aSeries.setName("Bank A");
            bSeries.setName("Bank B");
            series.addAll(aSeries, bSeries);

            SpreadLC.getData().retainAll();
            SpreadLC.getData().addAll(series);
        });
    }

    private static List<Batteries> getAllBatt(LocalDate date){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Batteries> query = session.createQuery("FROM Batteries where date_ like :dateupd", Batteries.class);
        query.setParameter("dateupd", date);
        List<Batteries> bt = query.getResultList();
        transaction.commit();
        return bt;
    }

    private static List<Batteries> getErrBatt(LocalDate date, Double min_a_v, Double min_b_v){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        Query<Batteries> query = session.createQuery("FROM Batteries where date_ like :dateupd and bankA < :minA or bankB < :minB", Batteries.class);
        query.setParameter("dateupd", date);
        query.setParameter("minA", min_a_v);
        query.setParameter("minB", min_b_v);
        List<Batteries> bt = query.getResultList();
        transaction.commit();
        return bt;
    }

    private void fillTable(List<Batteries> bt){
        str = 0;
        //clear table
        rootB.getChildren().clear();

        bt.stream().forEach((Result)-> {
            if(Result.getStreamerNumber() != str){
                TreeItem<Batteries> strN = new TreeItem<>(new Batteries(str+1, "C", 0, 0.0, 0.0, "a"));
                strN.getChildren().add(new TreeItem<>(new Batteries(Result.getStreamerNumber(), Result.getUnit(), Result.getUnitNumber(), Result.getBankA(), Result.getBankB(), Result.getActiveBank())));
                str++;
                rootB.getChildren().add(strN);
            } else {
                TreeItem t = rootB.getChildren().get(str-1);
                t.getChildren().add(new TreeItem<>(new Batteries(Result.getStreamerNumber(), Result.getUnit(), Result.getUnitNumber(), Result.getBankA(), Result.getBankB(), Result.getActiveBank())));
            }
        });
        str = 0;
    }

}

