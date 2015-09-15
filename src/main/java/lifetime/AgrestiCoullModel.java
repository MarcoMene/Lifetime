package lifetime;

/**
 * Created by u0h2247 on 9/15/2015.
 */
public class AgrestiCoullModel implements ConfidenceIntervalModel {

    public ConfidenceInterval getConfidenceInterval(int success, int trials, double z) {

        if(trials < 1){
            return new ConfidenceInterval(Double.NaN,Double.NaN);
        }

        double n = trials;
        double y = success;

        double nHat = n + z * z;
        double pHat = 1/nHat * (y + z * z / 2);

        double width = z * Math.sqrt( 1/nHat * pHat * (1 - pHat) );

        return new ConfidenceInterval(pHat,width);

    }

}
