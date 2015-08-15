package pivotal.io.batch;

import pivotal.io.batch.domain.*;

import java.util.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class StateMachine {

    public ArrayList<StateCommand> commands = new ArrayList();

    protected Map<State.type, State> stateObjectMap = new HashMap<State.type, State>();
    protected Map<StateCommand.type, StateCommand> stateCommandAllMap = new HashMap<StateCommand.type, StateCommand>();
    protected Set<StateCommand.type> stateCommandAllMapKeyset;


    public StateInfo stateInfo = new StateInfo();

    private StateReturnHolder stateReturnHolder = new StateReturnHolder();

    public StateMachine(){

        initCommand();
        initStateObject();
    }

    public State getState(State.type state){
        return this.stateObjectMap.get(state);
    }

    public boolean transit(byte[] bytes){
        // 현재 상태에서 가능한 명령 확인, ckel
        // 상태에 따라 명령의 해석이 달라짐.
        stateInfo.clear();
        stateReturnHolder.clear();

        this.stateInfo.isTransit=stateInfo.currentState.nextState(stateCommandAllMap, bytes, stateReturnHolder);
        this.stateInfo.newData=bytes;
        if(this.stateInfo.isTransit){
            this.stateInfo.prevState = this.stateInfo.currentState;
            this.stateInfo.currentState = stateObjectMap.get(stateReturnHolder.nextStateType);
            this.stateInfo.newCommand = this.stateReturnHolder.nextCommand;
        }

        return this.stateInfo.isTransit;
    }


    private void initStateObject(){
        stateObjectMap.put(State.type.Undefined, StateUndefined.getInstance());
        stateObjectMap.put(State.type.ActivePowerDown, StateActivePowerDown.getInstance());
        stateObjectMap.put(State.type.BankActive, StateBankActive.getInstance());
        stateObjectMap.put(State.type.IDLE,  StateIDLE.getInstance());
        stateObjectMap.put(State.type.Reading,  StateReading.getInstance());
        stateObjectMap.put(State.type.Writing,  StateWriting.getInstance());

        Set<StateCommand.type> cmdSet = stateCommandAllMap.keySet();


    }


    private void initCommand(){


        stateCommandAllMap.put(StateCommand.type.UND, StateCommandUndefined.getInstance());
        stateCommandAllMap.put(StateCommand.type.ACT, StateCommandACT.getInstance());
        stateCommandAllMap.put(StateCommand.type.CKE_L, StateCommandCLEL.getInstance());
        stateCommandAllMap.put(StateCommand.type.PDE, StateCommandPDE.getInstance());
        stateCommandAllMap.put(StateCommand.type.PDX, StateCommandPDX.getInstance());
        stateCommandAllMap.put(StateCommand.type.PRE, StateCommandPRE.getInstance());
        stateCommandAllMap.put(StateCommand.type.RD, StateCommandRD.getInstance());
        stateCommandAllMap.put(StateCommand.type.REF, StateCommandREF.getInstance());
        stateCommandAllMap.put(StateCommand.type.WR, StateCommandWR.getInstance());
        stateCommandAllMap.put(StateCommand.type.ZQC, StateCommandZQC.getInstance());
        stateCommandAllMap.put(StateCommand.type.SRE, StateCommandZQC.getInstance());
        stateCommandAllMap.put(StateCommand.type.MRS, StateCommandMRS.getInstance());

        stateCommandAllMapKeyset = stateCommandAllMap.keySet();
        stateCommandAllMapKeyset.remove(StateCommand.type.CKE_L);
        stateCommandAllMapKeyset.remove(StateCommand.type.UND);

    }


    public StateCommand getStateCommand(byte[] bytes){

        StateCommand cmd=null;
        for(StateCommand.type cmdType: stateCommandAllMapKeyset){
            cmd = stateCommandAllMap.get(cmdType);
            if(cmd==null){
                continue;
            }
            if(cmd.isMatching(bytes)){
                return cmd;
            }
        }
        return StateCommandUndefined.getInstance();
    }

    public StateCommand.type getStateCommandType(byte[] bytes){
        return getStateCommand(bytes).getName();
    }

    public String toString(){
        return this.stateInfo.toString();
    }


}
