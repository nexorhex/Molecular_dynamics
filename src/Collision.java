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


public class Collision extends Application {


    AnimationTimer atimer;
    Simulation sim;

    Pane drawingPane;
    Button btnStart, btnStop;
    Circle circle;
    double dt = 0.005;
    double t;

    boolean runnig = false;
    int nAtoms = 2;

    XYChart.Series eP = new XYChart.Series();
    XYChart.Series eC = new XYChart.Series();
    XYChart.Series eKin = new XYChart.Series();
    XYChart.Series vx1 = new XYChart.Series();
    XYChart.Series x1 = new XYChart.Series();
    XYChart.Series y1 = new XYChart.Series();
    XYChart.Series vx2 = new XYChart.Series();
    XYChart.Series x2 = new XYChart.Series();
    XYChart.Series y2 = new XYChart.Series();
    XYChart.Series vy1 = new XYChart.Series();
    XYChart.Series vy2 = new XYChart.Series();


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


        btnStart = new Button("Start");
        btnStart.prefWidthProperty().bind(hBox.widthProperty());

        btnStart.setOnAction(e -> {


            if (!runnig) {

                double[] xStart = new double[2];
                xStart[0] = 195;
                xStart[1] = 205;
                double[] yStart = new double[2];
                yStart[0] = 195;
                yStart[1] = 205;

                double[] vxStart = new double[2];
                vxStart[0] = 2;
                vxStart[1] = -2;
                double[] vyStart = new double[2];
                vyStart[0] = 2;
                vyStart[1] = -2;

                sim = new Simulation(xStart, yStart, vxStart, vyStart, 400);
                t = 0;

                double[] xArray = sim.getX();
                double[] yArray = sim.getY();

                for (int i = 0; i < nAtoms; i++) {
                    circle = new Circle(2, Color.BLUE);
                    circle.relocate(xArray[i], yArray[i]);
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
                            //drawingPane.getChildren().addAll(circ);
                            i++;
                        }
                        i = 0;
                        t += dt;
                        ChartData();
                        lastUpdate = now;
                    } else {
                        sim.verletStep(dt);
                        //ChartData();
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

            Chart2 chart = new Chart2(eP, eKin, eC, x1, x2, y1, y2, vx1, vx2, vy1, vy2);
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
        x1.getData().add(new XYChart.Data(t, sim.getX()[0]));
        x2.getData().add(new XYChart.Data(t, sim.getX()[1]));
        y1.getData().add(new XYChart.Data(t, sim.getY()[0]));
        y2.getData().add(new XYChart.Data(t, sim.getY()[1]));
        vx1.getData().add(new XYChart.Data(t, sim.getVx()[0]));
        vx2.getData().add(new XYChart.Data(t, sim.getVx()[1]));
        vy1.getData().add(new XYChart.Data(t, sim.getVy()[0]));
        vy2.getData().add(new XYChart.Data(t, sim.getVy()[1]));
    }


    public static void main(String[] args) {
        launch(args);
    }
}