import jdk.nashorn.internal.ir.annotations.Ignore;
import junit.framework.TestCase;
import pivotal.io.batch.StateMachine;
import pivotal.io.batch.domain.*;

public class BitTest extends TestCase{


    public BitTest(String testName)
    {
        super( testName );
    }


    public void testBitSetGet(){
        StateCommand.INDEX[] poses= StateCommand.INDEX.values();
        for(int i=0;i<poses.length;i++) {
            bitSetGet(poses[i]);

        }
    }

    private boolean  bitSetGet(StateCommand.INDEX type){
        int pos=type.getBinaryIndex();
        byte bytes[] = new byte[]{0,0,0,0};
        StateCommand.setBit(bytes, pos, 1);

        for(int i=0; i<=31; i++){
            if(i == pos){
                assertEquals(type.toString(), 1, StateCommand.getBit(bytes,pos));
            }else{
                assertEquals(type.toString(), 0, StateCommand.getBit(bytes,i));
            }
        }
        String bytesBits=StateCommand.byteToBits(bytes);
        System.out.println(String.format("%8s %2s %s",type,type.getHumanIndex(),bytesBits));
        return true;
    }


    public void testCommandDetect()
    {

        StateMachine sm = new StateMachine();

        byte[] data;


//        data= javax.xml.bind.DatatypeConverter.parseHexBinary("81DD01CD");
//        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
//        assertEquals("command detect: ", StateCommand.type.ACT, sm.getStateCommand(data).getName());
//
//        data=javax.xml.bind.DatatypeConverter.parseHexBinary("F8D201CD");
//        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
//        assertEquals("command detect: ", StateCommand.type.ACT, sm.getStateCommand(data).getName());
//
//        data=javax.xml.bind.DatatypeConverter.parseHexBinary("0B5003FD");
//        System.out.println(StateCommand.parse(data)+ " " + StateCommand.byteToBits(data)+"> "+ sm.getStateCommand(data));
//        assertEquals("command detect: ", StateCommand.type.DES, sm.getStateCommand(data).getName());
//
//        data=javax.xml.bind.DatatypeConverter.parseHexBinary("00000ACD");
//        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
//        assertEquals("command detect: ", StateCommand.type.ACT, sm.getStateCommand(data).getName());



        //01001101-00000010-00110111-11001000 4D0237C8  CKE0-24:1	 CS0-26:1	 ACTN-17:1	 A16-21:0	 A15-20:0	 A14-19:0	deselec
        String hex="4D0237C8";
        data=javax.xml.bind.DatatypeConverter.parseHexBinary(hex);
        assertEquals(hex, "01001101-00000010-00110111-11001000",StateCommand.byteToBits(data));
        assertEquals(hex, 1,StateCommand.getBit(data, StateCommand.INDEX.CKE0.getBinaryIndex()));
        assertEquals(hex, 1,StateCommand.getBit(data, StateCommand.INDEX.CS0.getBinaryIndex()));
        assertEquals(hex, 1,StateCommand.getBit(data, StateCommand.INDEX.ACTN.getBinaryIndex()));
        assertEquals(hex, 0,StateCommand.getBit(data, StateCommand.INDEX.A16.getBinaryIndex()));
        assertEquals(hex, 0,StateCommand.getBit(data, StateCommand.INDEX.A15.getBinaryIndex()));
        assertEquals(hex, 0,StateCommand.getBit(data, StateCommand.INDEX.A14.getBinaryIndex()));

        //01111101-00000010-00010010-00000011	7D021203	CKE0-24:1	 CS0-26:1	 ACTN-17:1	 A16-21:0	 A15-20:0	 A14-19:0
        hex="7D021203";
        data=javax.xml.bind.DatatypeConverter.parseHexBinary(hex);
        assertEquals(hex, "01111101-00000010-00010010-00000011",StateCommand.byteToBits(data));
        assertEquals(hex, 1,StateCommand.getBit(data, StateCommand.INDEX.CKE0.getBinaryIndex()));
        assertEquals(hex, 1,StateCommand.getBit(data, StateCommand.INDEX.CS0.getBinaryIndex()));
        assertEquals(hex, 1,StateCommand.getBit(data, StateCommand.INDEX.ACTN.getBinaryIndex()));
        assertEquals(hex, 0,StateCommand.getBit(data, StateCommand.INDEX.A16.getBinaryIndex()));
        assertEquals(hex, 0,StateCommand.getBit(data, StateCommand.INDEX.A15.getBinaryIndex()));
        assertEquals(hex, 0, StateCommand.getBit(data, StateCommand.INDEX.A14.getBinaryIndex()));

//        System.out.println(sm.getStateCommand(data));
    }



