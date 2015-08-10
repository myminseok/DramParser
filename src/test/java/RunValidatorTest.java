import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import pivotal.io.batch.Parser;
import pivotal.io.batch.StateMachine;
import pivotal.io.batch.domain.Command;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RunValidatorTest extends TestCase {

    private String infilepath=null;
    private String outdirpath=null;
    private String outfileextension=null;


    public void execute() throws Exception{

        long lStartTime = System.currentTimeMillis();

        File infile = new File(infilepath);

        File outfiletemp = new File(outdirpath+File.separator+infile.getName()+".tmp");

        String outfilepath=outdirpath + File.separator + infile.getName() + "." + outfileextension;

        if(!infile.exists()){
            System.out.println("file not found:"+infilepath);
        }

        if(!outfiletemp.exists()){
            System.out.println("file not found:"+outdirpath);
        }

        FileReader is=null;
        FileWriter os=null;

        try {

            is = new FileReader(infile);
            BufferedReader br = new BufferedReader(is);
            os = new FileWriter(outfiletemp);
            int serial=0;
            String line;
            String[] split;
            byte[] bytedata = new byte[4];
            String bigHex="";
            String bits="";
            String result;
            line = br.readLine();

            Command command;
            StateMachine sm = new StateMachine();
            while (line!=null) {
                serial++;
                split= StringUtils.split(line,',');
                bigHex=split[2];
                bytedata= javax.xml.bind.DatatypeConverter.parseHexBinary(bigHex);
                command= sm.getCommand(bytedata);
                bits= command.byteToBits();
                bigHex=command.byteToHexs();

                StringBuilder sb = new StringBuilder();
//                sb.append(serial).append(",");
                sb.append(line).append(",").append("->").append(",");
                sb.append(command.getName()).append(",");
                sb.append(bits).append(",");
                sb.append(bigHex).append("\n");
                result = sb.toString();
                os.write(result);
                line = br.readLine();
            }

        }finally{
            if(is!=null) is.close();
            if(os!=null) os.close();
        }

        System.out.println("renaming: " + outfiletemp.getAbsolutePath() + " to \n" + outfilepath);
        outfiletemp.renameTo(new File(outfilepath));


        long lEndTime = System.currentTimeMillis()- lStartTime;
        System.out.println(GetFormattedInterval(lEndTime));

    }


    public static String GetFormattedInterval(final long ms) {
        long millis = ms % 1000;
        long x = ms / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        x /= 60;
        long hours = x % 24;

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }


    public void testApp() throws Exception{
        infilepath="/Users/kimm5/_dev/DramParser/src/test/data/out2/rawdata.txt.unique";
        outdirpath="/Users/kimm5/_dev/DramParser/src/test/data/out2";
        outfileextension="csv";
        execute();
    }


}
