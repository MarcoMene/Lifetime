package lifetime;

import junit.framework.TestCase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DataRetrieverTest extends TestCase {

    public void testDataRetriever() throws Exception {

        String installsFilename = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\tot_installs_20150622-20150904.csv";
        String filename1 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150622 - 20150630.csv";
        String filename2 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150701 - 20150715.csv";
        String filename3 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150716 - 20150731.csv";
        String filename4 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150801 - 20150816.csv";
        String filename5 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150817 - 20150904.csv";

        List<String> sessionFiles = new LinkedList<String>();
        sessionFiles.add(filename1);
        sessionFiles.add(filename2);
        sessionFiles.add(filename3);
        sessionFiles.add(filename4);
        sessionFiles.add(filename5);


        try {
            DataRetriever dr = new DataRetriever(installsFilename);

            for (String sessionFile : sessionFiles) {
                dr.addSessionFile(sessionFile);
            }

            DataPacker dp = new DataPacker(dr.getUsers(), dr.getData(), dr.getLatestDateInSessions(), new WilsonModel());

            dp.dumpConfidenceIntervalsToCsv("C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\dumpCI.csv");

        } catch (Exception e) {
            System.out.print("Nothing done. Need to fix file path probably..");
            e.printStackTrace();
            return;
        }
    }

    public void testDateIssues() throws Exception {

        // test System.currentTimeMillis()
        long myApproximateTime = (long) 1.4412384e+12; // the current time of test execution
        long ct = System.currentTimeMillis();
        assertTrue(ct > myApproximateTime);

        // test date conversion and millisecond diffing
        DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        DateFormat DATE_FORMAT2 = new SimpleDateFormat("dd/MM/yy");
        DATE_FORMAT2.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));

        Date d1 = DATE_FORMAT.parse("13/09/15");  // the same date comes before in Europe than in America/Pacific
        Date d2 = DATE_FORMAT2.parse("13/09/15");

        assertTrue(d2.compareTo(d1) < 0);
        assertTrue(d2.getTime() < d1.getTime());

        long diff = d1.getTime() - d2.getTime();
        long hoursDiff = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);

        assertTrue(hoursDiff == 9L);

        // parse milliseconds, compare dates
        Date d3 = new Date(myApproximateTime);
        Date d4 = new Date(ct);

        assertTrue(d4.compareTo(d3) > 0);

    }

}