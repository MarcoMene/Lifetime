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

    private Map<String, Date> users = new HashMap<String, Date>();  // map users, install-date
    private Map<String, Set<Integer>> data = new HashMap<String, Set<Integer>>();  // map users, set of usage days, in ints
    private Date latestDateInSessions =  new Date(0);  // date-zero: 1st Jan 1970

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
    private static long currentTimeMillis = System.currentTimeMillis();

    static {
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
     }

    public DataRetriever(String installsFilename) throws IOException {

        parseInstalls(installsFilename);

    }

    public void addSessionFile(String filename) throws IOException {
        parseSessions(filename);
    }

    public Map<String, Date> getUsers() {
        return users;
    }

    public Map<String, Set<Integer>> getData() {
        return data;
    }

    public Date getLatestDateInSessions() {
        return latestDateInSessions;
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
            if (nextLine.length < 3 || nextLine[0].equals("")) { // skip the header or bad lines
                continue;
            }

            // parse the line:
            String userId = nextLine[1].trim();  // no quality check needed for the id, trim if by chance there are spaces

            if (!users.containsKey(userId)) {
                continue; // unknown user.. skip entry
            }
            Date userEntryDate = users.get(userId);

            String tsString = nextLine[2];
//                String durationString = nextLine[3];  // useless, so far

            Long dayDiff = null;
            try {

                Long milliSeconds = Long.parseLong(tsString);
                if (isBadTimestamp(milliSeconds)) {  // quality check
                    continue;
                }

                // get the date (for latest date)
                Date currentDate = new Date(milliSeconds);
                if(currentDate.compareTo(latestDateInSessions) > 0){ // if later, update latest
                    latestDateInSessions = currentDate;
                }

                // evaluate day diff
                long diff = milliSeconds - userEntryDate.getTime();
                dayDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                if(dayDiff < 0) continue; // multiple installations

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

    private static boolean isBadTimestamp(long milliSeconds) {

        // can be customized to meet other quality criteria
        if (milliSeconds < 0 || milliSeconds > currentTimeMillis) {
            return true;
        }
        return false;
    }

}
