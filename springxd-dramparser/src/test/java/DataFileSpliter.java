import org.apache.hadoop.fs.Path;

import java.io.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class DataFileSpliter {

    public static void main(String[] args) throws Exception {
        String infilepath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/rawdata/rawdata.txt").toString();
        String outdirpath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/sampledata").toString();
        DataFileSpliter parser = new DataFileSpliter();
        parser.execute(infilepath, outdirpath);
    }



    public void execute(String indatapath, String outdirpath) throws Exception {

        long lStartTime = System.currentTimeMillis();
        File infile = new File(indatapath);
        if (!infile.exists()) {
            System.out.println("file not found:" + indatapath);
            throw new FileNotFoundException(indatapath);
        }

        InputStream is = null;
        OutputStream oFile = null;

        try {
            is = new FileInputStream(infile);
            int serial = 0;
            byte[] buffer = new byte[4];
            byte[] bufferFinal;

            int fileunit = 1000000;
            while (is.read(buffer) >= 0 && serial < fileunit * 2) {
                if (serial % fileunit == 0) {
                    if (oFile != null) oFile.close();
                    File outfile = new File(outdirpath + File.separator + infile.getName() + ".sample." + (serial / fileunit));
                    oFile = new FileOutputStream(outfile);
                }
                serial++;
                oFile.write(buffer);
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
