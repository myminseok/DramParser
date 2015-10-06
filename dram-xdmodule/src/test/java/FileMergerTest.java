import org.apache.hadoop.fs.Path;

import java.io.*;

/**
 * Created by kimm5 on 10/5/15.
 */
public class FileMergerTest {

    public static void main(String[] args) throws Exception {

        System.out.println("begin"+Integer.MAX_VALUE);
        String infilepath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/rawdata/rawdata.txt").toString();
        String outdirpath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/out/").toString();
        String outfilepath=outdirpath + File.separator+"merged";
        FileMergerTest obj = new FileMergerTest();
        obj.execute(infilepath, outfilepath);

        System.out.println("complete");

    }


    public void execute(String indatapath, String outfilepath) throws Exception {

        File infile = new File(indatapath);
        if (!infile.exists()) {
            System.out.println("file not found:" + indatapath);
            throw new FileNotFoundException(indatapath);
        }
        InputStream is = null;
        OutputStream out = null;
        int mergeCount=0;
        try {

            is = new FileInputStream(infile);
            out = new FileOutputStream(new File(outfilepath));

            byte[] buffer = new byte[4];
            int length=0;
            int limit = Integer.MAX_VALUE-4;
            while(mergeCount<4 ) {
                while (is.read(buffer) >= 0 && length<limit) {
                    out.write(buffer);
                    length+=4;
                }
                System.out.println("done:" + mergeCount);
                if (is != null) is.close();
                is = new FileInputStream(infile);
                mergeCount++;
            }

            System.out.println("length:"+length);


        } finally {
            if (is != null) is.close();
            if (out != null) out.close();
        }

    }
}
