package pivotal.io.batch;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.util.StringUtils;

import java.io.*;

public class DataFileSplit {


    private FSDataOutputStream getHDFSOutputStream(String fsUri, String outputfile) throws IOException{
        Configuration conf = new Configuration();
        conf.set("fs.default.name",fsUri);
        FileSystem hdfs = FileSystem.get(conf);
        Path outfile = new Path(outputfile);
        if(hdfs.exists(outfile)){
            hdfs.delete(outfile,true);
        }
        System.out.println("output:" + outputfile);
        return  hdfs.create(outfile);
    }

    private OutputStream getFileOutputStream(String outputfile) throws FileNotFoundException{
        File outfile = new File(outputfile);
        System.out.println("output:" + outputfile);
        return  new FileOutputStream(outfile);
    }

    public void execute(String indatapath, String outdirpath, String fsUri, String rollover) throws Exception {

        long lStartTime = System.currentTimeMillis();
        File infile = new File(indatapath);
        if (!infile.exists()) {
            System.out.println("file not found:" + indatapath);
            throw new FileNotFoundException(indatapath);
        }
        long rolloverItems = Long.valueOf(rollover)/4;
        String sourceFilename=infile.getName();
        int outFileSerial=0;

        InputStream is = null;
        OutputStream out = null;
        try {
            is = new FileInputStream(infile);
            if(!StringUtils.isEmpty(fsUri)) {
                out = getHDFSOutputStream(fsUri, fsUri+File.separator+outdirpath + File.separator + sourceFilename + '-' + outFileSerial);
            }else {
                out = getFileOutputStream(outdirpath + File.separator+sourceFilename+'-'+outFileSerial);
            }

            int serial = 0;
            byte[] buffer = new byte[4];
            while (is.read(buffer) >= 0) {

                if (serial % rolloverItems == 0) {
                    if (out != null) out.close();
                    if(!StringUtils.isEmpty(fsUri)) {
                        out = getHDFSOutputStream(fsUri, fsUri + File.separator + outdirpath + File.separator + sourceFilename + '-' + outFileSerial);
                    }else{
                        out = getFileOutputStream(outdirpath + File.separator+sourceFilename+'-'+outFileSerial);
                    }
                    outFileSerial++;
                }
                out.write(buffer);
                serial++;
            }

        } finally {
            if (is != null) is.close();
            if (out != null) out.close();
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
