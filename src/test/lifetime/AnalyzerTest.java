package lifetime;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnalyzerTest extends TestCase {

    public void testAverageLifetime() throws Exception {

        // test normal behavior on a small, known dataset

        Map<String, Set<Integer>> data = new HashMap<String, Set<Integer>>();
        Set<Integer> set1 = new HashSet<Integer>();
        set1.add(1);
        set1.add(3);
        set1.add(4);
        Set<Integer> set2 = new HashSet<Integer>();
        set2.add(1);
        set2.add(5);
        set2.add(9);
        set2.add(11);

        data.put("pippo", set1);
        data.put("pluto", set2);

        Analyzer analyzer = new Analyzer(data);

        assertEquals(3.5, analyzer.getAverageLifetime(), 0.001);
        assertTrue(analyzer.getAverageLifetimeUncertainty() > 0);

    }

}