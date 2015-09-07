import org.apache.hadoop.fs.Path;

import java.io.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class CommandExtracter {

    public static void main(String[] args) throws Exception {
        String infilepath = new Path(System.getProperty("user.dir") + File.separator + "src/test/rawdata/500M.txt_PtrnSort_0.txt").toString();
        String outdirpath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/sampledata").toString();
        CommandExtracter parser = new CommandExtracter();
        parser.execute(infilepath, outdirpath);

    }

    public CommandExtracter() {

    }

    private void execute(String infilepath, String outdirpath) throws Exception {

        File infile = new File(infilepath);

        if (!infile.exists()) {
            System.out.println("file not found:" + infilepath);
            throw new FileNotFoundException(infilepath);
        }

        BufferedReader is = null;
        FileWriter oFile = null;

        try {
            is = new BufferedReader(new FileReader(infile));
            int serial = 0;
            String line = "";
            int fileunit = 1000000;
            String[] splits = null;
            while ((line = is.readLine()) != null) {
                line = line.trim();
                splits = line.split(";");
                if (splits.length <= 0) {
                    continue;
                }
                for (String split : splits) {
                    if (serial % fileunit == 0) {
                        if (oFile != null) oFile.close();
                        File outfile = new File(outdirpath + File.separator + infile.getName() + ".cmd." + (serial / fileunit));
                        oFile = new FileWriter(outfile);
                    }
                    if (split.indexOf("(") > 0) {
                        split = split.substring(0, split.indexOf("(")).trim();
                    }
                    if (split.isEmpty()) {
                        continue;
                    }
                    oFile.write(split + "\n");
                    serial++;
                }

                System.out.println(serial);


            }

        } finally {
            if (is != null) is.close();
            if (oFile != null) oFile.close();
        }

    }
}

