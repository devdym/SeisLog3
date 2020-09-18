package main.insTest;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.util.Callback;
import main.Controller;
import main.entities.InsTestLimits;
import main.entities.InsTestRes;
import main.general.ReadData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import static javafx.collections.FXCollections.observableArrayList;

public class insTest_Controller {

    static final Logger logger = LogManager.getLogger(Controller.class.getName());

    @FXML public DatePicker InsTestDateDatePicker;
    @FXML public TextArea calcInfoArea;
    @FXML public Spinner<Integer> dS2;
    @FXML public Spinner<Integer> dS3;
    @FXML public Spinner<Integer> dS4;
    @FXML public Spinner<Integer> dS5;
    @FXML public Spinner<Integer> dS6;
    @FXML public Spinner<Integer> tS2;
    @FXML public Spinner<Integer> tS3;
    @FXML public Spinner<Integer> tS4;
    @FXML public Spinner<Integer> tS5;
    @FXML public Spinner<Integer> tS6;
    @FXML public TextField lkgTF;
    @FXML public TextField noiseTF;
    @FXML public TextField capTolTF;
    @FXML public TextField cutTolTF;
    @FXML public LineChart<String, Number> SpreadLC;
    @FXML public CategoryAxis xAxisIns;
    @FXML public NumberAxis yAxisIns;
    @FXML public Spinner<Integer> StrSP;
    @FXML public TreeTableView<InsTestRes> InsTable;
    @FXML public TreeTableColumn<InsTestRes, Number> CapColumn;
    @FXML public TreeTableColumn<InsTestRes, Number> CutoffColumn;
    @FXML public TreeTableColumn<InsTestRes, Number> NoiseColumn;
    @FXML public TreeTableColumn<InsTestRes, Number> LeakageColumn;
    @FXML public ToggleButton CapButton;
    @FXML public ToggleButton CutButton;
    @FXML public ToggleButton LeakageButton;
    @FXML public ToggleButton NoiseButton;
    @FXML public ToggleButton errorsButton;
    @FXML public TableView<InsTestLimits> LimitsTable;
    @FXML public TableView<resultTable> resultsTable;
    @FXML public BarChart<String, Number> CapHistogram;
    @FXML public BarChart<String, Number> CutoffHistogram;

    private final TreeItem<InsTestRes> root = new TreeItem<>();
    private List<LocalDate> InsTestDates = new ArrayList<>();
    private int streamer = 0;
    DecimalFormat DecFormatOneOne = new DecimalFormat("#.#");
    List<InsTestLimits> CalculatedLimits = new ArrayList<>();
    List<InsTestLimits> limits = new ArrayList<>();
    List<InsTestRes> testResults = new ArrayList<>();

