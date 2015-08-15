package pivotal.io.batch;

import pivotal.io.batch.domain.State;
import pivotal.io.batch.domain.StateCommand;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private String infilepath=null;
    private String outdirpath=null;
    private String outfileextension=null;


    public Parser(String infilepath, String outdirpath, String outfileextension){
        this.infilepath=infilepath;
        this.outdirpath=outdirpath;
        this.outfileextension=outfileextension;
        System.out.println("infilepath:"+infilepath);
        System.out.println("outdirpath:"+outdirpath);
//        System.out.println("outfileextension:"+outfileextension);
    }


    private void printHeaderTransit(StateMachine sm ,FileWriter oTransit)  throws Exception{
        StringBuilder sboTransit = new StringBuilder();
        sboTransit.append("serial,\t");
        sboTransit.append(sm.stateInfo.toStringHeader(true)).append(",\t");
        sboTransit.append("bits,\t");
        sboTransit.append(StateCommand.parseHeader());
        sboTransit.append("\n");
        oTransit.write(sboTransit.toString());
    }
    private void printHeaderInvalid(StateMachine sm ,FileWriter oInvalid)  throws Exception{
        StringBuilder sboInvalid = new StringBuilder();
        sboInvalid.append("serial,\t");
        sboInvalid.append(sm.stateInfo.toStringHeader(false)).append(",\t");
        sboInvalid.append("cmd,\t");
        sboInvalid.append("bits,\t");
        sboInvalid.append(StateCommand.parseHeader());
        sboInvalid.append("\n");
        oInvalid.write(sboInvalid.toString());
    }

    public void execute() throws Exception{

        long lStartTime = System.currentTimeMillis();

        File infile = new File(infilepath);

        File outfileUnique = new File(outdirpath+File.separator+infile.getName()+".unique.csv");
        File outfiletransit = new File(outdirpath+File.separator+infile.getName()+".transit.csv");
        File outfileinvalid = new File(outdirpath+File.separator+infile.getName()+".invalid.csv");

        if(!infile.exists()){
            System.out.println("file not found:"+infilepath);
            throw new FileNotFoundException(infilepath);
        }

        InputStream is=null;
        FileWriter oUnique=null;
        FileWriter oTransit=null;
        FileWriter oInvalid=null;


        StateMachine sm = new StateMachine();

        Map<String, String> commandMap = new HashMap<String, String>();
        try {

            is = new FileInputStream(infile);
            oUnique = new FileWriter(outfileUnique);
            oTransit = new FileWriter(outfiletransit);
            oInvalid = new FileWriter(outfileinvalid);
            int serial=0;
            String bigHex="";
            String bits="";
            String parsed="";
            byte[] buffer = new byte[4];
            byte[] bufferFinal = new byte[4];

            boolean isTransit=false;


            printHeaderTransit(sm,oTransit );
            printHeaderInvalid(sm, oInvalid);


            String prevTransit="";
            String prevNoTransit="";

            while (is.read(buffer) >= 0 && serial < 1000000) {
                serial++;
                bufferFinal = StateCommand.getBigEndian(buffer);
                isTransit = sm.transit(bufferFinal);
                bits = StateCommand.byteToBits(bufferFinal);
                bigHex = StateCommand.bytesToHex(bufferFinal);
                parsed = StateCommand.parse(bufferFinal);

                if(isTransit){

                    if(prevTransit.equals(bigHex)){ // prevent dup
                        continue;
                    }
                    oTransit.write(String.format("%10s, %30s, %s, %s\n", serial, sm, bits, parsed));
                    prevTransit=bigHex;

                }else{

                    if(prevNoTransit.equals(bigHex)){// prevent dup
                        continue;
                    }
                    oInvalid.write(String.format("%10s, %15s, %15s, %s, %s\n", serial, sm, sm.getStateCommandType(bufferFinal), bits, parsed));
//                    oInvalid.write(String.format("%10s, %15s, %s, %s\n", serial, sm,  bits, parsed));
                    prevNoTransit=bigHex;
                }

                if(!commandMap.containsKey(bits)){
                    commandMap.put(bits, String.format("%s, %s, %s\n", bits, bigHex, parsed));
                    oUnique.write( commandMap.get(bits));
                }


            }

        }finally{
            if(is!=null) is.close();
            if(oTransit!=null) oTransit.close();
            if(oInvalid!=null) oInvalid.close();
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

}
