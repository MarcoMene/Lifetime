package lifetime;

import junit.framework.TestCase;

public class AgrestiCoullModelTest extends TestCase {

    public void testGetConfidenceInterval() throws Exception {

        ConfidenceIntervalModel model = new AgrestiCoullModel();

        int[] successes = new int[]{100,80,70,50,40,30,20,10,1,0};
        int[] trials = new int[]{100,100,100,100,100,100,100,100,100,100};

        for(int i = 0; i < successes.length; ++i){

            int success = successes[i];
            int trialsCount = trials[i];

            ConfidenceInterval ci = model.getConfidenceInterval(success, trialsCount, 2);
            Double center = ci.getCenter();
            Double width = ci.getWidth();

            assertTrue(center >= 0 && center <= 1);
            assertTrue(width > 0);

            System.out.println( "Agresti-Coull CI: success: " + success + " trials: " + trialsCount
                    + " --> " + center + " +/- " + width + " classical estimate: " + ((double)success)/trialsCount );

        }

    }
}