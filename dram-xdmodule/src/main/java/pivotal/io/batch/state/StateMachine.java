package pivotal.io.batch.state;

import pivotal.io.batch.command.*;
import pivotal.io.batch.state.*;

import java.util.*;

/**
 * Created by kimm5 on 8/1/15.
 */
public class StateMachine {

    public ArrayList<Command> commands = new ArrayList();

    protected Map<State.type, State> stateObjectMap = new HashMap<State.type, State>();
    protected Map<Command.type, Command> stateCommandAllMap = new HashMap<Command.type, Command>();
    protected Set<Command.type> stateCommandAllMapKeyset;
    protected Set<Command.type> stateCommandIgnoreMapKeyset;


    public StateInfo stateInfo = new StateInfo();

    private StateReturnHolder trialValueHolder = new StateReturnHolder();

    public StateMachine(){

        initCommand();
        initStateObject();
    }

    public StateReturnHolder getTrialValueHolder() {
        return trialValueHolder;
    }

    public State getState(State.type state){
        return this.stateObjectMap.get(state);
    }

    public boolean transit(byte[] bytes){
        // 현재 상태에서 가능한 명령 확인, ckel
        // 상태에 따라 명령의 해석이 달라짐.

        stateInfo.clear();
        trialValueHolder.clear();

        this.stateInfo.isTransit=stateInfo.currentState.nextState(stateCommandAllMap, bytes, trialValueHolder);
        this.stateInfo.newData=bytes;
        if(this.stateInfo.isTransit){// transit.
            this.stateInfo.prevState = this.stateInfo.currentState;
            this.stateInfo.currentState = stateObjectMap.get(trialValueHolder.triedStateType);
            this.stateInfo.newCommand = this.trialValueHolder.triedCommand;
        }

        return this.stateInfo.isTransit;
    }


    private void initStateObject(){
        stateObjectMap.put(State.type.Undefined, StateUndefined.getInstance());
        stateObjectMap.put(State.type.ActivePowerDown, StateActivePowerDown.getInstance());
        stateObjectMap.put(State.type.BankActive, StateBankActive.getInstance());
        stateObjectMap.put(State.type.IDLE, StateIDLE.getInstance());
        stateObjectMap.put(State.type.Reading,  StateReading.getInstance());
        stateObjectMap.put(State.type.Writing,  StateWriting.getInstance());

        Set<Command.type> cmdSet = stateCommandAllMap.keySet();


    }


    private void initCommand(){


        stateCommandAllMap.put(Command.type.UND, CommandUndefined.getInstance());
        stateCommandAllMap.put(Command.type.NOP, CommandNOP.getInstance());
        stateCommandAllMap.put(Command.type.DES, CommandDES.getInstance());
        stateCommandAllMap.put(Command.type.ACT, CommandACT.getInstance());
        stateCommandAllMap.put(Command.type.CKE_L, CommandCLEL.getInstance());
        stateCommandAllMap.put(Command.type.PDE, CommandPDE.getInstance());
        stateCommandAllMap.put(Command.type.PDX, CommandPDX.getInstance());
        stateCommandAllMap.put(Command.type.PRE, CommandPRE.getInstance());
        stateCommandAllMap.put(Command.type.RD, CommandRD.getInstance());
        stateCommandAllMap.put(Command.type.REF, CommandREF.getInstance());
        stateCommandAllMap.put(Command.type.WR, CommandWR.getInstance());
        stateCommandAllMap.put(Command.type.ZQC, CommandZQC.getInstance());
        stateCommandAllMap.put(Command.type.SRE, CommandZQC.getInstance());
        stateCommandAllMap.put(Command.type.MRS, CommandMRS.getInstance());

        stateCommandAllMapKeyset = stateCommandAllMap.keySet();
        stateCommandAllMapKeyset.remove(Command.type.CKE_L);
        stateCommandAllMapKeyset.remove(Command.type.UND);
        stateCommandAllMapKeyset.remove(Command.type.PDX);

        stateCommandIgnoreMapKeyset = new HashSet();
        stateCommandIgnoreMapKeyset.add(Command.type.NOP);
        stateCommandIgnoreMapKeyset.add(Command.type.DES);
    }

    public boolean isIgnoreCommand(byte[] bytes){

        Command cmd=null;
        for(Command.type cmdType: stateCommandIgnoreMapKeyset){
            cmd = stateCommandAllMap.get(cmdType);
            if(cmd==null){
                continue;
            }
            if(cmd.isMatching(bytes)){
                return true;
            }
        }
        return false;
    }


    public Command findStateCommand(byte[] bytes){

        Command cmd=null;
        for(Command.type cmdType: stateCommandAllMapKeyset){
            cmd = stateCommandAllMap.get(cmdType);
            if(cmd==null){
                continue;
            }
            if(cmd.isMatching(bytes)){
                return cmd;
            }
        }
        return CommandUndefined.getInstance();
    }

    public Command.type getStateCommandType(byte[] bytes){
        return findStateCommand(bytes).getName();
    }

    public String toString(){
        return this.stateInfo.toString();
    }


}
