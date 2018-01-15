import MonteCarlo.Method;
import MonteCarlo.VecOp;

import java.util.Random;

import static Diagramm.Plot2D.plot;

public class Test {
    static VecOp x;
    static VecOp y;

    public static void main(String[] args) {
        System.out.println("Test");
        x = new VecOp(new double[]{1, 2, 3});
        y = new VecOp(new double[]{31, 35, 59});
        Random r = new Random();
        Method m = new Method(r, new VecOp(new double[]{1, 1, 1}), x -> error(x));
        VecOp fact = m.runMethod(100000, 0.1, new VecOp(new double[]{0, 0, 0}));
        System.out.printf("args: %f, %f, %f\n", fact.vect[0], fact.vect[1], fact.vect[2]);
        double[] vdy = new double[100];
        for (int i = 0; i < 100; i++) {
            vdy[i] = ((double) i) / 100 * 4;
        }
        VecOp vd = (new VecOp(vdy));
        vd.op(a -> formula(a, fact));
        plot(vd.vect);
    }

    static private double error(VecOp args) {
        VecOp x1 = x.clone();
        x1.op((a, b) -> formula(a, args) - b, y);
        return x1.fold(0, (acc, v) -> acc + v * v);
    }

    private static double formula(double x, VecOp args) {
        double a0 = args.vect[0];
        double a1 = args.vect[1];
        double a2 = args.vect[2];
        return (a1 * Math.exp(a0 * x) + a2);
    }
}