package lifetime;

import au.com.bytecode.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by u0h2247 on 9/11/2015.
 */
public class DataRetriever {

    private Map<String, Date> users = new HashMap<String, Date>();  // map users, install-date (in Unix time)
    private Map<String, Set<Integer>> data = new HashMap<String, Set<Integer>>();  // map users, set of usage days, in ints

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
    private static long msInADay = 1000 * 60 * 60 * 24;

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
     }

    public DataRetriever(String installsFilename, String... filenames) throws IOException {

        parseInstalls(installsFilename);
        for (String filename : filenames) {
            parseSessions(filename);
        }

    }

    public Map<String, Date> getUsers() {
        return users;
    }

    public Map<String, Set<Integer>> getData() {
        return data;
    }

    private void parseInstalls(String filename) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(filename));
        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {

            // nextLine[] is an array of values from the line
            if (nextLine.length < 3 || nextLine[0].equals("")) { // skip the header
                continue;
            }

            // parse the line:
            String userId = nextLine[1].trim();  // no quality check needed for the id, trim if by chance there are spaces
            String dateString = nextLine[2].trim();

            Date date = null;
            try {
                date = DATE_FORMAT.parse(dateString);
            } catch (ParseException e) {
                System.out.println("Unparsable date: " + dateString + ". skipping entry");  // simply skip, we don't want to waste everything for a bugged entry, should be logged somewhere..
                e.printStackTrace();
            }

            if (date != null) {  // we don't want to add rubbish
                users.put(userId, date);
            }

        }

    }

    private void parseSessions(String filename) throws IOException {

        CSVReader reader = new CSVReader(new FileReader(filename));
        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {

            // nextLine[] is an array of values from the line
            if (nextLine.length < 3 || nextLine[0].equals("")) { // skip the header
                continue;
            }

            // parse the line:
            String userId = nextLine[1].trim();  // no quality check needed for the id, trim if by chance there are spaces

            if (!users.containsKey(userId)) {
                continue; // unknown user.. skip entry
            }
            Date userEntryDate = users.get(userId);

            String tsString = nextLine[2];
//                String durationString = nextLine[3];b  // useless, so far

            Long dayDiff = null;
            try {
                Long milliSeconds = Long.parseLong(tsString);
                long diff = milliSeconds - userEntryDate.getTime();
                if (isBadTimeDiff(diff)) {  // entry quality check
                    continue;
                }
                dayDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                System.out.println("Semething went wrong while parsing time-stamp: " + tsString + ". skipping entry");  // simply skip, we don't want to waste everything for a bugged entry, should be logged somewhere..
                e.printStackTrace();
            }

            if (dayDiff != null) {  // we don't want to add rubbish
                if (!data.containsKey(userId)) {
                    data.put(userId, new HashSet<Integer>());
                }
                data.get(userId).add(dayDiff.intValue());
            }

        }

    }

    private static boolean isBadTimeDiff(long diff) {
        // can be customized to meet other quality criteria
        if (diff < 0 || diff > (1000 * msInADay)) {
            return true;
        }
        return false;
    }

}
