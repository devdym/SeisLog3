package main.tension;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import main.Controller;
import main.entities.InsTestRes;
import main.entities.Tension;
import main.general.ReadData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static javafx.collections.FXCollections.observableArrayList;

public class tension_Controller {

    @FXML public ToggleButton weekTButton;
    @FXML public ToggleButton monthTButton;
    @FXML public ToggleButton projectTButton;
    @FXML public TableView<TensionTable> TensionTableRes;
    @FXML public LineChart<String, Number> LineChart;
    @FXML public CategoryAxis xAxis;
    @FXML public NumberAxis yAxis;

    static final Logger logger = LogManager.getLogger(Controller.class.getName());

    static LocalDate current_date = LocalDate.now();
    static LocalDate until_date = current_date;


    public void initialize() {
        current_date = LocalDate.now();
        until_date = current_date.minusMonths(6);
        List<Tension> tension = ReadData.getTension(current_date, until_date);
        List<TensionTable> tension_table = pivotData(tension);
        filltable(tension_table);
        drawChart(tension_table);
    }

    public static void getTensionData() {

    }

    private static List<TensionTable> pivotData(List<Tension> tension) {
        List<TensionTable> result = new ArrayList<>();

        List<Tension> strNB = tension.stream().filter(distinctByStr(p -> p.getStreamer())).collect(Collectors.toList());
        List<Tension> dates = tension.stream().filter(distinctByDate(p -> p.getDate_())).collect(Collectors.toList());

        for(Tension d : dates){
            int s1 = 0;
            int s2 = 0;
            int s3 = 0;
            int s4 = 0;
            int s5 = 0;
            int s6 = 0;

            for (Tension str : strNB) {
//                logger.warn(str.getStreamer());

                List<Tension> r1 = tension
                        .stream()
                        .filter(c -> c.getStreamer() == 1 && c.getDate_() == d.getDate_())
                        .collect(Collectors.toList());

                s1 = r1.isEmpty() ? s1 : r1.get(0).getTension();

                List<Tension> r2 = tension
                        .stream()
                        .filter(c -> c.getStreamer() == 2 && c.getDate_() == d.getDate_())
                        .collect(Collectors.toList());

                s2 = r2.isEmpty() ? s2 : r2.get(0).getTension();

                List<Tension> r3 = tension
                        .stream()
                        .filter(c -> c.getStreamer() == 3 && c.getDate_() == d.getDate_())
                        .collect(Collectors.toList());

                s3 = r3.isEmpty() ? s3 : r3.get(0).getTension();

                List<Tension> r4 = tension
                        .stream()
                        .filter(c -> c.getStreamer() == 4 && c.getDate_() == d.getDate_())
                        .collect(Collectors.toList());

                s4 = r4.isEmpty() ? s4 : r4.get(0).getTension();

                List<Tension> r5 = tension
                        .stream()
                        .filter(c -> c.getStreamer() == 5 && c.getDate_() == d.getDate_())
                        .collect(Collectors.toList());

                s5 = r5.isEmpty() ? s5 : r5.get(0).getTension();

                List<Tension> r6 = tension
                        .stream()
                        .filter(c -> c.getStreamer() == 6 && c.getDate_() == d.getDate_())
                        .collect(Collectors.toList());

                s6 = r6.isEmpty() ? s6 : r6.get(0).getTension();

            }
            result.add(new TensionTable(d.getDate_(), s1, s2, s3, s4, s5, s6));
        }

        return result;
    }

    public static <T> Predicate<T> distinctByStr(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public static <T> Predicate<T> distinctByDate(Function<? super T, Object> keyExtractor)
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public void filltable(List<TensionTable> tension_table){
        Platform.runLater(() -> {
            final ObservableList<TensionTable> data = FXCollections.observableArrayList(tension_table);
            TensionTableRes.setItems(data);
        });
    }

    public void drawChart(List<TensionTable> data){
        Platform.runLater(() -> {
            ObservableList<XYChart.Series<String, Number>> series = observableArrayList();
            series.clear();

            XYChart.Series<String, Number> Series1 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series2 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series3 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series4 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series5 = new XYChart.Series<>();
            XYChart.Series<String, Number> Series6 = new XYChart.Series<>();

            Series1.setName("Trace 1");
            Series2.setName("Trace 2");
            Series3.setName("Trace 3");
            Series4.setName("Trace 4");
            Series5.setName("Trace 5");
            Series6.setName("Trace 6");


            for (TensionTable d : data) {

                Series1.getData().add(new XYChart.Data<>(d.getDate_().toString(), d.getStr1()));

                Series2.getData().add(new XYChart.Data<>(d.getDate_().toString(), d.getStr2()));

                Series3.getData().add(new XYChart.Data<>(d.getDate_().toString(), d.getStr3()));

                Series4.getData().add(new XYChart.Data<>(d.getDate_().toString(), d.getStr4()));

                Series5.getData().add(new XYChart.Data<>(d.getDate_().toString(), d.getStr5()));

                Series6.getData().add(new XYChart.Data<>(d.getDate_().toString(), d.getStr6()));

            }

            series.addAll(Series1, Series2, Series3, Series4, Series5, Series6);
            yAxis.setLabel("Cap");
            xAxis.setLabel("Date");
            LineChart.getData().clear();
            LineChart.getData().addAll(series);
        });
    }
}
