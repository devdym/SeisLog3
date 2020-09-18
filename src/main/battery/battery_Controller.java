package main.battery;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.util.Callback;
import main.entities.Batteries;
import main.general.ReadData;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class battery_Controller {
    @FXML public ToggleButton errorsButton;
    @FXML public DatePicker BatteriesDate;
    @FXML public Spinner<Double> minBankA;
    @FXML public Spinner<Double> minBankB;
    @FXML public TreeTableView<Batteries> ResultTable;
    @FXML public TreeTableColumn<Batteries, Number> bankAColumn;
    @FXML public TreeTableColumn<Batteries, Number> bankBColumn;
    @FXML public LineChart<String, Double> BatteriesGraph;
    @FXML public CategoryAxis xAxisDate;
    @FXML public NumberAxis yAxisVolts;

    private final TreeItem<Batteries> root = new TreeItem<>();
    private List<LocalDate> BattDates = new ArrayList<>();
    private int str = 0;

    public void initialize() {
        ResultTable.setRoot(root);

        errorsButton.setSelected(true);
        // Value factory.
        SpinnerValueFactory<Double> valueAFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(4.2, 6.2, 3, 0.1);
        minBankA.setValueFactory(valueAFactory);

        // Value factory.
        SpinnerValueFactory<Double> valueBFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(4.2, 6.2, 3, 0.1);
        minBankB.setValueFactory(valueBFactory);

        //query dates from DB for DatePicker
        Runnable readBatteriesDates = () -> {
            BattDates = ReadData.readBattDate();

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
                                setStyle("-fx-background-color: #FFCC00;");
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
        ResultTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                drawBattGraph(newValue.getValue()));

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
                        setText(item.toString());
                        setStyle(item.doubleValue() > 4.2
                                ? ""
                                : "-fx-text-fill:#FF6633");
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
                        setText(item.toString());
                        setStyle(item.doubleValue() > 4.2
                                ? ""
                                : "-fx-text-fill:#FF6633");
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
                minBankA.setDisable(false);
                minBankB.setDisable(false);
                bt = ReadData.getErrBatt(date, minBankA.getValue(), minBankB.getValue());
                fillTable(bt);
            }else {
                bt = ReadData.getAllBatt(date);
                fillTable(bt);
                minBankA.setDisable(true);
                minBankB.setDisable(true);
            }
        };
        new Thread(getRes).start();
    }

    private void fillTable(List<Batteries> bt){
        //clear table
        root.getChildren().clear();

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
                root.getChildren().add(strN);
            } else {
                TreeItem<Batteries> t = root.getChildren().get(str-1);
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
        Platform.runLater(() -> {
            String selected = unit.getUnitName();
            List<Batteries> sp = ReadData.readBattDataForGraph(selected);

            ObservableList<XYChart.Series<String, Double>> series = observableArrayList();
            series.clear();
            XYChart.Series<String, Double> aSeries = new XYChart.Series<>();
            XYChart.Series<String, Double> bSeries = new XYChart.Series<>();
            aSeries.setName("Bank A");
            bSeries.setName("Bank B");

            for (Batteries batteries : sp) {
                aSeries.getData().add(new XYChart.Data<>(batteries.getDate_().toString(), batteries.getBankA()));
                bSeries.getData().add(new XYChart.Data<>(batteries.getDate_().toString(), batteries.getBankB()));
            }
            series.addAll(aSeries, bSeries);

            BatteriesGraph.getData().clear();
            BatteriesGraph.getData().addAll(series);
        });
    }

}

