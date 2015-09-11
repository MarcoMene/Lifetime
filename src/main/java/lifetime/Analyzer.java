package lifetime;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by u0h2247 on 9/11/2015.
 */
public class Analyzer {

    private Map<String, Set<Integer>> data;  // map users, set of usage days
    private Map<Integer, Long> histogram;

    private Double avgLt = Double.NaN;
    private Double avgLtUncertainty = Double.NaN;

    public Analyzer(Map<String, Set<Integer>> data) {

        if (data == null) {
            throw new IllegalArgumentException("Null data map!");
        }

        this.data = data;

        evaluateAvgLt();  // evaluate avg LT immediately, leave the bootstrap for later..
    }

    public Map<Integer, Long> getHistogram() {
        if(histogram == null){
            buildHistogram();
        }
        return histogram;
    }

    private void buildHistogram() {

        histogram = new HashMap<Integer, Long>();

        for (Map.Entry<String, Set<Integer>> entry : data.entrySet()) {
            Set<Integer> integerDaysSet = entry.getValue();
            for (Integer dayOfUsage : integerDaysSet) {
                if(!histogram.containsKey(dayOfUsage)){
                    histogram.put(dayOfUsage, Long.valueOf(0));
                }
                long currentCount = histogram.get(dayOfUsage);
                histogram.put(dayOfUsage, ++currentCount);
            }
        }

    }

    public Double getAverageLifetime() {
        return avgLt;
    }

    public Double getAverageLifetimeUncertainty() {
        if (Double.isNaN(avgLtUncertainty)) {
            evaluateAvgLtUncertaintyWithBootstrap(10);
        }
        return avgLtUncertainty;
    }

    private void evaluateAvgLtUncertaintyWithBootstrap(int N) {

        DescriptiveStatistics avgLts = new DescriptiveStatistics();

        // run bootstrap over your data
        Object[] usersArray = data.keySet().toArray();

        for (int i = 0; i < N; ++i) {

            DescriptiveStatistics Lts = new DescriptiveStatistics();

            int nUsers = usersArray.length;
            for (int j = 0; j < nUsers; ++j) {

                Random rnd = new Random();
                int userIndex = rnd.nextInt(nUsers);

                String user = (String)usersArray[userIndex];  // pick up a random user from the set
                Set<Integer> integerDaysSet = data.get(user);
                int Lt = integerDaysSet.size();
                Lts.addValue(Lt);
            }
            avgLts.addValue(Lts.getMean());

        }

        avgLtUncertainty = avgLts.getStandardDeviation();
    }

    private void evaluateAvgLt() {

        DescriptiveStatistics Lts = new DescriptiveStatistics();

        for (Map.Entry<String, Set<Integer>> entry : data.entrySet()) {
            Set<Integer> integerDaysSet = entry.getValue();
            int Lt = integerDaysSet.size();
            Lts.addValue(Lt);
        }

        avgLt = Lts.getMean();

    }

}
