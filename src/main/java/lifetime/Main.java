package lifetime;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by u0h2247 on 9/11/2015.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        final int nFiles = args.length;
        if (nFiles < 3) {
            throw new Exception("Provide at least 3 files as input");
        }
        String installsFilename = args[0];

        String targetFilename = args[1];

        List<String> sessionFiles = new LinkedList<String>();
        for (int i = 2; i < nFiles; ++i) {
            sessionFiles.add(args[i]);
        }

        DataRetriever dr = new DataRetriever(installsFilename);

        for (String sessionFile : sessionFiles) {
            dr.addSessionFile(sessionFile);
        }

        DataPacker dp = new DataPacker(dr.getUsers(), dr.getData(), dr.getLastDateInSessions(), new WilsonModel());

        dp.dumpConfidenceIntervalsToCsv(targetFilename);

        System.out.println("Find dumped confidence intervals in " + targetFilename);

    }

}
