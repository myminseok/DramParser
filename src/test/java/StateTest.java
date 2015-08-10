import junit.framework.TestCase;
import pivotal.io.batch.StateMachine;
import pivotal.io.batch.domain.State;
import pivotal.io.batch.domain.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class StateTest extends TestCase{


    public StateTest(String testName)
    {
        super( testName );
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
