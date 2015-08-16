import junit.framework.TestCase;
import org.apache.hadoop.fs.Path;
import pivotal.io.batch.Parser;
import pivotal.io.batch.StateMachine;
import pivotal.io.batch.domain.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kimm5 on 8/1/15.
 */
public class RunCommandParserTest extends TestCase{


    public RunCommandParserTest(String testName)
    {
        super( testName );
    }

    public void testApp() throws Exception{
        String infilepath=new Path(System.getProperty("user.dir")+ File.separator +"src/test/data/sampledata/rawdata.txt.sample.0").toString();
        String outdirpath=new Path(System.getProperty("user.dir")+ File.separator +"src/test/data/out").toString();
        new CommandParser(infilepath,outdirpath, "csv").execute();
    }

}

class CommandParser {

    private String infilepath=null;
    private String outdirpath=null;
    private String outfileextension=null;


    public CommandParser(String infilepath, String outdirpath, String outfileextension){
        this.infilepath=infilepath;
        this.outdirpath=outdirpath;
        this.outfileextension=outfileextension;
        System.out.println("infilepath:"+infilepath);
        System.out.println("outdirpath:"+outdirpath);
        System.out.println("outfileextension:"+outfileextension);
    }

    public void execute() throws Exception{

        long lStartTime = System.currentTimeMillis();

        File infile = new File(infilepath);

        File outfileUnique = new File(outdirpath+File.separator+infile.getName()+".unique.csv");
        File outfiletransit = new File(outdirpath+File.separator+infile.getName()+".transit.csv");
        File outfileinvalid = new File(outdirpath+File.separator+infile.getName()+".invalid.csv");


        String outfilepath=outdirpath + File.separator + infile.getName() + "." + outfileextension;

        if(!infile.exists()){
            System.out.println("file not found:"+infilepath);
            throw new FileNotFoundException(infilepath);
        }

        InputStream is=null;
        FileWriter oUnique=null;
        FileWriter oTransit=null;


        StateMachine sm = new StateMachine();

        Map<String, String> commandMap = new HashMap<String, String>();
        try {

            is = new FileInputStream(infile);
            oUnique = new FileWriter(outfileUnique);
            oTransit = new FileWriter(outfiletransit);
            int serial=0;
            String bigHex="";
            String bits="";
            String parsed="";
            String result;
            StateCommand command;
            byte[] buffer = new byte[4];
            byte[] bufferFinal = new byte[4];

            boolean transit=false;

            while (is.read(buffer) >= 0 ) {
                serial++;
                bufferFinal = StateCommand.getBigEndian(buffer);
                command= sm.getStateCommand(bufferFinal);
                bits = StateCommand.byteToBits(bufferFinal);
                bigHex = StateCommand.bytesToHex(bufferFinal);
                parsed = StateCommand.parse(bufferFinal);

                StringBuilder sb = new StringBuilder();
                sb.append(serial).append(",");
                sb.append(bits).append(",");
                sb.append(bigHex).append(",");
                sb.append(parsed).append(",");
                sb.append(command.getName()).append(",");
                sb.append("\n");
                result = sb.toString();

                if(!commandMap.containsKey(bits)){
                    commandMap.put(bits, result);
                    oUnique.write(result);
                }


            }

        }finally{
            if(is!=null) is.close();
            if(oTransit!=null) oTransit.close();
            if(oUnique!=null) oUnique.close();
        }

//        System.out.println("renaming: " + outfiletemp.getAbsolutePath() + " to \n" + outfilepath);
//        outfiletemp.renameTo(new File(outfilepath));


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


//    public static void main(String[] args) throws Exception{
//
//        String infilepath="/Users/kimm5/_dev/DramParser/src/test/resources/testdata/sample";
//        String outdirpath="/Users/kimm5/_dev/DramParser/src/test/resources/out";
//        new Parser(infilepath,outdirpath).execute();
//    }

}