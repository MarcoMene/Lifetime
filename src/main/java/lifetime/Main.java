package lifetime;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by u0h2247 on 9/11/2015.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        final int nFiles = args.length;
        if(nFiles < 2)  {
            throw new Exception("Provide at least 2 files as input");
        }
        String installsFilename = args[0];

        List<String> sessionFiles = new LinkedList<String>();
        for (int i = 1; i < nFiles; ++i){
            sessionFiles.add(args[i]);
        }

        DataRetriever dr = new DataRetriever(installsFilename);

        for (String sessionFile : sessionFiles) {
            dr.addSessionFile(sessionFile);
        }

        Analyzer analyzer = new Analyzer(dr.getData());

        Double averageLifetime = analyzer.getAverageLifetime();
        Double averageLifetimeUncertainty = analyzer.getAverageLifetimeUncertainty();

        System.out.println("Average lifetime: " + averageLifetime + " +/- " + averageLifetimeUncertainty);

        analyzer.dumpLtsToCsvFile("C:\\Users\\marco.meneghelli\\Desktop\\BS\\trialDumpLts.csv");
        analyzer.dumpAvgLtsToCsvFile("C:\\Users\\marco.meneghelli\\Desktop\\BS\\trialAvgLtDumpLts.csv");

    }

}
