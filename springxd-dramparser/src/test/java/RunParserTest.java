import junit.framework.TestCase;
import org.apache.hadoop.fs.Path;
import pivotal.io.batch.Parser;

import java.io.File;

/**
 * Created by kimm5 on 8/1/15.
 */
public class RunParserTest extends TestCase{


    public RunParserTest(String testName)
    {
        super( testName );
    }

    public void testApp() throws Exception{
        String infilepath=new Path(System.getProperty("user.dir")+ File.separator +"src/test/data/sampledata/rawdata.txt.sample.0").toString();
        String outdirpath=new Path(System.getProperty("user.dir")+ File.separator +"src/test/data/out").toString();

        new Parser(infilepath,outdirpath, "csv").execute();
    }

}

