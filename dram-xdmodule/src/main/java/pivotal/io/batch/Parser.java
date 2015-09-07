package pivotal.io.batch;

import pivotal.io.batch.command.Command;
import pivotal.io.batch.command.CommandUndefined;
import pivotal.io.batch.state.StateMachine;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private String infile =null;
    private String outdir =null;


    public Parser(String infile, String outdir){
        this.infile = infile;
        this.outdir = outdir;
        System.out.println("infile:"+ infile);
        System.out.println("outdir:"+ outdir);
    }


    private void printHeaderTransit(StateMachine sm ,BufferedWriter out)  throws Exception{
        StringBuilder sboTransit = new StringBuilder();
        sboTransit.append("serial,");
        sboTransit.append(sm.stateInfo.toStringHeader(true)).append(",");
        sboTransit.append("bits,");
        sboTransit.append(Command.parseHeader()+",");
        sboTransit.append("isTransit");
        sboTransit.append("\n");
        out.write(sboTransit.toString());
    }
    private void printHeaderInvalid(StateMachine sm ,BufferedWriter out)  throws Exception{
        StringBuilder sb = new StringBuilder();
        sb.append("serial,");
        sb.append(sm.stateInfo.toStringHeader(false)).append(",");
        sb.append("cmd,");
        sb.append("bits,");
        sb.append(Command.parseHeader());
        sb.append("\n");
        out.write(sb.toString());
    }

    public void execute() throws Exception{

        long lStartTime = System.currentTimeMillis();

        File infile = new File(this.infile);

        File outfileUnique = new File(outdir +File.separator+infile.getName()+".unique.csv");
        File outfiletransit = new File(outdir +File.separator+infile.getName()+".transit.csv");
        File outfileinvalid = new File(outdir +File.separator+infile.getName()+".undefined.csv");

        if(!infile.exists()){
            System.out.println("file not found:"+ this.infile);
            throw new FileNotFoundException(this.infile);
        }

        InputStream is=null;
        BufferedWriter oUnique=null;
        BufferedWriter oTransit=null;
        BufferedWriter oinvalid=null;

        StateMachine sm = new StateMachine();

        Map<String, String> commandMap = new HashMap<String, String>();
        try {

            is = new FileInputStream(infile);
            oUnique = new BufferedWriter(new FileWriter(outfileUnique));
            oTransit = new BufferedWriter(new FileWriter(outfiletransit));
            oinvalid = new BufferedWriter(new FileWriter(outfileinvalid));
            int serial=0;
            String bigHex="";
            String bits="";
            String parsed="";
            byte[] bufferFinal = new byte[4];

            boolean isTransit=false;

            printHeaderTransit(sm,oTransit );
            printHeaderInvalid(sm,oinvalid);

            Command command;
            Command prevCmd=null;
            Command undefinedCmd= CommandUndefined.getInstance();
//            while (is.read(bufferFinal) >= 0 && serial < 1000000) {
            while (is.read(bufferFinal) >= 0 ) {
                serial++;
                if(serial % 100000==0) {
                    System.out.println("serial: "+serial);
                }
//                bufferFinal = Command.getBigEndian(buffer);
                if(sm.isIgnoreCommand(bufferFinal)){
                    continue;
                }
                isTransit = sm.transit(bufferFinal);
                bits = Command.byteToBits(bufferFinal);
                bigHex = Command.bytesToHex(bufferFinal);
                parsed = Command.parse(bufferFinal);
                command= sm.getTrialValueHolder().triedCommand;
                if(undefinedCmd.equals(command)){
                    oinvalid.write(String.format("%10s, %s, %s,  %s, %s\n", serial, sm, sm.findStateCommand(bufferFinal).getName(), bits, parsed));
                    continue;
                }
                if(command.equals(prevCmd)){ // prevent dup
                    continue;
                }
                prevCmd=command;
                if(isTransit){
                    oTransit.write(String.format("%10s, %30s, %s, %s, %s\n", serial, sm, bits, parsed, isTransit));
                }else{
                    oTransit.write(String.format("%10s, %30s, %s, %s, %s\n", serial, sm, bits, parsed, isTransit));
                }

                if(!commandMap.containsKey(bits)){
                    commandMap.put(bits, String.format("%s, %s, %s\n", bits, bigHex, parsed));
                    oUnique.write( commandMap.get(bits));
                }


            }

        }finally{
            if(is!=null) is.close();
            if(oTransit!=null) oTransit.close();
            if(oinvalid!=null) oinvalid.close();
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
