package lifetime;

/**
 * Created by u0h2247 on 9/15/2015.
 */
public interface ConfidenceIntervalModel {

    public ConfidenceInterval getConfidenceInterval(int success, int trials, double z);
}
