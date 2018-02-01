import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Chart {

    private Stage chartWindow;
    LineChart<Number, Number> fig;
    XYChart.Series eP = new XYChart.Series();
    XYChart.Series eC = new XYChart.Series();
    XYChart.Series eKin = new XYChart.Series();
    XYChart.Series elasticEnergy = new XYChart.Series();

    public Chart(XYChart.Series eP, XYChart.Series eKin, XYChart.Series eC, XYChart.Series elasticEnergy) {
        this.eP = eP;
        this.eKin = eKin;
        this.eC = eC;
        this.elasticEnergy = elasticEnergy;
    }


    public void display() {

        chartWindow = new Stage();
        chartWindow.initModality(Modality.NONE);

        chartWindow.setTitle("Chart");

        VBox layout = new VBox();
        layout.setSpacing(15);
        layout.setPadding(new Insets(25, 25, 30, 30));

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        fig = new LineChart<Number, Number>(x, y);
        layout.getChildren().add(fig);

        Scene scene = new Scene(layout, 1400, 500);
        chartWindow.setScene(scene);
        chartWindow.show();


        fig.setLegendVisible(true);
        fig.getData().clear();
        fig.getData().add(eP);
        eP.setName("eP");
        fig.getData().add(eKin);
        eKin.setName("eKin");
        fig.getData().add(eC);
        eC.setName("eC");
        fig.getData().add(elasticEnergy);
        elasticEnergy.setName("eElastic");

    }
}