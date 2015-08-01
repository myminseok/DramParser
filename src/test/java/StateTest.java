import jdk.nashorn.internal.ir.annotations.Ignore;
import junit.framework.TestCase;
import pivotal.io.batch.StateMachine;
import pivotal.io.batch.domain.State;
import pivotal.io.batch.domain.StateIDLE;
import pivotal.io.batch.domain.StateUndefined;
import pivotal.io.batch.domain.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class StateTest extends TestCase{


    public StateTest(String testName)
    {
        super( testName );
    }

    public void testApp() {

        StateMachine sm = new StateMachine();

        Command desSample = new CommandDES();
        desSample.setCKE0(1);
        desSample.setCS0(1);
        assertEquals("DES", sm.getCommandType(desSample.getBytes()), desSample.getName());

        Command nop = new CommandNOP();
        nop.setCKE0(1);
        nop.setCS0(0);
        nop.setACTN(1);
        nop.setA16(1);
        nop.setA15(1);
        nop.setA14(1);
        assertEquals("nop", sm.getCommandType(nop.getBytes()), nop.getName());

        Command ref = new CommandREF();
        ref.setCKE0(1);
        ref.setCS0(0);
        ref.setACTN(1);
        ref.setA16(0);
        ref.setA15(0);
        ref.setA14(1);
        assertEquals("ref", sm.getCommandType(ref.getBytes()), ref.getName());


        Command pre = new CommandPRE();//PRE,PREA
        pre.setCKE0(1);
        pre.setCS0(0);
        pre.setACTN(1);
        pre.setA16(0);
        pre.setA15(1);
        pre.setA14(0);
        pre.setA10(0);

        assertEquals("pre", sm.getCommandType(pre.getBytes()), pre.getName());

        Command prea = new CommandPRE();//PRE,PREA
        prea.setCKE0(1);
        prea.setCS0(0);
        prea.setACTN(1);
        prea.setA16(0);
        prea.setA15(1);
        prea.setA14(0);
        prea.setA10(1);
        assertEquals("PRE", sm.getCommandType(prea.getBytes()), prea.getName());

        Command wr = new CommandWR();
        wr.setCKE0(1);
        wr.setCS0(0);
        wr.setACTN(1);
        wr.setA16(1);
        wr.setA15(0);
        wr.setA14(0);
        assertEquals("wr", sm.getCommandType(wr.getBytes()), wr.getName());


        Command rd = new CommandRD();
        rd.setCKE0(1);
        rd.setCS0(0);
        rd.setACTN(1);
        rd.setA16(1);
        rd.setA15(0);
        rd.setA14(1);
        assertEquals("rd", sm.getCommandType(rd.getBytes()), rd.getName());

    }

    public void testCommandDetect()
    {

        StateMachine sm = new StateMachine();

        byte[] data = javax.xml.bind.DatatypeConverter.parseHexBinary("81DD01CD");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals("command detect: ", Command.type.ACT, sm.getCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("F8D201CD");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals("command detect: ", Command.type.ACT, sm.getCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("0B5003FD");
        System.out.println(Command.parse(data)+ " " + Command.byteToBits(data)+"> "+ sm.getCommand(data));
        assertEquals("command detect: ", Command.type.DES, sm.getCommand(data).getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("00000ACD");
        System.out.println(Command.parse(data) + " " + Command.byteToBits(data) + "> " + sm.getCommand(data));
        assertEquals("command detect: ", Command.type.ACT, sm.getCommand(data).getName());

    }


    public void testTransit()
    {

        StateMachine sm2 = new StateMachine();// undefined
        testTransitRun(sm2, new CommandUndefined(), false);
        testTransitRun(sm2, new CommandREF(), true);// => IDLE
        assertEquals("transit test: ", State.type.IDLE, sm2.currentState);

        testTransitRun(sm2, new CommandUndefined(), false);
        assertEquals("transit test: ", State.type.IDLE, sm2.currentState);

        testTransitRun(sm2, new CommandACT(), true);
        assertEquals("transit test: ", State.type.BankActive, sm2.currentState);

        testTransitRun(sm2, new CommandPDE(), true);
        assertEquals("transit test: ", State.type.ActivePowerDown, sm2.currentState);

        testTransitRun(sm2, new CommandPDX(), true);
        assertEquals("transit test: ", State.type.BankActive, sm2.currentState);

        testTransitRun(sm2, new CommandRD(), true);
        assertEquals("transit test: ", State.type.Reading, sm2.currentState);

        testTransitRun(sm2, new CommandRD(), true);
        assertEquals("transit test: ", State.type.Reading, sm2.currentState);

        testTransitRun(sm2, new CommandWR(), true);
        assertEquals("transit test: ", State.type.Writing, sm2.currentState);

        testTransitRun(sm2, new CommandRD(), true);
        assertEquals("transit test: ", State.type.Reading, sm2.currentState);

        testTransitRun(sm2, new CommandUndefined(), false);
        assertEquals("transit test: ", State.type.Reading, sm2.currentState);

        testTransitRun(sm2, new CommandPRE(), true);
        assertEquals("transit test: ", State.type.IDLE, sm2.currentState);

    }

    private void testTransitRun(StateMachine sm, Command cmd, boolean expect) {
        assertEquals("transit test"+ cmd + " \n => :" + sm, expect, sm.transit(cmd));

    }

}
