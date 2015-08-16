import org.apache.hadoop.fs.Path;
import pivotal.io.batch.domain.StateCommand;

import java.io.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class DataFileSpliter {

    public static void main(String[] args) throws Exception {
        String infilepath=new Path(System.getProperty("user.dir")+ File.separator +"src/test/data/sampledata/rawdata.txt.sample.0").toString();
        String outdirpath=new Path(System.getProperty("user.dir")+ File.separator +"src/test/data/out").toString();
        Parser parser = new Parser(infilepath, outdirpath, "csv");
        parser.execute();
    }

}

class Parser {

    private String infilepath = null;
    private String outdirpath = null;

    public Parser(String infilepath, String outdirpath, String outfileextension) {
        this.infilepath = infilepath;
        this.outdirpath = outdirpath;
        System.out.println("infilepath:" + infilepath);
        System.out.println("outdirpath:" + outdirpath);
    }

    public void execute() throws Exception {

        long lStartTime = System.currentTimeMillis();

        File infile = new File(infilepath);




        if (!infile.exists()) {
            System.out.println("file not found:" + infilepath);
            throw new FileNotFoundException(infilepath);
        }

        InputStream is = null;
        OutputStream oFile = null;

        try {
            is = new FileInputStream(infile);
            int serial = 0;
            byte[] buffer = new byte[4];
            byte[] bufferFinal;

            int fileunit=1000000;
            while (is.read(buffer) >= 0 && serial < fileunit*3) {
                if(serial % fileunit == 0){
                    if (oFile != null) oFile.close();
                    File outfile = new File(outdirpath + File.separator + infile.getName() + ".sample."+ (serial / fileunit));
                    oFile = new FileOutputStream(outfile);
                }
                serial++;
                bufferFinal = StateCommand.getBigEndian(buffer);
                oFile.write(bufferFinal);
            }

        } finally {
            if (is != null) is.close();
            if (oFile != null) oFile.close();
        }


        long lEndTime = System.currentTimeMillis() - lStartTime;
        System.out.println(GetFormattedInterval(lEndTime));

    }


    public String GetFormattedInterval(final long ms) {
        long millis = ms % 1000;
        long x = ms / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        x /= 60;
        long hours = x % 24;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }

}
