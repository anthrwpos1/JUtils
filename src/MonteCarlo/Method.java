package MonteCarlo;

import java.util.Random;

public class Method {
    private double errMin;
    private Random r;
    private VecOp bestArg;
    private VecOp factor;
    private double dec = 1;
    private Functional error;

    public Method(Random r, VecOp factor, Functional error) {
        this.r = r;//Ссылка на герератор случайных чисел
        this.factor = factor;//число в районе которого возможна смена знака
        this.error = error;//функционал ошибки
    }

    public VecOp runMethod(int iterMax, double errMax, VecOp initValue) {
        int len = initValue.getLen();
        bestArg = initValue;
        errMin = error.functional(initValue);
        int iter = 0;
        VecOp add = new VecOp(new double[len]);
        VecOp mul = new VecOp(new double[len]);
        VecOp testArg;
        double err;
        while (iter < iterMax && errMin > errMax) {
            for (int i = 0; i < add.getLen(); i++) {
                add.vect[i] = (r.nextDouble() - 0.5) / dec;
                mul.vect[i] = ((r.nextDouble() * 2 - 1) / dec) + 1;
            }
            add.op((x, y) -> x * y, factor);
            testArg = bestArg.clone();
            testArg.op((x, y) -> x * y, mul);
            testArg.op((x, y) -> x + y, add);
            err = error.functional(testArg);
            dec++;
            if (err < errMin) {
                errMin = err;
                bestArg = testArg;
                dec = 1;
                //System.out.printf("V = I*%f+%f\nerror = %f, iter = %d\n",bestArg.vect[0],bestArg.vect[1],errMin,iter);
            }
            iter++;
        }
        return bestArg;
    }

    public interface Functional {//интерфейс-функционал. Содержит преобразование функции в число.

        public double functional(VecOp function);
    }
}