    public void ignoretestCommandParsing() {

        StateMachine sm = new StateMachine();

        StateCommand cmd = new StateCommandUndefined();
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(1);
        cmd.setA15(1);
        cmd.setA14(1);
        assertEquals(StateCommand.byteToBits(cmd.getBytes()) + " " + StateCommand.parse(cmd.getBytes()), StateCommand.type.NOP, sm.getStateCommandType(cmd.getBytes()));

        cmd = new StateCommandUndefined();
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(0);
        cmd.setA15(0);
        cmd.setA14(1);
        assertEquals(StateCommand.byteToBits(cmd.getBytes()) + " " + StateCommand.parse(cmd.getBytes()), StateCommand.type.REF, sm.getStateCommandType(cmd.getBytes()));


        cmd = new StateCommandUndefined();//PRE,PREA
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(0);
        cmd.setA15(1);
        cmd.setA14(0);
        cmd.setA10(0);
        assertEquals(StateCommand.byteToBits(cmd.getBytes()) + " " + StateCommand.parse(cmd.getBytes()), StateCommand.type.PRE, sm.getStateCommandType(cmd.getBytes()));

        cmd = new StateCommandUndefined();//PRE,PREA
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(0);
        cmd.setA15(1);
        cmd.setA14(0);
        cmd.setA10(1);
        assertEquals(StateCommand.byteToBits(cmd.getBytes()) + " " + StateCommand.parse(cmd.getBytes()), StateCommand.type.PRE, sm.getStateCommandType(cmd.getBytes()));

        cmd = new StateCommandUndefined();
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(1);
        cmd.setA15(0);
        cmd.setA14(0);
        assertEquals(StateCommand.byteToBits(cmd.getBytes()) + " " + StateCommand.parse(cmd.getBytes()), StateCommand.type.WR, sm.getStateCommandType(cmd.getBytes()));

        cmd = new StateCommandUndefined();
        cmd.setCKE0(1);
        cmd.setCS0(0);
        cmd.setACTN(1);
        cmd.setA16(1);
        cmd.setA15(0);
        cmd.setA14(1);
        assertEquals(StateCommand.byteToBits(cmd.getBytes()) + " " + StateCommand.parse(cmd.getBytes()), StateCommand.type.RD, sm.getStateCommandType(cmd.getBytes()));

//        cmd = new StateCommandUndefined();
//        cmd.setCKE0(1);
//        cmd.setCS0(0);
//        cmd.setACTN(1);
//        cmd.setA16(0);
//        cmd.setA15(1);
//        cmd.setA14(1);
//        assertEquals(StateCommand.byteToBits(cmd.getBytes()) + " " + StateCommand.parse(cmd.getBytes()), StateCommand.type.RFU, sm.getStateCommandType(cmd.getBytes()));

    }


    public void ignoretestCommandDetectHex()
    {

        StateMachine sm = new StateMachine();

        byte[] data = javax.xml.bind.DatatypeConverter.parseHexBinary("81DD01CD");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.ACT, sm.getStateCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("F8D201CD");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.UND, sm.getStateCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("0B5003FD");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.ACT, sm.getStateCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("00000ACD");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.UND, sm.getStateCommand(data).getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("002712C9");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.WR, sm.getStateCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("002712CD");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.WR, sm.getStateCommand(data).getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("C02B12C9");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.RD, sm.getStateCommand(data).getName());


        //   00111100-00000000-00000000-11001001	3C0000C9	    CKE0-24: 0	      CS0-26: 1	     ACTN-17: 0	      A16-21: 0	      A15-20: 0	      A14-19: 0
        data=javax.xml.bind.DatatypeConverter.parseHexBinary("3C0000C9");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.PDE, sm.getStateCommand(data).getName());

        //00000111-00001001-00100010-11001101	070922CD	    CKE0-24: 1	      CS0-26: 1	     ACTN-17: 0	      A16-21: 0	      A15-20: 0	      A14-19: 1	 	PDX

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("070922CD");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.PDX, sm.getStateCommand(data).getName());


        //00000011-00001010-00101010-11001001	030A2AC9	    CKE0-24: 1	      CS0-26: 0	     ACTN-17: 1	      A16-21: 0	      A15-20: 0	      A14-19: 1
        data=javax.xml.bind.DatatypeConverter.parseHexBinary("030A2AC9");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.REF, sm.getStateCommand(data).getName());


//        //00000011-00011010-00101010-11001001	031A2AC9	    CKE0-24: 1	      CS0-26: 0	     ACTN-17: 1	      A16-21: 0	      A15-20: 1	      A14-19: 1
//        data=javax.xml.bind.DatatypeConverter.parseHexBinary("031A2AC9");
//        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
//        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.RFU, sm.getStateCommand(data).getName());


        //00000011-00010010-00000010-01111101	0312027D	    CKE0-24: 1	      CS0-26: 0	     ACTN-17: 1	      A16-21: 0	      A15-20: 1	      A14-19: 0	 	PRE
        data=javax.xml.bind.DatatypeConverter.parseHexBinary("0312027D");
        System.out.println(StateCommand.parse(data) + " " + StateCommand.byteToBits(data) + "> " + sm.getStateCommand(data));
        assertEquals(StateCommand.byteToBits(data) + " " + StateCommand.parse(data), StateCommand.type.PRE, sm.getStateCommand(data).getName());



    }



}
