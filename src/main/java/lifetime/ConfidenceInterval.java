package lifetime;

/**
 * Created by u0h2247 on 9/15/2015.
 */
public class ConfidenceInterval {

    private Double center;
    private Double width;

    public ConfidenceInterval(Double center, Double width) {
        this.center = center;
        this.width = width;
    }

    public Double getCenter() {
        return center;
    }

    public Double getWidth() {
        return width;
    }
}
