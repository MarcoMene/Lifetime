package lifetime;

import java.io.IOException;

/**
 * Created by u0h2247 on 9/11/2015.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        String installsFilename = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\tot_installs_20150622-20150904.csv";
        String filename1 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150622 - 20150630.csv";
        String filename2 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150701 - 20150715.csv";
        String filename3 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150716 - 20150731.csv";
        String filename4 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150801 - 20150816.csv";
        String filename5 = "C:\\Users\\u0h2247\\Desktop\\Altro\\BS\\challenge\\Archive\\Archive\\Sessions 20150817 - 20150904.csv";

        DataRetriever dr = new DataRetriever(installsFilename, filename1, filename2, filename3, filename4, filename5);

        Analyzer analyzer = new Analyzer(dr.getData());

        Double averageLifetime = analyzer.getAverageLifetime();
        Double averageLifetimeUncertainty = analyzer.getAverageLifetimeUncertainty();

        System.out.println("Average lifetime: " + averageLifetime + " +/- " + averageLifetimeUncertainty);
    }

}
