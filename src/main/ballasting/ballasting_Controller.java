package main.ballasting;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import main.Controller;
import main.entities.Ballasting;
import main.general.ReadData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class ballasting_Controller {

    static final Logger logger = LogManager.getLogger(Controller.class.getName());

    @FXML public ToggleButton errorsButton;
    @FXML public ToggleButton avgsButton;
    @FXML public ToggleButton minButton;
    @FXML public ToggleButton maxButton;
    @FXML public TreeTableView<SeqData> BallTable;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq1;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq2;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq3;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq4;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq5;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq6;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq7;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq8;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq9;
    @FXML public TreeTableColumn<SeqData, Number> ballColumnSeq10;
    @FXML public Spinner<Double> minWA;
    @FXML public Spinner<Double> maxWA;
    @FXML public Spinner<Integer> skipHead;
    @FXML public Spinner<Integer> skipTail;
    @FXML public LineChart<Number, Number> BallastingLC;
    @FXML public NumberAxis xAxis;
    @FXML public NumberAxis yAxis;

    private static TreeItem<SeqData> root = new TreeItem<>();
    private static int str = 0;
    public static List<Ballasting> last_seq_data = new ArrayList<>();
    //list of last 10 seq
    public static List<Integer>seq = new ArrayList<>();
    //list of streamers
    public static List<Integer>strList = new ArrayList<>();
    //list of compasses
    public static List<Integer>compass = new ArrayList<>();
    public static List<SeqData> tenSeq = new ArrayList<>();
    public static List<SeqData> tenSeqMin = new ArrayList<>();
    public static List<SeqData> tenSeqMax = new ArrayList<>();

    public void initialize() {
        BallTable.setRoot(root);

        // Value factory.
        SpinnerValueFactory<Double> valueminWAFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(-4, 15, -15, 0.1);
        minWA.setValueFactory(valueminWAFactory);
        // Value factory.
        SpinnerValueFactory<Double> valuemaxWAFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(4, 15, -15, 0.1);
        maxWA.setValueFactory(valuemaxWAFactory);
        // Value factory.
        SpinnerValueFactory<Integer> valueskipHeadFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 40, 0, 1);
        skipHead.setValueFactory(valueskipHeadFactory);
        // Value factory.
        SpinnerValueFactory<Integer> valueskipTailFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 40, 0, 1);
        skipTail.setValueFactory(valueskipTailFactory);

        ballColumnSeq1.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });
        ballColumnSeq2.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });
        ballColumnSeq3.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });
        ballColumnSeq4.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });
        ballColumnSeq5.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });
        ballColumnSeq6.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });
        ballColumnSeq7.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });
        ballColumnSeq8.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });
        ballColumnSeq9.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });
        ballColumnSeq10.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell<SeqData, Number> cell = new TreeTableCell<>(){
                @Override
                //by using Number we don't have to parse a String
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<SeqData> cttr = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        cttr.setStyle("");
                        setStyle("");
                    } else {
//                        ttr.setStyle(item.doubleValue() > 4.2
//                                ? "-fx-background-color:lightgreen"
//                                : "-fx-background-color:pink");
                        setText(item.toString());
                        setStyle(item.doubleValue() < 5.0
                                ? ""
                                : "-fx-text-fill:#FF6633");
                    }
                }
            };
            return cell;
        });

        // table select listener
        BallTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> drawChGraph(newValue.getValue()));
    }

    public void drawChGraph(SeqData unit) {
        Platform.runLater(() -> {

            List<Ballasting> sp = ReadData.getBallastinDataForGraph(unit);

            ObservableList<XYChart.Series<Number, Number>> series = observableArrayList();
            series.retainAll();
            XYChart.Series<Number, Number> Series1 = new XYChart.Series<>();
            XYChart.Series<Number, Number> Series2 = new XYChart.Series<>();
            XYChart.Series<Number, Number> Series3 = new XYChart.Series<>();

            for (Ballasting data : sp) {
                Series1.getData().add(new XYChart.Data<>(data.getSeq(), data.getMin()));
                Series2.getData().add(new XYChart.Data<>(data.getSeq(), data.getMean()));
                Series3.getData().add(new XYChart.Data<>(data.getSeq(), data.getMax()));
            }
            Series1.setName("min");
            Series2.setName("mean");
            Series3.setName("max");

            series.addAll(Series1, Series2, Series3);
            xAxis.setLabel("value");
            yAxis.setLabel("seq");
            BallastingLC.getData().retainAll();
            BallastingLC.getData().addAll(series);
        });
    }

    public static void doBallasting() {
        last_seq_data = new ArrayList<>();
        //list of last 10 seq
        seq = ReadData.getLastSeq();
        //list of streamers
        strList = ReadData.getStrList();
        //list of compasses
        compass = ReadData.getCompassList();

        for(Integer s : seq) {
            List<Ballasting> data = ReadData.getDataList(s);
            last_seq_data.addAll(data);
        }

        for (int st : strList) {
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

                double s1min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(0))
                        .collect(Collectors.toList()).get(0).getMin();
                double s2min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(1))
                        .collect(Collectors.toList()).get(0).getMin();
                double s3min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(2))
                        .collect(Collectors.toList()).get(0).getMin();
                double s4min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(3))
                        .collect(Collectors.toList()).get(0).getMin();
                double s5min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(4))
                        .collect(Collectors.toList()).get(0).getMin();
                double s6min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(5))
                        .collect(Collectors.toList()).get(0).getMin();
                double s7min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(6))
                        .collect(Collectors.toList()).get(0).getMin();
                double s8min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(7))
                        .collect(Collectors.toList()).get(0).getMin();
                double s9min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(8))
                        .collect(Collectors.toList()).get(0).getMin();
                double s10min = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(9))
                        .collect(Collectors.toList()).get(0).getMin();

                tenSeqMin.add(new SeqData(st, u, s1min, s2min, s3min, s4min, s5min, s6min, s7min, s8min, s9min, s10min));

                double s1max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(0))
                        .collect(Collectors.toList()).get(0).getMin();
                double s2max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(1))
                        .collect(Collectors.toList()).get(0).getMin();
                double s3max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(2))
                        .collect(Collectors.toList()).get(0).getMin();
                double s4max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(3))
                        .collect(Collectors.toList()).get(0).getMin();
                double s5max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(4))
                        .collect(Collectors.toList()).get(0).getMin();
                double s6max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(5))
                        .collect(Collectors.toList()).get(0).getMin();
                double s7max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(6))
                        .collect(Collectors.toList()).get(0).getMin();
                double s8max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(7))
                        .collect(Collectors.toList()).get(0).getMin();
                double s9max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(8))
                        .collect(Collectors.toList()).get(0).getMin();
                double s10max = last_seq_data
                        .stream()
                        .filter(c -> c.getStreamer() == st && c.getCompass() == u && c.getSeq() == seq.get(9))
                        .collect(Collectors.toList()).get(0).getMin();

                tenSeqMax.add(new SeqData(st, u, s1max, s2max, s3max, s4max, s5max, s6max, s7max, s8max, s9max, s10max));
            }
        }
    }

    @FXML public void update(ActionEvent actionEvent){
        logger.warn("errorButton pressed");
        List<SeqData> resErrorsOnly = new ArrayList<>();
        if(errorsButton.isSelected()){
            //filter tenSeq
            logger.warn("errors is active");
            List<SeqData> filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq1_mean() < -3 || c.getSeq1_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);
            filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq2_mean() < -3 || c.getSeq2_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);
            filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq3_mean() < -3 || c.getSeq3_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);
            filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq4_mean() < -3 || c.getSeq4_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);
            filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq5_mean() < -3 || c.getSeq5_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);
            filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq6_mean() < -3 || c.getSeq6_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);
            filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq7_mean() < -3 || c.getSeq7_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);
            filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq8_mean() < -3 || c.getSeq8_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);
            filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq9_mean() < -3 || c.getSeq9_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);
            filteredData = tenSeq.stream()
                    .filter(c -> (c.getSeq10_mean() < -3 || c.getSeq10_mean() > 3)).collect(Collectors.toList());
            resErrorsOnly.addAll(filteredData);

            Collections.sort(resErrorsOnly, new Sortbystr());
            //fill table
            fillTable(resErrorsOnly);
        } else {
            logger.warn("errors is NOT active");
            fillTable(tenSeq);
        }
    }

    static class Sortbystr implements Comparator<SeqData> {
        public int compare(SeqData a, SeqData b){
            return a.getStreamer() - b.getStreamer();
        }
    }

    @FXML public void updateData(ActionEvent actionEvent){
        if(avgsButton.isSelected()){
            fillTable(tenSeq);
        } else if(minButton.isSelected()){
            fillTable(tenSeqMin);
        } else if(maxButton.isSelected()){
            fillTable(tenSeqMax);
        }
    }

    public void fillTable(List<SeqData> tenSeq){
        Platform.runLater(() -> {
            root.getChildren().clear();
            tenSeq.stream().forEach((Result)-> {
                if(Result.getStreamer() != str){
                    TreeItem<SeqData> strN = new TreeItem<>(
                            new SeqData(str+1,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0,
                                    0));
                    strN.setExpanded(true);
                    strN.getChildren().add(new TreeItem<>(
                            new SeqData(Result.getStreamer(),
                                    Result.getUnit(),
                                    Result.getSeq1_mean(),
                                    Result.getSeq2_mean(),
                                    Result.getSeq3_mean(),
                                    Result.getSeq4_mean(),
                                    Result.getSeq5_mean(),
                                    Result.getSeq6_mean(),
                                    Result.getSeq7_mean(),
                                    Result.getSeq8_mean(),
                                    Result.getSeq9_mean(),
                                    Result.getSeq10_mean())));
                    str++;
                    root.getChildren().add(strN);
                } else {
                    TreeItem t = root.getChildren().get(str-1);
                    t.getChildren().add(new TreeItem<>(
                            new SeqData(Result.getStreamer(),
                                    Result.getUnit(),
                                    Result.getSeq1_mean(),
                                    Result.getSeq2_mean(),
                                    Result.getSeq3_mean(),
                                    Result.getSeq4_mean(),
                                    Result.getSeq5_mean(),
                                    Result.getSeq6_mean(),
                                    Result.getSeq7_mean(),
                                    Result.getSeq8_mean(),
                                    Result.getSeq9_mean(),
                                    Result.getSeq10_mean())));
                }
            });
            str = 0;

        });
    }
}
