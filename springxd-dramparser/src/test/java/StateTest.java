import jdk.nashorn.internal.ir.annotations.Ignore;
import junit.framework.TestCase;
import pivotal.io.batch.StateMachine;
import pivotal.io.batch.domain.State;
import pivotal.io.batch.domain.*;

public class StateTest extends TestCase{


    public StateTest(String testName)
    {
        super( testName );
    }

    public void testBitSetGet(){
    }

    public void ignoretestTransit()
    {
        byte[] data = null;
        StateCommand cmd=null;
        StateMachine sm = new StateMachine();// undefined


        StateCommand pre = new StateCommandUndefined();//PRE,PREA
        pre.setCKE0(1);
        pre.setCS0(0);
        pre.setACTN(1);
        pre.setA16(0);
        pre.setA15(1);
        pre.setA14(0);
        assertEquals("", StateCommand.type.PRE, sm.getStateCommandType(pre.getBytes()));


        StateCommand mrs = new StateCommandUndefined();
        mrs.setCKE0(1);
        mrs.setCS0(0);
        mrs.setACTN(1);
        mrs.setA16(0);
        mrs.setA15(0);
        mrs.setA14(0);
        assertEquals("", StateCommand.type.MRS, sm.getStateCommandType(mrs.getBytes()));


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("00000000"); //und
        assertEquals(sm.toString(), false, sm.transit(data));
        assertEquals(sm.toString(), State.type.Undefined, sm.stateInfo.prevState.getName());

        data= javax.xml.bind.DatatypeConverter.parseHexBinary("81DD01CD"); //ACT
        cmd = sm.getStateCommand(data);
        assertEquals("" + sm, StateCommand.type.ACT, cmd.getName());
        assertEquals(sm.toString(), false, sm.transit(data));
        assertEquals(sm.toString(), State.type.Undefined, sm.stateInfo.prevState.getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("030A2AC9"); //REF
        assertEquals("" + sm, true, sm.transit(data));
        assertEquals("transit test: ", State.type.IDLE, sm.stateInfo.prevState.getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("00000000"); //und
        assertEquals("" + sm, false, sm.transit(data));
        assertEquals("transit test: ", State.type.IDLE, sm.stateInfo.prevState.getName());



        data=javax.xml.bind.DatatypeConverter.parseHexBinary("81DD01CD"); //act
        assertEquals("" + sm, true, sm.transit(data));
        assertEquals("transit test: ", State.type.BankActive, sm.stateInfo.prevState.getName());

        // invalid command case => log file.
        assertEquals("" + sm, false, sm.transit(mrs.getBytes()));
        assertEquals("transit test: ", State.type.BankActive, sm.stateInfo.prevState.getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("3C0000C9"); //PDE
        assertEquals("" + sm, true, sm.transit(data));
        assertEquals("transit test: ", State.type.ActivePowerDown, sm.stateInfo.prevState.getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("070922CD"); //PDx
        assertEquals("" + sm, true, sm.transit(data));
        assertEquals("transit test: ", State.type.BankActive, sm.stateInfo.prevState.getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("C02B12C9"); //RD
        assertEquals("" + sm, true, sm.transit(data));
        assertEquals("transit test: ", State.type.Reading, sm.stateInfo.prevState.getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("C02B12C9"); //RD
        assertEquals("" + sm, true, sm.transit(data));
        assertEquals("transit test: ", State.type.Reading, sm.stateInfo.prevState.getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("002712CD"); //WR
        assertEquals("" + sm, true, sm.transit(data));
        assertEquals("transit test: ", State.type.Writing, sm.stateInfo.prevState.getName());

        data=javax.xml.bind.DatatypeConverter.parseHexBinary("C02B12C9"); //RD
        assertEquals("" + sm, true, sm.transit(data));
        assertEquals("transit test: ", State.type.Reading, sm.stateInfo.prevState.getName());


        data=javax.xml.bind.DatatypeConverter.parseHexBinary("00000000"); //und
        cmd = sm.getStateCommand(data);
        assertEquals("" + sm, false, sm.transit(data));
        assertEquals("transit test: ", State.type.Reading, sm.stateInfo.prevState.getName());



        data=javax.xml.bind.DatatypeConverter.parseHexBinary("0312027D"); //PRE
        assertEquals("" + sm, true, sm.transit(data));
        assertEquals("transit test: ", State.type.IDLE, sm.stateInfo.prevState.getName());



    }


}
