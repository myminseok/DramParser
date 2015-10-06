import junit.framework.TestCase;
import org.apache.hadoop.fs.Path;
import pivotal.io.batch.Parser;

import java.io.File;

/**
 * Created by kimm5 on 8/1/15.
 */
public class RunParserTest {


    public static void main(String[] args) throws Exception {
//        String indatapath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/sampledata/rawdata.txt.sample.0").toString();
//        String indatapath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/rawdata/rawdata.txt").toString();
          String indatapath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/out/rawdata_3GB.txt").toString();

        String outdirpath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/out").toString();
        new Parser(indatapath,outdirpath).execute();
    }

}

