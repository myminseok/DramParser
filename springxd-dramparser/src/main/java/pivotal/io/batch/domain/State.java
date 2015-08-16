package pivotal.io.batch.domain;


import pivotal.io.batch.StateMachine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class State {

    public type name;

    public enum type {
        Undefined, IDLE, BankActive, Writing, Reading, ActivePowerDown ;
    }

    public type getName() {
        return name;
    }

    protected Map<StateCommand.type, State.type> validTransitMap = new HashMap<StateCommand.type, State.type>();

    protected Set<StateCommand.type> validTransitMapKeySet;

    public State() {
        initValidTransitMap();
        validTransitMapKeySet= validTransitMap.keySet();
    }

    abstract void initValidTransitMap();


    public boolean nextState(Map<StateCommand.type, StateCommand> stateCommandFullSetMap, byte[] bytes,StateReturnHolder stateReturnHolder){
        // type별 command 구현체 추출.
        // command matching확인.
        StateCommand cmd=null;
        for(StateCommand.type cmdType: validTransitMapKeySet){
            cmd = stateCommandFullSetMap.get(cmdType);
            if(cmd==null){
                continue;
            }

            if(cmd.isMatching(bytes)){
                stateReturnHolder.nextCommand=cmd;
                stateReturnHolder.nextStateType=validTransitMap.get(cmdType);
                return true;
            }
        }
        stateReturnHolder.nextStateType=type.Undefined;
        return false;
    }

}
