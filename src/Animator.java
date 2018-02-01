import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class Animator extends Application {


    AnimationTimer atimer;
    Simulation sim;

    Pane drawingPane;
    Button btnStart, btnStop;
    Circle circle;
    double dt = 0.001;
    double t;

    boolean runnig = false;
    int nAtoms = 200;

    XYChart.Series eP = new XYChart.Series();
    XYChart.Series eC = new XYChart.Series();
    XYChart.Series eKin = new XYChart.Series();
    XYChart.Series elasticEnergy = new XYChart.Series();


    @Override
    public void start(Stage primaryStage) {


        ArrayList<Circle> atoms = new ArrayList<Circle>();


        HBox hBox = new HBox();
        VBox vBox = new VBox();

        vBox.setSpacing(15);
        vBox.setPadding(new Insets(25, 25, 30, 30));


        drawingPane = new Pane();
        drawingPane.setPrefSize(400, 400);

        drawingPane.prefWidthProperty().bind(vBox.widthProperty());
        drawingPane.prefHeightProperty().bind(vBox.heightProperty());

        //ToScreenCoords convert = new ToScreenCoords(drawingPane.getWidth(),drawingPane.getHeight(),100);

        btnStart = new Button("Start");
        btnStart.prefWidthProperty().bind(hBox.widthProperty());

        btnStart.setOnAction(e -> {

            eC.getData().clear();
            eP.getData().clear();
            eKin.getData().clear();
            elasticEnergy.getData().clear();


            if (!runnig) {

                sim = new Simulation(nAtoms, 400);
                t = 0;

                double[] xArray = sim.getX();
                double[] yArray = sim.getY();

                for (int i = 0; i < nAtoms; i++) {
                    circle = new Circle(3, Color.BLUE);
                    circle.relocate(xArray[i], yArray[i]);
                    //circle.relocate(convert.transformX(xArray[i]),convert.transformY(yArray[i]));
                    atoms.add(circle);
                }

                for (Circle circ : atoms)
                    drawingPane.getChildren().addAll(circ);


                btnStart.setDisable(true);
                btnStop.setDisable(false);
                runnig = true;

                ChartData();

            }

            atimer = new AnimationTimer() {
                private long lastUpdate;

                @Override
                public void handle(long now) {

                    if (now - lastUpdate > 50_000_000) {
                        sim.verletStep(dt);
                        double[] xArray = sim.getX();
                        double[] yArray = sim.getY();

                        int i = 0;
                        for (Circle circ : atoms) {

                            circ.relocate(xArray[i], yArray[i]);
                            //circ.relocate(convert.transformX(xArray[i]),convert.transformY(yArray[i]));
                            //drawingPane.getChildren().addAll(circ);
                            i++;
                        }
                        i = 0;
                        t += dt;
                        ChartData();
                        lastUpdate = now;
                    } else {
                        sim.verletStep(dt);
                        ChartData();
                    }

                }


            };

            atimer.start();


        });


        btnStop = new Button("Stop");
        btnStop.prefWidthProperty().bind(hBox.widthProperty());
        btnStop.setOnAction(e -> {

            atoms.clear();

            atimer.stop();
            runnig = false;

            drawingPane.getChildren().clear();
            circle = null;


            btnStart.setDisable(false);
            btnStop.setDisable(true);

            Chart chart = new Chart(eP, eKin, eC, elasticEnergy);
            chart.display();

        });


        hBox.getChildren().add(btnStart);
        hBox.getChildren().add(btnStop);
        //hBox.getChildren().add(fig);

        vBox.getChildren().addAll(hBox, drawingPane);


        Scene scene = new Scene(vBox, 500, 500);

        primaryStage.setTitle("Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public void ChartData() {

        eP.getData().add(new XYChart.Data(t, sim.getePot()));
        eC.getData().add(new XYChart.Data(t, sim.geteC()));
        eKin.getData().add(new XYChart.Data(t, sim.geteKin()));
        elasticEnergy.getData().add(new XYChart.Data(t, sim.getelasticEnergy()));
    }


    public static void main(String[] args) {
        launch(args);
    }
}