    public void initialize() {
        //Control pane
        InsTestDates = ReadData.getInsTestDates();

        // Date Picker color code
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
                                setStyle("-fx-background-color: #FFCC00;");
                                setDisable(false);
                            }
                        });
                    }
                };
            }
        };
        InsTestDateDatePicker.setDayCellFactory(InsDateFactory);
        // Date Picker on Action
        InsTestDateDatePicker.setOnAction(event -> update(InsTestDateDatePicker.getValue()));
        //Errors only button
        errorsButton.setSelected(true);
        errorsButton.setOnAction(event -> update(InsTestDateDatePicker.getValue()));

        //Result Table
        InsTable.setRoot(root);
        // table select listener
        InsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> drawChGraph(newValue.getValue()));
        // table color code
        CapColumn.setCellFactory((TreeTableColumn<InsTestRes, Number> param) -> {
            TreeTableCell<InsTestRes, Number> cell = new TreeTableCell<>(){
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<InsTestRes> cap = getTreeTableRow();

                    if (item == null || empty){
                        setText(null);
                        cap.setStyle("");
                        setStyle("");
                    } else {
                        if(cap.getTreeItem().getValue().getType() == 2){
                            if(cap.getTreeItem().getValue().getCap() > limits.get(0).getCap_max() ||
                                cap.getTreeItem().getValue().getCap() < limits.get(0).getCap_min()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(cap.getTreeItem().getValue().getType() == 3){
                            if(cap.getTreeItem().getValue().getCap() > limits.get(1).getCap_max() ||
                                    cap.getTreeItem().getValue().getCap() < limits.get(1).getCap_min()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(cap.getTreeItem().getValue().getType() == 4){
                            if(cap.getTreeItem().getValue().getCap() > limits.get(2).getCap_max() ||
                                    cap.getTreeItem().getValue().getCap() < limits.get(2).getCap_min()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(cap.getTreeItem().getValue().getType() == 5){
                            if(cap.getTreeItem().getValue().getCap() > limits.get(3).getCap_max() ||
                                    cap.getTreeItem().getValue().getCap() < limits.get(3).getCap_min()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                    }
                }
            };
            return cell;
        });
        CutoffColumn.setCellFactory((TreeTableColumn<InsTestRes, Number> param) -> {
            TreeTableCell<InsTestRes, Number> cell = new TreeTableCell<>(){
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<InsTestRes> value = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        value.setStyle("");
                        setStyle("");
                    } else {
                        if(value.getTreeItem().getValue().getType() == 2){
                            if(value.getTreeItem().getValue().getCutoff() > limits.get(0).getCutoff_max() ||
                                    value.getTreeItem().getValue().getCutoff() < limits.get(0).getCutoff_min()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(value.getTreeItem().getValue().getType() == 3){
                            if(value.getTreeItem().getValue().getCutoff() > limits.get(1).getCutoff_max() ||
                                    value.getTreeItem().getValue().getCutoff() < limits.get(1).getCutoff_min()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(value.getTreeItem().getValue().getType() == 4){
                            if(value.getTreeItem().getValue().getCutoff() > limits.get(2).getCutoff_max() ||
                                    value.getTreeItem().getValue().getCap() < limits.get(2).getCutoff_min()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(value.getTreeItem().getValue().getType() == 5){
                            if(value.getTreeItem().getValue().getCutoff() > limits.get(3).getCutoff_max() ||
                                    value.getTreeItem().getValue().getCutoff() < limits.get(3).getCutoff_min()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                    }
                }
            };
            return cell;
        });
        NoiseColumn.setCellFactory((TreeTableColumn<InsTestRes, Number> param) -> {
            TreeTableCell<InsTestRes, Number> cell = new TreeTableCell<>(){
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<InsTestRes> value = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        value.setStyle("");
                        setStyle("");
                    } else {
                        if(value.getTreeItem().getValue().getType() == 2){
                            if(value.getTreeItem().getValue().getNoise() > limits.get(0).getNoise()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(value.getTreeItem().getValue().getType() == 3){
                            if(value.getTreeItem().getValue().getNoise() > limits.get(1).getNoise()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(value.getTreeItem().getValue().getType() == 4){
                            if(value.getTreeItem().getValue().getNoise() > limits.get(2).getNoise()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(value.getTreeItem().getValue().getType() == 5){
                            if(value.getTreeItem().getValue().getNoise() > limits.get(3).getNoise()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                    }
                }
            };
            return cell;
        });
        LeakageColumn.setCellFactory((TreeTableColumn<InsTestRes, Number> param) -> {
            TreeTableCell<InsTestRes, Number> cell = new TreeTableCell<>(){
                @Override
                protected void updateItem(Number item, boolean empty) {
                    super.updateItem(item, empty);
                    TreeTableRow<InsTestRes> value = getTreeTableRow();
                    if (item == null || empty){
                        setText(null);
                        value.setStyle("");
                        setStyle("");
                    } else {
                        if(value.getTreeItem().getValue().getType() == 2){
                            if(value.getTreeItem().getValue().getLeakage() < limits.get(0).getLeakage()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(value.getTreeItem().getValue().getType() == 3){
                            if(value.getTreeItem().getValue().getLeakage() < limits.get(1).getLeakage()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(value.getTreeItem().getValue().getType() == 4){
                            if(value.getTreeItem().getValue().getLeakage() < limits.get(2).getLeakage()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                        if(value.getTreeItem().getValue().getType() == 5){
                            if(value.getTreeItem().getValue().getLeakage() < limits.get(3).getLeakage()){
                                setText(item.toString());
                                setStyle("-fx-text-fill: #FF6633");
                            } else {
                                setText(item.toString());
                                setStyle("-fx-text-fill: #CCCCCC");
                            }
                        }
                    }
                }
            };
            return cell;
        });

        // Value factory.
        SpinnerValueFactory<Integer> valueAFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 6, 1, 1);
        StrSP.setValueFactory(valueAFactory);
        StrSP.valueProperty().addListener(event -> updateGraph(InsTestDateDatePicker.getValue()));

        tS2.valueProperty().addListener((observable) -> limitsFunc());
        tS3.valueProperty().addListener((observable) -> limitsFunc());
        tS4.valueProperty().addListener((observable) -> limitsFunc());
        tS5.valueProperty().addListener((observable) -> limitsFunc());
        dS2.valueProperty().addListener((observable) -> limitsFunc());
        dS3.valueProperty().addListener((observable) -> limitsFunc());
        dS4.valueProperty().addListener((observable) -> limitsFunc());
        dS5.valueProperty().addListener((observable) -> limitsFunc());
    }

    private void updateGraph(LocalDate date){
        if(CapButton.isSelected()){
            logger.warn("cap is selected");
            ShowCap();
        } else if(CutButton.isSelected()){
            logger.warn("cut is selected");
            ShowCut();
        } else if(LeakageButton.isSelected()){
            ShowLeakage();
        } else if(NoiseButton.isSelected()){
            ShowNoise();
        } else {
            logger.warn("No value selected");
        }
    }

    private void update(LocalDate date){
        //clear table
        root.getChildren().clear();

        //populate tree table
        Platform.runLater(() -> {
            //Get Limits for selected date
            limits = ReadData.getInsTestLimits(date);
            //fill limits table
            final ObservableList<InsTestLimits> data = FXCollections.observableArrayList(limits);
            LimitsTable.setItems(data);

            //Get Test Result
            testResults = ReadData.getInsTestRes(date);

            if(errorsButton.isSelected()){
                List<InsTestRes> resErrorsOnly = new ArrayList<>();
                logger.warn("get errors only InsTest result");

                // filter Cap
                for(InsTestLimits l : limits){
                    List<InsTestRes> capError;
                    capError = testResults.stream()
                            .filter(c -> (c.getCap() < l.getCap_min()
                                    || l.getCap_max() < c.getCap()
                                    && c.getType() == l.getSensor_nb()))
                            .collect(Collectors.toList());
                    resErrorsOnly.addAll(capError);
                }
                // filter Cutoff
                for(InsTestLimits l : limits){
                    List<InsTestRes> cutoffError;
                    cutoffError = testResults.stream()
                            .filter(c -> (c.getCutoff() < l.getCutoff_min()
                                    || l.getCutoff_max() < c.getCutoff()
                                    && c.getType() == l.getSensor_nb()))
                            .collect(Collectors.toList());
                    resErrorsOnly.addAll(cutoffError);
                }
                // filter Leakage
                for(InsTestLimits l : limits){
                    List<InsTestRes> leakageError;
                    leakageError = testResults.stream()
                            .filter(c -> (c.getLeakage() < l.getLeakage()
                                    && c.getType() == l.getSensor_nb()))
                            .collect(Collectors.toList());
                    resErrorsOnly.addAll(leakageError);
                }
                // filter Leakage
                for(InsTestLimits l : limits){
                    List<InsTestRes> noiseError;
                    noiseError = testResults.stream()
                            .filter(c -> (c.getNoise() > l.getNoise()
                                    && c.getType() == l.getSensor_nb()))
                            .collect(Collectors.toList());
                    resErrorsOnly.addAll(noiseError);
                }

                List<InsTestRes> listWithoutDuplicates = resErrorsOnly.stream()
                        .distinct()
                        .collect(Collectors.toList());

                Collections.sort(listWithoutDuplicates, new Sortbystr());
                fillTable(listWithoutDuplicates);

                getStats(listWithoutDuplicates);
            } else {
                logger.warn("get all InsTest result");
                fillTable(testResults);
            }

            //build histogram
            List<Double> capHist = new ArrayList<>();
            List<Double> cutoffHist = new ArrayList<>();

            for(InsTestRes r : testResults){
                capHist.add(r.getCap());
                cutoffHist.add(r.getCutoff());
            }
            //get min
            double capMin = capHist.stream().min(Comparator.comparing(Double::valueOf)).get();
            double cutoffMin = cutoffHist.stream().min(Comparator.comparing(Double::valueOf)).get();

            //get max
            double capMax = capHist.stream().max(Comparator.comparing(Double::valueOf)).get();
            double cutoffMax = cutoffHist.stream().max(Comparator.comparing(Double::valueOf)).get();

            XYChart.Series<String, Number> capSeries1 = new XYChart.Series<>();
            XYChart.Series<String, Number> cutoffSeries1 = new XYChart.Series<>();

            //bin nb
            int capbin = 10;
            double cutoffbin = 0.2;

            double capIter = capMin;
            while(capIter < capMax){
                capIter = capIter + capbin;
                double i = capIter;
                long st = testResults.stream().filter(c -> c.getCap() < i
                        && c.getCap() < i + capbin )
                        .count();
                String com = i + "-" + (i + capbin);
                capSeries1.getData().add(new XYChart.Data<>(com, (int) st));
            }
            CapHistogram.getData().clear();
            CapHistogram.getData().add(capSeries1);

            double cutoffIter = cutoffMin;
            while(cutoffIter < cutoffMax){
                cutoffIter = cutoffIter + cutoffbin;
                double i = cutoffIter;
                long st = testResults.stream().filter(c -> c.getCutoff() < i
                        && c.getCutoff() < i + cutoffbin )
                        .count();
                String com = String.format("%.1f", i) + "-" + String.format("%.1f", i+cutoffbin);
                cutoffSeries1.getData().add(new XYChart.Data<>(com, (int) st));
            }
            CutoffHistogram.getData().clear();
            CutoffHistogram.getData().add(cutoffSeries1);
        });
    }

    public void fillTable(List<InsTestRes> res){
        streamer = 0;
        res.stream().forEach((Result)-> {
            if(Result.getStreamer() != streamer){
                TreeItem<InsTestRes> strN =
                        new TreeItem<>(
                                new InsTestRes(
                                        streamer +1,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0));
                strN.setExpanded(true);
                strN.getChildren().add(
                        new TreeItem<>(
                                new InsTestRes(
                                        Result.getStreamer(),
                                        Result.getTrace(),
                                        Result.getType(),
                                        Result.getAss_sn(),
                                        Result.getCap(),
                                        Result.getCutoff(),
                                        Result.getNoise(),
                                        Result.getLeakage())));
                streamer++;
                root.getChildren().add(strN);
            } else {
                TreeItem<InsTestRes> t = root.getChildren().get(streamer -1);
                t.getChildren().add(
                        new TreeItem<>(
                                new InsTestRes(
                                        Result.getStreamer(),
                                        Result.getTrace(),
                                        Result.getType(),
                                        Result.getAss_sn(),
                                        Result.getCap(),
                                        Result.getCutoff(),
                                        Result.getNoise(),
                                        Result.getLeakage())));
            }
        });
        streamer = 0;

    }

    public void getStats(List<InsTestRes> resErrorsOnly){
        final ObservableList<resultTable> resData =
                FXCollections.observableArrayList();

        long totalS1 = resErrorsOnly.stream()
                .filter(c -> (1 == c.getStreamer()))
                .count();
        resData.add(new resultTable(1, 0, 0, 0, 0, (int)totalS1));
        long totalS2 = resErrorsOnly.stream()
                .filter(c -> (2 == c.getStreamer()))
                .count();
        resData.add(new resultTable(2, 0, 0, 0, 0, (int)totalS2));
        long totalS3 = resErrorsOnly.stream()
                .filter(c -> (3 == c.getStreamer()))
                .count();
        resData.add(new resultTable(3, 0, 0, 0, 0, (int)totalS3));
        long totalS4 = resErrorsOnly.stream()
                .filter(c -> (4 == c.getStreamer()))
                .count();
        resData.add(new resultTable(4, 0, 0, 0, 0, (int)totalS4));
        long totalS5 = resErrorsOnly.stream()
                .filter(c -> (5 == c.getStreamer()))
                .count();
        resData.add(new resultTable(5, 0, 0, 0, 0, (int)totalS5));
        long totalS6 = resErrorsOnly.stream()
                .filter(c -> (6 == c.getStreamer()))
                .count();
        resData.add(new resultTable(6, 0, 0, 0, 0, (int)totalS6));

        resultsTable.setItems(resData);
    }

    //        Collections.sort(result, new Sortbystr());
    static class Sortbystr implements Comparator<InsTestRes> {
        public int compare(InsTestRes a, InsTestRes b){
            return a.getStreamer() - b.getStreamer();
        }
    }

    private void limitsFunc() {
        CalculatedLimits.clear();
        int s = 2;
        double cap, cutoff, minCap, maxCap, minCutoff, maxCutoff, capTol, CutoffTol;
        capTol = Double.parseDouble(capTolTF.getText());
        CutoffTol = Double.parseDouble(cutTolTF.getText());
        List<Integer> te = new ArrayList<>();
        List<Integer> pr = new ArrayList<>();

        te.add(tS2.getValue());
        te.add(tS3.getValue());
        te.add(tS4.getValue());
        te.add(tS5.getValue());

        pr.add(dS2.getValue());
        pr.add(dS3.getValue());
        pr.add(dS4.getValue());
        pr.add(dS5.getValue());

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

//        //--------
//        s = 2;
//        for (Integer i : te) {
//            double temp = i;
//            double pressure = pr.get(te.indexOf(i));
//            capTol = Double.parseDouble(capTolTF.getText());
//            CutoffTol = Double.parseDouble(cutTolTF.getText());
//            pressure = pressure / 10;
//            cap = 260.0 + 1.742 * (temp - 20) - 9.230 * pressure;
//            cutoff = 1 / (0.4741 + 0.00277 * (temp - 20) - 0.0147 * pressure);
//
//            minCap = cap - (cap * capTol / 100);
//            maxCap = cap + (cap * capTol / 100);
//
//            minCutoff = cutoff - (cutoff * CutoffTol / 100);
//            maxCutoff = cutoff + (cutoff * CutoffTol / 100);
//            s++;
//        }
    }

    @FXML public void ShowCap() {
        Platform.runLater(() -> {
            int SelStr = StrSP.getValue();
            LocalDate date = InsTestDateDatePicker.getValue();
            List<InsTestRes> sp = ReadData.getCapGraphData(SelStr, date);

            XYChart.Series<String, Number> aSeries = new XYChart.Series<>();
            aSeries.setName("Cap");

            for (InsTestRes insTestRes : sp) {
                aSeries.getData().add(new XYChart.Data<>(Integer.toString(insTestRes.getTrace()), insTestRes.getCap()));
            }

            SpreadLC.getData().clear();
            SpreadLC.getData().add(aSeries);
        });
    }

    @FXML public void ShowCut() {
        Platform.runLater(() -> {
            int SelStr = StrSP.getValue();
            LocalDate date = InsTestDateDatePicker.getValue();
            List<InsTestRes> sp = ReadData.getCutGraphData(SelStr, date);

            XYChart.Series<String, Number> aSeries = new XYChart.Series<>();
            aSeries.setName("cut off");

            for (InsTestRes insTestRes : sp) {
                aSeries.getData().add(new XYChart.Data<>(Integer.toString(insTestRes.getTrace()), insTestRes.getCutoff()));
            }

            SpreadLC.getData().clear();
            SpreadLC.getData().add(aSeries);
        });
    }

    @FXML public void ShowLeakage() {
        Platform.runLater(() -> {
            int SelStr = StrSP.getValue();
            LocalDate date = InsTestDateDatePicker.getValue();
            List<InsTestRes> sp = ReadData.getLeakageGraphData(SelStr, date);

            XYChart.Series<String, Number> aSeries = new XYChart.Series<>();
            aSeries.setName("leakage");

            for (InsTestRes insTestRes : sp) {
                aSeries.getData().add(new XYChart.Data<>(Integer.toString(insTestRes.getTrace()), insTestRes.getLeakage()));
            }

            SpreadLC.getData().clear();
            SpreadLC.getData().add(aSeries);
        });
    }

    @FXML public void ShowNoise() {
        Platform.runLater(() -> {
            int SelStr = StrSP.getValue();
            LocalDate date = InsTestDateDatePicker.getValue();
            List<InsTestRes> sp = ReadData.getNoiseGraphData(SelStr, date);

            XYChart.Series<String, Number> aSeries = new XYChart.Series<>();
            aSeries.setName("noise");

            for (InsTestRes insTestRes : sp) {
                aSeries.getData().add(new XYChart.Data<>(Integer.toString(insTestRes.getTrace()), insTestRes.getNoise()));
            }

            SpreadLC.getData().clear();
            SpreadLC.getData().add(aSeries);
        });
    }

    public void drawChGraph(InsTestRes section) {
        Platform.runLater(() -> {
            int selected = section.getAss_sn();

            List<InsTestRes> sp = ReadData.getChGraphData(selected);

            List<Integer> sectionTrace = new ArrayList<>();
            for(InsTestRes r : sp){
                sectionTrace.add(r.getTrace());
            }
            List<Integer> dTrace = sectionTrace.stream().distinct().collect(Collectors.toList());

            ObservableList<XYChart.Series<String, Number>> series = observableArrayList();
            series.clear();
            XYChart.Series<String, Number> Series1 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series2 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series3 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series4 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series5 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series6 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series7 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series8 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series9 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series10 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series11 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series12 = new XYChart.Series<>();
            Series1.setName("trace 1");
            Series2.setName("trace 2");
            Series3.setName("trace 3");
            Series4.setName("trace 4");
            Series5.setName("trace 5");
            Series6.setName("trace 6");
            Series7.setName("trace 7");
            Series8.setName("trace 8");
            Series9.setName("trace 9");
            Series10.setName("trace 10");
            Series11.setName("trace 11");
            Series12.setName("trace 12");

            for (InsTestRes instestres : sp) {
                if(instestres.getTrace() == dTrace.get(0)){
                    Series1.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(1)){
                    Series2.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(2)){
                    Series3.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(3)){
                    Series4.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(4)){
                    Series5.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(5)){
                    Series6.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(6)){
                    Series7.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(7)){
                    Series8.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(8)){
                    Series9.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(9)){
                    Series10.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(10)){
                    Series11.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
                if(instestres.getTrace() == dTrace.get(11)){
                    Series12.getData().add(new XYChart.Data<>(instestres.getUpdated().toString(), instestres.getCap()));
                }
            }

            series.addAll(Series1, Series2, Series3, Series4, Series5, Series6,
                    Series7, Series8, Series9, Series10, Series11, Series12);
            yAxisIns.setLabel("Cap");
            xAxisIns.setLabel("Date");
            SpreadLC.getData().clear();
            SpreadLC.getData().addAll(series);
        });
    }


}
