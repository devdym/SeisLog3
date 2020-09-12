package main.ballasting;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class ballasting_Controller {

    @FXML public ToggleButton errorsButton;
    @FXML public ToggleButton avgsButton;
    @FXML public ToggleButton sdButton;
    @FXML public ToggleButton diffButton;
    @FXML public TreeTableView BallTable;
    @FXML public TreeTableColumn<SeqData, Number> ballColumn;
    private static TreeItem<SeqData> root = new TreeItem<>();
    private static int str = 0;

    public void initialize() {
        BallTable.setRoot(root);
        errorsButton.setSelected(true);
        ToggleGroup toggleGroup = new ToggleGroup();

        avgsButton.setToggleGroup(toggleGroup);
        sdButton.setToggleGroup(toggleGroup);
        diffButton.setToggleGroup(toggleGroup);
        toggleGroup.selectToggle(avgsButton);

        toggleGroup.selectedToggleProperty().addListener(event -> {
            System.out.println((ToggleButton)  toggleGroup.getSelectedToggle());
        });

        ballColumn.setCellFactory((TreeTableColumn<SeqData, Number> param) -> {
            TreeTableCell cell = new TreeTableCell<SeqData, Number>(){
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
                                : "-fx-background-color:red");
                    }
                }
            };
            return cell;
        });
    }

    public static void fillTable(List<SeqData> tenSeq){
        Runnable getRes = () -> {
        //clear table
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
        };
        new Thread(getRes).start();
    }
}
