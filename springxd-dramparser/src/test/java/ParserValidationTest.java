import org.apache.hadoop.fs.Path;
import pivotal.io.batch.state.StateMachine;
import pivotal.io.batch.command.Command;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kimm5 on 8/1/15.
 */
public class ParserValidationTest {

    public static void main(String[] args) throws Exception {
        String indatapath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/sampledata/rawdata.txt.sample.0").toString();
        String incmdpath  = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/sampledata/500M.txt_PtrnSort_0.filtered.txt").toString();
        String outdirpath = new Path(System.getProperty("user.dir") + File.separator + "src/test/data/sampledata").toString();
        ParserValidationTest parser = new ParserValidationTest();
        parser.execute(indatapath, incmdpath, outdirpath);
    }

    public void execute(String indatapath, String incmdpath, String outdirpath) throws Exception {

        long lStartTime = System.currentTimeMillis();

        File indatafile = new File(indatapath);
        File incmdfile = new File(incmdpath);
        File outfileUnique = new File(outdirpath + File.separator + indatafile.getName() + ".unique.csv");
        File outfilefail = new File(outdirpath + File.separator + indatafile.getName() + ".pivotal.csv");

        if (!indatafile.exists()) {
            throw new FileNotFoundException(indatapath);
        }

        if (!incmdfile.exists()) {
            throw new FileNotFoundException(incmdpath);
        }

        InputStream indata = null;
        BufferedReader incmd = null;
        FileWriter oUnique = null;
        FileWriter ofail = null;
        StateMachine sm = new StateMachine();
        Map<String, String> commandMap = new HashMap<String, String>();
        try {

            indata = new FileInputStream(indatafile);
            incmd = new BufferedReader(new FileReader(incmdfile));
            oUnique = new FileWriter(outfileUnique);
            ofail = new FileWriter(outfilefail);
            int serial = 0;
            String bigHex = "";
            String bits = "";
            String parsed = "";
            String result;
            Command command;
            Command prevCmd=null;
            byte[] buffer = new byte[4];
            byte[] bufferFinal = new byte[4];

            String inputCommand="";
            boolean matching=false;

            StringBuilder sb1 = new StringBuilder();
            sb1.append("serial").append(",");
            sb1.append("bits").append(",");
            sb1.append("hex").append(",");
            sb1.append("parsed").append(",");
            sb1.append("cmd").append(",");
            sb1.append("pivotal_cmd").append(",");
            sb1.append("matching").append(",");
            sb1.append("\n");
            result = sb1.toString();
            ofail.write(result);

            while (indata.read(buffer) >= 0) {
                serial++;

                bufferFinal = Command.getBigEndian(buffer);
                command = sm.findStateCommand(bufferFinal);
                bits = Command.byteToBits(bufferFinal);
                bigHex = Command.bytesToHex(bufferFinal);
                parsed = Command.parse(bufferFinal);

                if(command.equals(prevCmd)){
                    //parsed="dup-"+parsed;
                    continue;
                }else{
                    inputCommand = incmd.readLine();
                }
                prevCmd=command;

                matching = command.getName().toString().equals(inputCommand );
                StringBuilder sb = new StringBuilder();
                sb.append(serial).append(",");
                sb.append(bits).append(",");
                sb.append(bigHex).append(",");
                sb.append(parsed).append(",");
                sb.append(inputCommand).append(",");
                sb.append(command.getName()).append(",");
                sb.append(matching).append(",");
                sb.append("\n");
                result = sb.toString();
                ofail.write(result);

                if (!commandMap.containsKey(bits)) {
                    commandMap.put(bits, result);
                    oUnique.write(result);
                }

            }

        } finally {
            if (indata != null) indata.close();
            if (incmd != null) incmd.close();
            if (ofail != null) ofail.close();
            if (oUnique != null) oUnique.close();
        }

        long lEndTime = System.currentTimeMillis() - lStartTime;
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
}