package lifetime;

import au.com.bytecode.opencsv.CSVWriter;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by u0h2247 on 9/11/2015.
 */
public class Analyzer {

    private Map<String, Set<Integer>> data;  // map users, set of usage days
    private Map<Integer, Long> histogram;

    private List<Integer> lts;
    private List<Double> avgLts;
    private DescriptiveStatistics ltStats;
    private DescriptiveStatistics avgLtStats;

    private static int nBoot = 50;

    public Analyzer(Map<String, Set<Integer>> data) {

        if (data == null) {
            throw new IllegalArgumentException("Null data map!");
        }

        this.data = data;

    }

    public Map<Integer, Long> getHistogram() {
        if (histogram == null) {
            buildHistogram();
        }
        return histogram;
    }

    private void buildHistogram() {

        histogram = new HashMap<Integer, Long>();

        for (Map.Entry<String, Set<Integer>> entry : data.entrySet()) {
            Set<Integer> integerDaysSet = entry.getValue();
            for (Integer dayOfUsage : integerDaysSet) {
                if (!histogram.containsKey(dayOfUsage)) {
                    histogram.put(dayOfUsage, Long.valueOf(0));
                }
                long currentCount = histogram.get(dayOfUsage);
                histogram.put(dayOfUsage, ++currentCount);
            }
        }

    }

    public void dumpLtsToCsvFile(String filename) throws IOException {

        CSVWriter csvWriter = new CSVWriter(new FileWriter(filename), ',', CSVWriter.NO_QUOTE_CHARACTER);

        for (Integer lt : getLts()) {
            csvWriter.writeNext(new String[]{lt.toString()});
        }

        csvWriter.close();
    }

    public void dumpAvgLtsToCsvFile(String filename) throws IOException {

        CSVWriter csvWriter = new CSVWriter(new FileWriter(filename), ',', CSVWriter.NO_QUOTE_CHARACTER);

        for (Double avgLt : getAvgLts()) {
            csvWriter.writeNext(new String[]{avgLt.toString()});
        }

        csvWriter.close();
    }

    public List<Integer> getLts() {
        if (lts == null) {
            evaluateLtStats();
        }
        return lts;
    }

    public List<Double> getAvgLts() {
        if (avgLts == null) {
            evaluateAvgLtStatsWithBootstrap(nBoot);
        }
        return avgLts;
    }

    public DescriptiveStatistics getLtStats() {
        if (ltStats == null) {
            evaluateLtStats();
        }
        return ltStats;
    }

    public DescriptiveStatistics getAvgLtStats() {
        if (avgLtStats == null) {
            evaluateAvgLtStatsWithBootstrap(nBoot);
        }
        return avgLtStats;
    }

    public Double getAverageLifetime() {
        return getLtStats().getMean();
    }

    public Double getAverageLifetimeUncertainty() {
        return getAvgLtStats().getStandardDeviation();
    }

    private void evaluateLtStats() {

        ltStats = new DescriptiveStatistics();
        lts = new ArrayList<Integer>();

        for (Map.Entry<String, Set<Integer>> entry : data.entrySet()) {
            Set<Integer> integerDaysSet = entry.getValue();
            int lt = integerDaysSet.size();
            ltStats.addValue(lt);
            lts.add(lt);
        }

    }

    private void evaluateAvgLtStatsWithBootstrap(int N) {

        avgLtStats = new DescriptiveStatistics();
        avgLts = new ArrayList<Double>();

        // run bootstrap over your data
        Object[] usersArray = data.keySet().toArray();

        for (int i = 0; i < N; ++i) {

            DescriptiveStatistics Lts = new DescriptiveStatistics();

            int nUsers = usersArray.length;
            for (int j = 0; j < nUsers; ++j) {

                Random rnd = new Random();
                int userIndex = rnd.nextInt(nUsers);

                String user = (String) usersArray[userIndex];  // pick up a random user from the set
                Set<Integer> integerDaysSet = data.get(user);
                int lt = integerDaysSet.size();
                Lts.addValue(lt);
            }
            Double avgLt = Lts.getMean();
            avgLtStats.addValue(avgLt);
            avgLts.add(avgLt);

        }

    }


}
