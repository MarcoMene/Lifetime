package lifetime;

/**
 * Created by u0h2247 on 9/15/2015.
 */
public class WilsonModel implements ConfidenceIntervalModel {

    public ConfidenceInterval getConfidenceInterval(int success, int trials, double z) {

        if(trials < 1){
            return new ConfidenceInterval(Double.NaN,Double.NaN);
        }

        double n = trials;
        double y = success;

        double pHat = y/n;

        double center = (pHat + z * z / (2 * n)) / (1 + z * z / n);

        double width = z * Math.sqrt( 1/n * pHat * (1 - pHat) + z * z / (4 * n * n) )  / (1 + z * z / n);

        return new ConfidenceInterval(center,width);

    }

}
