import junit.framework.TestCase;
import pivotal.io.batch.Parser;
import pivotal.io.batch.StateMachine;
import pivotal.io.batch.domain.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class ParserTest extends TestCase{


    public ParserTest(String testName)
    {
        super( testName );
    }

    public void testApp() throws Exception{
//        String infilepath="/Users/kimm5/_dev/skhynix/rawdata/rawdata.txt";
        String infilepath="/Users/kimm5/_dev/DramParser/src/test/resources/testdata/rawdata.txt";
        String outdirpath="/Users/kimm5/_dev/DramParser/src/test/resources/out2";
        new Parser(infilepath,outdirpath, "csv").execute();
    }

}
