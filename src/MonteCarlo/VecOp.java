package MonteCarlo;

public class VecOp {
    public double[] vect;
    private int len;

    public VecOp(double[] vect) {
        this.vect = vect;
        len = vect.length;
    }

    public int getLen() {
        return len;
    }

    public void op(Functor f) {
        for (int i = 0; i < len; i++) {
            vect[i] = f.map(vect[i]);
        }
    }

    public void op(Applicative ap,VecOp x) {
        for (int i = 0; i < len; i++) {
            vect[i] = ap.apply(vect[i],x.vect[i]);
        }
    }

    public double fold (double acc0,Foldable fld){
        double acc = acc0;
        for (double v : vect) {
            acc = fld.fun (acc,v);
        }
        return acc;
    }

    public VecOp clone() {
        return new VecOp(vect.clone());
    }

    public interface Functor {
        public double map(double x);
    }

    public interface Applicative{
        public double apply(double x, double y);
    }

    public interface Foldable{
        public double fun(double acc, double val);
    }
}
