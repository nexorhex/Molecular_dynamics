import java.util.Random;

public class Simulation {

    private int nAtoms;
    private int boxWidth;
    private int stepCounter;
    private final double rCut2 = 64;
    private final double wallStiffness = 50;

    private double[] x, y, vx, vy, ax, ay;
    public double ePot, eKin, eC = 0;
    private double dt;
    private int maxSteps;
    private double elasticEnergy;

    public Simulation(double[] xStart, double[] yStart, double[] vxStart, double[] vyStart, int boxWidth) {

        nAtoms = xStart.length;
        this.boxWidth = boxWidth;

        //x = xStart.clone();

        x = new double[nAtoms];
        vx = new double[nAtoms];
        ax = new double[nAtoms];
        y = new double[nAtoms];
        vy = new double[nAtoms];
        ay = new double[nAtoms];
        dt = 0.01;

        for (int i = 0; i < nAtoms; i++) {
            x[i] = xStart[i];
            y[i] = yStart[i];
            vx[i] = vxStart[i];
            vy[i] = vyStart[i];
        }

        calculateAcceleration();
        calculateKineticEnergy();

    }

    public Simulation(int nAtoms, int boxWidth) {

        this.nAtoms = nAtoms;
        this.boxWidth = boxWidth;

        x = new double[nAtoms];
        vx = new double[nAtoms];
        ax = new double[nAtoms];
        y = new double[nAtoms];
        vy = new double[nAtoms];
        ay = new double[nAtoms];
        dt = 0.01;

        Random rand = new Random();

        for (int i = 0; i < nAtoms; i++) {
            double xrand = rand.nextDouble() * boxWidth;
            double yrand = rand.nextDouble() * boxWidth;
            for (int j = 0; j < i; j++) {
                double d = Math.sqrt((xrand - x[j]) * (xrand - x[j]) + (yrand - y[j]) * (yrand - y[j]));
                if (d < 1.5 || xrand < 1.5 || xrand > boxWidth - 1.5 || yrand < 1.5 || yrand > boxWidth - 1.5) {
                    xrand = rand.nextDouble() * boxWidth;
                    yrand = rand.nextDouble() * boxWidth;
                    j = 0;
                } else {
                    xrand = rand.nextDouble() * boxWidth;
                    yrand = rand.nextDouble() * boxWidth;
                }
            }
            x[i] = xrand;
            y[i] = yrand;
            vx[i] = rand.nextGaussian() * 3;
            vy[i] = rand.nextGaussian() * 3;
        }

        calculateAcceleration();
        calculateKineticEnergy();

    }

    private void calculateAcceleration() {

        for (int i = 0; i < nAtoms; i++) {
            ax[i] = 0;
            ay[i] = 0;
        }

        ePot = 0;
        elasticEnergy = 0;

        for (int i = 0; i < nAtoms - 1; i++)
            for (int j = i + 1; j < nAtoms; j++) { //j + 1 - zeby nie liczyc dwukrotnie tych samych oddzialywan i oddzialywan z samym soba
                double dx = x[i] - x[j];
                double dy = y[i] - y[j];
                double rij2 = dx * dx + dy * dy;

                double d = 0;

                if (x[i] < 0.5) {
                    d = 0.5 - x[i];
                    ax[i] += wallStiffness * d;
                    elasticEnergy += 0.5 * wallStiffness * d * d;
                }

                if (x[i] > (boxWidth - 0.5)) {
                    d = (boxWidth - 0.5 - x[i]);
                    ax[i] += wallStiffness * d;
                    elasticEnergy += 0.5 * wallStiffness * d * d;
                }

                if (y[i] < 0.5) {

                    d = 0.5 - y[i];
                    ay[i] += wallStiffness * d;
                    elasticEnergy += 0.5 * wallStiffness * d * d;
                }

                if (y[i] > (boxWidth - 0.5)) {
                    System.out.println("TEST " + y[i] + " " + boxWidth);
                    d = (boxWidth - 0.5 - y[i]);
                    ay[i] += wallStiffness * d;
                    elasticEnergy += 0.5 * wallStiffness * d * d;
                }

                if (rij2 < rCut2) { //liczymy sily dla czasteczek ktorych odleglosc jest mniejsza niz 4 - dla wiekszych sie nie oplaca, bo sily sa zbyt male
                    double fr2 = 1. / rij2;
                    double fr6 = fr2 * fr2 * fr2;
                    double fr = 48. * fr6 * (fr6 - 0.5) * fr2;

                    double frx = fr * dx;
                    double fry = fr * dy;

                    ax[i] += frx;
                    ay[i] += fry;
                    ax[j] -= frx;
                    ay[j] -= fry;

                    ePot += (4 * fr6 * (fr6 - 1.0));
                }
            }
    }

    private void calculateKineticEnergy() {
        eKin = 0;
        for (int i = 0; i < nAtoms; i++) {
            eKin += ((vx[i] * vx[i]) + (vy[i] * vy[i])) / 2;
        }

        //eKin = (vx[nAtoms] * vx[nAtoms]) / 2 + (vy[nAtoms] * vy[nAtoms]) / 2;

    }

    void verletStep(double dt) {


        for (int i = 0; i < nAtoms; i++) {
            vx[i] = vx[i] + dt * ax[i] / 2;
            vy[i] = vy[i] + dt * ay[i] / 2;

            x[i] = x[i] + dt * vx[i];
            y[i] = y[i] + dt * vy[i];
            //System.out.println(x[i]);
        }

        calculateAcceleration();

        for (int i = 0; i < nAtoms; i++) {
            vx[i] = vx[i] + dt * (ax[i]) / 2;
            vy[i] = vy[i] + dt * (ay[i]) / 2;
        }

        calculateKineticEnergy();

        eC = eKin + ePot + elasticEnergy;

    }

    public double[] getX() {
        return x;
    }

    public void setX(double[] x) {
        this.x = x;
    }

    public double[] getY() {
        return y;
    }

    public void setY(double[] y) {
        this.y = y;
    }

    public double getePot() {
        return ePot;
    }

    public void setePot(double ePot) {
        this.ePot = ePot;
    }

    public double geteKin() {
        return eKin;
    }

    public void seteKin(double eKin) {
        this.eKin = eKin;
    }

    public double geteC() {
        return eC;
    }

    public void seteC(double eC) {
        this.eC = eC;
    }

    public double[] getVx() {
        return vx;
    }

    public void setVx(double[] vx) {
        this.vx = vx;
    }

    public double[] getVy() {
        return vy;
    }

    public void setVy(double[] vy) {
        this.vy = vy;
    }

    public double getelasticEnergy() {
        return elasticEnergy;
    }

}