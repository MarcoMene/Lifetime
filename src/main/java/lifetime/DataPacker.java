package lifetime;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by u0h2247 on 9/15/2015.
 */
public class DataPacker {

    private Map<String, Date> users = new HashMap<String, Date>();  // map users, install-date
    private Map<String, Set<Integer>> data;  // map users, set of usage days
    private Date latestDateInSessions;

    private ConfidenceIntervalModel model;

    private Map<Integer, ConfidenceInterval> confidenceIntervals = null;

    public DataPacker(Map<String, Date> users, Map<String, Set<Integer>> data, Date latestDateInSessions, ConfidenceIntervalModel model) {

        if (users == null) {
            throw new IllegalArgumentException("Null users map!");
        }
        if (data == null) {
            throw new IllegalArgumentException("Null data map!");
        }
        if (latestDateInSessions == null) {
            throw new IllegalArgumentException("Null latestDateInSessions!");
        }
        if (model == null) {
            throw new IllegalArgumentException("Null model!");
        }

        this.users = users;
        this.data = data;
        this.latestDateInSessions = latestDateInSessions;
        this.model = model;
    }

    public Map<Integer, ConfidenceInterval> getConfidenceIntervals() {
        if (confidenceIntervals == null) {
            buildConfidenceIntervals();
        }
        return confidenceIntervals;
    }

    public void dumpConfidenceIntervalsToCsv(String filename) throws IOException {

        CSVWriter csvWriter = new CSVWriter(new FileWriter(filename), ',', CSVWriter.NO_QUOTE_CHARACTER);

        csvWriter.writeNext(new String[]{"t", "p", "dp"});

        for (Map.Entry<Integer, ConfidenceInterval> entry : getConfidenceIntervals().entrySet()) {

            Integer t = entry.getKey();
            ConfidenceInterval confidenceInterval = entry.getValue();
            Double p = confidenceInterval.getCenter();
            Double dp = confidenceInterval.getWidth();

            csvWriter.writeNext(new String[]{t.toString(), p.toString(), dp.toString()});
        }

        csvWriter.close();

    }

    private void buildConfidenceIntervals() {

        final Collection<Date> dates = users.values();
        Date minDateInUsers = Collections.min(dates);

        final int N = getFillableNDays(minDateInUsers);

        int[] tTrials = new int[N];
        int[] tSuccesses = new int[N];
        for (int i = 0; i < N; ++i) {
            tTrials[i] = 0;
            tSuccesses[i] = 0;
        }

        // fill the data
        for (String user : data.keySet()) {

            if (!users.containsKey(user)) {  // it should be always there
                continue;
            }
            int n = getFillableNDays(users.get(user));

            for (int i = 0; i < n; ++i) {
                tTrials[i] += 1;
            }
            for (Integer i : data.get(user)) {
                tSuccesses[i] += 1;
            }

        }

        // build confidence intervals
        confidenceIntervals = new HashMap<Integer, ConfidenceInterval>();

        for (int i = 0; i < N; ++i) {

            int n = tTrials[i];
            int y = tSuccesses[i];

            if (n <= 0) {
                continue;  // no data here
            }
            confidenceIntervals.put(i, model.getConfidenceInterval(y, n, 2));

        }

    }

    private int getFillableNDays(Date d) {

        long diff = latestDateInSessions.getTime() - d.getTime();
        int dayMaxDiff = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if(dayMaxDiff <0){
            return -1;
        }
        return dayMaxDiff + 1;

    }

}
