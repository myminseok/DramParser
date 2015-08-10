import junit.framework.TestCase;
import pivotal.io.batch.Parser;

/**
 * Created by kimm5 on 8/1/15.
 */
public class RunParserTest extends TestCase{


    public RunParserTest(String testName)
    {
        super( testName );
    }

    public void testApp() throws Exception{
        String infilepath="/Users/kimm5/_dev/DramParser/src/test/data/testdata/rawdata.txt";
        String outdirpath="/Users/kimm5/_dev/DramParser/src/test/data/out";
        new Parser(infilepath,outdirpath, "csv").execute();
    }

}

