import junit.framework.TestCase;
import pivotal.io.batch.StateMachine;
import pivotal.io.batch.domain.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class BitTest extends TestCase{


    public BitTest(String testName)
    {
        super( testName );
    }


    public void testBitSetGet(){
        Command.POS[] poses= Command.POS.values();
        for(int i=0;i<poses.length;i++) {
            bitSetGet(poses[i]);

        }
    }

    private boolean  bitSetGet(Command.POS type){
        int pos=type.getBinaryIndex();
        byte bytes[] = new byte[]{0,0,0,0};
        Command.setBit(bytes, pos, 1);

        for(int i=0; i<=31; i++){
            if(i == pos){
                assertEquals(type.toString(), 1, Command.getBit(bytes,pos));
            }else{
                assertEquals(type.toString(), 0, Command.getBit(bytes,i));
            }
        }
        String bytesBits=Command.byteToBits(bytes);
        System.out.println(String.format("%8s %2s %s",type,type.getHumanIndex(),bytesBits));
        return true;
    }


    public void testCommandDetect()
    {

        StateMachine sm = new StateMachine();

        byte[] data;


//        data= javax.xml.bind.DatatypeConverter.parseHexBinary("81DD01CD");
//        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
//        assertEquals("command detect: ", Command.type.ACT, sm.getCommand(data).getName());
//
//        data=javax.xml.bind.DatatypeConverter.parseHexBinary("F8D201CD");
//        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
//        assertEquals("command detect: ", Command.type.ACT, sm.getCommand(data).getName());
//
//        data=javax.xml.bind.DatatypeConverter.parseHexBinary("0B5003FD");
//        System.out.println(Command.parse(data)+ " " + Command.byteToBits(data)+"> "+ sm.getCommand(data));
//        assertEquals("command detect: ", Command.type.DES, sm.getCommand(data).getName());
//
//        data=javax.xml.bind.DatatypeConverter.parseHexBinary("00000ACD");
//        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
//        assertEquals("command detect: ", Command.type.ACT, sm.getCommand(data).getName());



        //01001101-00000010-00110111-11001000 4D0237C8  CKE0-24:1	 CS0-26:1	 ACTN-17:1	 A16-21:0	 A15-20:0	 A14-19:0	deselec
        String hex="4D0237C8";
        data=javax.xml.bind.DatatypeConverter.parseHexBinary(hex);
        assertEquals(hex, "01001101-00000010-00110111-11001000",Command.byteToBits(data));
        assertEquals(hex, 1,Command.getBit(data, Command.POS.CKE0.getBinaryIndex()));
        assertEquals(hex, 1,Command.getBit(data, Command.POS.CS0.getBinaryIndex()));
        assertEquals(hex, 1,Command.getBit(data, Command.POS.ACTN.getBinaryIndex()));
        assertEquals(hex, 0,Command.getBit(data, Command.POS.A16.getBinaryIndex()));
        assertEquals(hex, 0,Command.getBit(data, Command.POS.A15.getBinaryIndex()));
        assertEquals(hex, 0,Command.getBit(data, Command.POS.A14.getBinaryIndex()));

        //01111101-00000010-00010010-00000011	7D021203	CKE0-24:1	 CS0-26:1	 ACTN-17:1	 A16-21:0	 A15-20:0	 A14-19:0
        hex="7D021203";
        data=javax.xml.bind.DatatypeConverter.parseHexBinary(hex);
        assertEquals(hex, "01111101-00000010-00010010-00000011",Command.byteToBits(data));
        assertEquals(hex, 1,Command.getBit(data, Command.POS.CKE0.getBinaryIndex()));
        assertEquals(hex, 1,Command.getBit(data, Command.POS.CS0.getBinaryIndex()));
        assertEquals(hex, 1,Command.getBit(data, Command.POS.ACTN.getBinaryIndex()));
        assertEquals(hex, 0,Command.getBit(data, Command.POS.A16.getBinaryIndex()));
        assertEquals(hex, 0,Command.getBit(data, Command.POS.A15.getBinaryIndex()));
        assertEquals(hex, 0, Command.getBit(data, Command.POS.A14.getBinaryIndex()));

//        System.out.println(sm.getCommand(data));
    }



    public void testCommandParsing() {

        StateMachine sm = new StateMachine();

        Command cmd = new CommandUndefined();
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(1);
        cmd.setA15(1);
        cmd.setA14(1);
        assertEquals(Command.byteToBits(cmd.getBytes()) + " " + Command.parse(cmd.getBytes()), Command.type.NOP, sm.getCommandType(cmd.getBytes()));

        cmd = new CommandUndefined();
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(0);
        cmd.setA15(0);
        cmd.setA14(1);
        assertEquals(Command.byteToBits(cmd.getBytes()) + " " + Command.parse(cmd.getBytes()), Command.type.REF, sm.getCommandType(cmd.getBytes()));


        cmd = new CommandUndefined();//PRE,PREA
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(0);
        cmd.setA15(1);
        cmd.setA14(0);
        cmd.setA10(0);
        assertEquals(Command.byteToBits(cmd.getBytes()) + " " + Command.parse(cmd.getBytes()), Command.type.PRE, sm.getCommandType(cmd.getBytes()));

        cmd = new CommandUndefined();//PRE,PREA
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(0);
        cmd.setA15(1);
        cmd.setA14(0);
        cmd.setA10(1);
        assertEquals(Command.byteToBits(cmd.getBytes()) + " " + Command.parse(cmd.getBytes()), Command.type.PRE, sm.getCommandType(cmd.getBytes()));

        cmd = new CommandUndefined();
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(1);
        cmd.setA15(0);
        cmd.setA14(0);
        assertEquals(Command.byteToBits(cmd.getBytes()) + " " + Command.parse(cmd.getBytes()), Command.type.WR, sm.getCommandType(cmd.getBytes()));

        cmd = new CommandUndefined();
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(1);
        cmd.setA15(0);
        cmd.setA14(1);
        assertEquals(Command.byteToBits(cmd.getBytes()) + " " + Command.parse(cmd.getBytes()), Command.type.RD, sm.getCommandType(cmd.getBytes()));

        cmd = new CommandUndefined();
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(0);
        cmd.setA15(1);
        cmd.setA14(1);
        assertEquals(Command.byteToBits(cmd.getBytes()) + " " + Command.parse(cmd.getBytes()), Command.type.RFU, sm.getCommandType(cmd.getBytes()));

    }


    public void testCommandDetectHex()
    {

        StateMachine sm = new StateMachine();

        byte[] data = javax.xml.bind.DatatypeConverter.parseHexBinary("81DD01CD");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.ACT, sm.getCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("F8D201CD");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.UND, sm.getCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("0B5003FD");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.ACT, sm.getCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("00000ACD");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.UND, sm.getCommand(data).getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("002712C9");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.WR, sm.getCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("002712CD");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.WR, sm.getCommand(data).getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("C02B12C9");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.RD, sm.getCommand(data).getName());


        //   00111100-00000000-00000000-11001001	3C0000C9	    CKE0-24: 0	      CS0-26: 1	     ACTN-17: 0	      A16-21: 0	      A15-20: 0	      A14-19: 0
        data=javax.xml.bind.DatatypeConverter.parseHexBinary("3C0000C9");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.PDE, sm.getCommand(data).getName());

        //00000111-00001001-00100010-11001101	070922CD	    CKE0-24: 1	      CS0-26: 1	     ACTN-17: 0	      A16-21: 0	      A15-20: 0	      A14-19: 1	 	PDX

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("070922CD");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.PDX, sm.getCommand(data).getName());


        //00000011-00001010-00101010-11001001	030A2AC9	    CKE0-24: 1	      CS0-26: 0	     ACTN-17: 1	      A16-21: 0	      A15-20: 0	      A14-19: 1
        data=javax.xml.bind.DatatypeConverter.parseHexBinary("030A2AC9");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.REF, sm.getCommand(data).getName());


        //00000011-00011010-00101010-11001001	031A2AC9	    CKE0-24: 1	      CS0-26: 0	     ACTN-17: 1	      A16-21: 0	      A15-20: 1	      A14-19: 1
        data=javax.xml.bind.DatatypeConverter.parseHexBinary("031A2AC9");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.RFU, sm.getCommand(data).getName());


        //00000011-00010010-00000010-01111101	0312027D	    CKE0-24: 1	      CS0-26: 0	     ACTN-17: 1	      A16-21: 0	      A15-20: 1	      A14-19: 0	 	PRE
        data=javax.xml.bind.DatatypeConverter.parseHexBinary("0312027D");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals(Command.byteToBits(data) + " " + Command.parse(data), Command.type.PRE, sm.getCommand(data).getName());



    }



}
