import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Chart2 {

    private Stage chartWindow;
    LineChart<Number, Number> fige, figx, figy, figvx, figvy;
    XYChart.Series eP = new XYChart.Series();
    XYChart.Series eC = new XYChart.Series();
    XYChart.Series eKin = new XYChart.Series();
    XYChart.Series x1 = new XYChart.Series();
    XYChart.Series y1 = new XYChart.Series();
    XYChart.Series vx1 = new XYChart.Series();
    XYChart.Series vy1 = new XYChart.Series();
    XYChart.Series x2 = new XYChart.Series();
    XYChart.Series y2 = new XYChart.Series();
    XYChart.Series vx2 = new XYChart.Series();
    XYChart.Series vy2 = new XYChart.Series();

    public Chart2(XYChart.Series eP, XYChart.Series eKin, XYChart.Series eC, XYChart.Series x1, XYChart.Series x2, XYChart.Series y1, XYChart.Series y2, XYChart.Series vx1, XYChart.Series vx2, XYChart.Series vy1, XYChart.Series vy2) {
        this.eP = eP;
        this.eKin = eKin;
        this.eC = eC;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.vx1 = vx1;
        this.vx2 = vx2;
        this.vy1 = vy1;
        this.vy2 = vy2;
    }


    public void display() {

        chartWindow = new Stage();
        chartWindow.initModality(Modality.NONE);

        chartWindow.setTitle("Chart");

        VBox layout = new VBox();
        layout.setSpacing(15);
        layout.setPadding(new Insets(25, 25, 30, 30));
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();

        //NumberAxis X = new NumberAxis();
        //NumberAxis Y = new NumberAxis();
        fige = new LineChart<Number, Number>(new NumberAxis(), new NumberAxis());
        figx = new LineChart<Number, Number>(new NumberAxis(), new NumberAxis());
        figy = new LineChart<Number, Number>(new NumberAxis(), new NumberAxis());
        figvx = new LineChart<Number, Number>(new NumberAxis(), new NumberAxis());
        figvy = new LineChart<Number, Number>(new NumberAxis(), new NumberAxis());

        hbox1.getChildren().add(figvx);
        hbox1.getChildren().add(figvy);
        hbox2.getChildren().add(figx);
        hbox2.getChildren().add(figy);

        layout.getChildren().add(hbox1);
        layout.getChildren().add(hbox2);
        layout.getChildren().add(fige);

        Scene scene = new Scene(layout, 1400, 800);
        chartWindow.setScene(scene);
        chartWindow.show();


        fige.setLegendVisible(true);
        fige.getData().clear();
        fige.getData().add(eP);
        eP.setName("eP");
        fige.getData().add(eKin);
        eKin.setName("eKin");
        fige.getData().add(eC);
        eC.setName("eC");

        figvx.setLegendVisible(true);
        figvx.getData().clear();
        figvx.getData().add(vx1);
        vx1.setName("vx1");
        figvx.getData().add(vx2);
        vx2.setName("vx2");

        figvy.setLegendVisible(true);
        figvy.getData().clear();
        figvy.getData().add(vy1);
        vy1.setName("vy1");
        figvy.getData().add(vy2);
        vy2.setName("vy2");

        figx.setLegendVisible(true);
        figx.getData().clear();
        figx.getData().add(x1);
        x1.setName("x1");
        figx.getData().add(x2);
        x2.setName("x2");

        figy.setLegendVisible(true);
        figy.getData().clear();
        figy.getData().add(y1);
        y1.setName("y1");
        figy.getData().add(y2);
        y2.setName("y2");

    }
}